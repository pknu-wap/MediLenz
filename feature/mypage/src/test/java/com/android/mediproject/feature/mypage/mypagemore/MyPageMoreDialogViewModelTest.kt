package com.android.mediproject.feature.mypage.mypagemore

import com.android.mediproject.core.domain.EditUserAccountUseCase
import com.android.mediproject.core.test.MainCoroutineRule
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MyPageMoreDialogViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    lateinit var viewModel: MyPageMoreDialogViewModel

    @Mock
    lateinit var editUserAccountUseCase: EditUserAccountUseCase

    @Before
    fun setUp() {
        viewModel = MyPageMoreDialogViewModel(editUserAccountUseCase, UnconfinedTestDispatcher())
    }

    @Test
    fun `비밀번호 변경 시 비밀번호가 4글자 미만이면 안된다`() = runTest {
        //given
        val notValidPassword = "a12"


        //when
        viewModel.changePassword(notValidPassword)

        //then
        val actual = viewModel.myPageMoreDialogState.value
        val expected = MyPageMoreDialogViewModel.MyPageDialogState.Error(MyPageMoreDialogViewModel.MyPageDialogFlag.CHANGEPASSWORD)
        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `비밀번호 변경 시 비밀번호가 16글자 초과이면 안된다`() {
        //given
        val notValidPassword = "123456789abcdefghijklmn"

        //when
        viewModel.changePassword(notValidPassword)

        //then
        val actual = viewModel.myPageMoreDialogState.value
        val expected = MyPageMoreDialogViewModel.MyPageDialogState.Error(MyPageMoreDialogViewModel.MyPageDialogFlag.CHANGEPASSWORD)
        Truth.assertThat(actual).isEqualTo(expected)
    }
}
