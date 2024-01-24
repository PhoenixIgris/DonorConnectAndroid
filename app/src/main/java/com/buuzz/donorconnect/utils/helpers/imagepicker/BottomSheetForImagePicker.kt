package com.buuzz.donorconnect.utils.helpers.imagepicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.buuzz.donorconnect.databinding.FragmentBottomSheetForImagePickerBinding
import com.buuzz.donorconnect.utils.helpers.AppData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetForImagePicker() : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetForImagePickerBinding
    private var rvAdapter: ImagePickerAdapter? = null
    private var imagePickerCallback: ImagePickerInterface? = null

    fun setImagePickerInterface(listener: ImagePickerInterface) {
        imagePickerCallback = listener
        rvAdapter = ImagePickerAdapter(listener)
        rvAdapter?.updateData(arrayListOf(AppData.Camera, AppData.Gallery))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBottomSheetForImagePickerBinding.inflate(layoutInflater, container, false)
        val linearLayoutManager = LinearLayoutManager(context)
        binding.imagePicker.apply {
            rvAdapter?.let {
                layoutManager = linearLayoutManager
                adapter = rvAdapter
                addItemDecoration(
                    DividerItemDecoration(
                        context,
                        linearLayoutManager.orientation
                    )
                )
            }
        }
        return binding.root
    }


    fun dismissDialog() {
        if (isAdded) {
            dismiss()
        }
    }

    companion object {
        fun newInstance(): BottomSheetForImagePicker = BottomSheetForImagePicker()
    }
}
