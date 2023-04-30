package com.android.mediproject

import android.annotation.SuppressLint
import com.android.mediproject.core.ui.base.BaseActivity
import com.android.mediproject.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity() : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    @SuppressLint("CommitTransaction")
    override fun afterBinding() {
        binding.apply {

        }
    }

}