package com.example.desafioandroid.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.desafioandroid.data.model.RepositoriesModel
import com.example.desafioandroid.databinding.ItemRepositoryBinding
import com.example.desafioandroid.util.loadImage

class RepositoriesAdapter : RecyclerView.Adapter<RepositoriesAdapter.RepositoriesViewHolder>() {

    inner class RepositoriesViewHolder(val binding: ItemRepositoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<RepositoriesModel>() {

        override fun areItemsTheSame(oldItem: RepositoriesModel, newItem: RepositoriesModel): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: RepositoriesModel, newItem: RepositoriesModel): Boolean {
            return oldItem.description == newItem.description && oldItem.forks == newItem.forks &&
                    oldItem.nameRepo == newItem.nameRepo && oldItem.owner == newItem.owner &&
                    oldItem.stars == newItem.stars
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    var repositories: List<RepositoriesModel>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoriesViewHolder {
        return RepositoriesViewHolder(
            ItemRepositoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RepositoriesViewHolder, position: Int) {
        val repository = repositories[position]
        holder.binding.apply {
            textNameRepository.text = repository.nameRepo
            textDescription.text = repository.description
            textFork.text = repository.forks.toString()
            textStar.text = repository.stars.toString()
            textUserName.text = repository.owner.name
            loadImage(imageAvatar, repository.owner.avatar)
        }
    }

    override fun getItemCount(): Int = repositories.size

}