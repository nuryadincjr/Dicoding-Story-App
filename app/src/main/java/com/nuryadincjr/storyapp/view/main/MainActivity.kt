package com.nuryadincjr.storyapp.view.main

import android.content.Context
import android.content.Intent
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nuryadincjr.storyapp.R
import com.nuryadincjr.storyapp.adapter.ListStoriesAdapter
import com.nuryadincjr.storyapp.data.Result
import com.nuryadincjr.storyapp.data.factory.StoriesFactory
import com.nuryadincjr.storyapp.data.model.UsersPreference
import com.nuryadincjr.storyapp.data.remote.response.Stories
import com.nuryadincjr.storyapp.data.remote.response.StoryItem
import com.nuryadincjr.storyapp.databinding.ActivityMainBinding
import com.nuryadincjr.storyapp.util.Constant.PREF_SESSION
import com.nuryadincjr.storyapp.util.Constant.SPAN_COUNT
import com.nuryadincjr.storyapp.view.added.AddStoryActivity
import com.nuryadincjr.storyapp.view.settings.SettingsActivity
import com.nuryadincjr.storyapp.widget.ListStoryWidget

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREF_SESSION)

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels {
        val preference = UsersPreference.getInstance(dataStore)
        StoriesFactory.getInstance(this, preference)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        onSubscribe()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_settings) startSettings()
        return super.onOptionsItemSelected(item)
    }

    private fun startSettings() {
        val intent = Intent(this, SettingsActivity::class.java)

        startActivity(
            intent,
            ActivityOptionsCompat
                .makeSceneTransitionAnimation(this)
                .toBundle()
        )
    }

    private fun setupView() {
        binding.apply {
            fabStory.setOnClickListener {
                val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
                startActivity(
                    intent,
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity).toBundle()
                )
            }

            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) fabStory.hide() else fabStory.show()
                }
            })
        }
    }

    private fun onSubscribe() {
        mainViewModel.apply {
            getUser().observe(this@MainActivity) { user ->
                setToken(user.token)
                getStories().observe(this@MainActivity) {
                    onResult(it)
                }
            }
        }
    }

    private fun onResult(result: Result<List<StoryItem>>) {
        binding.apply {
            when (result) {
                is Result.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    val listStory = result.data

                    progressBar.visibility = View.GONE
                    showRecyclerList(listStory)

                    val listStoryWidget = Stories(listStory)
                    ListStoryWidget.setList(listStoryWidget)
                }
                is Result.Error -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@MainActivity, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showRecyclerList(list: List<StoryItem>) {
        val listUsersAdapter = ListStoriesAdapter(list)

        binding.recyclerView.apply {
            layoutManager =
                if (resources?.configuration?.orientation == ORIENTATION_LANDSCAPE) {
                    GridLayoutManager(context, SPAN_COUNT)
                } else LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = listUsersAdapter
        }
    }
}