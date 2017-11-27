package com.brown.moha.kotlinapp.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.brown.moha.kotlinapp.ParseUrlIntentService
import com.brown.moha.kotlinapp.R
import com.brown.moha.kotlinapp.SignInActivityView
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.util.*


class SignInActivity : AppCompatActivity() , SignInActivityView {

    private val RC_SIGN_IN=2
    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var mAuth: FirebaseAuth
    private lateinit var  mAuthListener:FirebaseAuth.AuthStateListener

    override fun onStart() {
        super.onStart()
        settingServiceAlarm() //set the alarm that fires the service in first launch then start it every ~4h
        mAuth.addAuthStateListener(mAuthListener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        mAuth=FirebaseAuth.getInstance()

//        val htmlIntent = Intent(this, ParseUrlIntentService::class.java)
//        htmlIntent.data= Uri.parse("http://ulfds1.ul.edu.lb/")
//        startService(htmlIntent) //original

        onFirebaseAuthCheckCurrentUser()//open the corresponding activity based on current user

        setupGoogleSignRequestOnDevice()//related to google API

        googleSignInBtn.setOnClickListener {
            showLoadingBar() //show loading bar
            signIn()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                // Google Sign In was successful, authenticate with Firebase
                val account = result.signInAccount
                firebaseAuthWithGoogle(account)
                //startActivity<SubmitFormActivity>()

            } else {
                toast("Google Sign In failed,Please Try Again")
                hideLoadingBar()
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }


////////////////////////////////////////////////////////////////////////////////
    override fun onFirebaseAuthCheckCurrentUser() {

        val isFormSubmitted= getSubmitformStateInPrefs()//check if the form has been submitted or not
        mAuthListener=FirebaseAuth.AuthStateListener {
            firebaseAuth ->
            if(firebaseAuth.currentUser!=null) {
                //println("sharedTest2 " + isFormSubmitted)
                if(isFormSubmitted) startActivity<ConnectWithActivity>()
                else startActivity<SubmitFormActivity>()
                hideLoadingBar()
            }
            else toast("Please Sign IN To Continue")
        }
    }

    override fun signIn(){
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    override fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        //Log.d(FragmentActivity.TAG, "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        toast("firebase sign in success")
                    } else {
                        // If sign in fails, display a message to the user.
                        toast("Authentication Failed,Please Try Again")
                        hideLoadingBar()
                    }

                    // ...
                }
    }

    override fun settingServiceAlarm(){

        val intent = Intent(this, ParseUrlIntentService::class.java)
        val alarmIntent= PendingIntent.getService(this,0,intent,0)
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val interval=(Random().nextInt(3_600_000) + 14_400_000).toLong()//about~4h
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                interval,
                alarmIntent)
    }

    override fun hideLoadingBar(){
        progressBar2.visibility= View.GONE
    }

    override fun showLoadingBar(){
        progressBar2.visibility= View.VISIBLE
    }

    override fun getSubmitformStateInPrefs(): Boolean {
        val sharedPref = getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE)
        val isFormSubmitted = sharedPref.getBoolean("isFormSubmitted",false)
        //println("sharedTest1 " + isFormSubmitted)
        return isFormSubmitted

    }

    override fun setupGoogleSignRequestOnDevice(){
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,{ toast("Something Went Wrong,Please Try Again")  })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }
}

