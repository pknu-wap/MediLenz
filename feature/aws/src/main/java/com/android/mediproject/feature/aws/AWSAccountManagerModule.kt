package com.android.mediproject.feature.aws

import android.content.Context
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.regions.Regions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AWSAccountManagerModule {

    private val userPoolId = "us-east-2_mYPkrflCj"
    private val userClientId = "776ei62ntqa54shn1nanajofs4"
    private val userClientSecretKey = "gvbg7oisijo8aqlu4o09hcja9ghmiso4dj65bvugca8qkd55ijo"
    private val regions = Regions.US_EAST_2

    @Provides
    @Singleton
    internal fun providesUserPool(@ApplicationContext context: Context) =
        CognitoUserPool(context, userPoolId, userClientId, userClientSecretKey, regions)

    @Provides
    fun providesSingUpAWS(userPool: CognitoUserPool): SignUpAWS = SignUpAWSImpl(userPool)

    @Provides
    fun providesSignInOutAWS(userPool: CognitoUserPool): SignInOutAWS = SignInOutAWSImpl(userPool)

}
