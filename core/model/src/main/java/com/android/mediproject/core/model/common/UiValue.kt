package com.android.mediproject.core.model.common

/**
 * UI에서 사용하는 값을 나타내기 위한 자료형
 *
 * @param T UI에서 사용하는 값의 자료형
 * @property value UI에서 사용하는 값
 * @property isEmpty UI에서 사용하는 값이 비어있는지 여부
 *
 * LocalDate와 같이 Date값의 존재 여부를 Null대신 구분짓기 위해 사용한다.
 */
interface UiValue<T> {
    val value: T
    val isEmpty: Boolean
}
