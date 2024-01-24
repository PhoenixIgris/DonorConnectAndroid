package com.buuzz.donorconnect.utils.helpers.imagepicker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.buuzz.donorconnect.R
import com.buuzz.donorconnect.databinding.ImagePickerItemHolderBinding
import com.buuzz.donorconnect.utils.helpers.AppData
import com.buuzz.donorconnect.utils.helpers.imagepicker.ImagePickerAdapter.ImagePickerViewHolder

class ImagePickerAdapter(private val imagePickerCallback: ImagePickerInterface) :
    RecyclerView.Adapter<ImagePickerViewHolder>() {

    private val mList = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagePickerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_picker_item_holder, parent, false)
        return ImagePickerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImagePickerViewHolder, position: Int) {
        holder.setUpView(mList[position])
        holder.binding.root.setOnClickListener {
            imagePickerCallback.onImagePickerSelected(mList[position])
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    internal fun updateData(data: ArrayList<String>) {
        mList.clear()
        mList.addAll(data)
        notifyDataSetChanged()
    }

    class ImagePickerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal val binding = ImagePickerItemHolderBinding.bind(itemView)

        internal fun setUpView(name: String) {
            var title = ""
            var image = 0
            when (name) {
                AppData.Camera -> {
                    title = "Camera"
                    image = android.R.drawable.ic_menu_camera
                }

                AppData.Gallery -> {
                    title = "Gallery"
                    image = android.R.drawable.ic_menu_gallery
                }
            }
            binding.pickerText.text = title
            Glide.with(binding.pickerImage).load(image).into(binding.pickerImage)
        }
    }
}