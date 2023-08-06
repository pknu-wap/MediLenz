package com.android.mediproject.feature.intro

import com.android.mediproject.core.domain.sign.SignUseCase
import com.android.mediproject.core.test.repositories.FakeSignRepository
import com.android.mediproject.core.test.repositories.FakeUserInfoRepository
import com.google.common.truth.Truth
import comandroid.mediproject.core.test.MainCoroutineRule
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class SignUpViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    lateinit var viewModel: SignUpViewModel
    lateinit var signUseCase: SignUseCase

    @Before
    fun setUp() {
        val signRepository = FakeSignRepository()
        val userInfoRepository = FakeUserInfoRepository()
        signUseCase = SignUseCase(signRepository, userInfoRepository)
        viewModel = SignUpViewModel(signUseCase, UnconfinedTestDispatcher())
    }

    @Test
    fun `회원가입시 이메일 형식이 맞지 않으면 RegexError를 반환한다`() {
        //given
        val notValidEmail = "example@.com"
        val validPassword = "abcd123456"
        val nickName = "Wap"

        //when
        viewModel.signUpWithCheckRegex(
            email = notValidEmail,
            password = validPassword,
            checkPassword = validPassword,
            nickName = nickName)

        //then
        val result = viewModel.signUpState.value
        Truth.assertThat(result).isEqualTo(SignUpViewModel.SignUpState.RegexError)
    }

    @Test
    fun `회원가입시 비밀번호가 3글자 이하면 RegexError를 반환한다`() {
        //given
        val validEmail = "example@gmail.com"
        val notValidPassword = "ab1"
        val nickName = "Wap"

        //when
        viewModel.signUpWithCheckRegex(
            email = validEmail,
            password = notValidPassword,
            checkPassword = notValidPassword,
            nickName = nickName)

        //then
        val result = viewModel.signUpState.value
        Truth.assertThat(result).isEqualTo(SignUpViewModel.SignUpState.RegexError)
    }

    @Test
    fun `회원가입시 비밀번호가 17글자 이상이면 RegexError를 반환한다`() {
        //given
        val validEmail = "example@gmail.com"
        val notValidPassword = "123456789abcdefghijklmn"
        val nickName = "Wap"

        //when
        viewModel.signUpWithCheckRegex(
            email = validEmail,
            password = notValidPassword,
            checkPassword = notValidPassword,
            nickName = nickName)

        //then
        val result = viewModel.signUpState.value
        Truth.assertThat(result).isEqualTo(SignUpViewModel.SignUpState.RegexError)
    }

    @Test
    fun `회원가입시 비밀번호와 비밀번호 체크가 같지 않으면 PasswordError를 반환한다`() {
        //given
        val validEmail = "example@gmail.com"
        val validPassword = "abcd123456"
        val anotherValidPassword = "123456abcd"
        val nickName = "Wap"

        //when
        viewModel.signUpWithCheckRegex(
            email = validEmail,
            password = validPassword,
            checkPassword = anotherValidPassword,
            nickName = nickName)

        //then
        val result = viewModel.signUpState.value
        Truth.assertThat(result).isEqualTo(SignUpViewModel.SignUpState.PasswordError)
    }

    @Test
    fun `회원가입시 아이디가 이메일 형식이 맞고, 비밀번호가 4글자 이상 16글자 이하이며, 체크 비밀번호가 같을경우 SignUpSuccess를 반환한다`() {
        //given
        val validEmail = "example@gmail.com"
        val validPassword = "abcd123456"
        val nickName = "Wap"

        //when
        viewModel.signUpWithCheckRegex(
            email = validEmail,
            password = validPassword,
            checkPassword = validPassword,
            nickName = nickName)

        //then
        val result = viewModel.signUpState.value
        Truth.assertThat(result).isEqualTo(SignUpViewModel.SignUpState.SignUpSuccess)
    }
}
