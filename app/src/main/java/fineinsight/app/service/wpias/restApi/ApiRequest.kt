package fineinsight.app.service.wpias.restApi

import com.google.gson.GsonBuilder
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiRequest {

    private var retrofit : Retrofit? = null

    private var client = OkHttpClient()

    fun getResponse(baseURL: String): Retrofit? {

        client = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .addInterceptor { chain ->
                var requestOrigin = chain.request()

                var authToken = Credentials.basic("WPIAS","WPIAS0916")

                var requestBuilder = requestOrigin.newBuilder()
                    .header("Authorization", authToken)
                    .method(requestOrigin.method(), requestOrigin.body())

                var request = requestBuilder.build()

                chain.proceed(request)
            }.build()

        if (retrofit == null) {

            retrofit = Retrofit.Builder()
                .baseUrl(baseURL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        }

        return retrofit

    }


    fun getStringResponse(baseURL : String) : Retrofit?{

        var gson = GsonBuilder()
            .setLenient()
            .create()

        client = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .addInterceptor { chain ->
                var requestOrigin = chain.request()

                var authToken = Credentials.basic("WPIAS","WPIAS0916")

                var requestBuilder = requestOrigin.newBuilder()
                    .header("Authorization", authToken)
                    .method(requestOrigin.method(), requestOrigin.body())

                var request = requestBuilder.build()

                chain.proceed(request)
            }.build()

        if (retrofit == null) {

            retrofit = Retrofit.Builder()
                .baseUrl(baseURL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        }

        return retrofit

    }


    fun getInterAction(baseURL : String) : Retrofit? {

        if (retrofit == null) {

            retrofit = Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        }

        return retrofit
    }
}