package com.android.mediproject.core.network.module

import android.content.Context
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.regions.Regions
import com.android.mediproject.core.common.BuildConfig
import com.android.mediproject.core.common.util.AesCoder
import com.android.mediproject.core.model.comments.CommentChangedResponse
import com.android.mediproject.core.model.comments.CommentListResponse
import com.android.mediproject.core.model.comments.LikeResponse
import com.android.mediproject.core.model.comments.MyCommentsListResponse
import com.android.mediproject.core.model.favoritemedicine.CheckFavoriteMedicineResponse
import com.android.mediproject.core.model.favoritemedicine.DeleteFavoriteMedicineResponse
import com.android.mediproject.core.model.favoritemedicine.FavoriteMedicineListResponse
import com.android.mediproject.core.model.favoritemedicine.NewFavoriteMedicineResponse
import com.android.mediproject.core.model.medicine.MedicineIdResponse
import com.android.mediproject.core.model.requestparameters.AddFavoriteMedicineParameter
import com.android.mediproject.core.model.requestparameters.ChangeNicknameParameter
import com.android.mediproject.core.model.requestparameters.ChangePasswordParameter
import com.android.mediproject.core.model.requestparameters.DeleteCommentParameter
import com.android.mediproject.core.model.requestparameters.EditCommentParameter
import com.android.mediproject.core.model.requestparameters.GetMedicineIdParameter
import com.android.mediproject.core.model.requestparameters.NewCommentParameter
import com.android.mediproject.core.model.user.remote.ChangeNicknameResponse
import com.android.mediproject.core.model.user.remote.ChangePasswordResponse
import com.android.mediproject.core.model.user.remote.WithdrawalResponse
import com.android.mediproject.core.network.datasource.comments.CommentsDataSource
import com.android.mediproject.core.network.datasource.comments.CommentsDataSourceImpl
import com.android.mediproject.core.network.datasource.favoritemedicine.FavoriteMedicineDataSource
import com.android.mediproject.core.network.datasource.favoritemedicine.FavoriteMedicineDataSourceImpl
import com.android.mediproject.core.network.datasource.medicineid.MedicineIdDataSource
import com.android.mediproject.core.network.datasource.medicineid.MedicineIdDataSourceImpl
import com.android.mediproject.core.network.datasource.sign.LoginDataSource
import com.android.mediproject.core.network.datasource.sign.LoginDataSourceImpl
import com.android.mediproject.core.network.datasource.sign.SignupDataSource
import com.android.mediproject.core.network.datasource.sign.SignupDataSourceImpl
import com.android.mediproject.core.network.datasource.user.UserDataSource
import com.android.mediproject.core.network.datasource.user.UserDataSourceImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ServerNetwork {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun providesUserPool(@ApplicationContext context: Context) = CognitoUserPool(
        context, BuildConfig.AWS_USER_POOL, BuildConfig.AWS_USER_CLIENT_ID, BuildConfig.AWS_USER_CLIENT_SECRET,
        Regions.US_EAST_2,
    )

    @Provides
    @Singleton
    fun providesAwsNetworkApi(
        @Named("okHttpClientWithAccessTokens") okHttpClient: OkHttpClient,
    ): AwsNetworkApi = Retrofit.Builder().client(okHttpClient).addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BuildConfig.AWS_BASE_URL).build().create(AwsNetworkApi::class.java)

    @Provides
    @Singleton
    fun provideFavoriteMedicineDatasource(
        awsNetworkApi: AwsNetworkApi,
    ): FavoriteMedicineDataSource = FavoriteMedicineDataSourceImpl(awsNetworkApi)


    @Provides
    @Singleton
    fun providesCommentsDataSource(awsNetworkApi: AwsNetworkApi): CommentsDataSource = CommentsDataSourceImpl(awsNetworkApi)

    @Provides
    fun providesSignDataSource(
        userPool: CognitoUserPool,
    ): LoginDataSource = LoginDataSourceImpl(userPool)

    @Provides
    fun providesSignupDataSource(
        userPool: CognitoUserPool,
    ): SignupDataSource = SignupDataSourceImpl(userPool)

    @Provides
    @Singleton
    fun providesGetMedicineIdDataSource(awsNetworkApi: AwsNetworkApi): MedicineIdDataSource = MedicineIdDataSourceImpl(awsNetworkApi)

    @Provides
    @Singleton
    fun providesUserDataSource(awsNetworkApi: AwsNetworkApi, aesCoder: AesCoder): UserDataSource = UserDataSourceImpl(awsNetworkApi, aesCoder)

}

interface AwsNetworkApi {

    @GET(value = "medicine/favorite")
    suspend fun getFavoriteMedicineList(): Response<FavoriteMedicineListResponse>


    /*@POST(value = "user/register")
    suspend fun signUp(
        @Body signUpRequestParameter: SignUpRequestParameter,
    ): Response<SignUpResponse>

    @POST(value = "user/login")
    suspend fun login(
        @Body loginRequestParameter: LoginRequestParameter,
    ): Response<SignInResponse>


    @POST(value = "user/reissue")
    suspend fun reissueTokens(
    ): Response<ReissueTokenResponse>*/

    /**
     * 특정 약에 대한 댓글 목록 조회
     */
    @GET(value = "comment/medicine_id={medicineId}&page={page}&rows={rows}&userId={userId}")
    suspend fun getCommentsByMedicineId(
        @Path("medicineId", encoded = true) medicineId: Long,
        @Path("page", encoded = true) page: Int,
        @Path("rows", encoded = true) rows: Int,
        @Path("userId", encoded = true) userId: String = "-1",
    ): Response<CommentListResponse>

    /**
     * 내가 작성한 댓글 목록 조회
     */
    @GET(value = "user/comment")
    suspend fun getMyCommentsList(): Response<MyCommentsListResponse>

    /**
     * 댓글 수정
     */
    @PATCH(value = "medicine/comment")
    suspend fun editComment(
        @Body editCommentParameter: EditCommentParameter,
    ): Response<CommentChangedResponse>

    /**
     * 댓글 삭제
     */
    @DELETE(value = "medicine/comment")
    suspend fun deleteComment(
        @Body deleteCommentParameter: DeleteCommentParameter,
    ): Response<CommentChangedResponse>

    /**
     * 댓글 좋아요 추가
     */
    @POST(value = "medicine/comment/{medicineId}/like/{commentId}")
    suspend fun likeComment(
        @Path("medicineId", encoded = true) medicineId: Long,
        @Path("commentId", encoded = true) commentId: Long,
    ): Response<LikeResponse>

    /**
     * 댓글 좋아요 해제
     */
    @DELETE(value = "medicine/comment/{medicineId}/like/{commentId}")
    suspend fun unlikeComment(
        @Path("medicineId", encoded = true) medicineId: Long,
        @Path("commentId", encoded = true) commentId: Long,
    ): Response<LikeResponse>

    /**
     * 댓글 등록
     */
    @POST(value = "comment")
    suspend fun applyNewComment(
        @Body newCommentParameter: NewCommentParameter,
    ): Response<CommentChangedResponse>

    /**
     * 닉네임 변경
     */
    @PATCH(value = "user")
    suspend fun changeNickname(
        @Body changeNicknameParameter: ChangeNicknameParameter,
    ): Response<ChangeNicknameResponse>

    /**
     * 비밀번호 변경
     */
    @PATCH(value = "user")
    suspend fun changePassword(
        @Body changePasswordParameter: ChangePasswordParameter,
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
        @Body getMedicineIdParameter: GetMedicineIdParameter,
    ): Response<MedicineIdResponse>

    /**
     * 관심 약 조회
     */
    @GET(value = "medicine/favorite")
    suspend fun checkFavoriteMedicine(
        @Query("ITEM_SEQ") itemSeq: Long,
    ): Response<CheckFavoriteMedicineResponse>

    /**
     * 관심 약 추가
     */
    @POST(value = "medicine/favorite")
    suspend fun addFavoriteMedicine(
        @Body addFavoriteMedicineParameter: AddFavoriteMedicineParameter,
    ): Response<NewFavoriteMedicineResponse>

    /**
     * 관심 약 삭제
     */
    @DELETE(value = "medicine/favorite")
    suspend fun deleteFavoriteMedicine(
        @Query("medicineId") medicineId: Long,
    ): Response<DeleteFavoriteMedicineResponse>
}
