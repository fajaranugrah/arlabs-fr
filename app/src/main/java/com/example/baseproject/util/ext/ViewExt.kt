package com.example.baseproject.util.ext

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import com.google.android.material.snackbar.Snackbar


fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.visibility(isVisible: Boolean) {
    this.visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.showSnackbar(
    message: String?,
    duration: Int = Snackbar.LENGTH_SHORT,
) {
    Snackbar.make(this, message ?: "", duration).show()
}

fun View.visibleWithAnim() {
    val shortAnimationDuration =
        context?.resources?.getInteger(android.R.integer.config_shortAnimTime) ?: 0

    // Set the content view to 0% opacity but visible, so that it is visible
    // (but fully transparent) during the animation.
    alpha = 0f
    visibility = View.VISIBLE

    // Animate the content view to 100% opacity, and clear any animation
    // listener set on the view.
    animate()
        .alpha(1f)
        .setDuration(shortAnimationDuration.toLong())
        .setListener(null)
}

fun View.goneWithAnim() {
    val shortAnimationDuration =
        context?.resources?.getInteger(android.R.integer.config_shortAnimTime) ?: 0

    // Animate the showProgress view to 0% opacity. After the animation ends,
    // set its visibility to GONE as an optimization step (it won't
    // participate in layout passes, etc.)
    animate()
        ?.alpha(0f)
        ?.setDuration(shortAnimationDuration.toLong())
        ?.setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                visibility = View.GONE
            }
        })
}

fun View.invisibleWithAnim() {
    val shortAnimationDuration =
        context?.resources?.getInteger(android.R.integer.config_shortAnimTime) ?: 0

    // Animate the showProgress view to 0% opacity. After the animation ends,
    // set its visibility to GONE as an optimization step (it won't
    // participate in layout passes, etc.)
    animate()
        ?.alpha(0f)
        ?.setDuration(shortAnimationDuration.toLong())
        ?.setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                visibility = View.INVISIBLE
            }
        })
}