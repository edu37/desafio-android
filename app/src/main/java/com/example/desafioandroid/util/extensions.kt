package com.example.desafioandroid.util

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.desafioandroid.R
import com.google.android.material.imageview.ShapeableImageView
import de.hdodenhof.circleimageview.CircleImageView

/** Carrega uma imagem utilizando Glide. */
fun loadImage(imageView: CircleImageView, url: String) {
    Glide.with(imageView.context)
        .load(url)
        .placeholder(R.drawable.perfil)
        .error(R.drawable.perfil)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .into(imageView)
}

/** Função que pega uma string e a retorna com o numero de characteres que é passado. */
fun String.limitDescription(characters: Int): String {
    if (this.length > characters) {
        val firstCharacter = 0
        return "${this.substring(firstCharacter, characters)}..."
    }
    return this
}