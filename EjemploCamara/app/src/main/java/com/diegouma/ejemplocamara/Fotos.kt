package com.diegouma.ejemplocamara

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.widget.ImageView
import android.widget.Toast
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class Fotos(private var activity: Activity, private var imageView: ImageView) {

    private val SOLICITUD_PARA_TOMAR_FOTO = 1
    private val SOLICITUD_PARA_SELECCIONAR_FOTO = 2

    private val PERMISO_CAMERA = android.Manifest.permission.CAMERA
    private val PERMISO_READ_STORAGE = android.Manifest.permission.READ_EXTERNAL_STORAGE
    private val PERMISO_WRITE_STORAGE = android.Manifest.permission.WRITE_EXTERNAL_STORAGE

    //Variable de guardado
    var urlFotoActual = ""

    fun tomarFoto() {
        pedirPermisosTomarFoto()
    }

    fun seleccionarFoto() {
        pedirPermisosSeleccionFoto()
    }

    private fun pedirPermisosTomarFoto() {
        val deboProveerContexto = ActivityCompat.shouldShowRequestPermissionRationale(activity, PERMISO_CAMERA)

        if(deboProveerContexto){
            solicitudPermisosTomarFoto()
        }else{
            solicitudPermisosTomarFoto()
        }
    }

    private fun pedirPermisosSeleccionFoto() {
        val deboProveerContexto = ActivityCompat.shouldShowRequestPermissionRationale(activity, PERMISO_READ_STORAGE)

        if(deboProveerContexto){
            solicitudPermisosSeleccionFoto()
        }else{
            solicitudPermisosSeleccionFoto()
        }
    }

    private fun solicitudPermisosTomarFoto() {
        activity.requestPermissions(arrayOf(PERMISO_CAMERA,PERMISO_READ_STORAGE,PERMISO_WRITE_STORAGE), SOLICITUD_PARA_TOMAR_FOTO )
    }

    private fun solicitudPermisosSeleccionFoto() {
        activity.requestPermissions(arrayOf(PERMISO_READ_STORAGE,PERMISO_WRITE_STORAGE), SOLICITUD_PARA_SELECCIONAR_FOTO )
    }

    fun requestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray){
        when (requestCode) {
            SOLICITUD_PARA_TOMAR_FOTO -> {
                if (grantResults.size > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED
                ) {
                    //Tiene permiso
                    dispararIntentTomarFoto()
                } else {
                    //No tiene permiso
                    Toast.makeText(activity.applicationContext, "No tienme permiso para tomar fotos", Toast.LENGTH_SHORT).show()
                }
            }

            SOLICITUD_PARA_SELECCIONAR_FOTO -> {
                if (grantResults.size > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    //Tiene permiso
                    dispararIntentSeleccionarFoto()
                } else {
                    //No tiene permiso
                    Toast.makeText(activity.applicationContext, "No tienme permiso para acceder a tus fotos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun dispararIntentSeleccionarFoto() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)

        intent.setType("image/*")

        activity.startActivityForResult(Intent.createChooser(intent, "Selecciona una foto"), SOLICITUD_PARA_SELECCIONAR_FOTO)

    }

    private fun dispararIntentTomarFoto(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (intent.resolveActivity(activity.packageManager) != null){

            var archivoFoto: File?  = null

            archivoFoto = crearArchivoImagen()

            if(archivoFoto != null){
                val urlFoto = FileProvider.getUriForFile(activity.applicationContext, "com.diegouma.ejemplocamara", archivoFoto)

                intent.putExtra(MediaStore.EXTRA_OUTPUT, urlFoto)
                activity.startActivityForResult(intent, SOLICITUD_PARA_TOMAR_FOTO)
            }
        }
    }

    fun activityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            SOLICITUD_PARA_TOMAR_FOTO -> {
                if(resultCode == Activity.RESULT_OK){
                    //Obtiene imagen
                    //Log.d("ACTIVITY_RESULT", resultCode.toString())
                    /*
                    val extra = data?.extras
                    val imageBitmap = extra!!.get("data") as Bitmap //se hace un casteo
                    */
                    mostrarBitmap(urlFotoActual)
                    agregarImagenGaleria()

                }else{
                    //Cancela imagen
                }
            }

            SOLICITUD_PARA_SELECCIONAR_FOTO -> {
                if(resultCode == Activity.RESULT_OK){
                    mostrarBitmap(data?.data.toString())

                }else{
                    //Cancela imagen
                }
            }
        }
    }

    private fun mostrarBitmap(url:String){
        val uri = Uri.parse(url)
        val stream = activity.contentResolver.openInputStream(uri)
        val imageBitmap = BitmapFactory.decodeStream(stream)
        imageView.setImageBitmap(imageBitmap)
    }

    private fun crearArchivoImagen():File{
        val timeStamp = SimpleDateFormat("yyyMMdd_HHmmss").format(Date())
        val nombreArchivosImagen = "JPEG_" + timeStamp + "_"

        //val directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES)//directorio interno de guardado

        val directorio = Environment.getExternalStorageDirectory() //  regresa url de las carpetas
        val directorioImagenes = File(directorio.absolutePath + "/Pictures/")
        val imagen = File.createTempFile(nombreArchivosImagen,".jpg", directorioImagenes)

        urlFotoActual = "file://" + imagen.absolutePath

        return  imagen
    }

    private fun agregarImagenGaleria(){
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val file = File(urlFotoActual)
        val uri = Uri.fromFile(file)
        intent.setData(uri)
        activity.sendBroadcast(intent)
    }

}