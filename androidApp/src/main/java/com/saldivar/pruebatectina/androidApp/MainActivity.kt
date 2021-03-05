package com.saldivar.pruebatectina.androidApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.saldivar.pruebatectina.shared.Greeting
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.saldivar.pruebatectina.androidApp.home.view.ListTareasFragment

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_main)
        /*val tv: TextView = findViewById(R.id.textView)
        tv.text = greet()*/
        openFragment(ListTareasFragment.newInstance())
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
