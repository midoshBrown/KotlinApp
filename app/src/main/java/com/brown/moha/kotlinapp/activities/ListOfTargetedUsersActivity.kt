package com.brown.moha.kotlinapp.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.brown.moha.kotlinapp.CustomAdapter
import com.brown.moha.kotlinapp.OnItemClickListener
import com.brown.moha.kotlinapp.R
import com.brown.moha.kotlinapp.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_target_users.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast



class ListOfTargetedUsersActivity : AppCompatActivity(), OnItemClickListener {


    val TAG="myTargetedUsersActivity"
    lateinit var query:Query

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_target_users)

        progressBar1.visibility=View.VISIBLE
        targetUsersRv.layoutManager= LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        //crating an arraylist to store users using the data class user
        val users = ArrayList<User>()

        val intnt= intent
        val year=intnt.extras.get("year").toString()
        val major=intnt.extras.get("major").toString()
        val lang=intnt.extras.get("lang").toString()
        val place=intnt.extras.get("place").toString()
//        val usr1=User(year=year,major = major,lang = lang,place = place)
        val fieldsMap= mapOf("y" to year,"m" to major,"l" to lang,"p" to place )
        val myMap =fieldsMap.filter { it.value!="none" }.map { it  }
        println(" myMap $myMap ${myMap.size}")



        val db  = FirebaseFirestore.getInstance()
        query = db.collection("users")

        myMap.forEach { (k,v)-> when(k){
            "y"->query = query.whereEqualTo("year",v)
            "m"->query = query.whereEqualTo("major",v)
            "l"->query = query.whereEqualTo("lang",v)
            "p"->query = query.whereEqualTo("place",v)
          }
        }

     query.get()
          .addOnCompleteListener { task ->
              if (task.isComplete){
                 // println("myResult  ${task.result.isEmpty}")
                  if(task.result.isEmpty) {
                      toast("No Results Found")
                      progressBar1.visibility= View.GONE
                  }

                  if (task.isSuccessful) {
                      for (document in task.result) {
                          Log.d(TAG, document.id + " => " + document.data)
                          val usr= User(name = document.data["name"].toString(), phoneEmail = document.data["phoneEmail"].toString())
                          users.add(usr)
                          //creating our adapter
                          val adapter = CustomAdapter(users, this)

                          //now adding the adapter to recyclerview
                          targetUsersRv.adapter = adapter
                          progressBar1.visibility= View.GONE


                      }

                    }
                   else {
                      Log.w(TAG, "Error getting documents.", task.exception)
                      //progressBar1.visibility= View.GONE
                      toast("error getting documents")
                      println("myResult  ${task.result.isEmpty}")

                    }

              }

   }




    }

    override fun onItemClicked(s1: String, s2: String) {

        toast("hey you callback thank you $s1  $s2")
        startActivity<DetailsUsersActivity>("name" to s1,"phoneEmail" to s2)
    }

}
