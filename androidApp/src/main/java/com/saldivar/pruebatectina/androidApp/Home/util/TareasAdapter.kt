package com.saldivar.pruebatectina.androidApp.Home.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.saldivar.pruebatectina.androidApp.R
import com.saldivar.pruebatectina.shared.modulos.home.objeto.Tareas

class TareasAdapter(private val context: Context, private val listener: RecyclerTareasListener)
    : RecyclerView.Adapter<TareasAdapter.MainViewHolder>() {
    var flight = mutableListOf<Tareas>()
    fun setDataList(data: MutableList<Tareas>) {
        flight = data
    }

    fun addItem(position: Int, tarea: Tareas) {
        flight.add(position, tarea)
        notifyItemInserted(position)
    }

    fun removeItem(position: Int) {
        flight.removeAt(position)
        notifyItemRemoved(position)
    }

    fun eliminarListaAnterior() {
        flight.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):TareasAdapter.MainViewHolder{
        val view:View = LayoutInflater.from(context).inflate(R.layout.item_recyler_tareas,parent,false)
        return MainViewHolder(view)
    }


    override fun onBindViewHolder(holder: MainViewHolder, position: Int) = holder.bin(
        flight[position],
        listener
    )

    override fun getItemCount() = flight.size

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bin(tarea: Tareas, listener: RecyclerTareasListener) = with(itemView) {
            val ordenNumero = findViewById<TextView>(R.id.ordenNumero)
            val titulo = findViewById<TextView>(R.id.titulo)
            val descripcion = findViewById<TextView>(R.id.descripcion)
            val creacion = findViewById<TextView>(R.id.creacion)
            val finalizacion = findViewById<TextView>(R.id.finalizacion)
            val estado = findViewById<CheckBox>(R.id.estado)
            val item_tarea = findViewById<CardView>(R.id.item_tarea)
            if (tarea.id!! < 10) {
                ordenNumero.text = "${"0" + tarea.id.toString()}"
            } else {
                ordenNumero.text = "${tarea.id}"
            }

            titulo.text = tarea.titulo
            descripcion.text = tarea.descripcion
            creacion.text = "Creación: ${tarea.creacion}"
            finalizacion.text = "Finalizado: ${tarea.finalizacion}"
            if (tarea.estado!!) {
                estado.isChecked = true
            }
            item_tarea.setOnClickListener { listener.onClick(tarea, adapterPosition) }
            estado.setOnCheckedChangeListener { _, _ ->
                listener.change(tarea, adapterPosition)
            }
        }
    }
}