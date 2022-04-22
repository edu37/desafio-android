package com.example.desafioandroid.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desafioandroid.data.model.RepositoriesModel
import com.example.desafioandroid.repository.GitRepository
import com.example.desafioandroid.ui.state.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoriesViewModel @Inject constructor(
    private val gitRepository: GitRepository
) : ViewModel() {

    private val mTest = MutableStateFlow<State<List<RepositoriesModel>>>(State.Empty())
    val test = mTest.asStateFlow()

    fun repositories(list: List<RepositoriesModel>) {
        if (list.count() > 0) {
            mTest.value = State.Sucess(list)
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    mTest.value = State.Loading()
                    delay(500)
                    mTest.value = gitRepository.repositories()
                } catch (t: Throwable) {
                    mTest.value = State.Error(t.message)
                    Log.e("RepositoriesVM", t.message.toString())
                }
            }
        }
    }

    fun saveData(userName: String, repoName: String) {
        viewModelScope.launch {
            gitRepository.saveShared(userName, repoName)
        }
    }

}