package com.alberto.wifind.presentation

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import com.alberto.wifind.R
import com.alberto.wifind.model.Aula
import com.alberto.wifind.model.Wifi
import com.alberto.wifind.model.Relacion
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException
import okhttp3.OkHttpClient

class MainActivity : AppCompatActivity() {

    var aulaSeleccionada: Aula? = null
    var comentario: String? = ""
    var listaAulas = ArrayList<Aula>()
    var listaWifisAux = ArrayList<ScanResult>()
    var listaWifis = ArrayList<Wifi>()
    var listaRelaciones = ArrayList<Relacion>()
    lateinit var wifiManager: WifiManager

    val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            listaWifisAux = wifiManager.scanResults as ArrayList<ScanResult>
            Log.d("TESTING", "onReceive Called")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true);
        getSupportActionBar()?.setIcon(R.mipmap.ic_launcher);

        establecerPermisos();

        rellenarSpinner()

        wifiManager = this.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        this.button_enviar_datos.setOnClickListener{view -> comenzarEscaneo()}

    }

    /**
     * Método para comprobar si la aplicación tiene todos los permisos necesarios
     * para ejecutarse.
     */
    private fun establecerPermisos() {
       val permisoUbicacion = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)

        if (permisoUbicacion != PackageManager.PERMISSION_GRANTED) {
            peticionPermisos()
        }
    }

    /**
     * Método para pedir los permisos necesarios para la aplicacion
     */
    private fun peticionPermisos() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
    }

    /**
     * Método que rellena el spinner con las aulas cogidas de una llamada a la API
     */
    private fun rellenarSpinner() {
        val spinner = this.spinner_localizaciones
        val url = "http://192.168.1.104:8080/api/aulas"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call?, response: Response?) {
                val body = response?.body()?.string()
                val json = GsonBuilder().create()
                listaAulas = json.fromJson(body, object: TypeToken<List<Aula>>() {}.type) as ArrayList<Aula>

                runOnUiThread {
                    val arrayAdapter: ArrayAdapter<Aula> = ArrayAdapter<Aula>(applicationContext, android.R.layout.simple_spinner_item, listaAulas)
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner?.adapter = arrayAdapter;
                }

                Log.d("RELLENADO SPINNER", listaAulas.toString())
            }

            override fun onFailure(call: Call?, e: IOException?) {}
        })
    }

    /**
     * Método para comenzar a escanear las redes wifis
     */
    private fun comenzarEscaneo() {
        registerReceiver(broadcastReceiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))

        wifiManager.startScan()

        Handler().postDelayed({pararEscaneo()},10000)
    }

    /**
     * Método para parar escaneo de las redes wifi
     */
    private fun pararEscaneo() {
        unregisterReceiver(broadcastReceiver)
        for (wifi in listaWifisAux) {
            //TODO ScanResult parece que tiene un campo distancia, podría ser interesante
            listaWifis.add(Wifi(null, wifi.SSID, wifi.level))
        }
        Log.d("WIFIS DETECTADAS", listaWifis.toString())

        //Primero insertamos las wifis detectadas en la base de datos
        insertarWifis()
    }

    /**
     * Método para enviar los datos en la base de datos
     */
    private fun enviarDatos() {

    }

    /**
     * Método que inserta las wifis detectadas
     */
    private fun insertarWifis() {
        val JSON = MediaType.parse("application/json; charset=utf-8")
        val json = Gson().toJson(listaWifis)
        val requestBody = RequestBody.create(JSON,json)
        val url = "http://192.168.1.104:8080/api/wifis"
        val request = Request.Builder().url(url).post(requestBody).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call?, response: Response?) {
               Log.d("WIFIS INSERTADAS", listaWifis.toString())
            }

            override fun onFailure(call: Call?, e: IOException?) {}
        })

        //Cogemos que aula es la seleccionada
        aulaSeleccionada = this.spinner_localizaciones.selectedItem as Aula

        val comentario = this.editText_comentario

        //Para cada wifi creamos una relacion con el aula y el comentario
        for (wifi in listaWifis) {
            listaRelaciones.add(Relacion(aulaSeleccionada, wifi, "comentario de prueba"))
        }
        insertarRelaciones()


    }

    /**
     * Método que inserta las relaciones entre el wifi y el aula
     */
    private fun insertarRelaciones() {
        val JSON = MediaType.parse("application/json; charset=utf-8")
        val json = Gson().toJson(listaRelaciones)
        val requestBody = RequestBody.create(JSON,json)
        val url = "http://192.168.1.104:8080/api/relaciones"
        val request = Request.Builder().url(url).post(requestBody).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call?, response: Response?) {
                Log.d("RELACIONES INSERTADAS", listaRelaciones.toString())
            }

            override fun onFailure(call: Call?, e: IOException?) {}
        })

        //Borramos las wifis detectadas y las relaciones
        listaWifisAux.clear()
        listaWifis.clear()
        listaRelaciones.clear()
    }

}