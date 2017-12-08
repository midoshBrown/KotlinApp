package com.brown.moha.kotlinapp.activities

import android.arch.lifecycle.ViewModel
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.brown.moha.kotlinapp.R
import com.brown.moha.kotlinapp.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_submit_form.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class SubmitFormActivity : AppCompatActivity() {

    private val TAG = "myMainActivity"
    lateinit var usr: User
    lateinit var viewModel: ViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_form)

        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().uid

        hideLoadingBar()
        //set each spinner adapter with the adapter that handle the specific array for each one
        setupAdaptersForSpinners()
        usr = User()
        val spinners = arrayOf(yearSpn1, majorSpn1, yearSpn1, majorSpn1)
        for (spn in spinners) {
            spn.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val item = parent?.getItemAtPosition(position).toString()
                    when (spn) {
                        yearSpn1 -> usr.year = item
                        majorSpn1 -> usr.major = item
                        yearSpn1 -> usr.lang = item
                        majorSpn1 -> usr.place = item
                    }
                }
            }
        }

        submitBtn.setOnClickListener {

            showLoadingBar()
            usr.phoneNumber = phoneNumberEdt.text.toString().trim()
            val isAllFieldsSubmitted = usr.phoneNumber != "" &&
                    usr.year != "none" &&
                    usr.major != "none" &&
                    usr.place != "none" &&
                    usr.lang != "none"

            if (isAllFieldsSubmitted) {
                submitBtn.isEnabled = false
                println("midosh3  " + usr.toString())
                // Add a new document with a generated ID
                db.collection("users")
                        .document(userId.toString())
                        .set(usr)
                        .addOnSuccessListener {
                            //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.id)
                            toast("Information Submitted")
                            saveFormStateInPrefs()
                            startActivity<ConnectWithActivity>()
                            // progressBar3.visibility=View.GONE
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                            submitBtn.isEnabled = true
                        }
                        .addOnCompleteListener {
                            hideLoadingBar()
                            println("myProgressBar3 ")

                        }


            } else {
                longToast("Please fill and choose all needed information")
                hideLoadingBar()

            }

            //startActivity<ConnectWithActivity>()//temp: remove later

        }

    }

    private fun saveFormStateInPrefs() {
        val sharedPref = getSharedPreferences(getString(R.string.my_Prefs_File), Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("isFormSubmitted", true)
        editor.apply()
    }

    private fun setupAdaptersForSpinners() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        val yearsAdapter = ArrayAdapter.createFromResource(this, R.array.years_array,
                android.R.layout.preference_category)
        yearsAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
        yearSpn1.adapter = yearsAdapter
        //yearSpn1.setSelection(0)

        val majorsAdapter = ArrayAdapter.createFromResource(this, R.array.majors_array,
                android.R.layout.preference_category)
        majorsAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
        majorSpn1.adapter = majorsAdapter

        val langAdapter = ArrayAdapter.createFromResource(this, R.array.lang_array,
                android.R.layout.preference_category)
        langAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
        langSpn1.adapter = langAdapter

        val placesAdapter = ArrayAdapter.createFromResource(this, R.array.places_array,
                android.R.layout.preference_category)
        placesAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
        placeSpn1.adapter = placesAdapter
    }


    private fun hideLoadingBar() {
        progressBar3.visibility = View.GONE
    }

    private fun showLoadingBar() {
        progressBar3.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        moveTaskToBack(true)
    }
}


