//package com.example.storyapp.data
//
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import com.example.storyapp.data.response.StoryResponse
//import com.example.storyapp.data.retrofit.ApiService
//
//class StoryPagingSource(private val apiService: ApiService): PagingSource<Int, StoryResponse>() {
//    override fun getRefreshKey(state: PagingState<Int, StoryResponse>): Int? {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryResponse> {
//        return try{
//            val position = params.key ?: INITIAL_PAGE_INDEX
//            val responseData = apiService.getAllStories("Bearer $token",position, params.loadSize, null)
//
//            LoadResult.Page(
////                data = responseData,
//
//
//            )
//        }catch (e: Exception){
//            return LoadResult.Error(e)
//        }
//    }
//
//    private companion object{
//        const val INITIAL_PAGE_INDEX =1
//    }
//}