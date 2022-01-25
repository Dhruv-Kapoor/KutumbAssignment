package com.example.kutumbassignment.utils

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import java.text.NumberFormat

object BindingUtils {

    @JvmStatic
    @BindingAdapter("numberedText")
    fun setNumberedText(view: TextView, number: Int){
        val text = NumberFormat.getInstance().format(number)
        view.text = text
    }

    @JvmStatic
    @BindingAdapter("glide")
    fun setImageUsingGlide(view: ImageView, url: String){
        Glide.with(view).load(url).into(view)
    }

    @JvmStatic
    @BindingAdapter("color")
    fun setColor(view: ImageView, color: String?){
        color?:return
        view.setBackgroundColor(Color.parseColor(color))
    }

    fun Int.dp(context: Context): Float{
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
        )
    }

    @JvmStatic
    @BindingAdapter("elevationBasedOnExpanded")
    fun setElevationBasedOnExpansion(view: View, isExpanded: Boolean){
        view.elevation = if (isExpanded) 8.dp(view.context) else 0F
    }
}