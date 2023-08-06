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
    fun `회원가입 시 이메일 형식이 맞지 않으면 안된다`() {
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
        val actual = viewModel.signUpState.value
        val expected = SignUpViewModel.SignUpState.RegexError
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `회원가입 시 비밀번호가 4글자 미만이면 안된다`() {
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
        val actual = viewModel.signUpState.value
        val expected = SignUpViewModel.SignUpState.RegexError
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `회원가입 시 비밀번호가 16글자 초과면 안된다`() {
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
        val actual = viewModel.signUpState.value
        val expected = SignUpViewModel.SignUpState.RegexError
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `회원가입 시 비밀번호와 확인 비밀번호가 다르면 안된다`() {
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
        val actual = viewModel.signUpState.value
        val expected = SignUpViewModel.SignUpState.PasswordError
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `회원가입 시 아이디가 이메일 형식이 맞고, 비밀번호가 4글자 이상 16글자 이하이며, 비밀번호와 확인 비밀번호가 같아야 한다`() {
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
        val actual = viewModel.signUpState.value
        val expected = SignUpViewModel.SignUpState.SignUpSuccess
        assertThat(actual).isEqualTo(expected)
    }
}
