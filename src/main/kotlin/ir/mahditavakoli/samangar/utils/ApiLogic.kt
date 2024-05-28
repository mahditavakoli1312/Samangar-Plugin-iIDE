package ir.mahditavakoli.samangar.utils

import com.example.common.ResultWrapper
import ir.mahditavakoli.samangar.task.model.RecordDataResponse
import com.example.tasks.model.remote.ReportRequest
import com.google.gson.GsonBuilder
import ir.mahditavakoli.samangar.login.AuthenticationApi
import ir.mahditavakoli.samangar.login.model.AuthenticationRequest
import ir.mahditavakoli.samangar.login.model.SamangarResponse
import ir.mahditavakoli.samangar.login.model.UserResponse
import ir.mahditavakoli.samangar.task.ReportApi
import ir.mahditavakoli.samangar.task.model.ReportDataResponse
import ir.mahditavakoli.samangar.task.model.SamangarListResponse
import retrofit2.Retrofit


import retrofit2.converter.gson.GsonConverterFactory


class ApiLogic {

    private val apiService: AuthenticationApi
    private val reportService: ReportApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
            .build()

        apiService = retrofit.create(AuthenticationApi::class.java)
        reportService = retrofit.create(ReportApi::class.java)
    }

    companion object {
        const val BASE_API_URL = "https://samangar.partdp.ai/"
        const val LOGIN = "app/auth/login"
        const val CREATE_TASK = "app/record/records"
        const val GET_PROJECT_WITH_USER_VIA_TAG = "app/tag/user-tags"

    }

    suspend fun login(username: String, password: String): ResultWrapper<SamangarResponse<UserResponse?>> {
        return safeCall {
            apiService.login(
                AuthenticationRequest(
                    username = username,
                    password = password
                )
            )
        }
    }

    suspend fun createTask(
        token: String,
        reportRequest: ReportRequest
    ): ResultWrapper<SamangarResponse<RecordDataResponse?>> {
        println("retrofit-api : token => $token , reportRequest => $reportRequest  ")
        return safeCall {
            reportService.createReport(
                token = token,
                reportRequest = reportRequest
            )
        }
    }

    suspend fun getAllProjectsOfUserViaTags(
        token: String,
    ): ResultWrapper<SamangarListResponse<ReportDataResponse>> =
        safeCall {
            reportService.getAllProjectsOfUserViaTags(
                token = token,
            )
        }

}