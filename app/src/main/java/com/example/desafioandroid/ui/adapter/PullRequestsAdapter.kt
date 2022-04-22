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
import java.text.SimpleDateFormat
import java.util.*

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

        /** Atribuir os valores de cada Pull Request
         *  para os ítens da Recycler. */
        holder.binding.apply {
            textUserName.text = pullRequest.user.name
            textBody.text = pullRequest.body.toString()
            textTituloPull.text = pullRequest.title

            /** Formatação da data recebida pela API. */
            val dateFormat = pullRequest.date.limitDescription(10)
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(dateFormat)
            textDate.text = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(date!!)

            /** Função extension para carregar imagens. */
            loadImage(imageAvatar, pullRequest.user.avatar)


            /** Listeners para quando a imagem ou o nome do usuário for clicado. */
            textUserName.setOnClickListener {
                onUserClickListener?.let {
                    it(pullRequest)
                }
            }
            imageAvatar.setOnClickListener {
                onUserClickListener?.let {
                    it(pullRequest)
                }
            }
        }

        /** Listener para quando um ítem é clicado. */
        holder.itemView.setOnClickListener {
            onClickListener?.let {
                it(pullRequest)
            }
        }
    }

    /** Listeners das ações enviadas pelo fragment e suas funções de acesso: */
    private var onClickListener: ((PullRequestsModel) -> Unit)? = null

    private var onUserClickListener: ((PullRequestsModel) -> Unit)? = null

    fun setOnClickListener(listener: (PullRequestsModel) -> Unit) {
        onClickListener = listener
    }

    fun setOnUserClickListener(listener: (PullRequestsModel) -> Unit) {
        onUserClickListener = listener
    }

}