package com.example.desafioandroid.ui.view

import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desafioandroid.R
import com.example.desafioandroid.databinding.FragmentPullRequestsBinding
import com.example.desafioandroid.ui.adapter.PullRequestsAdapter
import com.example.desafioandroid.ui.state.State
import com.example.desafioandroid.ui.viewmodel.PullRequestsViewModel
import com.example.desafioandroid.util.network.NetworkCheck
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PullRequestsFragment : Fragment() {

    private val mViewModel: PullRequestsViewModel by viewModels()
    private val mAdapter by lazy { PullRequestsAdapter() }

    /** Verifica a conexão com a Internet */
    private val networkCheck by lazy {
        NetworkCheck(
            ContextCompat.getSystemService(
                requireContext(), ConnectivityManager::class.java
            )
        )
    }

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

        /** Coletores de StateFlow do ViewModel */
        collector()

        /** Configurações para a Recycler funcionar */
        setupRecycler()

        /** Carregar a lista de PullRequests da Api */
        loadList()

        /** Ações a serem feitas quando um ítem da Recycler é clicado */
        clickAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun pullRequestNotFound(message: String) {
        binding.progressCircular.visibility = View.INVISIBLE
        binding.textNotFound.visibility = View.VISIBLE
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun collector() = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            mViewModel.pullRequests.collectLatest { state ->
                when (state) {
                    is State.Sucess -> {
                        binding.progressCircular.visibility = View.INVISIBLE
                        state.data?.let { list ->
                            if (list.count() > 0) {
                                binding.textNotFound.visibility = View.INVISIBLE
                                mAdapter.pullRequests = list
                            } else {
                                mAdapter.pullRequests = arrayListOf()
                                pullRequestNotFound(getString(R.string.not_found_pull_request))
                            }
                        }
                    }
                    is State.Loading -> {
                        binding.progressCircular.visibility = View.VISIBLE
                    }
                    is State.Error -> {
                        pullRequestNotFound(getString(R.string.error))
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
        if (networkCheck.hasConnection()) {
            mViewModel.listPull()
        } else {
            pullRequestNotFound(getString(R.string.internet_error))
        }
    }

    private fun clickAdapter() {
        mAdapter.apply {
            setOnClickListener { pullRequest ->
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(pullRequest.urlPull)
                startActivity(intent)
            }
            setOnUserClickListener { pullRequest ->
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(pullRequest.user.userUrl)
                startActivity(intent)
            }
        }
    }
}