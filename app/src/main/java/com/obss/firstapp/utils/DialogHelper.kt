package com.obss.firstapp.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import coil.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.obss.firstapp.R
import com.obss.firstapp.data.model.actor.Actor
import com.obss.firstapp.ui.view.detail.DetailFragment.Companion.BIOGRAPHY_MAX_LENGTH
import com.obss.firstapp.ui.view.detail.DetailFragment.Companion.BIOGRAPHY_MAX_LINE
import com.obss.firstapp.utils.Constants.IMAGE_BASE_URL
import com.obss.firstapp.utils.ext.formatAndCalculateAge

object DialogHelper {
    fun showCustomAlertDialog(
        context: Context,
        message: String,
    ): Dialog {
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
        return alertDialog
    }

    fun showActorDialog(
        context: Context,
        actor: Actor,
        onDismissAction: () -> Unit,
    ): BottomSheetDialog {
        val actorDialog = LayoutInflater.from(context).inflate(R.layout.bottomsheet_dialog, null)
        val dialog = BottomSheetDialog(context, R.style.DialogAnimation)
        val behavior = dialog.behavior
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.setContentView(actorDialog)
        val tvActorBiography = dialog.findViewById<TextView>(R.id.tv_actor_biography)
        val tvBiographySeeMore = dialog.findViewById<TextView>(R.id.tv_actor_biography_see_more)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setOnDismissListener {
            onDismissAction()
        }
        if (actor.profilePath.isNullOrEmpty()) {
            if (actor.gender == 1) {
                dialog.findViewById<ImageView>(R.id.iv_actor_profile)?.setImageResource(R.drawable.face_female_24)
            } else {
                dialog.findViewById<ImageView>(R.id.iv_actor_profile)?.setImageResource(R.drawable.face_male_24)
            }
        } else {
            dialog.findViewById<ImageView>(R.id.iv_actor_profile)?.load("$IMAGE_BASE_URL${actor.profilePath}")
        }
        dialog.findViewById<TextView>(R.id.tv_actor_name)?.text = actor.name
        dialog.findViewById<TextView>(R.id.tv_actor_birthday)?.text =
            if (actor.birthday.isNullOrEmpty()) "-" else actor.birthday.formatAndCalculateAge()
        dialog.findViewById<TextView>(R.id.tv_place_of_birth)?.text =
            if (actor.placeOfBirth.isNullOrEmpty()) "-" else actor.placeOfBirth.toString()
        val ibActorWebsite = dialog.findViewById<ImageButton>(R.id.ib_actor_webpage)
        ibActorWebsite?.isVisible = actor.homepage != null
        ibActorWebsite?.setOnClickListener {
            val url = actor.homepage.toString()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        }
        if (actor.biography.isNullOrEmpty()) {
            tvActorBiography?.text = "-"
        } else {
            tvActorBiography?.text = actor.biography
            setActorDetailsSeeMoreButton(context, actor, tvActorBiography, tvBiographySeeMore)
        }
        return dialog
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

    fun showToastMessage(
        context: Context,
        message: String,
        drawable: Int = R.drawable.custom_toast_background,
        icon: Int = R.drawable.add_movie_24,
    ) {
        val view = LayoutInflater.from(context).inflate(R.layout.toast_background, null)
        view.background = ContextCompat.getDrawable(context, drawable)
        val toastMessage = view.findViewById<TextView>(R.id.tv_toast_message)
        val toastIcon = view.findViewById<ImageView>(R.id.iv_toast_icon)
        toastMessage.text = message
        toastIcon.setImageResource(icon)

        val toast = Toast(context)
        toast.view = view
        toast.duration = Toast.LENGTH_SHORT
        toast.show()
    }
}
