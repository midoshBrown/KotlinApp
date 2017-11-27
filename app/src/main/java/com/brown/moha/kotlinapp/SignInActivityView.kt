package com.brown.moha.kotlinapp

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

/**
 *
 */
interface SignInActivityView {

    fun onFirebaseAuthCheckCurrentUser()
    fun signIn()
    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?)
    fun settingServiceAlarm()
    fun hideLoadingBar()
    fun showLoadingBar()
    fun getSubmitformStateInPrefs(): Boolean
    fun setupGoogleSignRequestOnDevice()
}