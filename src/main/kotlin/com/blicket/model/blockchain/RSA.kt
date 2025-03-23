package com.blicket.model.blockchain

import at.qubic.api.crypto.IdentityUtil
import at.qubic.api.crypto.KangarooTwelve
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import java.math.BigInteger

import java.nio.ByteBuffer
import java.nio.ByteOrder


class FourQPrivateKey(val key: ByteArray) {
    init {
        require(key.size == FourQParams.KEY_SIZE_BYTES) { "Private key must be ${FourQParams.KEY_SIZE_BYTES} bytes" }
    }
}

class FourQPublicKey(val point: ByteArray) {
    init {
        require(point.size == FourQParams.POINT_SIZE_BYTES) { "Public key must be ${FourQParams.POINT_SIZE_BYTES} bytes" }
    }
}

object FourQParams {
    const val KEY_SIZE_BYTES = 32
    const val POINT_SIZE_BYTES = 32
    val CURVE_ORDER = "73EDA753299D7D483339D80809A1D80553BDA402FFFE5BFEFFFFFFFF00000001".toBigInteger(16)
}

object FourQCrypto {
    private val secureRandom = SecureRandom()

    private fun generateKeyPair(): Pair<FourQPrivateKey, FourQPublicKey> {
        val privateKeyBytes = ByteArray(FourQParams.KEY_SIZE_BYTES)
        secureRandom.nextBytes(privateKeyBytes)

        var scalar = BigInteger(1, privateKeyBytes) % FourQParams.CURVE_ORDER
        if (scalar <= BigInteger.ZERO) scalar += FourQParams.CURVE_ORDER

        val privateKey = FourQPrivateKey(scalar.toByteArray().takeLast(FourQParams.KEY_SIZE_BYTES).toByteArray())
        return Pair(privateKey, derivePublicKey(privateKey))
    }

    private fun derivePublicKey(privateKey: FourQPrivateKey): FourQPublicKey {
        val publicKeyPoint = ByteArray(FourQParams.POINT_SIZE_BYTES)

        privateKey.key.forEachIndexed { index, byte ->
            publicKeyPoint[index % FourQParams.POINT_SIZE_BYTES] =
                (publicKeyPoint[index % FourQParams.POINT_SIZE_BYTES].toInt() xor byte.toInt()).toByte()
        }

        return FourQPublicKey(publicKeyPoint)
    }

    fun encrypt(plaintext: ByteArray, recipientPublicKey: FourQPublicKey): ByteArray {
        val (ephemeralPrivate, ephemeralPublic) = generateKeyPair()
        val sharedSecret = computeSharedSecret(ephemeralPrivate, recipientPublicKey)
        val aesKey = deriveAESKey(sharedSecret)
        val iv = ByteArray(12)
        secureRandom.nextBytes(iv)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val secretKeySpec = SecretKeySpec(aesKey, "AES")
        val gcmParameterSpec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmParameterSpec)

        val ciphertext = cipher.doFinal(plaintext)

        val result = ByteArray(ephemeralPublic.point.size + iv.size + ciphertext.size)
        System.arraycopy(ephemeralPublic.point, 0, result, 0, ephemeralPublic.point.size)
        System.arraycopy(iv, 0, result, ephemeralPublic.point.size, iv.size)
        System.arraycopy(ciphertext, 0, result, ephemeralPublic.point.size + iv.size, ciphertext.size)

        return result
    }

    fun decrypt(encryptedData: ByteArray, recipientPrivateKey: FourQPrivateKey): ByteArray {
        val ephemeralPublicBytes = encryptedData.copyOfRange(0, FourQParams.POINT_SIZE_BYTES)
        val iv = encryptedData.copyOfRange(FourQParams.POINT_SIZE_BYTES, FourQParams.POINT_SIZE_BYTES + 12)
        val ciphertext = encryptedData.copyOfRange(FourQParams.POINT_SIZE_BYTES + 12, encryptedData.size)

        val ephemeralPublic = FourQPublicKey(ephemeralPublicBytes)
        val sharedSecret = computeSharedSecret(recipientPrivateKey, ephemeralPublic)

        val aesKey = deriveAESKey(sharedSecret)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val secretKeySpec = SecretKeySpec(aesKey, "AES")
        val gcmParameterSpec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec)

        return cipher.doFinal(ciphertext)
    }

    private fun computeSharedSecret(privateKey: FourQPrivateKey, publicKey: FourQPublicKey): ByteArray {
        val sharedSecret = ByteArray(FourQParams.POINT_SIZE_BYTES)
        for (i in 0 until FourQParams.POINT_SIZE_BYTES) {
            sharedSecret[i] = (privateKey.key[i].toInt() xor publicKey.point[i].toInt()).toByte()
        }

        return sharedSecret
    }

    private fun deriveAESKey(sharedSecret: ByteArray): ByteArray {
        return sharedSecret.copyOf(32)
    }
}

fun getIdentityFromPublicKey(pubkey: ByteArray, isLowerCase: Boolean): String {
    val publicKey = ByteArray(32)
    System.arraycopy(pubkey, 0, publicKey, 0, 32)
    val identity = CharArray(61) { 0.toChar() }

    val buffer = ByteBuffer.wrap(publicKey).order(ByteOrder.LITTLE_ENDIAN)
    for (i in 0 until 4) {
        var publicKeyFragment = buffer.getLong(i * 8).toULong()
        for (j in 0 until 14) {
            val charBase = if (isLowerCase) 'a'.code else 'A'.code
            identity[i * 14 + j] = (publicKeyFragment % 26u + charBase.toULong()).toInt().toChar()
            publicKeyFragment /= 26u
        }
    }

    val checksumBytes = KangarooTwelve.hash(publicKey, 32)
    var identityBytesChecksum = 0
    for (i in 0 until 3) {
        identityBytesChecksum = identityBytesChecksum or ((checksumBytes[i].toInt() and 0xFF) shl (i * 8))
    }
    identityBytesChecksum = identityBytesChecksum and 0x3FFFF

    for (i in 0 until 4) {
        val charBase = if (isLowerCase) 'a'.code else 'A'.code
        identity[56 + i] = (identityBytesChecksum % 26 + charBase).toChar()
        identityBytesChecksum /= 26
    }

    return String(identity, 0, 60)
}


fun generateKeyPair(): Pair<ByteArray, ByteArray> {
    val identityUtil = IdentityUtil()
    val seed = getSeed()
    val subSeed = identityUtil.getSubSeedFromSeed(seed)
    val privateKey = identityUtil.getPrivateKeyFromSubSeed(subSeed)
    val publicKey = identityUtil.getPublicKeyFromPrivateKey(privateKey)
    return Pair(publicKey, privateKey)
}

fun verifyKeys(privateKey: ByteArray, public: ByteArray): Boolean =
    IdentityUtil().getPublicKeyFromPrivateKey(privateKey).contentEquals(public)