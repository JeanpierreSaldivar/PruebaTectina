package com.saldivar.pruebatectina.androidApp.home.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.saldivar.pruebatecnica.helper.MyAplicationClass
import com.saldivar.pruebatectina.androidApp.detalleTarea.DetalleTareaActivity
import com.saldivar.pruebatectina.androidApp.R
import com.saldivar.pruebatectina.androidApp.helper.fechaActual
import com.saldivar.pruebatectina.androidApp.helper.hideSoftKeyBoard
import com.saldivar.pruebatectina.androidApp.helper.toastMessage
import com.saldivar.pruebatectina.androidApp.home.util.DatosListTareasFragment
import com.saldivar.pruebatectina.androidApp.home.util.RecyclerTareasListener
import com.saldivar.pruebatectina.androidApp.home.util.TareasAdapter
import com.saldivar.pruebatectina.shared.DatabaseDriverFactory
import com.saldivar.pruebatectina.shared.modulos.home.objeto.Tareas
import com.saldivar.pruebatectina.shared.modulos.home.viewmodel.ViewModelTareas
import java.util.*

class ListTareasFragment : Fragment(),View.OnClickListener {
    private lateinit var recycler: RecyclerView
    private lateinit var visualizadorOjo: ImageView
    private  lateinit var floatingBtn: FloatingActionButton
    private lateinit var adapter: TareasAdapter
    private var bundle: Bundle ?=null
    private var listTareas = mutableListOf<Tareas>()
    private val driver = DatabaseDriverFactory(MyAplicationClass.ctx!!)
    private val viewModel = ViewModelTareas(driver)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootview = inflater.inflate(R.layout.fragment_list_tareas, container, false)
        visualizadorOjo = activity!!.findViewById(R.id.visualizador)
        floatingBtn= rootview.findViewById(R.id.flotanButton)
        recycler = rootview.findViewById(R.id.recycler_tareas)
        floatingBtn.setOnClickListener(this)
        visualizadorOjo.setOnClickListener(this)
        bundle = activity!!.intent.extras
        setearEstadoOjo(DatosListTareasFragment.estadoOjo)
        inicio()
        return rootview
    }

    private fun inicio() {
        revisarEstadoOjo()
        consultar()
        eliminarTarea()
    }

    private fun eliminarTarea() {
        if(bundle?.getString("eliminar","NO") =="SI"){
            if (bundle?.getString("eliminado","NO") =="eliminado"){

                if (DatosListTareasFragment.estadoTarea==DatosListTareasFragment.estadoOjo){
                    val  positionTarea = bundle?.getInt("position")
                    mostrarEnRecyclerDelete(positionTarea!!)
                    if (listTareas.size==1){
                        toastMessage("No hay tareas disponibles")
                    }
                }
                viewModel.eliminarTarea(bundle?.getInt("idTarea")!!)
            }
        }
    }

    private fun mostrarEnRecyclerDelete(positionTarea: Int) {
        recycler.setHasFixedSize(true)
        recycler.itemAnimator = DefaultItemAnimator()
        recycler.layoutManager = LinearLayoutManager(MyAplicationClass.ctx)
        adapter.removeItem(positionTarea)
    }

    private fun consultar() {
        listTareas=viewModel.consultarListaTareas(DatosListTareasFragment.estadoOjo)
        if (listTareas.isEmpty()){
            mostrarEnRecycler(listTareas)
            toastMessage("No hay tareas disponibles")
        }
        else{
            val lista =viewModel.ordenMostrarTareas(listTareas)
            mostrarEnRecycler(listTareas)
        }
    }

    private fun revisarEstadoOjo() {
        if(bundle?.getString("eliminar","NO") =="SI"){
            DatosListTareasFragment.estadoOjo=bundle!!.getBoolean("estadoOjo")
            setearEstadoOjo(DatosListTareasFragment.estadoOjo)
        }
    }

    private fun setearEstadoOjo(estadoOjo: Boolean) {
        if (!estadoOjo){
            visualizadorOjo.background = resources.getDrawable(R.drawable.ic_baseline_remove_red_eye_24)
        }else{
            visualizadorOjo.background = resources.getDrawable(R.drawable.ic_baseline_visibility_off_24)
        }
    }

    companion object {
        fun newInstance(): ListTareasFragment = ListTareasFragment()
    }
    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.visualizador ->{
                if (DatosListTareasFragment.estadoOjo){
                    DatosListTareasFragment.estadoOjo= false
                    visualizadorOjo.background = resources.getDrawable(R.drawable.ic_baseline_remove_red_eye_24)
                } else{
                    DatosListTareasFragment.estadoOjo= true
                    visualizadorOjo.background = resources.getDrawable(R.drawable.ic_baseline_visibility_off_24)
                }
                consultar()
            }
            R.id.flotanButton ->{
                showDialogFragment()
            }
        }
    }

    private fun showDialogFragment() {
        val dialog =LayoutInflater.from(this.activity!!).
        inflate(R.layout.alert_dialog_nueva_tarea,this.activity!!.findViewById(R.id.alertNuevaTarea))
        val mBuilder = AlertDialog.Builder(this.activity!!).setView(dialog)
        val textTitulo = dialog.findViewById<EditText>(R.id.tituloAlert)
        val textContenido= dialog.findViewById<EditText>(R.id.contenidoAlert)
        val textFinaliza = dialog.findViewById<EditText>(R.id.finalizaAlert)
        val aceptar = dialog.findViewById<Button>(R.id.botonAceptar)
        val cancelar = dialog.findViewById<Button>(R.id.butonCancelar)
        val  mAlertDialog = mBuilder.show()
        mAlertDialog.setCanceledOnTouchOutside(false)
        mAlertDialog.window?.setBackgroundDrawable(null)
        textFinaliza.setOnClickListener {
            hideSoftKeyBoard(this.activity!!, textFinaliza)
            val c = Calendar.getInstance()
            val day = c.get(Calendar.DAY_OF_MONTH)
            val month = c.get(Calendar.MONTH)
            val year = c.get(Calendar.YEAR)
            val dpd = DatePickerDialog(this.activity!!, DatePickerDialog.OnDateSetListener{
                    _, mYear, mMonth, dayOfMonth ->
                val mes = mMonth+1
                var mesString =""
                mesString = if (mes<10){
                    "0$mes"
                }else{
                    mes.toString()
                }
                textFinaliza.setText("$dayOfMonth/$mesString/$mYear")
            },year,month,day)
            dpd.show()
        }
        aceptar.setOnClickListener {
            val titulo= textTitulo.text.toString()
            val contenido = textContenido.text.toString()
            val finalizacion = textFinaliza.text.toString()
            when(val respuesta = validacion(titulo,contenido,finalizacion)){
                "Ingrese el titulo de la tarea"->{
                    toastMessage(respuesta)
                    textTitulo.text.clear()
                }
                "Ingrese el contenido de la tarea"->{
                    toastMessage(respuesta)
                    textContenido.text.clear()
                }
                "Ingrese la fecha de la tarea"->{
                    toastMessage(respuesta)
                    textFinaliza.text.clear()
                }
                "El dia elegido no es valido"->{
                    toastMessage(respuesta)
                }
                "El mes elegido no es valido"->{
                    toastMessage(respuesta)
                }
                else ->{
                    mAlertDialog.dismiss()
                }
            }
        }
        cancelar.setOnClickListener { mAlertDialog.dismiss() }
    }

    private fun validacion(titulo: String, contenido: String, fecha: String): String {
        var retornar=""
        if(titulo.isEmpty() || titulo.isBlank()){
            retornar= "Ingrese el titulo de la tarea"
        }else{
            if (contenido.isEmpty() || contenido.isBlank()){
                retornar = "Ingrese el contenido de la tarea"
            }
            else{
                if(fecha.isEmpty() || fecha.isBlank()){
                    retornar = "Ingrese la fecha de la tarea"
                }
                else{
                    val fechaCreacion = fechaActual()
                    val fechaActual:List<String> = fechaCreacion.split("/")
                    val diaActual = fechaActual[0]
                    val mesActual = fechaActual[1]
                    val fechaElegida:List<String> = fecha.split("/")
                    val diaElegido = fechaElegida[0]
                    val mesElegido = fechaElegida[1]
                    val textFinalizaSinYear = "${fechaElegida[0]}/${fechaElegida[1]}"
                    if (mesActual.toInt()<=mesElegido.toInt()){
                        when{
                            mesActual.toInt() == mesElegido.toInt() && diaElegido.toInt()>=diaActual.toInt()->{
                                insertarUltimaTarea(titulo,contenido,fechaCreacion,textFinalizaSinYear)
                            }
                            mesActual.toInt() < mesElegido.toInt()->{
                                insertarUltimaTarea(titulo,contenido,fechaCreacion,textFinalizaSinYear)
                            }
                            else->{
                                retornar =  "El dia elegido no es valido"
                            }
                        }
                    }else{
                        retornar = "El mes elegido no es valido"
                    }
                }
            }
        }
        return  retornar
    }

    private fun insertarUltimaTarea(
        titulo: String,
        contenido: String,
        fechaCreacion: String,
        textFinalizaSinYear: String
    ) {
        viewModel.insertarTarea(titulo,contenido,fechaCreacion,textFinalizaSinYear)
        val ultimaTarea = viewModel.consultarUltimaTarea()
        mostrarEnRecyclerAdd(ultimaTarea)
    }

    private fun mostrarEnRecyclerAdd(ultimaTarea: Tareas) {
        if (!DatosListTareasFragment.estadoOjo){
            adapter.addItem(0,ultimaTarea)
        }else{
            toastMessage("tarea creada")
        }
    }

    private fun mostrarEnRecycler(list: MutableList<Tareas>) {
        recycler.setHasFixedSize(true)
        recycler.itemAnimator = DefaultItemAnimator()
        recycler.layoutManager = LinearLayoutManager(MyAplicationClass.ctx)
        adapter =TareasAdapter(MyAplicationClass.ctx!!,object:
            RecyclerTareasListener {
            override fun onClick(tarea: Tareas, position: Int) {
                DatosListTareasFragment.estadoTarea =viewModel.consultaEstadoTarea(tarea.id!!)

                nextActivity(tarea,position)
            }
            override fun change(tarea: Tareas, position: Int) {
                val estado =viewModel.consultaEstadoTarea(tarea.id!!)
                viewModel.updateEstadoTarea(tarea,!estado)
            }
        })
        recycler.adapter = adapter
        adapter.setDataList(list)
        adapter.notifyDataSetChanged()
    }

    private fun nextActivity(tarea: Tareas, position: Int) {
        val bundle = Bundle()
        bundle.putInt("tareaID",tarea.id!!)
        bundle.putString("tareaTitulo",tarea.titulo)
        bundle.putString("tareaDetalle",tarea.descripcion)
        bundle.putString("tareaCreacion",tarea.creacion)
        bundle.putString("tareaFinalizacion",tarea.finalizacion)
        bundle.putInt("position",position)
        bundle.putBoolean("estadoOjo",DatosListTareasFragment.estadoOjo)
        val intent = Intent(this.activity, DetalleTareaActivity::class.java)
        intent.putExtras(bundle)
        this.activity?.startActivity(intent)
        this.activity?.overridePendingTransition(
            R.anim.left_in, R.anim.left_out
        )
        this.activity?.finish()
    }
}