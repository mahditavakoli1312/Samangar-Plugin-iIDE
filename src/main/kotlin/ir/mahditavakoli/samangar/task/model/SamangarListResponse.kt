package ir.mahditavakoli.samangar.task.model

data class SamangarListResponse<T>(
    val status: String? = null,
    val data: List<T> = listOf(),
    val message: String? = null
)