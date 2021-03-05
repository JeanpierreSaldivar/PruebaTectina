package com.saldivar.pruebatectina.androidApp.detalleTarea.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.saldivar.pruebatectina.androidApp.R
import com.saldivar.pruebatectina.shared.modulos.detalleTarea.objeto.Comentarios

class ComentariosAdapter(private val context: Context)
    : RecyclerView.Adapter<ComentariosAdapter.MainViewHolder>()  {
    private var flight = mutableListOf<Comentarios>()
    fun setDataList(data: MutableList<Comentarios>) {
        flight = data
    }

    fun addItem(position: Int, tarea: Comentarios) {
        flight.add(position, tarea)
        notifyItemInserted(position)
    }

    fun removeItem(position: Int) {
        flight.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComentariosAdapter.MainViewHolder{
        val view:View = LayoutInflater.from(context).inflate(R.layout.item_recycler_comentarios,parent,false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int)= holder.bin(flight[position])

    override fun getItemCount()= flight.size

    class MainViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        fun bin(comentario: Comentarios)= with(itemView){
            val comentarioUser = findViewById<TextView>(R.id.comentario_user)
            comentarioUser.text =comentario.comentario
        }
    }
}