package com.brown.moha.kotlinapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_details_users.*

class DetailsUsersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_users)


        val intnt= intent
        val s1=intnt.extras.get("name")
        val s2=intnt.extras.get("phoneEmail")
        tx1.text=s1.toString()
        tx2.text=s2.toString()


    }
}
