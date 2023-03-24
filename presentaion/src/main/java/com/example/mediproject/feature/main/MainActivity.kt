package com.example.mediproject.feature.main

import androidx.activity.viewModels
import com.example.mediproject.base.BaseActivity
import com.example.mediproject.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>({ActivityMainBinding.inflate(it)}) {

    override val activityViewModel: MainViewModel by viewModels()

    override fun afterBinding() {
    }
}