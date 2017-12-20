package com.brown.moha.kotlinapp.activities

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

class UpdateProfileActivity : AppCompatActivity() {

    private val TAG = "myMainActivity"
    lateinit var usr: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)

        progressBar3.visibility = View.GONE

        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().uid

        // Create an ArrayAdapter using the string array and a default spinner layout
        val yearsAdapter = ArrayAdapter.createFromResource(this, R.array.years_array, android.R.layout.simple_spinner_item)
        yearsAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
        yearSpn1.adapter = yearsAdapter


        val majorsAdapter = ArrayAdapter.createFromResource(this, R.array.majors_array, android.R.layout.simple_spinner_item)
        majorsAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
        majorSpn1.adapter = majorsAdapter

        val langAdapter = ArrayAdapter.createFromResource(this, R.array.lang_array, android.R.layout.simple_spinner_item)
        langAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
        yearSpn1.adapter = langAdapter

        val placesAdapter = ArrayAdapter.createFromResource(this, R.array.places_array, android.R.layout.simple_spinner_item)
        placesAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
        placesAdapter.setNotifyOnChange(true)
        majorSpn1.adapter = placesAdapter


        val spinners = arrayOf(yearSpn1, majorSpn1, yearSpn1, majorSpn1)
        for (spn in spinners) {
            spn.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                    val item = parent?.getItemAtPosition(position).toString()
                    //toast("midosh2 " + item)
                    when (spn) {
                        yearSpn1 -> if (item !== "none") usr.year = item
                        majorSpn1 -> if (item !== "none") usr.major = item
                        yearSpn1 -> if (item !== "none") usr.lang = item
                        majorSpn1 -> if (item !== "none") usr.place = item
                    }
                }
            }
        }

        usr = User()

        submitBtn.setOnClickListener {
            progressBar3.visibility = View.VISIBLE
//            usr.name = nameEdt.text.toString()
//            usr.lastName = lastNameEdt.text.toString()
//            usr.phoneEmail = phoneFcbEdt.text.toString()

            val isAllFormSubmitted = usr.name != "" &&
                    usr.phoneEmail != "" &&
                    usr.year != "none" &&
                    usr.major != "none" &&
                    usr.place != "none" &&
                    usr.lang != "none"

            if (isAllFormSubmitted) {

                println("midosh3  " + usr.toString())
                // Add a new document with a generated ID
                db.collection("users")
                        .document(userId.toString())
                        .set(usr)
                        .addOnSuccessListener {
                            //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.id)
                            toast("Information Submitted")
                            val sharedPref = getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE)
                            val editor = sharedPref.edit()
                            editor.putBoolean("isFormSubmitted", true)
                            editor.apply()
                            val isFormSubmitted = sharedPref.getBoolean("isFormSubmitted", false)
                            println("mySharedPreference " + isFormSubmitted)

                            startActivity<ConnectWithActivity>()
                            // progressBar3.visibility=View.GONE

                        }
                        .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }


            } else {
                longToast("Please fill and choose all needed information")
                //progressBar3.visibility=View.GONE

            }


        }


    }
}
