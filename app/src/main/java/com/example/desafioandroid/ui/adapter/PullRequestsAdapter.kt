package com.example.desafioandroid.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.desafioandroid.data.model.PullRequestsModel
import com.example.desafioandroid.databinding.ItemPullRequestBinding
import com.example.desafioandroid.util.limitDescription
import com.example.desafioandroid.util.loadImage

class PullRequestsAdapter : RecyclerView.Adapter<PullRequestsAdapter.PullRequestsViewHolder>() {

    inner class PullRequestsViewHolder(val binding: ItemPullRequestBinding) :
        RecyclerView.ViewHolder(binding.root)
    
    private val differCallBack = object : DiffUtil.ItemCallback<PullRequestsModel>() {
        override fun areItemsTheSame(
            oldItem: PullRequestsModel,
            newItem: PullRequestsModel
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(
            oldItem: PullRequestsModel,
            newItem: PullRequestsModel
        ): Boolean {
            return oldItem.user == newItem.user && oldItem.title == newItem.title &&
                    oldItem.date == newItem.date && oldItem.body == newItem.body &&
                    oldItem.urlPull == newItem.urlPull
        }
    }

    private val differ = AsyncListDiffer(this, differCallBack)

    var pullRequests: List<PullRequestsModel>
    get() = differ.currentList
    set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PullRequestsViewHolder {
        return PullRequestsViewHolder(
            ItemPullRequestBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = pullRequests.size

    override fun onBindViewHolder(holder: PullRequestsViewHolder, position: Int) {
        val pullRequest = pullRequests[position]

        holder.binding.apply {
            textUserName.text = pullRequest.user.name
            textBody.text = pullRequest.body?.limitDescription(50)
            textTituloPull.text = pullRequest.title.limitDescription(30)
            textDate.text = pullRequest.date
            loadImage(imageAvatar, pullRequest.user.avatar)
        }

        holder.itemView.setOnClickListener {
            onClickListener?.let {
                it(pullRequest)
            }
        }
    }


    private var onClickListener: ((PullRequestsModel) -> Unit)? = null

    fun setOnClickListener(listener: (PullRequestsModel) -> Unit) {
        onClickListener = listener
    }

}