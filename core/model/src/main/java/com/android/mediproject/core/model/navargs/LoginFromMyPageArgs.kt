package com.android.mediproject.core.model.navargs

const val TOHOME = 201
const val TOMYPAGE = 202

data class LoginFromMyPageArgs(val flag: Int? = TOHOME) :
    BaseNavArgs(LoginFromMyPageArgs::class.java.name)
