package com.saldivar.pruebatectina.shared.cache

import com.saldivar.pruebatectina.shared.DatabaseDriverFactory
import com.saldivar.pruebatectina.shared.modulos.detalleTarea.objeto.Comentarios
import com.saldivar.pruebatectina.shared.modulos.home.objeto.Tareas

internal class ModelComentarios(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    internal fun getAllComentarios(idTareaBuscar:Int):MutableList<Comentarios>{
        val comentariosDevolver = mutableListOf<Comentarios>()
        val comentarios =dbQuery.getAllComentarios(idTareaBuscar).executeAsList()
        for (item in comentarios ){
            item.apply {
                val comentario = Comentarios(idComentario,idTarea, user, comentario)
                comentariosDevolver.add(comentario)
            }
        }
        return comentariosDevolver
    }

    internal fun  insertarComentarioDB(comentarioNuevo: Comentarios){
        dbQuery.insertComentario(comentarioNuevo.idTarea!!,comentarioNuevo.user!!,comentarioNuevo.comentario!!)
    }

    internal fun updateTarea(titulo:String,descripcion:String,finalizacion:String,idTarea:Int){
        dbQuery.actualizarTarea(titulo,descripcion,finalizacion,idTarea)
    }

    internal fun consultarDatosNuevosTarea(idTarea:Int): Tareas {
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

    internal fun eliminarComentarios(idTarea: Int){
        dbQuery.eliminarComentarios(idTarea)
    }

    internal fun consultarUltimoComentario(): Comentarios {
        val comentarioQuery = dbQuery.obtenerUltimoComentario().executeAsOne()
        val objectComentario = Comentarios()
        objectComentario.apply {
            idComentario = comentarioQuery.idComentario
            idTarea = comentarioQuery.idTarea
            user = comentarioQuery.user
            comentario = comentarioQuery.comentario
        }
        return objectComentario
    }
}