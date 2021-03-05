package com.saldivar.pruebatectina.shared.modulos.detalleTarea.viewmodel

import com.badoo.reaktive.observable.*
import com.badoo.reaktive.scheduler.computationScheduler
import com.badoo.reaktive.scheduler.ioScheduler
import com.saldivar.pruebatectina.shared.DatabaseDriverFactory
import com.saldivar.pruebatectina.shared.cache.ModelComentarios
import com.saldivar.pruebatectina.shared.modulos.detalleTarea.objeto.Comentarios
import com.saldivar.pruebatectina.shared.modulos.home.objeto.Tareas

open class ViewModelComentarios(databaseDriverFactory: DatabaseDriverFactory) {
    private val modelComentarios = ModelComentarios(databaseDriverFactory)

    open fun getAllComentarios(idTarea: Int): MutableList<Comentarios> {
        val lista = mutableListOf<Comentarios>()
        modelComentarios.getAllComentarios(idTarea).asObservable().map{
            lista.add(it)
        }.observeOn(
            ioScheduler).subscribe()
        val listaOrdenada = mutableListOf<Comentarios>()
        ordenarListaMostrar(lista).asObservable().map{
            listaOrdenada .add(it)
        }.observeOn(computationScheduler).subscribe()
        return listaOrdenada
    }

    open fun enviarNuevoComentario(comentario: String,idTarea:Int){
        val objectComentario = Comentarios(0, idTarea,
            "anonimus",comentario)
        modelComentarios.insertarComentarioDB(objectComentario).toObservable().observeOn(ioScheduler).subscribe()
    }

    open fun eliminarComentarios(idTarea: Int){
        modelComentarios.eliminarComentarios(idTarea).toObservable().observeOn(ioScheduler).subscribe()
    }

    open fun updateTarea(titulo:String,descripcion:String,finalizacion:String,idTarea:Int){
        modelComentarios.updateTarea(titulo, descripcion, finalizacion, idTarea).toObservable().
                observeOn(ioScheduler).subscribe()
    }

    open fun consultarDatosNuevosTarea(idTarea:Int): Tareas{
        var tarea = Tareas()
        modelComentarios.consultarDatosNuevosTarea(idTarea).toObservable().map{
            tarea = it
        }.observeOn(ioScheduler).subscribe()
        return tarea
    }

    private fun ordenarListaMostrar(listaObtenida:List<Comentarios>):MutableList<Comentarios>{
        val lista = mutableListOf<Comentarios>()
        val listaOrdenada =listaObtenida.sortedByDescending { it.idComentario }
        for (i in listaOrdenada){
            lista.add(i)
        }
        return lista
    }

    open fun obtenerUltimoComentario():Comentarios{
        var cometarioDevolver = Comentarios()
        modelComentarios.consultarUltimoComentario().toObservable().map{
            cometarioDevolver = it
        }.observeOn(ioScheduler).subscribe()
        return cometarioDevolver
    }
}