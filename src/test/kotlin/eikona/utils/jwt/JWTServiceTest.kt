package eikona.utils.jwt

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue


class JWTServiceTest {

    @Test
    fun notJWT() {
        assertTrue(JWTService.decode("abc").isLeft())
        assertTrue(JWTService.decode("abc.123").isLeft())
        assertTrue(JWTService.decode("abc.123.asd").isLeft())
        assertTrue(JWTService.decode("aksdlvasdiovyqv89p4512639v8nnesihlvnfsdiovfynwe9pfv23wynvirysiovyrfsdvnifyhsd").isLeft())
        assertTrue(JWTService.decode("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ").isLeft())
    }

    @Test
    fun illegalJWTAlgo() {
        assertTrue(
            JWTService.decode("eyJhbGciOiJIUzM4NCIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIn0.YHx93SMpPbde8iW5EOCkgLGPSqnl6rav6Q0_24UuQDiT6FKl5vfhQb1Ls42teMQA").isLeft()
        )
    }

    @Test
    fun unverifiableJWT() {
        assertTrue(
            JWTService.decode("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c").isLeft()
        )
    }

    @Test
    fun validJWT() {
        assertTrue(JWTService.decode("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIn0.07xXPajxnemrX3qoGg3R2vvAmA70pAisgzJN2aKIwLE").isRight())
    }
}