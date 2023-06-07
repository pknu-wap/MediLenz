package com.android.mediproject.core.network.module

import com.android.mediproject.core.common.BuildConfig
import com.android.mediproject.core.datastore.TokenDataSourceImpl
import com.android.mediproject.core.model.comments.CommentChangedResponse
import com.android.mediproject.core.model.comments.CommentListResponse
import com.android.mediproject.core.model.medicine.InterestedMedicine.InterestedMedicineListResponse
import com.android.mediproject.core.model.medicine.MedicineIdResponse
import com.android.mediproject.core.model.remote.sign.SignInResponse
import com.android.mediproject.core.model.remote.sign.SignUpResponse
import com.android.mediproject.core.model.remote.token.ReissueTokenResponse
import com.android.mediproject.core.model.requestparameters.ChangeNicknameParameter
import com.android.mediproject.core.model.requestparameters.ChangePasswordParamter
import com.android.mediproject.core.model.requestparameters.ChangePasswordRequestParameter
import com.android.mediproject.core.model.requestparameters.DeleteCommentParameter
import com.android.mediproject.core.model.requestparameters.EditCommentParameter
import com.android.mediproject.core.model.requestparameters.GetMedicineIdParameter
import com.android.mediproject.core.model.requestparameters.LikeCommentParameter
import com.android.mediproject.core.model.requestparameters.NewCommentParameter
import com.android.mediproject.core.model.user.remote.ChangeNicknameResponse
import com.android.mediproject.core.model.user.remote.ChangePasswordResponse
import com.android.mediproject.core.model.user.remote.WithdrawalResponse
import com.android.mediproject.core.network.datasource.comments.CommentsDataSource
import com.android.mediproject.core.network.datasource.comments.CommentsDataSourceImpl
import com.android.mediproject.core.network.datasource.interestedmedicine.InterestedMedicineDataSource
import com.android.mediproject.core.network.datasource.interestedmedicine.InterestedMedicineDataSourceImpl
import com.android.mediproject.core.network.datasource.medicineid.MedicineIdDataSource
import com.android.mediproject.core.network.datasource.medicineid.MedicineIdDataSourceImpl
import com.android.mediproject.core.network.datasource.sign.SignDataSource
import com.android.mediproject.core.network.datasource.sign.SignDataSourceImpl
import com.android.mediproject.core.network.datasource.user.UserDataSource
import com.android.mediproject.core.network.datasource.user.UserDataSourceImpl
import com.android.mediproject.core.network.parameter.SignInRequestParameter
import com.android.mediproject.core.network.parameter.SignUpRequestParameter
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AwsNetwork {

    @Provides
    @Singleton
    fun provideInterestedMedicineDatasource(
        @Named("awsNetworkApiWithAccessTokens") awsNetworkApi: AwsNetworkApi
    ): InterestedMedicineDataSource = InterestedMedicineDataSourceImpl(awsNetworkApi)

    @Provides
    @Named("awsNetworkApiWithAccessTokens")
    @Singleton
    fun providesAwsNetworkApi(
        @Named("okHttpClientWithAccessTokens") okHttpClient: OkHttpClient,
    ): AwsNetworkApi =
        Retrofit.Builder().client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(BuildConfig.AWS_BASE_URL).build().create(AwsNetworkApi::class.java)

    @Provides
    @Named("awsNetworkApiWithRefreshTokens")
    fun providesReissueTokenAwsNetworkApi(
        @Named("okHttpClientWithReissueTokens") okHttpClient: OkHttpClient,
    ): AwsNetworkApi =
        Retrofit.Builder().client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(BuildConfig.AWS_BASE_URL).build().create(AwsNetworkApi::class.java)

    @Provides
    @Singleton
    fun providesCommentsDataSource(@Named("awsNetworkApiWithAccessTokens") awsNetworkApi: AwsNetworkApi): CommentsDataSource =
        CommentsDataSourceImpl(awsNetworkApi)

    @Provides
    fun providesSignDataSource(
        @Named("awsNetworkApiWithRefreshTokens") awsNetworkApi: AwsNetworkApi,
        tokenDataSourceImpl: TokenDataSourceImpl,
    ): SignDataSource = SignDataSourceImpl(awsNetworkApi, tokenDataSourceImpl)

    @Provides
    @Singleton
    fun providesGetMedicineIdDataSource(@Named("awsNetworkApiWithAccessTokens") awsNetworkApi: AwsNetworkApi): MedicineIdDataSource =
        MedicineIdDataSourceImpl(awsNetworkApi)

    @Provides
    @Singleton
    fun providesUserDataSource(@Named("awsNetworkApiWithAccessTokens") awsNetworkApi: AwsNetworkApi): UserDataSource =
        UserDataSourceImpl(awsNetworkApi)
}

interface AwsNetworkApi {

    @GET(value = "medicine/favorite")
    suspend fun getInterestedMedicineList(): Response<InterestedMedicineListResponse>

    @POST(value = "user/register")
    suspend fun signUp(
        @Body signUpRequestParameter: SignUpRequestParameter
    ): Response<SignUpResponse>

    @POST(value = "user/login")
    suspend fun signIn(
        @Body signInRequestParameter: SignInRequestParameter
    ): Response<SignInResponse>


    @POST(value = "user/reissue")
    suspend fun reissueTokens(
    ): Response<ReissueTokenResponse>

    /**
     * 특정 약에 대한 댓글 목록 조회
     */
    @GET(value = "medicine/comment")
    suspend fun getComments(
        @Query("medicineId", encoded = true) medicineId: Long,
    ): Response<CommentListResponse>

    /**
     * 댓글 수정
     */
    @PATCH(value = "medicine/comment")
    suspend fun editComment(
        @Body editCommentParameter: EditCommentParameter
    ): Response<CommentChangedResponse>

    /**
     * 댓글 삭제
     */
    @DELETE(value = "medicine/comment")
    suspend fun deleteComment(
        @Body deleteCommentParameter: DeleteCommentParameter
    ): Response<CommentChangedResponse>

    /**
     * 댓글 좋아요
     */
    @POST(value = "medicine/comment")
    suspend fun likeComment(
        @Body likeCommentParameter: LikeCommentParameter
    ): Response<CommentChangedResponse>

    /**
     * 댓글 등록
     */
    @POST(value = "medicine/comment/writeTest")
    suspend fun applyNewComment(
        @Body newCommentParameter: NewCommentParameter
    ): Response<CommentChangedResponse>

    /**
     * 닉네임 변경
     */
    @PATCH(value = "user")
    suspend fun changeNickname(
        @Body changeNicknameParameter: ChangeNicknameParameter
    ): Response<ChangeNicknameResponse>

    /**
     * 비밀번호 변경
     */
    @PATCH(value = "user")
    suspend fun changePassword(
        @Body changePasswordRequestParameter: ChangePasswordRequestParameter
    ): Response<ChangePasswordResponse>

    /**
     * 회원탈퇴
     */
    @DELETE(value = "user")
    suspend fun withdrawal(): Response<WithdrawalResponse>

    /**
     * 약 ID 조회
     */
    @HTTP(method = "POST", path = "medicine/comment", hasBody = true)
    suspend fun getMedicineId(
        @Body getMedicineIdParameter: GetMedicineIdParameter
    ): Response<MedicineIdResponse>
}