package com.responsivebytes.gradeconverter

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_add.view.*
import kotlinx.android.synthetic.main.activity_info.view.*

class InfoDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)

        val dialogView = LayoutInflater.from(activity!!)
                .inflate(R.layout.activity_info, null, false)

        dialogView.infoCloseButton.setOnClickListener {
            dismiss()
        }

        dialogView.emailTextView.setOnClickListener {
            val email = (it as TextView).text.toString()
            openEmail(email)
        }

        builder.setView(dialogView)

        return builder.create()
    }

    override fun onActivityCreated(arg0: Bundle?) {
        super.onActivityCreated(arg0)
        dialog.window!!
                .attributes.windowAnimations = R.style.DialogAnimation
    }

    private fun openEmail(mailTo: String) {
        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", mailTo, null))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_title))
        startActivity(Intent.createChooser(emailIntent, "Send email to support..."))
    }
}