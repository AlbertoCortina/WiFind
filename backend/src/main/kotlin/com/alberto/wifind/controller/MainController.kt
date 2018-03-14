package com.alberto.wifind.controller

import com.alberto.wifind.model.Aula
import com.alberto.wifind.model.Relacion
import com.alberto.wifind.model.Wifi
import com.alberto.wifind.repository.AulaRepository
import com.alberto.wifind.repository.RelacionRepository
import com.alberto.wifind.repository.WifiRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/api")
@EnableWebMvc
class MainController {

    @Autowired
    lateinit var aulaRepository: AulaRepository

    @Autowired
    lateinit var relacionRepository: RelacionRepository

    @Autowired
    lateinit var wifiRepository: WifiRepository

    /**
     * Método para crear un Aula
     */
    @PostMapping("/aulas")
    fun createAula(@Valid @RequestBody aula: Aula) = aulaRepository.save(aula)

    /**
     * Método que devuelve una lista con todas las aulas
     */
    @GetMapping(value = "/aulas")
    fun getAllAulas(): List<Aula> = aulaRepository.findAll()

    @GetMapping(value = "/aulas/{id}")
    fun getAula(@PathVariable("id") aulaId: Long): Optional<Aula> = aulaRepository.findById(aulaId)

    /***
     * Método para crear un Wifi
     */
    @PostMapping(value ="/wifis")
    fun createWifi(@Valid @RequestBody wifis: List<Wifi>): List<Wifi> = wifiRepository.saveAll(wifis)


    @GetMapping(value = "/wifis/{id}")
    fun getWifi(@PathVariable("id") wifiId: Long): Optional<Wifi> = wifiRepository.findById(wifiId)

    /**
     * Método que crear una relacion entre un Aula y un Wifi
     */
    @PostMapping(value = "/relaciones")
    fun createRelacion(@Valid @RequestBody relaciones: List<Relacion>) = relacionRepository.saveAll(relaciones)

}
