package com.android.mediproject.core.network.retrofitext

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

internal class NetworkApiCallAdapterFactory : CallAdapter.Factory() {

    override fun get(returnType: Type, annotations: Array<out Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        if (Call::class.java != getRawType(returnType))
            return null


        check(returnType is ParameterizedType) {
            "반환 유형은 Call<NetworkApiResult<<Foo>> or Call<NetworkApiResult<out Foo>>로 매개변수화 되어야 합니다."
        }

        val responseType = getParameterUpperBound(0, returnType)
        if (getRawType(responseType) != NetworkApiResult::class.java)
            return null

        // the response type is ApiResponse and should be parameterized
        check(responseType is ParameterizedType) { "응답은 NetworkApiResult<Foo> or NetworkApiResult<out Foo>로 매개변수화 되어야 합니다." }

        val successBodyType = getParameterUpperBound(0, responseType)

        return NetworkApiCallAdapter<Any>(successBodyType)
    }
}

internal class NetworkApiCallAdapter<R>(
    private val successType: Type,
) : CallAdapter<R, Call<NetworkApiResult<R>>> {
    override fun adapt(call: Call<R>): Call<NetworkApiResult<R>> = NetworkApiCall(call, successType)

    override fun responseType(): Type = successType
}


internal class NetworkApiCall<R>(
    private val delegate: Call<R>,
    private val successType: Type,
) : Call<NetworkApiResult<R>> {

    override fun enqueue(callback: Callback<NetworkApiResult<R>>) = delegate.enqueue(
        object : Callback<R> {

            override fun onResponse(call: Call<R>, response: Response<R>) {
                val isSuccessful = response.isSuccessful
                val errorBody = response.errorBody()

                if (!isSuccessful) {
                    // errorBody가 null인 경우 -> UnknownError
                    // 아닌 경우 -> NetworkError
                    val throwable = Throwable(errorBody?.string() ?: "네트워크 응답 중 알수 없는 오류가 발생하였습니다!")
                    return callback.onResponse(
                        this@NetworkApiCall,
                        Response.success(
                            errorBody?.let { NetworkApiResult.Failure.ApiError(throwable) }
                                ?: NetworkApiResult.Failure.UnknownError(throwable),
                        ),
                    )
                }

                // body가 null인 경우 -> ApiError
                // 아닌 경우 -> Success
                return callback.onResponse(
                    this@NetworkApiCall,
                    Response.success(
                        response.body()?.let { NetworkApiResult.Success(it) }
                            ?: NetworkApiResult.Failure.UnknownError(
                                IllegalStateException(
                                    "네트워크 응답 결과 body가 null입니다!",
                                ),
                            ),
                    ),
                )
            }

            override fun onFailure(call: Call<R?>, throwable: Throwable) {
                callback.onResponse(this@NetworkApiCall, Response.success(NetworkApiResult.Failure.NetworkError(throwable)))
            }
        },
    )

    override fun isExecuted() = delegate.isExecuted

    override fun clone() = NetworkApiCall(delegate.clone(), successType)

    override fun isCanceled() = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<NetworkApiResult<R>> {
        throw UnsupportedOperationException("NetworkApiResult doesn't support execute")
    }

    override fun request(): Request = delegate.request()
    override fun timeout(): Timeout = delegate.timeout()

}
