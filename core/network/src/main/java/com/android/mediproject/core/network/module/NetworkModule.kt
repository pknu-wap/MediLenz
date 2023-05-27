package com.android.mediproject.core.network.module

import android.content.Context
import com.android.mediproject.core.network.BuildConfig
import com.android.mediproject.core.network.R
import com.android.mediproject.core.network.tokens.TokenInterceptor
import com.android.mediproject.core.network.tokens.TokenServer
import com.android.mediproject.core.network.tokens.TokenServerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.time.Duration
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
class CertificateHelper @Inject constructor(@ApplicationContext context: Context) {

    lateinit var tmf: TrustManagerFactory
    lateinit var sslContext: SSLContext

    init {
        try {
            context.resources.openRawResource(R.raw.gsrsaovsslca2018).use { caInput ->
                val cf: CertificateFactory = CertificateFactory.getInstance("X.509")
                val ca: Certificate = cf.generateCertificate(caInput)
                println("ca = ${(ca as X509Certificate).subjectDN}")

                val keyStoreType = KeyStore.getDefaultType()
                val keyStore = KeyStore.getInstance(keyStoreType)
                with(keyStore) {
                    load(null, null)
                    keyStore.setCertificateEntry("ca", ca)
                }

                val tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
                tmf = TrustManagerFactory.getInstance(tmfAlgorithm).apply {
                    init(keyStore)
                }

                sslContext = SSLContext.getInstance("TLS").apply {
                    init(null, tmf.trustManagers, java.security.SecureRandom())
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

@Module(includes = [DataGoKrNetwork::class, AwsNetwork::class])
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesOkHttpClient(certificateHelper: CertificateHelper): OkHttpClient {
        return OkHttpClient.Builder().run {
            addInterceptor(HttpLoggingInterceptor().apply {
                if (BuildConfig.DEBUG) {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            })
            readTimeout(Duration.ofSeconds(10))
            connectTimeout(Duration.ofSeconds(7))
            writeTimeout(Duration.ofSeconds(7))
            certificateHelper.apply {
                sslSocketFactory(
                    sslContext.socketFactory, tmf.trustManagers[0] as X509TrustManager
                )
            }
            build()
        }
    }

    @Provides
    @Singleton
    @Named("okHttpClientWithAccessTokens")
    fun providesOkHttpClientWithAccessTokens(
        tokenServer: TokenServer
    ): OkHttpClient = OkHttpClient.Builder().run {
        addInterceptor(HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.BODY
            }
        })
        addInterceptor(TokenInterceptor(tokenServer, TokenInterceptor.TokenType.ACCESS_TOKEN))
        readTimeout(Duration.ofSeconds(10))
        connectTimeout(Duration.ofSeconds(7))
        writeTimeout(Duration.ofSeconds(7))
        build()
    }

    @Provides
    @Singleton
    @Named("okHttpClientWithReissueTokens")
    fun providesOkHttpClientWithReissueTokens(
        tokenServer: TokenServer
    ): OkHttpClient = OkHttpClient.Builder().run {
        addInterceptor(HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.BODY
            }
        })
        addInterceptor(TokenInterceptor(tokenServer, TokenInterceptor.TokenType.REFRESH_TOKEN))
        readTimeout(Duration.ofSeconds(10))
        connectTimeout(Duration.ofSeconds(7))
        writeTimeout(Duration.ofSeconds(7))
        build()
    }

    @Provides
    @Singleton
    fun providesTokenServer(): TokenServer = TokenServerImpl()
}