package org.sabaini.pokedex.util

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette

object ColorUtils {

    fun calculateDominantColor(
        drawable: Drawable,
        onFinish: (Color) -> Unit,
    ) {
        if (drawable is BitmapDrawable) {
            val bmp = drawable.bitmap.copy(Bitmap.Config.ARGB_8888, true)

            Palette.from(bmp)
                .maximumColorCount(12)
                .resizeBitmapArea(100 * 100)
                .generate { palette ->
                    palette?.dominantSwatch?.rgb?.let {
                        onFinish(Color(it))
                    }
                }
        }
    }
}
