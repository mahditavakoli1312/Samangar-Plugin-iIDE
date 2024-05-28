package ir.mahditavakoli.samangar.login.model

data class UserResponse(
    val id: String,
    val organ: List<String>,
    val role: Int,
    val token: String
)