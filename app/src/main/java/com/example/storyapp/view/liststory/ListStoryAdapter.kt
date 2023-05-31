package com.example.storyapp.view.liststory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.storyapp.data.entity.UserStory
import com.example.storyapp.databinding.StoryListBinding
import com.example.storyapp.utils.withDateFormat

class ListStoryAdapter :
    PagingDataAdapter<UserStory,ListStoryAdapter.MyViewHolder>(DIFF_CALLBACK)
{
    private lateinit var onItemClickCallback: OnItemClickCallback
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = StoryListBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if(data !=null){
            holder.bind(data)
            holder.itemView.setOnClickListener{
                onItemClickCallback.onItemClicked(
                    data
                )
            }
        }
    }

    class MyViewHolder(
        private val binding: StoryListBinding
    ):
        ViewHolder(
        binding.root
    ){
        fun bind(story: UserStory){
            binding.storyName.text = story.name
            binding.storyDate.text = story.createdAt.withDateFormat()
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .into(binding.storyImage)
        }
    }
    interface OnItemClickCallback{
        fun onItemClicked(storyResponse: UserStory)
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserStory>() {
            override fun areItemsTheSame(oldItem: UserStory, newItem: UserStory): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: UserStory, newItem: UserStory): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}