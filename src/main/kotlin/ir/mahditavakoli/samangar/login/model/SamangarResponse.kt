package ir.mahditavakoli.samangar.login.model

data class SamangarResponse<T>(
    val status: String? = null,
    val data: T,
    val message: String? = null
)