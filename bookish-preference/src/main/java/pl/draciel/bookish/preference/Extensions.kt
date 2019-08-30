package pl.draciel.bookish.preference

import android.annotation.SuppressLint
import android.content.SharedPreferences

@SuppressLint("ApplySharedPref")
internal fun SharedPreferences.edit(action: (SharedPreferences.Editor) -> Unit) {
    val editor = edit()
    action(editor)
    editor.commit()
}
