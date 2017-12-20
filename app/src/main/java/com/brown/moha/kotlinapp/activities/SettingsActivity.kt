package com.brown.moha.kotlinapp.activities

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.brown.moha.kotlinapp.R
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        //putting this logic in OnDestroy is better
        notifySwitch.setOnCheckedChangeListener { _, isChecked ->
            val sharedPref = getSharedPreferences(getString(R.string.my_Prefs_File), Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            if (isChecked) {
                editor.putBoolean("notifyUser", true)
                editor.apply()
            } else {
                editor.putBoolean("notifyUser", false)
                editor.apply()
            }
        }
    }
}
