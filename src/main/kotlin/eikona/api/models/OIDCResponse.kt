package eikona.api.models

data class OIDCResponse(
    val access_token: String,
    val id_token: String,
    val scope: String = "",
    val expires_in: Int,
)

data class OAuthIdTokenPayload(
    val aud: String,
    val iss: String,
    val name: String,
    val sub: String,
)