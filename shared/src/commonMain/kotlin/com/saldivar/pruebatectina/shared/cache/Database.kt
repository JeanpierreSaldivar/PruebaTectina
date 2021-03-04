package com.saldivar.pruebatectina.shared.cache

import com.saldivar.pruebatectina.shared.DatabaseDriverFactory

internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    fun consultarListaTareas(estadoTarea:Boolean){
        dbQuery.getAllTareas(estadoTarea)
    }

    fun consultarEstadoTarea(idTarea: Int){
        dbQuery
    }
}