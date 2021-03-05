package com.saldivar.pruebatectina.shared.modulos.detalleTarea.objeto

data class Comentarios(var idComentario:Int ?= null, var idTarea:Int ?= null, var user:String ?= null,
                       var comentario:String ?= null)