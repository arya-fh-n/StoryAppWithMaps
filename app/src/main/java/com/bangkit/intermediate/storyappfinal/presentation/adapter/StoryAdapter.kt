package com.bangkit.intermediate.storyappfinal.presentation.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bangkit.intermediate.storyappfinal.core.feature.story.domain.response.ListStoryItem
import com.bangkit.intermediate.storyappfinal.databinding.LayoutStoryCardBinding
import com.bangkit.intermediate.storyappfinal.presentation.ui.StoryDetailsActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dagger.hilt.android.internal.managers.FragmentComponentManager

class StoryAdapter: PagingDataAdapter<ListStoryItem, StoryAdapter.StoriesViewHolder>(DIFF_CALLBACK) {

    class StoriesViewHolder(val binding: LayoutStoryCardBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem, context: Context) {
            binding.apply {
                tvName.text = story.name
                tvDesc.text = story.description

                Glide.with(context)
                    .load(story.photoUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imgPreview)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesViewHolder {
        val view = LayoutStoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoriesViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story, holder.itemView.context)
        }

        story?.let { storyItem ->
            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, StoryDetailsActivity::class.java)
                intent.putExtra(StoryDetailsActivity.EXTRA_STORY, storyItem)

                val optionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    FragmentComponentManager.findActivity(holder.itemView.context) as Activity,
                    Pair(holder.binding.imgPreview, "profile"),
                    Pair(holder.binding.tvName, "name"),
                    Pair(holder.binding.tvDesc, "description"),
                )
                holder.itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}