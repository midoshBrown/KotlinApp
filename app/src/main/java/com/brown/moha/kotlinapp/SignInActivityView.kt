package com.brown.moha.kotlinapp

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

/**
 *
 */
interface SignInActivityView {

    fun updateActivityForCurrentUser()
    fun signIn()
    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?)
    fun setServiceAlarm()
    fun hideLoadingBar()
    fun showLoadingBar()
    fun getSubmitformStateInPrefs(): Boolean
    fun setupGoogleSignRequestOnDevice()
}