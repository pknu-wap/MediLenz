package com.android.mediproject.core.model.token

/**
 * 토큰 요청 이유
 */
sealed interface RequestBehavior {

    /**
     * 토큰 재발급 요청(댓글, 좋아용 등을 처리해야하는데 토큰이 만료되어 버린 경우)
     */
    object ReissueTokens : RequestBehavior

    /**
     * 새로운 토큰 요청(로그인, 회원가입의 경우)
     */
    object NewTokens : RequestBehavior
}
