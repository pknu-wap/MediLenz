package com.android.mediproject.core.ui.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel>(private val inflate: Inflate<T>) :
    Fragment() {

    private var _binding: T? = null
    val binding get() = _binding!!

    protected abstract val fragmentViewModel: V

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        binding.lifecycleOwner = this
        afterBinding(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun afterBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    )

    fun log(str: String) = Log.e("wap", str) //for test
    fun toast(str: String) = Toast.makeText(requireContext(), str, Toast.LENGTH_LONG).show()

}