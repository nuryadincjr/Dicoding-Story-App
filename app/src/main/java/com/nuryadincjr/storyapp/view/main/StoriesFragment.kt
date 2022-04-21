package com.nuryadincjr.storyapp.view.main

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nuryadincjr.storyapp.R
import com.nuryadincjr.storyapp.adapter.LoadingStateAdapter
import com.nuryadincjr.storyapp.adapter.StoriesListAdapter
import com.nuryadincjr.storyapp.data.factory.StoriesFactory
import com.nuryadincjr.storyapp.data.model.SettingsPreference
import com.nuryadincjr.storyapp.data.model.SettingsPreference.Companion.dataStore
import com.nuryadincjr.storyapp.databinding.FragmentStoriesBinding
import com.nuryadincjr.storyapp.util.Constant
import com.nuryadincjr.storyapp.view.added.AddStoryActivity
import com.nuryadincjr.storyapp.view.location.MapsActivity
import com.nuryadincjr.storyapp.view.settings.SettingsActivity

class StoriesFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentStoriesBinding? = null
    private val binding get() = _binding

    private val storiesViewModel: StoriesViewModel by viewModels {
        val preference = SettingsPreference.getInstance(requireActivity().dataStore)
        StoriesFactory.getInstance(requireContext(), preference)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStoriesBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        onSubscribe()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.settings_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_settings) startSettings()
        return super.onOptionsItemSelected(item)
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
                Intent(requireContext(), clazz),
                ActivityOptionsCompat
                    .makeSceneTransitionAnimation(requireActivity())
                    .toBundle()
            )
        }
    }

    private fun startSettings() {
        val intent = Intent(requireContext(), SettingsActivity::class.java)

        startActivity(
            intent,
            ActivityOptionsCompat
                .makeSceneTransitionAnimation(requireActivity())
                .toBundle()
        )
    }

    private fun setupView() {
        binding?.recyclerView?.apply {
            layoutManager =
                if (resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    GridLayoutManager(requireContext(), Constant.SPAN_COUNT)
                } else LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        binding?.apply {
            fabStory.setOnClickListener(this@StoriesFragment)
            fabLocation.setOnClickListener(this@StoriesFragment)

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
        binding?.recyclerView?.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter(adapter::retry)
        )

        storiesViewModel.apply {
            getUser().observe(viewLifecycleOwner) { user ->
                setToken(user.token)

                getStory().observe(viewLifecycleOwner) {
                    adapter.submitData(lifecycle, it)
                }
            }
        }
    }

    companion object {
        fun newInstance() = StoriesFragment()
    }
}