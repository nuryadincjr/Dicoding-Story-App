package com.nuryadincjr.storyapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nuryadincjr.storyapp.R
import com.nuryadincjr.storyapp.data.remote.response.StoryItem
import com.nuryadincjr.storyapp.databinding.ItemListStoryBinding
import com.nuryadincjr.storyapp.util.Constant.DATA_STORY
import com.nuryadincjr.storyapp.view.detail.DetailsStoryActivity

class ListStoriesAdapter(private val listStory: List<StoryItem>) :
    RecyclerView.Adapter<ListStoriesAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.setDataToView(listStory[position])
    }

    override fun getItemCount(): Int = listStory.size

    class ListViewHolder(
        private var binding: ItemListStoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun setDataToView(storyItem: StoryItem) {
            binding.apply {
                tvName.text = storyItem.name
                tvDescription.text = storyItem.description

                Glide.with(itemView.context)
                    .load(storyItem.photoUrl)
                    .centerCrop()
                    .placeholder(R.drawable.ic_baseline_image_24)
                    .error(R.drawable.ic_baseline_broken_image_24)
                    .into(imageView)
            }

            itemView.apply {
                setOnClickListener {
                    context.startActivity(
                        Intent(
                            context,
                            DetailsStoryActivity::class.java
                        ).putExtra(DATA_STORY, storyItem)
                    )
                }
            }
        }
    }
}