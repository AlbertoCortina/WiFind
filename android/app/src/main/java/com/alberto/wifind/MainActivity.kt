package com.alberto.wifind

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.io.OutputStreamWriter
import android.widget.Toast
import java.io.File
import java.util.logging.Logger.global
import android.support.v4.content.FileProvider.getUriForFile
import android.support.v4.content.FileProvider
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {

    var listaWifis = ArrayList<ScanResult>()

    lateinit var wifiManager: WifiManager

    val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            listaWifis = wifiManager.scanResults as ArrayList<ScanResult>
            Log.d("TESTING", "Escaneando wifis...")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Establecemos los permisos necesarios para la correcta ejecución
        establecerPermisos()

        wifiManager = this.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        this.button_enviar_datos.setOnClickListener{view -> comenzarEscaneo()}
    }

    /**
     * Método para comprobar si la aplicación tiene todos los permisos necesarios
     * para ejecutarse.
     */
    fun establecerPermisos() {
        val permisoUbicacion = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val permisoEscritura = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val permisoLectura = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (permisoUbicacion != PackageManager.PERMISSION_GRANTED || permisoEscritura != PackageManager.PERMISSION_GRANTED || permisoLectura != PackageManager.PERMISSION_GRANTED) {
            peticionPermisos()
        }
    }

    /**
     * Método para pedir los permisos necesarios para la aplicacion
     */
    fun peticionPermisos() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE), 1)
    }

    /**
     * Método para comenzar a escanear las redes wifis durante 8 segundos (se puede cambiar)
     */
    fun comenzarEscaneo() {
        Log.d("TESTING", "Comienzo de escaneo")
        registerReceiver(broadcastReceiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))

        wifiManager.startScan()

        Handler().postDelayed({pararEscaneo()},3000)
    }

    /**
     * Método para parar el escaneo de las redes wifi
     */
    fun pararEscaneo() {
        unregisterReceiver(broadcastReceiver)

        Log.d("TESTING", "Se para el escaneo, se han detectado:"+listaWifis.toString())

        guardarDatos()
    }

    /**
     * Método que recoje todos los datos y los almacena en un fichero
     */
    fun guardarDatos() {
        val aulaSeleccionada = this.spinner_localizaciones.selectedItem as String
        Log.d("TESTING","El aula seleccionada es: $aulaSeleccionada")

        val comentario = this.editText_comentario.text.toString()
        Log.d("TESTING","El comentario es: $comentario")

        val path = this.filesDir.toString()+"/datos"
        val myDir = File(path)
        if(!myDir.exists()) {
            myDir.mkdirs()
        }
        for(file in myDir.list()){
            File(myDir, file).delete()
        }

        val file = File(path,"$aulaSeleccionada|$comentario.csv")
        if (file.exists()){
            file.delete()
        }
        file.createNewFile()
        val fout = FileOutputStream(file)

        val osw = OutputStreamWriter(fout)

        for(wifi in listaWifis) {
            osw.write("$aulaSeleccionada,")
            osw.write("$comentario,")
            osw.write(wifi.toString()+",")
            osw.write("distanciaCalculada:"+calcularDistancia(wifi.level.toDouble(), wifi.frequency.toDouble()))
            osw.write("\n")
        }

        osw.close()
        Log.d("TESTING", "Fichero con datos creado correctamente")

        // Enviamos un correo con los datos
        enviarCorreo(aulaSeleccionada,comentario)
    }

    /**
     * Calcula la distancia al punto wifi a partir de los db y MHz
     * Esta medición tiene error, ya que se considera que no hay objetos en medio
     * http://rvmiller.com/2013/05/part-1-wifi-based-trilateration-on-android/
     */
    fun calcularDistancia(level:Double, frequency: Double): Double {
        val exp = (27.55 - 20 * Math.log10(frequency) + Math.abs(level)) / 20.0
        return Math.pow(10.0, exp)
    }

    fun enviarCorreo(aulaSeleccionada: String, comentario: String) {
        val TO = arrayOf("albertocortina96@gmail.com") //aquí pon tu correo
         val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.data = Uri.parse("mailto:")
        emailIntent.type = "text/plain"

        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "WIFIND | $aulaSeleccionada")
//        emailIntent.putExtra(Intent.EXTRA_TEXT, "Adjunto datos obtenidos")
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        val fileUri = FileProvider.getUriForFile(this, "com.myfileprovider", File(this.filesDir.canonicalPath+"/datos/$aulaSeleccionada|$comentario.csv"))
        emailIntent.putExtra(Intent.EXTRA_STREAM, fileUri)

        try {
            startActivityForResult(Intent.createChooser(emailIntent, "Enviar email..."),1)
            finish()
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(this@MainActivity,
                    "No tienes clientes de email instalados.", Toast.LENGTH_SHORT).show()
        }
    }
}

//-80 dBm: es la señal mínima aceptable para establecer la conexión; puede ocurrir caídas de enlace.
//-70 dBm: enlace normal-bajo; es una señal medianamente buena, aunque se pueden sufrir problemas con lluvia y viento.
//-60 dBm: enlace bueno; ajustando TX y basic rates se puede lograr una conexión estable al 80%.
//-40 a -60 dBm: señal idónea con tasas de transferencia estables.
//-1 a -39 dBm: señal excelente, muy difícil de conseguir en un entorno normal.
//0 dBm: señal ideal, se logra solo en laboratorio.