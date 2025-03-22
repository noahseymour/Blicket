package com.blicket.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

const val MAX_USERNAME_LENGTH = 20
const val MAX_PASSWORD_LENGTH = 50
const val SALT_LENGTH = 16

class UnauthorisedUserException() : Exception()

object Database {

    fun init(dbFilePath: String = "./data/your_database.db") {
        // Ensure database directory exists
        val dbFile = File(dbFilePath)
        dbFile.parentFile?.mkdirs()

        // Connect to the database using HikariCP for connection pooling
        val config = HikariConfig().apply {
            driverClassName = "org.sqlite.JDBC"
            jdbcUrl = "jdbc:sqlite:$dbFilePath"
            maximumPoolSize = 3  // SQLite can only have one writing connection at a time
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_SERIALIZABLE"
            validate()
        }

        val dataSource = HikariDataSource(config)
        org.jetbrains.exposed.sql.Database.connect(dataSource)

        // Create tables if they don't exist
        transaction {
            SchemaUtils.create(UsersTable)
        }
    }

    private object UsersTable : IntIdTable() {
        val username = varchar("username", MAX_USERNAME_LENGTH).uniqueIndex()
        val password = varchar("password", MAX_PASSWORD_LENGTH)
        val salt = varchar("salt", 24)
    }

    class UsersEntity(id: EntityID<Int>) : IntEntity(id) {
        companion object : IntEntityClass<UsersEntity>(UsersTable)

        var username by UsersTable.username
        var password by UsersTable.password
        var salt by UsersTable.salt
    }

    private fun generateSalt(): String {
        val salt = ByteArray(SALT_LENGTH)
        SecureRandom().nextBytes(salt)
        return Base64.getEncoder().encodeToString(salt)
    }

    private fun hashPassword(password: String, salt: String): String {
        val saltBytes = Base64.getDecoder().decode(salt)
        val md = MessageDigest.getInstance("SHA-256")
        md.update(saltBytes)
        val hashedBytes = md.digest(password.toByteArray())
        return Base64.getEncoder().encodeToString(hashedBytes)
    }

    /**
     * name is a plaintext string
     * password is a plaintext password and will be stored into the database hashed
     */
    fun insertUser(name: String, password: String): UsersEntity = transaction {

        if (UsersEntity.find { UsersTable.username eq name }.singleOrNull() != null) {
            throw UnsupportedOperationException()
        }


        var id: Int? = null

        val salt = generateSalt()
        val hashedPassword = hashPassword(password, salt)

        val insertStatement = UsersTable.insert {
            it[username] = name
            it[UsersTable.password] = hashedPassword
            it[UsersTable.salt] = salt
        }
        id = insertStatement[UsersTable.id].value

        UsersEntity.find { UsersTable.id eq id }.first()
    }


    fun authUser(name: String, password: String): UsersEntity =
        transaction {
            val user = UsersEntity.find { UsersTable.username eq name }.singleOrNull()

            if (user != null) {
                val storedPasswordHash = user.password
                val salt = user.salt
                val hashedPasswordToCheck = hashPassword(password, salt)

                if (storedPasswordHash == hashedPasswordToCheck) {
                    return@transaction user
                }
            }
            throw UnauthorisedUserException()
        }
}
