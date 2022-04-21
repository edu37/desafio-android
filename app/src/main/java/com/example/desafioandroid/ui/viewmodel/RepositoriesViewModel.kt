package com.example.desafioandroid.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desafioandroid.data.model.RepositoriesModelResponse
import com.example.desafioandroid.repository.GitRepository
import com.example.desafioandroid.ui.state.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoriesViewModel @Inject constructor(
    private val gitRepository: GitRepository
) : ViewModel() {

    private val mTest = MutableStateFlow<State<RepositoriesModelResponse>>(State.Empty())
    val test = mTest.asStateFlow()

    fun repositories() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mTest.value = gitRepository.repositories()
            } catch (t: Throwable) {
                Log.e("RepositoriesVM", t.message.toString())
            }

        }
    }

    fun saveData(userName: String, repoName: String) {
        viewModelScope.launch {
            gitRepository.saveShared(userName, repoName)
        }
    }

}