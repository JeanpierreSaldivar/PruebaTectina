package com.saldivar.pruebatectina.shared.modulos.home.viewmodel

import com.badoo.reaktive.observable.*
import com.badoo.reaktive.scheduler.*
import com.saldivar.pruebatectina.shared.DatabaseDriverFactory
import com.saldivar.pruebatectina.shared.cache.ModelTareas
import com.saldivar.pruebatectina.shared.modulos.home.objeto.Tareas
import kotlin.native.concurrent.ThreadLocal

open class ViewModelTareas(databaseDriverFactory: DatabaseDriverFactory){
    private val modelTareas = ModelTareas(databaseDriverFactory)

    open fun consultarListaTareas(estadoOjo:Boolean):MutableList<Tareas>{
        val lista = mutableListOf<Tareas>()
        modelTareas.consultarListaTareas(estadoOjo).asObservable().map{
            lista.add(it)
        }.observeOn(
            ioScheduler).subscribe()
        return lista
    }

    open fun ordenMostrarTareas(listaObtenida:MutableList<Tareas>):MutableList<Tareas>{
        val lista = mutableListOf<Tareas>()
        val listaOrdenada =listaObtenida.sortedByDescending{ it.id }
        listaOrdenada.asObservable().map{
            lista.add(it)
        }.observeOn(computationScheduler)
            .subscribe()
        return lista
    }

    open fun consultaEstadoTarea(tareaId:Int):Boolean{
        var tarea = Tareas()
        modelTareas.consultarEstadoTarea(tareaId).toObservable().map {
            tarea = it
        }.observeOn(ioScheduler).subscribe()
        return tarea.estado!!
    }

    open fun updateEstadoTarea(tareas: Tareas, valorActualizar: Boolean){
        modelTareas.updateEstado(valorActualizar,tareas).toObservable().observeOn(ioScheduler).subscribe()
    }

    open fun eliminarTarea(idTarea:Int){
        modelTareas.eliminarTarea(idTarea).toObservable().observeOn(ioScheduler).subscribe()
    }

    open fun insertarTarea(titulo:String,contenido:String,fechaCreacion:String,textFinalizaSinYear:String){
        val objectVal0 = Tareas(
            0, titulo, contenido,fechaCreacion,
            textFinalizaSinYear,false)
        modelTareas.insertarNuevaTarea(objectVal0).toObservable().observeOn(ioScheduler).subscribe()
    }

    open fun consultarUltimaTarea(): Tareas {
        var tarea = Tareas()
         modelTareas.consultarUltimaTareaInsertada().toObservable().map{
            tarea = it
        }.observeOn(ioScheduler).subscribe()
        return tarea
    }

}