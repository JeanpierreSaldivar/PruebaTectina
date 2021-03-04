package com.saldivar.pruebatectina.shared.cache

import Tareas
import com.saldivar.pruebatectina.shared.DatabaseDriverFactory

internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    fun consultarListaTareas(estadoTarea:Boolean):List<Tareas>{
        val tareaDevolver = mutableListOf<Tareas>()
        val tareas =dbQuery.getAllTareas(estadoTarea).executeAsList()
        for (item in tareas){
            item.apply {
                val tarea = Tareas(id,titulo,descripcion, creacion, finalizacion, estado)
                tareaDevolver.add(tarea)
            }
        }
        return tareaDevolver

    }

    fun consultarEstadoTarea(idTarea: Int):Tareas{
        val tarea = dbQuery.consultarTarea(idTarea).executeAsOne()
        val objectTarea = Tareas()
        objectTarea.apply {
            id = tarea.id
            titulo = tarea.titulo
            descripcion = tarea.descripcion
            creacion = tarea.creacion
            finalizacion = tarea.finalizacion
            estado = tarea.estado
        }

        return objectTarea
    }

    fun updateEstado(valorActualizar:Boolean,tarea:Tareas){
        dbQuery.updateEstado(valorActualizar,tarea.id!!)
    }

     fun insertarNuevaTarea(nuevaTarea:Tareas){
         dbQuery.insertTarea(nuevaTarea.id,nuevaTarea.titulo!!,nuevaTarea.descripcion!!,
         nuevaTarea.creacion!!,nuevaTarea.finalizacion!!,nuevaTarea.estado!!)
     }

    fun consultarUltimaTareaInsertada():Tareas{
        val tarea = dbQuery.consultarUltimaTarea().executeAsOne()
        val objectTarea = Tareas()
        objectTarea.apply {
            id = tarea.id
            titulo = tarea.titulo
            descripcion = tarea.descripcion
            creacion = tarea.creacion
            finalizacion = tarea.finalizacion
            estado = tarea.estado
        }
        return objectTarea
    }
}