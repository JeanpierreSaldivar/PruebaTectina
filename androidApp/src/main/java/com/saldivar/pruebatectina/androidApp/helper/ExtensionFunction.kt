package com.saldivar.pruebatectina.androidApp.helper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.saldivar.pruebatecnica.helper.Animation
import com.saldivar.pruebatecnica.helper.MyAplicationClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun ViewGroup.inflate(layoutId: Int)= LayoutInflater.from(context).inflate(layoutId, this, false)!!


fun toastMessage(mensaje:String){
    Toast.makeText(MyAplicationClass.ctx,mensaje,Toast.LENGTH_LONG).show()
}
fun fechaActual():String {
    val date: Date = Calendar.getInstance().time
    val formatter: DateFormat = SimpleDateFormat("dd/MM/yyyy")
    val today: String = formatter.format(date)
    val partes :List<String> = today.split("/")
    return "${partes[0]}/${partes[1]}"
}

fun searchAutomatic(repetitiveTask:()->Unit, successTask:()->Unit) {
    CoroutineScope(Dispatchers.IO).async {
        while (Animation.comentarioEnProgreso.isEmpty() || Animation.comentarioEnProgreso.isBlank()) {
            delay(1500)
             repetitiveTask()
        }
        Animation.comentarioEnProgreso =""
        successTask()
    }
}

fun hideSoftKeyBoard(context: Context, view: View) {
    try {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    } catch (e: Exception) {
        // TODO: handle exception
        e.printStackTrace()
    }

}

