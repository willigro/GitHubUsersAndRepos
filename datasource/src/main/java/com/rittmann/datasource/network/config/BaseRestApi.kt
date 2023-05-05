package com.rittmann.datasource.network.config

import android.content.Context
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.net.SocketTimeoutException
import java.security.cert.CertificateException
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


object BaseRestApi {
    const val CACHE_SIZE: Long = 5 * 1024 * 1024 // 5MB

    fun getOkHttpClient(context: Context): OkHttpClient {
        try {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(
                    chain: Array<java.security.cert.X509Certificate>,
                    authType: String
                ) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(
                    chain: Array<java.security.cert.X509Certificate>,
                    authType: String
                ) {
                }

                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                    return arrayOf()
                }
            })
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            val sslSocketFactory = sslContext.socketFactory

            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier { hostname, session -> true }

            val requestTimeout = 5L
            val myCache = Cache(context.cacheDir, CACHE_SIZE)

            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            builder
                .cache(myCache)
                .readTimeout(requestTimeout, TimeUnit.SECONDS)
                .writeTimeout(requestTimeout, TimeUnit.SECONDS)
                .connectTimeout(requestTimeout, TimeUnit.SECONDS)

            return builder
//                .addInterceptor(Interceptor { chain -> onOnIntercept(chain) })
                .addInterceptor(interceptor)
                .addInterceptor(OkHttpExceptionInterceptor())
                .build()
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException(e)
        }
    }

    private class OkHttpExceptionInterceptor : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            try {
                return chain.proceed(chain.request())
            } catch (e: Throwable) {
                throw IOException()
            }
        }
    }

    @Throws(IOException::class)
    private  fun onOnIntercept(chain: Interceptor.Chain): Response {
        try {
            val response: Response = chain.proceed(chain.request())
            return response.newBuilder()
                .build()
        } catch (exception: SocketTimeoutException) {
            exception.printStackTrace()
        }
        return chain.proceed(chain.request())
    }
}

const val CONTENT_TYPE_JSON = "Content-Type:application/json"