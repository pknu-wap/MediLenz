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
    fun `로그인 시 이메일 형식이 맞지 않으면 RegexError를 반환한다`() {
        //given
        val notValidEmail = "example@.com"
        val validPassword = "abcd123456"

        //when
        viewModel.loginWithCheckRegex(notValidEmail, validPassword, false)

        //then
        val result = viewModel.loginState.value
        assertThat(result).isEqualTo(LoginViewModel.LoginState.RegexError)
    }
}
