package com.brown.moha.kotlinapp

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

/**
 *
 */
interface SignInView {

    fun onFirebaseAuthCheckCurrentUser()
    fun signIn()
    fun onFirebaseAuthWithGoogle(acct: GoogleSignInAccount?)
    fun startServiceWithSettingAlarm()
    fun hideLoadingBar()
    fun showLoadingBar()
    fun getSubmitformState(): Boolean
    fun setupGoogleSignRequestOnDevice()
}