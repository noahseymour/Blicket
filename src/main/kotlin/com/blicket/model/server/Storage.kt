package com.blicket.model.server

typealias publicKey = String
typealias privateKey = String
typealias name = String

class InvalidKeyException : Exception()

data class User(val publicKey: publicKey, val privateKey: privateKey, val name: name)

object Storage {

    private val users: MutableMap<publicKey, Pair<privateKey, name>> = mutableMapOf()

    fun addUser(publicKey: publicKey, privateKey: privateKey, name: name) {
        if (users[publicKey]?.first != privateKey) {
            throw InvalidKeyException()
        }

        users[publicKey] = Pair(privateKey, name)
    }

    fun getUser(publicKey: publicKey): User? {
        val pair = users[publicKey]
        return if (pair != null) {
            User(publicKey, pair.first, pair.second)
        } else null
    }

    fun contains(publicKey: publicKey) = users.containsKey(publicKey)
}