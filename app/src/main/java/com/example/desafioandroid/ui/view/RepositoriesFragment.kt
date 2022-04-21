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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desafioandroid.R
import com.example.desafioandroid.databinding.FragmentRepositoriesBinding
import com.example.desafioandroid.ui.adapter.RepositoriesAdapter
import com.example.desafioandroid.ui.state.State
import com.example.desafioandroid.ui.viewmodel.RepositoriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RepositoriesFragment : Fragment() {

    private val mViewModel: RepositoriesViewModel by viewModels()
    private val mAdapter by lazy { RepositoriesAdapter() }

    private var _binding: FragmentRepositoriesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRepositoriesBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Settar os coletores
        collector()

        // Criar o adapter da recycler
        setupRecycler()

        // Carregar a lista de repositórios da Api
        loadList()

        // Evento que ocorre ao clicar em um ítem da lista
        clickAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun collector() = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            mViewModel.test.collectLatest { state ->
                when (state) {
                    is State.Sucess -> {
                        state.data?.let { response ->
                            if (response.items.count() > 0) {
                                mAdapter.repositories = response.items.toList()
                            } else {
                                mAdapter.repositories = arrayListOf()
                                Toast.makeText(requireContext(), "Vazio", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    is State.Error -> {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun setupRecycler() = with(binding) {
        recyclerRepository.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun loadList() {
        mViewModel.repositories()
    }

    private fun clickAdapter() {
        mAdapter.setOnClickListener { repository ->
            val userName = repository.owner.name
            val repoName = repository.repoName
            mViewModel.saveData(userName, repoName)
            findNavController().navigate(R.id.action_RepositoriesFragment_to_PullRequestsFragment)
        }
        mAdapter.setOnUserClickListener { repository ->
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = (Uri.parse(repository.owner.userUrl))
            startActivity(intent)
        }
    }
}