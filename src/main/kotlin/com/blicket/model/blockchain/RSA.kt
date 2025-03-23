package com.blicket.model.blockchain

import at.qubic.api.crypto.IdentityUtil
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security
import java.security.PublicKey
import java.security.PrivateKey
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import at.qubic.api.crypto.IdentityUtil.*
import at.qubic.api.crypto.KangarooTwelve
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import java.util.Base64
import java.math.BigInteger

import java.nio.ByteBuffer
import java.nio.ByteOrder


object FourQParams {
    const val KEY_SIZE_BYTES = 32
    const val POINT_SIZE_BYTES = 32
    val CURVE_ORDER = "73EDA753299D7D483339D80809A1D80553BDA402FFFE5BFEFFFFFFFF00000001".toBigInteger(16)
}

/**
 * Represents a FourQ private key
 */
class FourQPrivateKey(val key: ByteArray) {
    init {
        require(key.size == FourQParams.KEY_SIZE_BYTES) { "Private key must be ${FourQParams.KEY_SIZE_BYTES} bytes" }
    }

    fun toPublicKey(): FourQPublicKey {
        // Convert private key to public key using scalar multiplication
        return FourQCrypto.derivePublicKey(this)
    }
}

/**
 * Represents a FourQ public key
 */
class FourQPublicKey(val point: ByteArray) {
    init {
        require(point.size == FourQParams.POINT_SIZE_BYTES) { "Public key must be ${FourQParams.POINT_SIZE_BYTES} bytes" }
    }
}


/**
 * Main cryptographic operations for FourQ
 */
object FourQCrypto {
    private val secureRandom = SecureRandom()

    /**
     * Generate a new FourQ key pair
     */
    fun generateKeyPair(): Pair<FourQPrivateKey, FourQPublicKey> {
        val privateKeyBytes = ByteArray(FourQParams.KEY_SIZE_BYTES)
        secureRandom.nextBytes(privateKeyBytes)

        // Ensure private key is in the valid range (0 < key < curve_order)
        var scalar = BigInteger(1, privateKeyBytes) % FourQParams.CURVE_ORDER
        if (scalar <= BigInteger.ZERO) scalar += FourQParams.CURVE_ORDER

        val privateKey = FourQPrivateKey(scalar.toByteArray().takeLast(FourQParams.KEY_SIZE_BYTES).toByteArray())
        return Pair(privateKey, derivePublicKey(privateKey))
    }

    /**
     * Derive a public key from a private key
     */
    fun derivePublicKey(privateKey: FourQPrivateKey): FourQPublicKey {
        // In a real implementation, this would perform scalar multiplication
        // on the FourQ base point. This is a placeholder.

        // For actual FourQ implementation, you would use the scalar multiplication algorithm
        val publicKeyPoint = ByteArray(FourQParams.POINT_SIZE_BYTES)

        // Placeholder: In reality, this would be the result of scalar multiplication
        privateKey.key.forEachIndexed { index, byte ->
            publicKeyPoint[index % FourQParams.POINT_SIZE_BYTES] =
                (publicKeyPoint[index % FourQParams.POINT_SIZE_BYTES].toInt() xor byte.toInt()).toByte()
        }

        return FourQPublicKey(publicKeyPoint)
    }

    /**
     * Encrypt data using the recipient's FourQ public key
     */
    fun encrypt(plaintext: ByteArray, recipientPublicKey: FourQPublicKey): ByteArray {
        // 1. Generate ephemeral key pair
        val (ephemeralPrivate, ephemeralPublic) = generateKeyPair()

        // 2. Perform ECDH to get shared secret
        val sharedSecret = computeSharedSecret(ephemeralPrivate, recipientPublicKey)

        // 3. Derive encryption key from shared secret
        val aesKey = deriveAESKey(sharedSecret)

        // 4. Encrypt data with AES-GCM
        val iv = ByteArray(12)
        secureRandom.nextBytes(iv)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val secretKeySpec = SecretKeySpec(aesKey, "AES")
        val gcmParameterSpec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmParameterSpec)

        val ciphertext = cipher.doFinal(plaintext)

        // 5. Combine ephemeral public key, IV, and ciphertext
        val result = ByteArray(ephemeralPublic.point.size + iv.size + ciphertext.size)
        System.arraycopy(ephemeralPublic.point, 0, result, 0, ephemeralPublic.point.size)
        System.arraycopy(iv, 0, result, ephemeralPublic.point.size, iv.size)
        System.arraycopy(ciphertext, 0, result, ephemeralPublic.point.size + iv.size, ciphertext.size)

        return result
    }

    /**
     * Decrypt data using the recipient's FourQ private key
     */
    fun decrypt(encryptedData: ByteArray, recipientPrivateKey: FourQPrivateKey): ByteArray {
        // 1. Extract ephemeral public key, IV, and ciphertext
        val ephemeralPublicBytes = encryptedData.copyOfRange(0, FourQParams.POINT_SIZE_BYTES)
        val iv = encryptedData.copyOfRange(FourQParams.POINT_SIZE_BYTES, FourQParams.POINT_SIZE_BYTES + 12)
        val ciphertext = encryptedData.copyOfRange(FourQParams.POINT_SIZE_BYTES + 12, encryptedData.size)

        val ephemeralPublic = FourQPublicKey(ephemeralPublicBytes)

        // 2. Perform ECDH to recover the shared secret
        val sharedSecret = computeSharedSecret(recipientPrivateKey, ephemeralPublic)

        // 3. Derive encryption key from shared secret
        val aesKey = deriveAESKey(sharedSecret)

        // 4. Decrypt data with AES-GCM
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val secretKeySpec = SecretKeySpec(aesKey, "AES")
        val gcmParameterSpec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec)

        return cipher.doFinal(ciphertext)
    }

    /**
     * Compute a shared secret using ECDH
     */
    private fun computeSharedSecret(privateKey: FourQPrivateKey, publicKey: FourQPublicKey): ByteArray {
        // In a real implementation, this would perform scalar multiplication
        // of the public key point by the private key scalar

        // For actual FourQ implementation, you would use the scalar multiplication
        val sharedSecret = ByteArray(FourQParams.POINT_SIZE_BYTES)

        // Placeholder: In reality, this would be the result of scalar multiplication
        for (i in 0 until FourQParams.POINT_SIZE_BYTES) {
            sharedSecret[i] = (privateKey.key[i].toInt() xor publicKey.point[i].toInt()).toByte()
        }

        return sharedSecret
    }

    /**
     * Derive an AES key from a shared secret
     */
    private fun deriveAESKey(sharedSecret: ByteArray): ByteArray {
        // In a real implementation, you would use a proper KDF like HKDF
        // This is a simplified version for demonstration
        return sharedSecret.copyOf(32) // Use first 32 bytes as AES-256 key
    }
}



/**
 * Generates a 60-character identity string from a public key
 * @param pubkey The 32-byte public key
 * @param isLowerCase Whether to use lowercase or uppercase letters
 * @return A 60-character identity string
 */
fun getIdentityFromPublicKey(pubkey: ByteArray, isLowerCase: Boolean): String {
    // Copy input to local buffer
    val publicKey = ByteArray(32)
    System.arraycopy(pubkey, 0, publicKey, 0, 32)

    // Create buffer for identity characters
    val identity = CharArray(61) { 0.toChar() }

    // Process public key in 4 chunks of 8 bytes each
    val buffer = ByteBuffer.wrap(publicKey).order(ByteOrder.LITTLE_ENDIAN)
    for (i in 0 until 4) {
        // Extract 8-byte fragment as unsigned long
        var publicKeyFragment = buffer.getLong(i * 8).toULong()

        // Generate 14 characters from this fragment
        for (j in 0 until 14) {
            val charBase = if (isLowerCase) 'a'.code else 'A'.code
            identity[i * 14 + j] = (publicKeyFragment % 26u + charBase.toULong()).toInt().toChar()
            publicKeyFragment /= 26u
        }
    }

    // Calculate checksum with KangarooTwelve
    val checksumBytes = KangarooTwelve.hash(publicKey, 32)

    // Process checksum into an 18-bit value
    var identityBytesChecksum = 0
    for (i in 0 until 3) {
        identityBytesChecksum = identityBytesChecksum or ((checksumBytes[i].toInt() and 0xFF) shl (i * 8))
    }
    identityBytesChecksum = identityBytesChecksum and 0x3FFFF

    // Generate 4 checksum characters
    for (i in 0 until 4) {
        val charBase = if (isLowerCase) 'a'.code else 'A'.code
        identity[56 + i] = (identityBytesChecksum % 26 + charBase).toChar()
        identityBytesChecksum /= 26
    }

    // Return first 60 characters (excluding null terminator)
    return String(identity, 0, 60)
}


fun generateKeyPair(): Pair<ByteArray, ByteArray> {
    val identityUtil: IdentityUtil = IdentityUtil()
    val seed = "wqbdupxgcaimwdsnchitjmsplzclkqokhadgehdxqogeeiovzvadstt"
    val subSeed = identityUtil.getSubSeedFromSeed(seed)
    val privateKey = identityUtil.getPrivateKeyFromSubSeed(subSeed)
    val publicKey = identityUtil.getPublicKeyFromPrivateKey(privateKey)
    return Pair(publicKey, privateKey)
}