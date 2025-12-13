package com.zakhardev.todolist.todos_list.data.api

import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response

class AuthInterceptor(
    private val token: String
) : Interceptor {
    override fun intercept(chain: Chain): Response {
        val req = chain.request().newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
        return chain.proceed(req)
    }
}
