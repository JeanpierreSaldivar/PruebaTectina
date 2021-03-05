package com.saldivar.pruebatectina.androidApp.home.util

import com.saldivar.pruebatectina.shared.modulos.home.objeto.Tareas

interface RecyclerTareasListener {
    fun onClick(tarea: Tareas, position :Int)
    fun change(tarea: Tareas, position: Int)
}