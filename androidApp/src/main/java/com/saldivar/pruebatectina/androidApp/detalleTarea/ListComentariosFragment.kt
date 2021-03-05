package com.saldivar.pruebatectina.androidApp.detalleTarea

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.saldivar.pruebatecnica.helper.MyAplicationClass
import com.saldivar.pruebatecnica.modulo.detalleTarea.util.DatosListComentariosFragment
import com.saldivar.pruebatectina.androidApp.MainActivity
import com.saldivar.pruebatectina.androidApp.R
import com.saldivar.pruebatectina.androidApp.detalleTarea.util.ComentariosAdapter
import com.saldivar.pruebatectina.androidApp.helper.fechaActual
import com.saldivar.pruebatectina.androidApp.helper.hideSoftKeyBoard
import com.saldivar.pruebatectina.androidApp.helper.toastMessage
import com.saldivar.pruebatectina.shared.DatabaseDriverFactory
import com.saldivar.pruebatectina.shared.modulos.detalleTarea.objeto.Comentarios
import com.saldivar.pruebatectina.shared.modulos.detalleTarea.viewmodel.ViewModelComentarios
import com.saldivar.pruebatectina.shared.modulos.home.objeto.Tareas
import java.util.*

class ListComentariosFragment : Fragment(),View.OnClickListener {
    private lateinit var recycler: RecyclerView
    private lateinit var textToolbar :TextView
    private lateinit var textCreacionDetalle : TextView
    private lateinit var textFinalizacionDetalle : TextView
    private lateinit var textDescripcionDetalle :TextView
    private lateinit var etNewComentario : EditText
    private lateinit var textNumeroComentarios: TextView
    private lateinit var delete :AppCompatImageView
    private lateinit var check: ImageView
    private lateinit var edit :AppCompatImageView
    private lateinit var adapter: ComentariosAdapter
    private val driver = DatabaseDriverFactory(MyAplicationClass.ctx!!)
    private val viewModel = ViewModelComentarios(driver)
    private var usuariosNumero = 0
    private  var textoCreacion =""
    private  var textoFinalizacion =""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootview =inflater.inflate(R.layout.fragment_list_comentarios, container, false)
        textToolbar = activity!!.findViewById(R.id.textViewToolbar)
        textCreacionDetalle = activity!!.findViewById(R.id.creacionDetalle)
        textFinalizacionDetalle = activity!!.findViewById(R.id.finalizacionDetalle)
        textDescripcionDetalle = activity!!.findViewById(R.id.descricionDetalle)
        etNewComentario = activity!!.findViewById(R.id.comentario_new)
        textNumeroComentarios = activity!!.findViewById(R.id.numeroComentarios)
        recycler = rootview.findViewById(R.id.recycler_comentarios)
        delete = activity!!.findViewById(R.id.delete)
        delete.setOnClickListener(this)
        check = activity!!.findViewById(R.id.check)
        check.setOnClickListener(this)
        edit = activity!!.findViewById(R.id.edit)
        edit.setOnClickListener(this)
        val bundle = activity!!.intent.extras
        mostrarDatos(bundle)

        return rootview
    }

    private fun mostrarDatos(bundle: Bundle?) {
        DatosListComentariosFragment.idTarea = bundle!!.getInt("tareaID",0)
        textToolbar.text = bundle.getString("tareaTitulo")
        textoCreacion = bundle.getString("tareaCreacion","")
        textoFinalizacion = bundle.getString("tareaFinalizacion","")
        textCreacionDetalle.text= "Creada: $textoCreacion"
        textFinalizacionDetalle.text = "Finaliza: $textoFinalizacion"
        textDescripcionDetalle.text= bundle.getString("tareaDetalle")
        DatosListComentariosFragment.positionTarea = bundle.getInt("position")
        DatosListComentariosFragment.estadoOjo= bundle.getBoolean("estadoOjo")

        val listaComentarios = viewModel.getAllComentarios(DatosListComentariosFragment.idTarea)
        usuariosNumero= listaComentarios.size
        setRecyclerView(listaComentarios)

    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.check->{
                val dato = etNewComentario.text.toString()
                etNewComentario.setText("")
                etNewComentario.clearFocus()
                hideSoftKeyBoard(MyAplicationClass.ctx!!, etNewComentario)
                if(dato.isNotEmpty() || dato.isNotBlank()){
                viewModel.enviarNuevoComentario(dato,DatosListComentariosFragment.idTarea)
                val ultimocomentario= viewModel.obtenerUltimoComentario()
                recyclerViewNuevoComentario(ultimocomentario)
                }

            }
            R.id.edit->{
                showDialog()
            }
            R.id.delete->{
                showDialogDelete()
            }
        }
    }

    private fun showDialogDelete(){
        val mDialogView =LayoutInflater.from(this.activity!!).
        inflate(R.layout.alert_dialog_delete, this.activity!!.findViewById(R.id.alertDeleteTarea))
        val mBuilder = AlertDialog.Builder(this.activity!!).setView(mDialogView)
        val titulo = mDialogView.findViewById(R.id.TituloDialogDelete) as TextView
        val recordatorio = mDialogView.findViewById(R.id.recordatorioDialogDelete) as TextView
        val aceptar = mDialogView.findViewById(R.id.botonConfirmar) as Button
        val cancelar = mDialogView.findViewById(R.id.butonCancelar) as Button
        val  mAlertDialog = mBuilder.show()
        mAlertDialog.window?.setBackgroundDrawable(null)
        titulo.text = "¿Seguro que quieres eliminar ${textToolbar.text}?"
        recordatorio.text = "Recuerda: Se van a borrar todos los comentarios"
        aceptar.setOnClickListener {
            viewModel.eliminarComentarios(DatosListComentariosFragment.idTarea)
            backActivity("eliminado")
            mAlertDialog.dismiss()
        }
        cancelar.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    internal fun backActivity(eliminar:String ="",context: Context = activity!!) {
        val bundle2 = Bundle()
        bundle2.putString("eliminar","SI")
        bundle2.putInt("position",DatosListComentariosFragment.positionTarea)
        bundle2.putInt("idTarea",DatosListComentariosFragment.idTarea)
        bundle2.putBoolean("estadoOjo",DatosListComentariosFragment.estadoOjo)
        if(eliminar=="eliminado"){
            bundle2.putString("eliminado",eliminar)
        }
        val intent = Intent(context,MainActivity::class.java)
        intent.putExtras(bundle2)
        context.startActivity(intent)
        (context as Activity).overridePendingTransition(
            R.anim.right_in, R.anim.right_out
        )
        (context as Activity).finish()
    }

    private fun showDialog(){
        val mDialogView =LayoutInflater.from(this.activity!!).
        inflate(R.layout.alert_dialog_nueva_tarea,this.activity!!.findViewById(R.id.alertNuevaTarea))
        val mBuilder = AlertDialog.Builder(this.activity!!).setView(mDialogView)
        val tituloAlert = mDialogView.findViewById<TextView>(R.id.TituloDialog)
        val containerTituloAlert = mDialogView.findViewById<TextInputLayout>(R.id.containerTituloAlert)
        val textTitulo = mDialogView.findViewById<EditText>(R.id.tituloAlert)
        val containerContenidoAlert = mDialogView.findViewById<TextInputLayout>(R.id.containerContenidoAlert)
        val textContenido= mDialogView.findViewById<EditText>(R.id.contenidoAlert)
        val containerFinalizaAlert = mDialogView.findViewById<TextInputLayout>(R.id.containerFinalizaAlert)
        val textFinaliza = mDialogView.findViewById<EditText>(R.id.finalizaAlert)
        val aceptar = mDialogView.findViewById<Button>(R.id.botonAceptar)
        val cancelar = mDialogView.findViewById<Button>(R.id.butonCancelar)
        tituloAlert.text = "Editar Actividad"
        containerTituloAlert.hint = "Titulo"
        textTitulo.setText(textToolbar.text.toString())
        containerContenidoAlert.hint = "Contenido"
        textContenido.setText(textDescripcionDetalle.text.toString())
        containerFinalizaAlert.hint = "Finaliza"
        textFinaliza.setText("$textoFinalizacion/2021")
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
            val titulo = textTitulo.text.toString()
            val contenido = textContenido.text.toString()
            val finaliza = textFinaliza.text.toString()
            val respuesta = validacion(titulo,contenido,finaliza,DatosListComentariosFragment.idTarea)
            when(respuesta){
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
            mAlertDialog.dismiss()
        }
        cancelar.setOnClickListener { mAlertDialog.dismiss() }
    }

    private fun validacion(titulo:String,contenido:String,fecha:String,idTarea: Int):String {
        var retornar=""
        if(titulo.isEmpty() || titulo.isBlank()){
            retornar= "Ingrese el titulo de la tarea"
        }else{
            if (contenido.isEmpty() ||contenido.isBlank()){
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
                        if(diaElegido.toInt()>=diaActual.toInt()){
                            viewModel.updateTarea(titulo,contenido, textFinalizaSinYear, idTarea)
                            val tareaActualizada= viewModel.consultarDatosNuevosTarea(idTarea)
                            setearDatosVista(tareaActualizada)
                        }else{
                            retornar =  "El dia elegido no es valido"
                        }
                    }else{
                        retornar = "El mes elegido no es valido"
                    }
                }
            }
        }
        return retornar
    }

    private fun setearDatosVista(tareaActualizada: Tareas) {
        DatosListComentariosFragment.idTarea = tareaActualizada.id!!
        textToolbar.text             = tareaActualizada.titulo
        textCreacionDetalle.text     =  "Creada: ${tareaActualizada.creacion}"
        textFinalizacionDetalle.text = "Finaliza: ${tareaActualizada.finalizacion}"
        textDescripcionDetalle.text  = tareaActualizada.descripcion
    }

    private fun setRecyclerView(datosComentario: MutableList<Comentarios>) {
        textNumeroComentarios.text=("Comentarios (${usuariosNumero})")
        recycler.setHasFixedSize(true)
        recycler.itemAnimator = DefaultItemAnimator()
        recycler.layoutManager = LinearLayoutManager(MyAplicationClass.ctx)
        adapter =(ComentariosAdapter(MyAplicationClass.ctx!!))
        recycler.adapter = adapter
        adapter.setDataList(datosComentario)
        adapter.notifyDataSetChanged()
    }

    private fun recyclerViewNuevoComentario(ultimocomentario: Comentarios) {
        val numeroComentarios = textNumeroComentarios.text.toString()
         numeroComentarios.split("(")
        usuariosNumero += 1
        textNumeroComentarios.text = "Comentarios (${usuariosNumero})"
        adapter.addItem(0,ultimocomentario)
        recycler.scrollBy(0,-150)

    }
    companion object {
        fun newInstance(): ListComentariosFragment = ListComentariosFragment()
    }

}