package com.android.mediproject.feature.intro


import com.android.mediproject.core.test.MainCoroutineRule
import com.android.mediproject.core.test.repositories.FakeSignRepository
import com.android.mediproject.core.test.repositories.FakeUserInfoRepository
import com.android.mediproject.feature.intro.login.LoginViewModel
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class LoginViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    lateinit var viewModel: LoginViewModel
    lateinit var signUseCase: SignUseCase

    @Before
    fun setUp() {
        val signRepository = FakeSignRepository()
        val userInfoRepository = FakeUserInfoRepository()
        signUseCase = SignUseCase(signRepository, userInfoRepository)
        viewModel = LoginViewModel(signUseCase, UnconfinedTestDispatcher())
    }

    @Test
    fun `로그인 시 이메일 형식이 맞지 않으면 안된다`() {
        //given
        val notValidEmail = "example@.com"
        val validPassword = "abcd123456"

        //when
        viewModel.loginWithCheckRegex(
            email = notValidEmail,
            password = validPassword,
            isEmailSaved = false,
        )

        //then
        val actual = viewModel.loginState.value
        val expected = LoginViewModel.LoginUiState.RegexError
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `로그인 시 비밀번호가 4글자 미만이면 안된다`() {
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
        val expected = LoginViewModel.LoginUiState.RegexError
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `로그인 시 비밀번호가 16글자 초과이면 안된다`() {
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
        val expected = LoginViewModel.LoginUiState.RegexError
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `로그인 시 아이디가 이메일 형식이고, 비밀번호가 4글자 이상, 16글자 이하여야 한다`() {
        //given
        val validEmail = "wap@gmail.com"
        val validPassword = "wapMediLenz2023"

        //when
        viewModel.loginWithCheckRegex(
            email = validEmail,
            password = validPassword,
            isEmailSaved = false,
        )

        //then
        val actual = viewModel.loginState.value
        val expected = LoginViewModel.LoginUiState.LoginSuccess
        assertThat(actual).isEqualTo(expected)
    }
}
