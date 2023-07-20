package com.android.mediproject.core.network.module

import android.content.Context
import com.android.mediproject.core.common.BuildConfig
import com.android.mediproject.core.network.R
import com.android.mediproject.core.network.tokens.TokenRequestInterceptor
import com.android.mediproject.core.network.tokens.TokenServer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.DelicateCoroutinesApi
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
import kotlin.properties.Delegates

@OptIn(DelicateCoroutinesApi::class)
@Module
@InstallIn(SingletonComponent::class)
class CertificateHelper @Inject constructor(@ApplicationContext context: Context) {

    var tmf: TrustManagerFactory by Delegates.notNull()
    var sslContext: SSLContext by Delegates.notNull()

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
            addInterceptor(
                HttpLoggingInterceptor().apply {
                    if (BuildConfig.DEBUG) {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                },
            )
            readTimeout(Duration.ofSeconds(10))
            connectTimeout(Duration.ofSeconds(10))
            callTimeout(Duration.ofSeconds(10))
            certificateHelper.apply {
                sslSocketFactory(sslContext.socketFactory, tmf.trustManagers[0] as X509TrustManager)
            }
            build()
        }
    }

    @Provides
    @Singleton
    @Named("okHttpClientWithAccessTokens")
    fun providesOkHttpClientWithAccessTokens(
        tokenServer: TokenServer,
    ): OkHttpClient = OkHttpClient.Builder().run {
        addInterceptor(
            HttpLoggingInterceptor().apply {
                if (BuildConfig.DEBUG) {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            },
        )
        addInterceptor(TokenRequestInterceptor(tokenServer, TokenRequestInterceptor.TokenType.AccessToken))
        build()
    }

    @Provides
    @Singleton
    @Named("okHttpClientWithReissueTokens")
    fun providesOkHttpClientWithReissueTokens(
        tokenServer: TokenServer,
    ): OkHttpClient = OkHttpClient.Builder().run {
        addInterceptor(
            HttpLoggingInterceptor().apply {
                if (BuildConfig.DEBUG) {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            },
        )
        addInterceptor(TokenRequestInterceptor(tokenServer, TokenRequestInterceptor.TokenType.RefreshToken))
        build()
    }


    @Provides
    @Singleton
    @Named("okHttpClientWithoutAny")
    fun providesOkHttpClientWithoutAny(): OkHttpClient {
        return OkHttpClient.Builder().run {
            addInterceptor(
                HttpLoggingInterceptor().apply {
                    if (BuildConfig.DEBUG) {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                },
            )
            build()
        }
    }
}
