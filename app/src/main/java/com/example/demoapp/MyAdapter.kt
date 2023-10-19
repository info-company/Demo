package com.example.demoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val userlist:ArrayList<User>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>(){
    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val  Amount :TextView = itemView.findViewById(R.id.AMT)
        val  Date :TextView = itemView.findViewById(R.id.Date)
        val  Invok :TextView = itemView.findViewById(R.id.inv)
        val  QYT :TextView = itemView.findViewById(R.id.QYT)
        val  RetalerName :TextView = itemView.findViewById(R.id.RetalerName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
    return userlist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
    holder.Amount.text =userlist[position].Amount
    holder.Date.text =userlist[position].Date
    holder.Invok.text =userlist[position].QYT
    holder.QYT.text =userlist[position].QYT
    holder.RetalerName.text =userlist[position].Retalername


    }
}