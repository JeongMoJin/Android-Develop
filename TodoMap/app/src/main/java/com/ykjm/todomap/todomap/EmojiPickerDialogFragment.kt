package com.ykjm.todomap.todomap

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.ykjm.todomap.todomap.R

class EmojiPickerDialogFragment : DialogFragment() {

    interface OnEmojiSelectedListener {
        fun onEmojiSelected(emoji: String)
    }

    private var emojiSelectedListener: OnEmojiSelectedListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("이모티콘 선택")
                .setItems(R.array.emojis) { dialog, which ->
                    val emojis = resources.getStringArray(R.array.emojis)
                    val selectedEmoji = emojis[which]
                    emojiSelectedListener?.onEmojiSelected(selectedEmoji)
                    dialog.dismiss()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun setOnEmojiSelectedListener(listener: OnEmojiSelectedListener) {
        emojiSelectedListener = listener
    }
}
