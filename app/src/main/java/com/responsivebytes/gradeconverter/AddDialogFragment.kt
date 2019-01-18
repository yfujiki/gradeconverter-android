package com.responsivebytes.gradeconverter

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import com.responsivebytes.gradeconverter.Adapters.AddRecyclerViewAdapter
import com.responsivebytes.gradeconverter.Models.LocalPreferences
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.activity_add.view.*
import javax.inject.Inject

class AddDialogFragment : DialogFragment() {
    @Inject
    lateinit var localPreferences: LocalPreferences

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)

        val dialogView = LayoutInflater.from(activity!!)
                .inflate(R.layout.activity_add, null, false)

        dialogView.recyclerView.layoutManager = LinearLayoutManager(activity!!)
        dialogView.recyclerView.adapter = AddRecyclerViewAdapter(localPreferences)
        dialogView.addCloseButton.setOnClickListener {
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