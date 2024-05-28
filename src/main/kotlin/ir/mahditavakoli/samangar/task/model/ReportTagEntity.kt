package ir.mahditavakoli.samangar.task.model

import com.example.tasks.model.remote.ReportTagsResponse

data class ReportTagEntity(
    val id: String,
    val projectId: String,
    val name: String,
    val priority: Int? = null,
    val editable: Boolean,
    val active: Boolean = true
)

fun ReportTagsResponse.toReportTagEntity(projectId: String): ReportTagEntity? {
    if (id.isNullOrEmpty() || name.isNullOrEmpty() || editable == null) return null
    return ReportTagEntity(
        id = id,
        projectId = projectId,
        name = name,
        priority = priority,
        editable = editable
    )
}