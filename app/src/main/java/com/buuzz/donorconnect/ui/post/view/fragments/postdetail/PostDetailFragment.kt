package com.buuzz.donorconnect.ui.post.view.fragments.postdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.buuzz.donorconnect.R
import com.buuzz.donorconnect.data.model.response.DeliveryDetails
import com.buuzz.donorconnect.data.model.response.Post
import com.buuzz.donorconnect.data.model.response.PostRequestResponse
import com.buuzz.donorconnect.databinding.FragmentPostDetailBinding
import com.buuzz.donorconnect.ui.base.BaseFragment
import com.buuzz.donorconnect.ui.post.ActionType
import com.buuzz.donorconnect.utils.apihelper.safeapicall.ApiCallListener
import com.buuzz.donorconnect.utils.helpers.AppLogger
import com.buuzz.donorconnect.utils.helpers.IntentParams
import com.buuzz.donorconnect.utils.helpers.formatDate
import com.google.android.gms.common.api.Api
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailFragment : BaseFragment() {


    private val viewModel: PostDetailViewModel by viewModels()
    private lateinit var binding: FragmentPostDetailBinding
    private var postDetail: Post? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppLogger.logD("POST", requireActivity().intent.getStringExtra(IntentParams.POST_DETAIL))
        postDetail = Gson().fromJson(
            requireActivity().intent.getStringExtra(IntentParams.POST_DETAIL),
            Post::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostDetailBinding.inflate(layoutInflater)
        setView()
        setClicks()
        return binding.root
    }

    private fun setClicks() {
        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.requestBtn.setOnClickListener {
            requestPost()
        }

        binding.cancelButton.setOnClickListener {
            cancelPost()
        }

        binding.bookmarkBtn.setOnClickListener {
            bookmarkUnBookmarkPost()
        }

    }

    private fun bookmarkUnBookmarkPost() {
        postDetail?.let {
            viewModel.bookmarkUnBookmarkPost(
                object : ApiCallListener {
                    override fun onSuccess(response: String?) {
                        showTopSnackBar(binding.root, response ?: "Done!")
                    }

                    override fun onError(errorMessage: String?) {
                        showTopSnackBar(binding.root, errorMessage ?: "Error!!")

                    }
                },
                it.id.toString(),
            )
        }


    }

    private fun requestPost() {
        viewModel.requestPost(object : ApiCallListener {
            override fun onSuccess(response: String?) {
                val data = Gson().fromJson(response, PostRequestResponse::class.java)
                if (data.isFirstRequest == true) {
                    openDeliveryDetail(data.delivery_details)
                } else {
                    showTopSnackBar(binding.root, data.message ?: "")
                }
            }

            override fun onError(errorMessage: String?) {
                showTopSnackBar(binding.root, errorMessage ?: "Error Requesting Item!!")
            }

        }, postId = postDetail?.id.toString())
    }

    private fun openDeliveryDetail(deliveryDetails: DeliveryDetails?) {
        deliveryDetails?.let {
            showErrorDialog(
                "${deliveryDetails.message}\nAddress: ${deliveryDetails.address}\nContact Number: ${deliveryDetails.contact_number}\n\nCode: ${deliveryDetails.queue_code}\n",
                false,
                yesBtnClicked = {
                    dismissErrorDialog()
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }, noBtnClicked = {
                    cancelPost()
                    dismissErrorDialog()
                })
        }

    }

    private fun cancelPost() {
        viewModel.cancelPost(object : ApiCallListener {
            override fun onSuccess(response: String?) {
                showTopSnackBar(binding.root, response ?: "")
            }

            override fun onError(errorMessage: String?) {
                showTopSnackBar(binding.root, errorMessage ?: "Error Requesting Item!!")
            }

        }, postId = postDetail?.id.toString())
    }

    private fun setView() {
        binding.apply {
            postDetail?.let { data ->
                Glide.with(binding.root.context).load(data.image)
                    .placeholder(R.drawable.donation_placeholder)
                    .into(binding.postImage)
                category.text = data.category?.name ?: ""
                val tagNames = data.tags?.map { it.name }
                tagList.text = tagNames?.joinToString(" | ") ?: ""
                title.text = data.title ?: ""
                likes.text = (data.likes ?: 0).toString()
                shareBtn.setOnClickListener {
                    showTopSnackBar(root, "Coming Soon")
                }
                comments.text = (data.comment_list?.size ?: 0).toString()
                date.text = formatDate(data.created_at)

                Glide.with(binding.root.context).load(data.user?.image)
                    .placeholder(R.drawable.person_placeholder)
                    .into(binding.userImage)
                userName.text = data.user?.name ?: ""
                userDetail.text = data.desc ?: ""
            }
        }
    }


    companion object {
        fun newInstance() = PostDetailFragment()
    }
}