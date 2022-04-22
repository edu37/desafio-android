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

    /** Observa o estado dos dados recebidos pelo [gitRepository]. */
    private val mRepositories =
        MutableStateFlow<State<List<RepositoriesModel>>>(State.Empty())
    val repositories = mRepositories.asStateFlow()

    /** Manda o [gitRepository] devolver uma lista com os repositórios da Api e testa se houver erro.  */
    fun repositories(list: List<RepositoriesModel>) {
        if (list.count() > 0) {
            mRepositories.value = State.Sucess(list)
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    mRepositories.value = State.Loading()
                    delay(500)
                    mRepositories.value = gitRepository.repositories()
                } catch (t: Throwable) {
                    mRepositories.value = State.Error(t.message.toString())
                    Log.e("RepositoriesVM", t.message.toString())
                }
            }
        }
    }

    /** Manda o [gitRepository] salvar estes dados no SharedPreferences. */
    fun saveData(userName: String, repoName: String) {
        viewModelScope.launch {
            gitRepository.saveShared(userName, repoName)
        }
    }
}