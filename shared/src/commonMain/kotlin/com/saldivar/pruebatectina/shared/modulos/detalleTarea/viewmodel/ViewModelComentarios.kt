package com.saldivar.pruebatectina.shared.modulos.detalleTarea.viewmodel

import com.saldivar.pruebatectina.shared.DatabaseDriverFactory
import com.saldivar.pruebatectina.shared.cache.ModelComentarios
import com.saldivar.pruebatectina.shared.modulos.detalleTarea.objeto.Comentarios
import com.saldivar.pruebatectina.shared.modulos.home.objeto.Tareas

open class ViewModelComentarios(databaseDriverFactory: DatabaseDriverFactory) {
    private val modelComentarios = ModelComentarios(databaseDriverFactory)

    open fun getAllComentarios(idTarea: Int): MutableList<Comentarios> {
        return ordenarListaMostrar(modelComentarios.getAllComentarios(idTarea))
    }

    open fun enviarNuevoComentario(comentario: String,idTarea:Int){
        if(comentario.isNotEmpty() && comentario.isNotBlank()){
            val objectComentario = Comentarios(0, idTarea,
                "anonimus",comentario)
            modelComentarios.insertarComentarioDB(objectComentario)
        }
    }

    open fun eliminarComentarios(idTarea: Int){
        modelComentarios.eliminarComentarios(idTarea)
    }

    open fun updateTarea(titulo:String,descripcion:String,finalizacion:String,idTarea:Int){
        modelComentarios.updateTarea(titulo, descripcion, finalizacion, idTarea)
    }

    open fun consultarDatosNuevosTarea(idTarea:Int): Tareas{
        return modelComentarios.consultarDatosNuevosTarea(idTarea)
    }
    open fun ordenarListaMostrar(listaObtenida:List<Comentarios>):MutableList<Comentarios>{
        val lista = mutableListOf<Comentarios>()
        val listaOrdenada =listaObtenida.sortedByDescending { it.idComentario }
        for (i in listaOrdenada){
            lista.add(i)
        }
        return lista
    }

}