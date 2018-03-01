package com.alberto.Intensidad.controller

import com.alberto.Intensidad.model.Aula
import com.alberto.Intensidad.model.Intensidad
import com.alberto.Intensidad.model.Localizacion
import com.alberto.Intensidad.model.Wifi
import com.alberto.Intensidad.repository.AulaRepository
import com.alberto.Intensidad.repository.IntensidadRepository
import com.alberto.Intensidad.repository.LocalizacionRepository
import com.alberto.Intensidad.repository.WifiRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class MainController {

    @Autowired
    lateinit var aulaRepository: AulaRepository

    @Autowired
    lateinit var intensidadRepository: IntensidadRepository

    @Autowired
    lateinit var localizacionRepository: LocalizacionRepository

    @Autowired
    lateinit var wifiRepository: WifiRepository

    @PostMapping("/aulas")
    fun createAula(@Valid @RequestBody aula: Aula) = aulaRepository.save(aula)

    @GetMapping(value = "/aulas")
    fun getAllAulas(): List<Aula> = aulaRepository.findAll()

    @PostMapping(value = "/localizaciones")
    fun createLocalizacion(@Valid @RequestBody localizacion: Localizacion) = localizacionRepository.save(localizacion)

    @PostMapping(value ="/wifis")
    fun createWifi(@Valid @RequestBody wifi: Wifi) = wifiRepository.save(wifi)

    @PostMapping(value = "/intensidades")
    fun creteIntensidad(@Valid @RequestBody intensidad: Intensidad) = intensidadRepository.save(intensidad)

}