package com.example.desafioandroid.ui.view

import android.content.Intent
import android.net.ConnectivityManager
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.desafioandroid.R
import com.example.desafioandroid.data.model.RepositoriesModel
import com.example.desafioandroid.databinding.FragmentRepositoriesBinding
import com.example.desafioandroid.ui.adapter.RepositoriesAdapter
import com.example.desafioandroid.ui.state.State
import com.example.desafioandroid.ui.viewmodel.RepositoriesViewModel
import com.example.desafioandroid.util.network.NetworkCheck
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RepositoriesFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private val mViewModel: RepositoriesViewModel by viewModels()
    private val mAdapter by lazy { RepositoriesAdapter() }

    /** Verifica a conexão com a Internet */
    private val networkCheck by lazy {
        NetworkCheck(
            ContextCompat.getSystemService(
                requireContext(), ConnectivityManager::class.java
            )
        )
    }

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

        /** Coletores de StateFlow do ViewModel */
        collector()

        /** Configurações para a Recycler funcionar */
        setupRecycler()

        /** Carregar a lista de Repositórios da Api */
        loadList()

        /** Ações a serem feitas quando um ítem da Recycler é clicado */
        clickAdapter()

        /** Atualiza a lista de repositórios quando o usuário arrasta a tela para baixo */
        refresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /** Função auxiliar para quando por algum motivo não for encontrado nenhum repositório,
     *  seja por não existir, por estar desconectado da Internet ou por erro interno da API */
    private fun repositoriesNotFound(message: String) {
        binding.swipeRefresh.isRefreshing = false
        binding.progressCircular.visibility = View.INVISIBLE
        binding.textNotFound.visibility = View.VISIBLE
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun collector() = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            mViewModel.repositories.collectLatest { state ->
                when (state) {
                    is State.Sucess -> {
                        binding.swipeRefresh.isRefreshing = false
                        binding.progressCircular.visibility = View.INVISIBLE
                        binding.recyclerRepository.visibility = View.VISIBLE
                        state.data?.let { list ->
                            if (list.count() > 0) {
                                mAdapter.repositories = list.toList()
                                binding.textNotFound.visibility = View.INVISIBLE
                            } else {
                                mAdapter.repositories = emptyList()
                                repositoriesNotFound(getString(R.string.not_found_repository))
                            }
                        }
                    }
                    is State.Loading -> {
                        binding.progressCircular.visibility = View.VISIBLE
                        binding.recyclerRepository.visibility = View.INVISIBLE
                    }
                    is State.Error -> {
                        repositoriesNotFound(getString(R.string.error))
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
        var list: List<RepositoriesModel> = emptyList()
        mViewModel.repositories.value.data?.let {
            list = it
        }
        if (networkCheck.hasConnection()) {
            mViewModel.repositories(list)
        } else {
            repositoriesNotFound(getString(R.string.internet_error))
        }
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

    private fun refresh() {
        binding.swipeRefresh.setOnRefreshListener {
            if (networkCheck.hasConnection()) {
                mViewModel.repositories(emptyList())
            } else {
                loadList()
            }
        }
    }

    override fun onRefresh() {}
}