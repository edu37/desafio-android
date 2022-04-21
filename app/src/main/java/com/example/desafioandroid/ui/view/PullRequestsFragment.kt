package com.example.desafioandroid.ui.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desafioandroid.databinding.FragmentPullRequestsBinding
import com.example.desafioandroid.ui.adapter.PullRequestsAdapter
import com.example.desafioandroid.ui.state.State
import com.example.desafioandroid.ui.viewmodel.PullRequestsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PullRequestsFragment : Fragment() {

    private val mViewModel: PullRequestsViewModel by viewModels()
    private val mAdapter by lazy { PullRequestsAdapter() }

    private var _binding: FragmentPullRequestsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPullRequestsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collector()


        setupRecycler()


        loadList()


        clickAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun collector() = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            mViewModel.pullRequests.collectLatest {
                when (it) {
                    is State.Sucess -> {
                        it.data?.let { list ->
                            if (list.count() > 0) {
                                mAdapter.pullRequests = list
                            } else {
                                mAdapter.pullRequests = arrayListOf()
                                Toast.makeText(
                                    requireContext(),
                                    "Vazio",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                    is State.Error -> {
                        Toast.makeText(
                            requireContext(),
                            "Erro ao carregar PullRequests",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun setupRecycler() = with(binding) {
        recyclerPullRequests.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun loadList() {
        mViewModel.listPull()
    }

    private fun clickAdapter() {
        mAdapter.setOnClickListener { pullRequest ->
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(pullRequest.urlPull)
            startActivity(intent)
        }
    }
}