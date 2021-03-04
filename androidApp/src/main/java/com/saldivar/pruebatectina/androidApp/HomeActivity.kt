package com.saldivar.pruebatectina.androidApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.saldivar.pruebatectina.shared.Greeting
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.saldivar.pruebatectina.androidApp.Home.ListTareasFragment
import com.saldivar.pruebatectina.androidApp.R
import com.saldivar.pruebatectina.shared.DatabaseDriverFactory
import com.saldivar.pruebatectina.shared.modulos.home.viewmodel.ViewModelTareas

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_main)
        val tv: TextView = findViewById(R.id.textView)
        tv.text = greet()
        /*val driver = DatabaseDriverFactory(applicationContext)
        val viewModel = ViewModelTareas(driver)*/
        openFragment(ListTareasFragment.newInstance())
        /*val list = mutableListOf<Tareas>()
        val object1 = Tareas(0,"a","b","c","d",false)
        list.add(0,object1)
        val object2 = Tareas(1,"1","2","3","4",true)
        list.add(1,object2)
        viewModel.insertarTarea(object1.titulo!!,object1.descripcion!!,object1.creacion!!,
        object1.finalizacion!!)
        val lisrOb = viewModel.consultarUltimaTarea()
        Log.d("SIEMPRE",lisrOb.creacion.toString())*/
    }


    private fun openFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container_tareas,fragment)
            addToBackStack(null)
            commit() }}


    override fun onBackPressed() {
        finish()
    }
}
