package com.srhan.bostaandroidtask.presentation.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.srhan.bostaandroidtask.databinding.FragmentImageBinding


class ImageFragment : Fragment() {


    lateinit var binding: FragmentImageBinding
    private val args: ImageFragmentArgs by navArgs()
    lateinit var url: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        url = args.url
        loadImage(url)

        binding.btnShare.setOnClickListener {
            shareImage(url)
        }
    }

    private fun loadImage(url: String) {
        Glide.with(this)
            .load(url)
            .into(object : CustomTarget<Drawable?>() {
                @SuppressLint("SetTextI18n")
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable?>?
                ) {
                    binding.imageView.setImageDrawable(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) = Unit

            })
    }

    private fun shareImage(imageUrl: String) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Check out this image!")
            putExtra(Intent.EXTRA_STREAM, imageUrl)
            type = "image/*"
        }
        startActivity(Intent.createChooser(shareIntent, "Share Image"))
    }


}