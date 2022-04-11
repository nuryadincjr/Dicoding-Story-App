package com.nuryadincjr.storyapp.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.nuryadincjr.storyapp.R
import com.nuryadincjr.storyapp.data.remote.response.StoryItem
import com.nuryadincjr.storyapp.databinding.ActivityDetailsStoryBinding
import com.nuryadincjr.storyapp.util.Constant.DATA_STORY

class DetailsStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_story)

        binding = ActivityDetailsStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyItem = intent.getParcelableExtra<StoryItem>(DATA_STORY) as StoryItem

        setupView(storyItem)
    }

    private fun setupView(user: StoryItem?) {
        binding.apply {
            tvName.text = user?.name
            tvDescription.text = user?.description

            Glide.with(this@DetailsStoryActivity)
                .load(user?.photoUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(imageView)
        }
    }
}