package com.example.desafioandroid.util

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.desafioandroid.R
import com.google.android.material.imageview.ShapeableImageView

fun loadImage(imageView: ShapeableImageView, url: String) {
    Glide.with(imageView.context)
        .load(url)
        .error(R.drawable.perfil)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .into(imageView)
}