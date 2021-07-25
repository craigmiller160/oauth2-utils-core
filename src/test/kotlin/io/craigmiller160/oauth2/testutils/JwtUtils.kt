package io.craigmiller160.oauth2.testutils

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.KeyUse
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.interfaces.RSAPublicKey
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

object JwtUtils {

    const val ROLE_1 = "ROLE_1"
    const val ROLE_2 = "ROLE_2"
    const val USERNAME = "username"
    const val ROLES_CLAIM = "roles"
    const val CLIENT_KEY = "clientKey"
    const val CLIENT_NAME = "clientName"
    const val FIRST_NAME = "firstName"
    const val LAST_NAME = "lastName"
    const val TOKEN_ID = "JWTID"
    const val EMAIL = "email"

    fun createKeyPair(): KeyPair {
        val keyPairGen = KeyPairGenerator.getInstance("RSA")
        return keyPairGen.genKeyPair()
    }

    fun createJwkSet(keyPair: KeyPair): JWKSet {
        val builder = RSAKey.Builder(keyPair.public as RSAPublicKey)
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS256)
                .keyID("oauth-jwt")
        return JWKSet(builder.build())
    }

    fun createJwt(expMinutes: Long = 100): SignedJWT {
        val header = JWSHeader.Builder(JWSAlgorithm.RS256)
                .build()

        val exp = ZonedDateTime.now(ZoneId.of("UTC")).plusMinutes(expMinutes)
        val expDate = Date.from(exp.toInstant())

        val claims = JWTClaimsSet.Builder()
                .jwtID(TOKEN_ID)
                .issueTime(Date())
                .subject(USERNAME)
                .expirationTime(expDate)
                .claim(ROLES_CLAIM, listOf(ROLE_1, ROLE_2))
                .claim("clientKey", CLIENT_KEY)
                .claim("clientName", CLIENT_NAME)
                .claim("firstName", FIRST_NAME)
                .claim("lastName", LAST_NAME)
                .claim("userEmail", EMAIL)
                .build()
        return SignedJWT(header, claims)
    }

    fun signAndSerializeJwt(jwt: SignedJWT, privateKey: PrivateKey): String {
        val signer = RSASSASigner(privateKey)
        jwt.sign(signer)
        return jwt.serialize()
    }

}