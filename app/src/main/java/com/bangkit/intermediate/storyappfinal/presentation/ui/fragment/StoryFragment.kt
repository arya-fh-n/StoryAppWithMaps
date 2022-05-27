package com.bangkit.intermediate.storyappfinal.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.intermediate.storyappfinal.databinding.FragmentStoryBinding
import com.bangkit.intermediate.storyappfinal.presentation.adapter.LoadingStateAdapter
import com.bangkit.intermediate.storyappfinal.presentation.adapter.StoryAdapter
import com.bangkit.intermediate.storyappfinal.presentation.viewmodel.StoryViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val USER_TOKEN = "token"

@AndroidEntryPoint
class StoryFragment : Fragment() {

    private lateinit var binding: FragmentStoryBinding
    private val storyViewModel: StoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStoryBinding.inflate(layoutInflater, container, false)
        binding.rvStory.layoutManager = LinearLayoutManager(activity)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            token = it.getString(USER_TOKEN)
        }

        token?.let {
            getStory(it)
        }
    }

    private fun getStory(token: String) {
        val storyAdapter = StoryAdapter()
        binding.rvStory.adapter = storyAdapter.withLoadStateFooter(
            LoadingStateAdapter {
                storyAdapter.retry()
            }
        )

        storyViewModel.getStoryPager("Bearer $token").observe(viewLifecycleOwner) { story ->
            storyAdapter.refresh()
            storyAdapter.submitData(lifecycle, story)
        }
    }

    companion object {
        var token: String? = null
    }
}