package com.blicket.model.blockchain

import at.qubic.api.crypto.IdentityUtil
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security
import java.security.PublicKey
import java.security.PrivateKey
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import javax.crypto.Cipher
import at.qubic.api.crypto.IdentityUtil.*


class RSA {
    companion object {
        init {
            Security.addProvider(BouncyCastleProvider())
        }

        private const val RSA_ALGORITHM = "RSA"
        private const val RSA_TRANSFORMATION = "RSA/ECB/PKCS1Padding"
        private const val PROVIDER = "BC"

        private fun encrypt(data: ByteArray, publicKey: PublicKey): ByteArray {
            val cipher = Cipher.getInstance(RSA_TRANSFORMATION, PROVIDER)
            cipher.init(Cipher.ENCRYPT_MODE, publicKey)
            return cipher.doFinal(data)
        }

        fun encryptToBase64(plainText: String, publicKey: String): String {
            val encryptedBytes = encrypt(plainText.toByteArray(), publicKeyFromString(publicKey))
            return Base64.getEncoder().encodeToString(encryptedBytes)
        }

        private fun decrypt(encryptedData: ByteArray, privateKey: PrivateKey): ByteArray {
            val cipher = Cipher.getInstance(RSA_TRANSFORMATION, PROVIDER)
            cipher.init(Cipher.DECRYPT_MODE, privateKey)
            return cipher.doFinal(encryptedData)
        }

        fun decryptFromBase64(encryptedBase64: String, privateKey: String): String {
            val encryptedBytes = Base64.getDecoder().decode(encryptedBase64)
            val decryptedBytes = decrypt(encryptedBytes, privateKeyFromString(privateKey))
            return String(decryptedBytes)
        }

        private fun privateKeyFromString(key: String): PrivateKey {
            val privateBytes: ByteArray = Base64.getDecoder().decode(key)
            val keySpec = PKCS8EncodedKeySpec(privateBytes)
            val keyFactory: KeyFactory = KeyFactory.getInstance(RSA_ALGORITHM)
            return keyFactory.generatePrivate(keySpec)
        }

        private fun publicKeyFromString(key: String): PublicKey {
            val publicBytes: ByteArray = Base64.getDecoder().decode(key)
            val keySpec = X509EncodedKeySpec(publicBytes)
            val keyFactory: KeyFactory = KeyFactory.getInstance(RSA_ALGORITHM)
            return keyFactory.generatePublic(keySpec)
        }

        fun generateKeyPair(): Pair<ByteArray, ByteArray> {
            val identityUtil: IdentityUtil = IdentityUtil()
            val seed = "wqbdupxgcaimwdsnchitjmsplzclkqokhadgehdxqogeeiovzvadstt"
            val subSeed = identityUtil.getSubSeedFromSeed(seed)
            val privateKey = identityUtil.getPrivateKeyFromSubSeed(subSeed)
            val publicKey = identityUtil.getPublicKeyFromPrivateKey(privateKey)
            println(publicKey.size)
            println(privateKey.size)
            return Pair(publicKey, privateKey)
        }
    }
}
