package com.nuryadincjr.storyapp.view.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.nuryadincjr.storyapp.adapter.StoriesListAdapter
import com.nuryadincjr.storyapp.data.remote.response.StoryItem
import com.nuryadincjr.storyapp.DataDummy
import com.nuryadincjr.storyapp.MainCoroutineRule
import com.nuryadincjr.storyapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoriesViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRules = MainCoroutineRule()

    @Mock
    private lateinit var storiesViewModel: StoriesViewModel

    private val dummyStories = DataDummy.generateDummyStoriesEntity()

    @Test
    fun `when getStory Should Not Null`() = mainCoroutineRules.runBlockingTest {
        val pagingStories = PagedTestDataSources.snapshot(dummyStories)
        val expectedStories = MutableLiveData<PagingData<StoryItem>>()
        expectedStories.value = pagingStories

        `when`(storiesViewModel.getStory()).thenReturn(expectedStories)
        val pagingStory = storiesViewModel.getStory().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoriesListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainCoroutineRules.dispatcher,
            workerDispatcher = mainCoroutineRules.dispatcher,
        )
        differ.submitData(pagingStory)

        advanceUntilIdle()

        verify(storiesViewModel).getStory()

        assertNotNull(differ.snapshot())
        assertEquals(dummyStories.size, differ.snapshot().size)
        assertEquals(dummyStories[0].id, differ.snapshot()[0]?.id)
    }

    class PagedTestDataSources private constructor() :
        PagingSource<Int, LiveData<List<StoryItem>>>() {
        companion object {
            fun snapshot(items: List<StoryItem>): PagingData<StoryItem> {
                return PagingData.from(items)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryItem>>>): Int {
            return 0
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryItem>>> {
            return LoadResult.Page(emptyList(), 0, 1)
        }
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}