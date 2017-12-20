package com.brown.moha.kotlinapp

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_layout.view.*

/**
 *
 */
class CustomAdapter(val userList: ArrayList<User>,
                    val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {


    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_layout, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: CustomAdapter.ViewHolder, position: Int) {
        holder.bindItems(userList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }


    //the class is hodling the list view
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {


        init {
            itemView.setOnClickListener(this)
        }


        override fun onClick(itemView: View?) {
            //Log.d("RecyclerView", "CLICK!")
            itemClickListener.onItemClicked(itemView?.userNameTv?.text.toString(),
                    itemView?.userPhoneEmailTv?.text.toString(),itemView?.userImageView?.tag.toString())


        }


        fun bindItems(user: User) {

            itemView.userNameTv.text = user.name
            itemView.userPhoneEmailTv.text = user.phoneEmail
            itemView.userImageView.tag=user.photoUrl

            Picasso.with(itemView.userImageView.context)
                    .load(Uri.parse(user.photoUrl))
                    .fit()
                    .into(itemView.userImageView)

        }


    }


}
