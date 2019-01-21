package com.responsivebytes.gradeconverter

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import com.responsivebytes.gradeconverter.Utilities.RatingUtility
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

        dialogView.rateTextView.setOnClickListener {
            RatingUtility.rateApp()
            dismiss()
        }

        builder.setView(dialogView)

        return builder.create()
    }

    override fun onActivityCreated(arg0: Bundle?) {
        super.onActivityCreated(arg0)
        dialog.window!!
                .attributes.windowAnimations = R.style.DialogAnimation
    }
}