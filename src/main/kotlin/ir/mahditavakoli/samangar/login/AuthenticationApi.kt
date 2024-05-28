package ir.mahditavakoli.samangar.login

import ir.mahditavakoli.samangar.login.model.AuthenticationRequest
import ir.mahditavakoli.samangar.login.model.SamangarResponse
import ir.mahditavakoli.samangar.login.model.UserResponse
import ir.mahditavakoli.samangar.utils.ApiLogic
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationApi {

    @POST(ApiLogic.LOGIN)
    suspend fun login(
        @Body authenticationRequest: AuthenticationRequest,
    ): Response<SamangarResponse<UserResponse?>>

}