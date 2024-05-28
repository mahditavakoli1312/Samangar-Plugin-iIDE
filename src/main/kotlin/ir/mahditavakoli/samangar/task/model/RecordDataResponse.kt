package ir.mahditavakoli.samangar.task.model

import com.example.tasks.model.remote.RecordIssueResponse


data class RecordDataResponse(
    val id: String? = null,
    val project: String? = null,
    val tag: String? = null,
    val date: String? = null,
    val start: String? = null,
    val end: String? = null,
    val description: String? = null,
    val issue: RecordIssueResponse? = RecordIssueResponse()
)