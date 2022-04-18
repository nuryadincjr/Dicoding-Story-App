package com.nuryadincjr.storyapp.view.main

import android.content.Intent
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nuryadincjr.storyapp.R
import com.nuryadincjr.storyapp.adapter.LoadingStateAdapter
import com.nuryadincjr.storyapp.adapter.StoriesListAdapter
import com.nuryadincjr.storyapp.data.factory.StoriesFactory
import com.nuryadincjr.storyapp.data.model.SettingsPreference
import com.nuryadincjr.storyapp.data.model.SettingsPreference.Companion.dataStore
import com.nuryadincjr.storyapp.databinding.ActivityMainBinding
import com.nuryadincjr.storyapp.util.Constant.SPAN_COUNT
import com.nuryadincjr.storyapp.view.added.AddStoryActivity
import com.nuryadincjr.storyapp.view.location.MapsActivity
import com.nuryadincjr.storyapp.view.settings.SettingsActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels {
        val preference = SettingsPreference.getInstance(dataStore)
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
        binding.recyclerView.apply {
            layoutManager =
                if (resources?.configuration?.orientation == ORIENTATION_LANDSCAPE) {
                    GridLayoutManager(this@MainActivity, SPAN_COUNT)
                } else LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }

        binding.apply {
            fabStory.setOnClickListener(this@MainActivity)
            fabLocation.setOnClickListener(this@MainActivity)

            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) {
                        fabStory.hide()
                        fabLocation.hide()
                    } else {
                        fabStory.show()
                        fabLocation.show()
                    }
                }
            })
        }
    }

    private fun onSubscribe() {
        val adapter = StoriesListAdapter()
        binding.recyclerView.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter(adapter::retry)
        )

        mainViewModel.apply {
            getUser().observe(this@MainActivity) { user ->
                setToken(user.token)

                getStory().observe(this@MainActivity) {
                    adapter.submitData(lifecycle, it)
                }
            }
        }
    }

    override fun onClick(p0: View?) {
        var clazz: Class<*>? = null
        when (p0?.id) {
            R.id.fab_story -> {
                clazz = AddStoryActivity::class.java

            }
            R.id.fab_location -> {
                clazz = MapsActivity::class.java
            }
        }

        if (clazz != null) {
            startActivity(
                Intent(this, clazz),
                ActivityOptionsCompat
                    .makeSceneTransitionAnimation(this)
                    .toBundle()
            )
        }
    }
}