package com.example.storyapp

import com.example.storyapp.data.entity.UserStory

object DataStoryDummy {
    fun generateDummyUserStoryResponse():List<UserStory>{
        val items: MutableList<UserStory> = arrayListOf()
        for(i in 0..100){
            val story = UserStory(
                i.toString(),
                "https://id-test-11.slatic.net/p/6a78913c131cfcd539813bd4b7c42459.png",
                "2023-30-05",
                "Tester"
            )
            items.add(story)
        }
        return items
    }
}