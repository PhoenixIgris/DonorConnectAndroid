package com.buuzz.donorconnect.ui.post.create

import android.Manifest
import android.R
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.buuzz.donorconnect.databinding.ActivityCreatePostBinding
import com.buuzz.donorconnect.ui.base.BaseActivity
import com.buuzz.donorconnect.ui.post.adapter.TagAdapter
import com.buuzz.donorconnect.utils.apihelper.safeapicall.ApiCallListener
import com.buuzz.donorconnect.utils.helpers.AppData
import com.buuzz.donorconnect.utils.helpers.AppLogger
import com.buuzz.donorconnect.utils.helpers.convertUriToBase64
import com.buuzz.donorconnect.utils.helpers.getTempUriForCamera
import com.buuzz.donorconnect.utils.helpers.hasCameraPermission
import com.buuzz.donorconnect.utils.helpers.imagepicker.BottomSheetForImagePicker
import com.buuzz.donorconnect.utils.helpers.imagepicker.ImagePickerInterface
import com.buuzz.donorconnect.utils.helpers.isPhotoPickerAvailable
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "CreatePostActivity"
@AndroidEntryPoint
class CreatePostActivity : BaseActivity(),
    ImagePickerInterface {
    private var postPicture: String? = ""
    private lateinit var binding: ActivityCreatePostBinding
    private var tagAdapter: TagAdapter? = null
    private val viewModel: CreatePostViewModel by viewModels()
    private val listPopupWindow by lazy { ListPopupWindow(this) }
    private var selectedCategoryId: Int = 0
    private var pictureUri: Uri? = null
    private var imagePickerSheet: BottomSheetForImagePicker? = null

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSaved ->
            if (isSaved) {
                loadImage(pictureUri)
            } else {
                AppLogger.logE(TAG, "Image not Saved ")
            }
        }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            cameraLauncher.launch(pictureUri)
        }
    }

    private val mainImagePicker =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            loadImage(uri)
        }

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            loadImage(uri)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setUpViews()

        setContentView(binding.root)
    }

    private fun setUpViews() {
        imagePickerSheet = BottomSheetForImagePicker.newInstance()
        imagePickerSheet?.setImagePickerInterface(this)
        pictureUri = getTempUriForCamera()
        setOnClicks()

        setCategoriesAndTags()
    }

    private fun validateData(): Boolean {
        postPicture =
            pictureUri?.let { convertUriToBase64(applicationContext, it) }
        if (postPicture.isNullOrEmpty()) {
            return false
        }
        return true
    }

    private fun setOnClicks() {
        binding.photo.setOnClickListener {
            imagePickerSheet?.show(supportFragmentManager, "Image Picker")
        }
        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.submit.setOnClickListener {
            if (validateData()) {
                submitPost()

            }
        }
    }

    private fun submitPost() {
        viewModel.createPost(
            title = binding.title.text.toString(),
            desc = binding.description.text.toString(),
            image = "data:image/png;base64,$postPicture",
            category_id = selectedCategoryId,
            tag_id = tagAdapter?.getSelectedTags(), callback = object : ApiCallListener {
                override fun onSuccess(response: String?) {
                    AppLogger.logD(TAG, response)
                }

                override fun onError(errorMessage: String?) {
                    AppLogger.logD(TAG, errorMessage)
                    showTopSnackBar(binding.root, errorMessage ?: "Failed to create post")
                }

            }
        )
    }

    private fun setCategoriesAndTags() {
        viewModel.getTagsList { tags ->
            tagAdapter = TagAdapter(tags) { selectedTag ->
                AppLogger.logD("POST", "selectedTags $selectedTag")
            }
            val layoutManager = FlexboxLayoutManager(this)
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_START
            binding.tagList.layoutManager = layoutManager
            binding.tagList.adapter = tagAdapter
        }
        viewModel.getCategoryList { categories ->
            listPopupWindow.setAdapter(
                ArrayAdapter(this,
                    R.layout.simple_list_item_1,
                    categories.map { it.name }
                )
            )
            listPopupWindow.setOnItemClickListener { _, _, position, _ ->
                selectedCategoryId = categories[position].id
                binding.categoryList.text = categories[position].name
                listPopupWindow.dismiss()
            }
            listPopupWindow.anchorView = binding.categoryList
            binding.categoryList.setOnClickListener {
                AppLogger.logD("POST", "categories $categories")
                listPopupWindow.show()
            }
        }

    }

    private fun loadImage(uri: Uri?) {
        lifecycleScope.launch(Dispatchers.Main) {
            if (uri != null) {
                imagePickerSheet?.dismissDialog()
            }
            uri?.let {
                pictureUri = uri
                binding.photo.setImageUri(uri)
            }
        }
    }

    override fun onImagePickerSelected(name: String) {
        when (name) {
            AppData.Camera -> {
                if (hasCameraPermission()) {
                    pictureUri = getTempUriForCamera()
                    cameraLauncher.launch(pictureUri)
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }

            AppData.Gallery -> {
                if (isPhotoPickerAvailable()) {
                    mainImagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                } else {
                    imagePickerLauncher.launch("image/*")
                }
            }
        }
    }
}