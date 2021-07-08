package com.citrus.ktordemo.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.citrus.ktordemo.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LeaveDialog : DialogFragment() {
    private var onPositiveButtonClickListener: (() -> Unit)? = null

    fun setPositiveButtonClickListener(listener: () -> Unit) {
        onPositiveButtonClickListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.dialog_leave_title)
            .setMessage(R.string.dialog_leave_message)
            .setPositiveButton(R.string.dialog_yes){ _, _ ->
                onPositiveButtonClickListener?.let { yes ->
                    yes()
                }
            }
            .setNegativeButton(R.string.dialog_leave_no){ dialogInterface, _ ->
                dialogInterface?.cancel()
            }
            .create()
    }
}