package ir.mahditavakoli.samangar.task.model

data class ReportProjectEntity(
    val id: String,
    val project: String,
    val priority: Int? = null,
    val sortable: Boolean? = null,
    val editable: Boolean? = null,
)

fun ReportDataResponse.toReportProjectEntity(): ReportProjectEntity? {
    if (
        id.isNullOrBlank() ||
        project.isNullOrBlank()
    ) return null
    return ReportProjectEntity(
        id = id,
        project = project,
        priority = priority,
        sortable = sortable,
        editable = editable
    )
}
