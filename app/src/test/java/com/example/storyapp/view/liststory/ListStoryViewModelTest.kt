package com.example.storyapp.view.liststory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storyapp.DataStoryDummy
import com.example.storyapp.MainDispatcherRule
import com.example.storyapp.data.entity.UserStory
import com.example.storyapp.data.local.StoryRepository
import com.example.storyapp.getOrAwaitValue
import com.example.storyapp.model.UserPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ListStoryViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    @Mock
    private lateinit var preference: UserPreference

    private val token  = "dummyToken"


    @Test
    fun `when Get Story Should Not Null and Return Data`() = runTest {
        val dummyStory = DataStoryDummy.generateDummyUserStoryResponse()
        val data: PagingData<UserStory> = StoryPagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<UserStory>>()
        expectedStory.value = data
        Mockito.`when`(storyRepository.getStory(token)).thenReturn(expectedStory)

        val listStoryViewModel = ListStoryViewModel(preference,storyRepository)
        val actualStory: PagingData<UserStory> = listStoryViewModel.getAllStories(token).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = ListStoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStory.size, differ.snapshot().size)
        assertEquals(dummyStory[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Story Empty Should Return No Data`() = runTest {
        val data: PagingData<UserStory> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<UserStory>>()
        expectedStory.value = data
        Mockito.`when`(storyRepository.getStory(token)).thenReturn(expectedStory)

        val listStoryViewModel = ListStoryViewModel(preference, storyRepository)
        val actualStory : PagingData<UserStory> = listStoryViewModel.getAllStories(token).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = ListStoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        assertEquals(0, differ.snapshot().size)
    }
}

val noopListCallback = object : ListUpdateCallback{
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}

class StoryPagingSource: PagingSource<Int, LiveData<List<UserStory>>>(){
    companion object{
        fun snapshot(item:List<UserStory>): PagingData<UserStory>{
            return PagingData.from(item)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<UserStory>>>): Int {
        return  0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<UserStory>>> {
        return LoadResult.Page(emptyList(), 0,1)
    }
}

