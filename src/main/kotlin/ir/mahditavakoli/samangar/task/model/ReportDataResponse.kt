package ir.mahditavakoli.samangar.task.model

import com.example.tasks.model.remote.ReportTagsResponse


data class ReportDataResponse(
    val project: String? = null,
    val id: String? = null,
    val priority: Int? = null,
    val sortable: Boolean? = null,
    val editable: Boolean? = null,
    val tags: List<ReportTagsResponse> = listOf()
)