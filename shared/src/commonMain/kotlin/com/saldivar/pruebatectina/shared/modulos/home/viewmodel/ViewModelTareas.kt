package com.saldivar.pruebatectina.shared.modulos.home.viewmodel

import com.badoo.reaktive.observable.*
import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.scheduler.mainScheduler
import com.saldivar.pruebatectina.shared.DatabaseDriverFactory
import com.saldivar.pruebatectina.shared.cache.ModelTareas
import com.saldivar.pruebatectina.shared.modulos.home.objeto.Tareas

open class ViewModelTareas(databaseDriverFactory: DatabaseDriverFactory){
    private val modelTareas = ModelTareas(databaseDriverFactory)

    open fun consultarListaTareas(estadoOjo:Boolean):MutableList<Tareas>{
        var tarea = mutableListOf<Tareas>()
        tarea =modelTareas.consultarListaTareas(estadoOjo)

        return tarea
    }

    open fun ordenMostrarTareas(listaObtenida:MutableList<Tareas>):MutableList<Tareas>{
        val lista = mutableListOf<Tareas>()
        val listaOrdenada =listaObtenida.sortedByDescending{ it.id }
        for (i in listaOrdenada){
            lista.add(i)}
        /*}
        observable<MutableList<Tareas>> {

        }.subscribeOn(ioScheduler)
            .observeOn(mainScheduler).threadLocal().subscribe()*/
        return lista
    }

    open fun consultaEstadoTarea(tareaId:Int):Boolean{
        val tarea = modelTareas.consultarEstadoTarea(tareaId)

        return tarea.estado!!
    }

    open fun updateEstadoTarea(tareas: Tareas, valorActualizar: Boolean){
        modelTareas.updateEstado(valorActualizar,tareas)

    }

    open fun eliminarTarea(idTarea:Int){
        modelTareas.eliminarTarea(idTarea)
    }

    open fun insertarTarea(titulo:String,contenido:String,fechaCreacion:String,textFinalizaSinYear:String){
        val objectVal0 = Tareas(
            0, titulo, contenido,fechaCreacion,
            textFinalizaSinYear,false)
        modelTareas.insertarNuevaTarea(objectVal0)


    }
    open fun consultarUltimaTarea(): Tareas {
        val tarea = modelTareas.consultarUltimaTareaInsertada()
        /*observable<Tareas> {

        }.subscribeOn(ioScheduler)
            .observeOn(mainScheduler).threadLocal().doOnBeforeNext {tarea = it}.subscribe()*/
        return tarea
    }

}