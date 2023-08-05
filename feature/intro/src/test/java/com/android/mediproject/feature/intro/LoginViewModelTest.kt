package com.android.mediproject.feature.intro

import android.text.Editable
import com.MainCoroutineRule
import com.android.mediproject.core.domain.sign.SignUseCase
import com.android.mediproject.core.test.repositories.FakeSignRepository
import com.android.mediproject.core.test.repositories.FakeUserInfoRepository
import com.google.common.truth.Truth.assertThat
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
        signUseCase = SignUseCase(signRepository,userInfoRepository)
        viewModel = LoginViewModel(signUseCase, UnconfinedTestDispatcher())
    }

    @Test
    fun `로그인을 할 때 이메일 형식이 맞지 않으면 RegexError를 반환한다`() {
        //given
        val notValidEmail = "example@.com" // 유효하지 않은 도메인
        val notValidEmailEditable: Editable = Editable.Factory.getInstance().newEditable(notValidEmail)

        val validPassword = "abcd123456"
        val validPasswordEditable: Editable = Editable.Factory.getInstance().newEditable(validPassword)

        //when
        viewModel.loginWithCheckRegex(notValidEmailEditable, validPasswordEditable, false)


        //then
        val result = viewModel.loginState.value
        assertThat(result).isEqualTo(LoginViewModel.LoginState.RegexError)
    }
}
