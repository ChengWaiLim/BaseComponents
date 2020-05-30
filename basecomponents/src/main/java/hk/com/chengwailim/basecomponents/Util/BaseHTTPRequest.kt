package hk.com.chengwailim.basecomponents.Util

import android.app.Activity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull

abstract class BaseHTTPRequest(private val context: Activity) {
    protected lateinit var client: OkHttpClient.Builder
    open protected fun request(
        url: String,
        method: Method,
        queryParameter: Map<String, String> = emptyMap<String, String>(),
        headers: Map<String, String> = emptyMap<String, String>(),
        body: JSONObject = JSONObject(),
        callback: (JSONObject) -> Unit,
        onFailCallback:(exception: Exception)->Unit
    ){
        client = OkHttpClient.Builder().connectTimeout(10, TimeUnit.MINUTES).readTimeout(10, TimeUnit.MINUTES).writeTimeout(10, TimeUnit.MINUTES)
        val httpBuilder = url.toHttpUrlOrNull()?.newBuilder()
        queryParameter.forEach { key, value->
            httpBuilder?.addQueryParameter(key, value)
        }
        if(httpBuilder == null) onFailCallback.invoke(Exception("Failed to connect the host"))
        else{
            val builder = Request.Builder().url(httpBuilder.build())
            headers.forEach { key, value->
                builder.addHeader(key, value)
            }
            when(method){
                Method.GET->builder.get()
                Method.POST->{
                    builder.post(
                        RequestBody.create(
                            "application/json; charset=utf-8".toMediaTypeOrNull(),
                            body.toString()
                        ))
                }
            }
            client.build().newCall(builder.build()).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    onFailCallback.invoke(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        response.use {
                            if (!response.isSuccessful)
                                onFailCallback.invoke(IOException("Unexpected code $response"))
                            else{
                                val result = JSONObject(response.body!!.string())
                                callback.invoke(result)
                            }
                        }
                    }catch (e: Exception){
                        onFailCallback.invoke(e)
                    }
                }
            })
        }
    }

    enum class Method{
        POST, GET
    }
}