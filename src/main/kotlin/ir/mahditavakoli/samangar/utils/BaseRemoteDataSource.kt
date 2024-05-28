package ir.mahditavakoli.samangar.utils

import com.example.common.NetworkState
import com.example.common.ResultWrapper
import com.example.data.network.NetworkException
import com.example.data.response.ErrorBodyResponse
import com.google.gson.GsonBuilder
import retrofit2.Response


private fun <T> Response<T>.bodyOrThrow(): T {
    if (isSuccessful)
        return body() ?: throw NullPointerException()
    else {
        val errorBody = errorBody()?.string()
        var errorMessage = ""

        errorBody?.let {
            errorMessage = try {
                GsonBuilder().serializeNulls().create().fromJson(errorBody, ErrorBodyResponse::class.java).message
            } catch (e: Exception) {
                e.message.toString()
            }
        }

        throw NetworkException(
            serverMessage = errorMessage, code = code()
        )
    }
}

suspend fun <T> safeCall(api: suspend () -> Response<T>): ResultWrapper<T> {
    return try {
        ResultWrapper.Success(resultData = api.invoke().bodyOrThrow())
    } catch (exception: Exception) {
        when (exception) {
            is NetworkException -> {
                ResultWrapper.Failure(
                    errorType = NetworkState.SERVER_ERROR,
                    message = exception.serverMessage,
                    code = exception.code,
                )
            }

            else -> {
                ResultWrapper.Failure(
                    errorType = NetworkState.APP_ERROR,
                    message = exception.localizedMessage ?: "",
                )
            }
        }
    }
}
