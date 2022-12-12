package com.android.sepiapets.utils

import android.widget.ImageView
import coil.load

fun ImageView.setImage(imageUrl: String) {
    this.load(imageUrl) {
        crossfade(1000)
        build()
    }
}

