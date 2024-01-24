package com.buuzz.donorconnect.ui.custom

import android.content.Context
import android.content.res.TypedArray
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.buuzz.donorconnect.R
import com.buuzz.donorconnect.databinding.LayoutDocumentDisplayBinding


class DocumentDisplayView : RelativeLayout {
    private var typedArray: TypedArray? = null
    private var binding: LayoutDocumentDisplayBinding? = null
    lateinit var baseContext: Context

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.DocumentDisplayView)
        init(context)
    }

    private fun init(context: Context) {
        this.baseContext = context
        binding = LayoutDocumentDisplayBinding.inflate(LayoutInflater.from(context), this, true)
        val instructionText = typedArray?.getString(R.styleable.DocumentDisplayView_instruction)

        binding?.apply {
            tvUploadInstruction.text = instructionText
        }

        binding?.cardDelete?.setOnClickListener {
            binding?.apply {
                layoutNotPicked.visibility = View.VISIBLE
            }
        }
    }

    fun setImageUri(uri: Uri) {
        binding?.layoutNotPicked?.visibility = View.GONE
        binding?.image?.let {
            Glide.with(context)
                .load(uri)
                .into(it)
        }
        binding?.cardDelete?.visibility = View.VISIBLE
    }

    fun setInstruction(instruction: String) {
        binding?.tvUploadInstruction?.text = instruction
    }
}