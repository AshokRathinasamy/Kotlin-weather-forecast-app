package com.mvvm.intelegenciamachinetest.data.source.remote

import com.mvvm.intelegenciamachinetest.data.Result
import com.mvvm.intelegenciamachinetest.data.Result.Success
import retrofit2.Response
import java.io.IOException
import java.lang.Exception

abstract class SafeApiRequest {

    suspend fun<T: Any> apiRequest(call: suspend () -> Response<T>): Result<T> {
        try {
            val response = call.invoke()
            if (response.isSuccessful) {
                return Result.Success(response.body()!!)
            } else {
                return Result.Error(response.code().toString(), response.message().toString())
            }
        } catch (e : Exception){
            return Result.Error("400", e.message!!)
        }
    }
}

class ApiException(message: String) : IOException(message)