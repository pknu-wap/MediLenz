package com.android.mediproject.feature.intro

import android.text.Editable
import android.widget.EditText
import com.MainCoroutineRule
import com.android.mediproject.core.domain.sign.SignUseCase
import com.android.mediproject.core.test.repositories.FakeSignRepository
import com.android.mediproject.core.test.repositories.FakeUserInfoRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var mockEditTextEmail: EditText

    @Mock
    private lateinit var mockEditTextPassword: EditText

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
    fun `로그인시 이메일 형식이 맞지 않으면 RegexError를 반환한다`() {
        //given
        val notValidEmail = "example@.com" // 유효하지 않은 도메인
        whenever(mockEditTextEmail.text).thenReturn(Editable.Factory.getInstance().newEditable(notValidEmail))

        val validPassword = "abcd123456"
        whenever(mockEditTextPassword.text).thenReturn(Editable.Factory.getInstance().newEditable(validPassword))


        //when
        viewModel.loginWithCheckRegex(mockEditTextEmail.text, mockEditTextPassword.text, false)


        //then
        val result = viewModel.loginState.value
        assertThat(result).isEqualTo(LoginViewModel.LoginState.RegexError)
    }
}
