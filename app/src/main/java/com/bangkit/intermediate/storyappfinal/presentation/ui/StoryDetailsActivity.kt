package com.bangkit.intermediate.storyappfinal.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.intermediate.storyappfinal.core.feature.story.domain.response.ListStoryItem
import com.bangkit.intermediate.storyappfinal.databinding.ActivityStoryDetailsBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class StoryDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = intent.getParcelableExtra<ListStoryItem>(EXTRA_STORY) as ListStoryItem
        binding.apply {
            Glide.with(applicationContext)
                .load(story.photoUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imgPhoto)
            tvName.text = story.name
            tvDesc.text = story.description
        }
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}