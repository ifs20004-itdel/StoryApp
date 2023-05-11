package com.example.storyapp.view.liststory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.storyapp.data.response.StoryResponse
import com.example.storyapp.databinding.StoryListBinding
import com.example.storyapp.utils.withDateFormat

class ListStoryAdapter(private val storyList: List<StoryResponse>): RecyclerView.Adapter<ListStoryAdapter.MyViewHolder>(){

    private lateinit var onItemClickCallback: OnItemClickCallback
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = StoryListBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return MyViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return storyList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(storyList[position])
        holder.itemView.setOnClickListener{
            onItemClickCallback.onItemClicked(
                storyList[position]
            )
        }
    }

    class MyViewHolder(
        private val binding: StoryListBinding
    ):ViewHolder(
        binding.root
    ){
        fun bind(story: StoryResponse){
            binding.storyName.text = story.name
            binding.storyDate.text = story.createdAt.withDateFormat()
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .into(binding.storyImage)
        }
    }
    interface OnItemClickCallback{
        fun onItemClicked(storyResponse: StoryResponse)
    }
}