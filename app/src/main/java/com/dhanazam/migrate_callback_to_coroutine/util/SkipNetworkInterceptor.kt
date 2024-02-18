package com.dhanazam.migrate_callback_to_coroutine.util

import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

private val FAKE_RESULTS = listOf(
    "Hello, coroutines!",
    "My favorite feature",
    "Async made easy",
    "Coroutines by example",
    "Check out the Advanced Coroutines codelab next!"
)

class SkipNetworkInterceptor: Interceptor {
    private var lastResult: String = ""
    private val gson = Gson()
    private var attempts = 0

    private fun wantRandomError() = attempts++ % 5 == 0

    override fun intercept(chain: Interceptor.Chain): Response {
        pretendToBlockNetworkRequest()
        return if (wantRandomError()) {
            makeErrorResult(chain.request())
        }
    }

    private fun pretendToBlockNetworkRequest() = Thread.sleep(500)

    private fun makeErrorResult(request: Request): Response {
        return
    }
}