package com.saldivar.pruebatectina.androidApp.detalleTarea

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.saldivar.pruebatecnica.helper.Animation
import com.saldivar.pruebatectina.androidApp.R
import com.saldivar.pruebatectina.androidApp.helper.searchAutomatic

class DetalleTareaActivity : AppCompatActivity() {
    private lateinit var comentario_new:EditText
    private lateinit var check:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_tarea)
        supportActionBar!!.hide()
        comentario_new= findViewById(R.id.comentario_new)
        check= findViewById(R.id.check)
        openFragment(ListComentariosFragment.newInstance())
        searchAutomatic(::repetitiveTask,::successTask)
    }

    override fun onBackPressed() {
        val fragment = ListComentariosFragment()
        fragment.backActivity("",this)
    }

    private fun successTask() {
        check.backgroundTintList = resources.getColorStateList(R.color.teal_700)
        searchAutomatic(::repetitiveTask,::successTask)
    }

    private fun repetitiveTask() {
        check.backgroundTintList = resources.getColorStateList(R.color.gris)
        Animation.comentarioEnProgreso = comentario_new.text.toString()
    }

    private fun openFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container_comentarios,fragment)
                .addToBackStack(null)
                .commit() }}
}
