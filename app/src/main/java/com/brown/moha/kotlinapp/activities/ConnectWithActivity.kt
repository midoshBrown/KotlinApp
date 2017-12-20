package com.brown.moha.kotlinapp.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.brown.moha.kotlinapp.R
import com.brown.moha.kotlinapp.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_connect_with.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class ConnectWithActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when {
            item.itemId == R.id.update_profile -> {
                startActivity<UpdateProfileActivity>()
                return true
            }
            item.itemId == R.id.settings -> {
                startActivity<SettingsActivity>()
                return true
            }
            item.itemId == R.id.log_out -> {
                FirebaseAuth.getInstance().signOut()
                toast("You Sign out")
                startActivity<SignInActivity>()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect_with)

        setupAdaptersForSpinners()

        val usr = User()
        val spinners = arrayOf(yearSpn1, majorSpn1, langSpn1, placeSpn1)
        for (spn in spinners) {
            spn.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val item = parent?.getItemAtPosition(position).toString()
//                    toast("midosh2 " + item)
                    when (spn) {
                        yearSpn1 -> usr.year = item
                        placeSpn1 -> usr.place = item
                        langSpn1 -> usr.lang = item
                        majorSpn1 -> usr.major = item
                    }
                }
            }
        }

        findStudentsBtn.setOnClickListener {

            val isAllFieldNotSubmitted = usr.year == "none" &&
                    usr.major == "none" &&
                    usr.lang == "none" &&
                    usr.place == "none"

            if (isAllFieldNotSubmitted)
                toast("plz choose some values")
            else startActivity<ListOfTargetedUsersActivity>("year".to(usr.year),
                    "major".to(usr.major),
                    "lang".to(usr.lang),
                    "place".to(usr.place))
        }

    }

    private fun setupAdaptersForSpinners() {
        val yearsAdapter = ArrayAdapter.createFromResource(this,
                R.array.years_array,
                android.R.layout.preference_category)
        yearsAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
        yearSpn1.adapter = yearsAdapter


        val majorsAdapter = ArrayAdapter.createFromResource(this,
                R.array.majors_array,
                android.R.layout.preference_category)
        majorsAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
        majorSpn1.adapter = majorsAdapter

        val langAdapter = ArrayAdapter.createFromResource(this,
                R.array.lang_array,
                android.R.layout.preference_category)
        langAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
        langSpn1.adapter = langAdapter

        val placesAdapter = ArrayAdapter.createFromResource(this,
                R.array.places_array,
                android.R.layout.preference_category)
        placesAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
        placeSpn1.adapter = placesAdapter
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        moveTaskToBack(true)
    }
}
