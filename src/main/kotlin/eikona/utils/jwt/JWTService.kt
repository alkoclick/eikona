package eikona.utils.jwt

import arrow.core.Either
import com.auth0.jwt.JWT
import com.auth0.jwt.JWT.require
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import eikona.Config

object JWTService {

    private val defaultAlgorithm: Algorithm = Algorithm.HMAC256(Config.auth.secret)
    private val verifier = require(defaultAlgorithm)
        .acceptLeeway(5) // There's some clock skew with Auth0
        .build()

    fun decode(input: String): Either<Throwable, DecodedJWT> =
        Either.catch { verifier.verify(JWT.decode(input)) }
}
