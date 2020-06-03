package com.car.sharing.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class TextDialog(private val text: String) :
    DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder =
                AlertDialog.Builder(it)
            builder.setMessage(text)
                .setNeutralButton("Done") { _, _ ->

                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}