package com.obss.firstapp.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.obss.firstapp.R
import com.obss.firstapp.ext.formatAndCalculateAge
import com.obss.firstapp.model.actor.Actor
import com.obss.firstapp.ui.detail.DetailFragment.Companion.BIOGRAPHY_MAX_LENGTH
import com.obss.firstapp.ui.detail.DetailFragment.Companion.BIOGRAPHY_MAX_LINE
import com.obss.firstapp.utils.Constants.IMAGE_BASE_URL

object DialogHelper {
    fun showCustomAlertDialog(
        context: Context,
        message: String,
    ) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.error_dialog, null)

        val alertDialogBuilder =
            AlertDialog
                .Builder(context)
                .setView(dialogView)

        val alertDialog = alertDialogBuilder.show()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(0))

        val messageTextView: TextView = dialogView.findViewById(R.id.tv_error_dialog_desc)
        val closeButton: Button = dialogView.findViewById(R.id.btn_error_close)

        messageTextView.text = message

        closeButton.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    fun showActorDialog(
        context: Context,
        actor: Actor,
    ) {
        val actorDialog = LayoutInflater.from(context).inflate(R.layout.bottomsheet_dialog, null)
        val dialog = BottomSheetDialog(context, R.style.DialogAnimation)
        dialog.setContentView(actorDialog)
        val tvActorBiography = dialog.findViewById<TextView>(R.id.tv_actor_biography)
        val tvBiographySeeMore = dialog.findViewById<TextView>(R.id.tv_actor_biography_see_more)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.findViewById<ImageView>(R.id.iv_actor_profile)?.load("$IMAGE_BASE_URL${actor.profilePath}")
        dialog.findViewById<TextView>(R.id.tv_actor_name)?.text = actor.name
        dialog.findViewById<TextView>(R.id.tv_actor_birthday)?.text =
            if (actor.birthday.isNullOrEmpty()) "-" else actor.birthday.formatAndCalculateAge()
        dialog.findViewById<TextView>(R.id.tv_place_of_birth)?.text =
            if (actor.placeOfBirth.isNullOrEmpty()) "-" else actor.placeOfBirth.toString()
        dialog.findViewById<TextView>(R.id.tv_actor_webpage)?.text = if (actor.homepage == null) "-" else actor.homepage.toString()
        if (actor.biography.isNullOrEmpty()) {
            tvActorBiography?.text = "-"
        } else {
            tvActorBiography?.text = actor.biography
            setActorDetailsSeeMoreButton(context, actor, tvActorBiography, tvBiographySeeMore)
        }
        dialog.show()
    }

    private fun setActorDetailsSeeMoreButton(
        context: Context,
        actor: Actor,
        tvActorBiography: TextView?,
        tvBiographySeeMore: TextView?,
    ) {
        if (actor.biography?.length!! > BIOGRAPHY_MAX_LENGTH) {
            tvBiographySeeMore?.visibility = View.VISIBLE
            tvActorBiography?.maxLines = BIOGRAPHY_MAX_LINE
            tvBiographySeeMore?.setOnClickListener {
                if (tvActorBiography?.maxLines == BIOGRAPHY_MAX_LINE) {
                    tvActorBiography.maxLines = Int.MAX_VALUE
                    tvBiographySeeMore.text = context.resources.getString(R.string.see_less)
                    val drawable = ContextCompat.getDrawable(context, R.drawable.see_less_24)
                    tvBiographySeeMore.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                } else {
                    tvActorBiography?.maxLines = BIOGRAPHY_MAX_LINE
                    tvBiographySeeMore.text = context.resources.getString(R.string.see_more)
                    val drawable = ContextCompat.getDrawable(context, R.drawable.see_more_24)
                    tvBiographySeeMore.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                }
            }
        }
    }
}
