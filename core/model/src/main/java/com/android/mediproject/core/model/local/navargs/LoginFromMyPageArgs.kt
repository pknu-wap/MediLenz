package com.android.mediproject.core.model.local.navargs


data class LoginFromMyPageArgs(val flag: Int? = TOHOME) :
    BaseNavArgs(LoginFromMyPageArgs::class.java.name)
