package com.android.mediproject.feature.intro


import com.android.mediproject.core.domain.sign.SignUseCase
import com.android.mediproject.core.test.repositories.FakeSignRepository
import com.android.mediproject.core.test.repositories.FakeUserInfoRepository
import com.google.common.truth.Truth.assertThat
import comandroid.mediproject.core.test.MainCoroutineRule
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized


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
    fun `로그인 시 이메일 형식이 아니면 RegexError를 반환한다`() {
        //given
        val notValidEmail = "example@.com"
        val validPassword = "abcd123456"

        //when
        viewModel.loginWithCheckRegex(notValidEmail, validPassword, false)

        //then
        val result = viewModel.loginState.value
        assertThat(result).isEqualTo(LoginViewModel.LoginState.RegexError)
    }

    @Test
    fun `로그인 시 비밀번호가 3글자 이하일 경우 RegexError를 반환한다`() {
        //given
        val validEmail = "wap@gmail.com"
        val notValidPassword = "a12"

        //when
        viewModel.loginWithCheckRegex(validEmail, notValidPassword, false)

        //then
        val result = viewModel.loginState.value
        assertThat(result).isEqualTo(LoginViewModel.LoginState.RegexError)
    }

    @Test
    fun `로그인 시 비밀번호가 17글자 이상일 경우 RegexError를 반환한다`() {
        //given
        val validEmail = "wap@gmail.com"
        val notValidPassword = "123456789abcdefghijklmn"

        //when
        viewModel.loginWithCheckRegex(validEmail, notValidPassword, false)

        //then
        val result = viewModel.loginState.value
        assertThat(result).isEqualTo(LoginViewModel.LoginState.RegexError)
    }

    @Test
    fun `로그인 시 아이디가 이메일 형식이고, 비밀번호가 4글자 이상, 16글자 이하일 경우 Success를 반환한다`() {
        //given
        val validEmail = "wap@gmail.com"
        val validPassword = "wapMediLenz2023"

        //when
        viewModel.loginWithCheckRegex(validEmail, validPassword, false)

        //then
        val result = viewModel.loginState.value
        assertThat(result).isEqualTo(LoginViewModel.LoginState.LoginSuccess)
    }
}
