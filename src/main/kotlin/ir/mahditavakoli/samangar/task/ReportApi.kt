package ir.mahditavakoli.samangar.task


import ir.mahditavakoli.samangar.task.model.RecordDataResponse
import com.example.tasks.model.remote.ReportRequest
import ir.mahditavakoli.samangar.login.model.SamangarResponse
import ir.mahditavakoli.samangar.task.model.SamangarListResponse
import ir.mahditavakoli.samangar.task.model.ReportDataResponse
import ir.mahditavakoli.samangar.utils.ApiLogic
import retrofit2.Response
import retrofit2.http.*

interface ReportApi {

    @GET(ApiLogic.GET_PROJECT_WITH_USER_VIA_TAG)
    suspend fun getAllProjectsOfUserViaTags(@Header("token") token: String): Response<SamangarListResponse<ReportDataResponse>>


    @POST(ApiLogic.CREATE_TASK)
    suspend fun createReport(
        @Header("token") token: String,
        @Body reportRequest: ReportRequest
    ): Response<SamangarResponse<RecordDataResponse?>>
}