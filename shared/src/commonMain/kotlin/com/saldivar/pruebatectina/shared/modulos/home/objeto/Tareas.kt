package com.saldivar.pruebatectina.shared.modulos.home.objeto

data class Tareas(var id:Int ?= null, var titulo:String ?= null, var descripcion:String ?= null,
                  var creacion:String ?= null, var finalizacion:String ?= null, var estado:Boolean ?= null )