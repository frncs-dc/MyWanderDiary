package com.example.mywanderdiary

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class DeleteDialogFragment : DialogFragment() {

    interface DeleteDialogListener {
        fun onDeleteConfirmed()
    }

    private lateinit var listener: DeleteDialogListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as DeleteDialogListener // Ensure the activity implements the interface
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction.
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Delete Journal Entry?")
                .setPositiveButton("Delete") { dialog, id ->
                    listener.onDeleteConfirmed()
                    dismiss()
                }
                .setNegativeButton("Cancel") { dialog, id ->
                    // User cancelled the dialog.
                    dismiss()
                }
            // Create the AlertDialog object and return it.
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}