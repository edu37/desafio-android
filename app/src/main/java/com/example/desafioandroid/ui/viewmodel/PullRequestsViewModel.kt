package com.example.desafioandroid.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desafioandroid.data.model.PullRequestsModel
import com.example.desafioandroid.repository.GitRepository
import com.example.desafioandroid.ui.state.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PullRequestsViewModel @Inject constructor(
    private val repository: GitRepository
) : ViewModel() {

    private val mPullRequests =
        MutableStateFlow<State<List<PullRequestsModel>>>(State.Empty())
    val pullRequests = mPullRequests.asStateFlow()

    fun listPull() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mPullRequests.value = repository.pullRequests()
            } catch (t: Throwable) {
                Log.i("PullRequestVM", t.message.toString())
            }
        }
    }
}