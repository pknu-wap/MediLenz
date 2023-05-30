package com.android.mediproject.core.model.local.navargs

import com.android.mediproject.core.common.TOHOME


data class LoginFromMyPageArgs(val flag: Int? = TOHOME) :
    BaseNavArgs(LoginFromMyPageArgs::class.java.name)
