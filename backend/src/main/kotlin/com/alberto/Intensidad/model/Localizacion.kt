package com.alberto.Intensidad.model

import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "TLocalizacion")
data class Localizacion (
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0,

        @NotBlank
        val nombre: String = "",

        @NotBlank
        val latitud: Double = 0.0,

        @NotBlank
        val longitud: Double = 0.0,

        @OneToMany(mappedBy = "localizacion")
        val intensidades: List<Intensidad> ?= null
)