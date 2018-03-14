package com.alberto.wifind.model

/**
 * Created by alberto on 2/03/18.
 */

class Aula(val id: Long?, val nombre: String)  {
    override fun toString(): String = nombre
}

class Wifi(val id: Long?, val ssid: String, val nivel: Int)

class Relacion(val aula: Aula?, val wifi: Wifi, val comentario: String)