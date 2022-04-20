package com.nuryadincjr.storyapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater.from
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nuryadincjr.storyapp.R
import com.nuryadincjr.storyapp.data.remote.response.StoryItem
import com.nuryadincjr.storyapp.databinding.ItemListStoryBinding
import com.nuryadincjr.storyapp.databinding.ItemListStoryBinding.inflate
import com.nuryadincjr.storyapp.util.Constant
import com.nuryadincjr.storyapp.view.detail.DetailsStoryActivity

class StoriesListAdapter :
    PagingDataAdapter<StoryItem, StoriesListAdapter.ListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = inflate(from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val storyItem = getItem(position)
        if (storyItem != null) {
            holder.bind(storyItem)
        }
    }

    class ListViewHolder(private val binding: ItemListStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(storyItem: StoryItem) {
            binding.apply {
                tvName.text = storyItem.name
                tvDescription.text = storyItem.description

                Glide.with(itemView.context)
                    .load(storyItem.photoUrl)
                    .centerCrop()
                    .placeholder(R.drawable.ic_baseline_image_24)
                    .error(R.drawable.ic_baseline_broken_image_24)
                    .into(imageView)

                itemView.apply {
                    setOnClickListener {
                        val intent = Intent(context, DetailsStoryActivity::class.java)
                        intent.putExtra(Constant.DATA_STORY, storyItem)
                        val optionsCompat: ActivityOptionsCompat =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                context as Activity,
                                Pair(imageView, "photo"),
                                Pair(tvName, "name"),
                                Pair(tvDescription, "description"),
                            )
                        context.startActivity(intent, optionsCompat.toBundle())
                    }
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryItem>() {
            override fun areItemsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}