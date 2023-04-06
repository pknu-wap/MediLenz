package com.android.mediproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.mediproject.core.ui.base.BaseActivity
import com.android.mediproject.databinding.ActivityMainBinding

class MainActivity() : AppCompatActivity(){

    private lateinit var binding:  ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.activity_main,null,false)
        setContentView(binding.root)
    }


    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onStop() {
        super.onStop()
    }
}