package com.android.mediproject.feature.mypage.mypagemore

import com.android.mediproject.core.domain.sign.SignUseCase
import com.android.mediproject.core.domain.user.UserUseCase
import com.android.mediproject.core.test.repositories.FakeSignRepository
import com.android.mediproject.core.test.repositories.FakeUserInfoRepository
import com.google.common.truth.Truth
import comandroid.mediproject.core.test.MainCoroutineRule
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock

class MyPageMoreDialogViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    lateinit var viewModel: MyPageMoreDialogViewModel

    @Mock
    lateinit var userUseCase: UserUseCase

    @Before
    fun setUp() {
        val signRepository = FakeSignRepository()
        val userInfoRepository = FakeUserInfoRepository()


        viewModel = MyPageMoreDialogViewModel(userUseCase, UnconfinedTestDispatcher())
    }

    @Test
    fun `비밀번호 변경 시 비밀번호가 4글자 미만이면 안된다`() {
        //given
        val validEmail = "wap@gmail.com"
        val notValidPassword = "a12"

        //when
        viewModel.loginWithCheckRegex(
            email = validEmail,
            password = notValidPassword,
            isEmailSaved = false,
        )

        //then
        val actual = viewModel.loginState.value
        val expected = LoginViewModel.LoginState.RegexError
        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `비밀번호 변경 시 비밀번호가 16글자 초과이면 안된다`() {
        //given
        val validEmail = "wap@gmail.com"
        val notValidPassword = "123456789abcdefghijklmn"

        //when
        viewModel.loginWithCheckRegex(
            email = validEmail,
            password = notValidPassword,
            isEmailSaved = false,
        )

        //then
        val actual = viewModel.loginState.value
        val expected = LoginViewModel.LoginState.RegexError
        Truth.assertThat(actual).isEqualTo(expected)
    }

}
