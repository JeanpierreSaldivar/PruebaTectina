package com.saldivar.pruebatectina.shared.cache

import com.saldivar.pruebatectina.shared.DatabaseDriverFactory
import com.saldivar.pruebatectina.shared.modulos.detalleTarea.Comentarios

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
    }
}