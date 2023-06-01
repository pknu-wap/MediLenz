package com.android.mediproject.feature.mypage.mypagemore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.android.mediproject.feature.mypage.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPackageMoreDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_package_more_dialog, container, false)
    }
}