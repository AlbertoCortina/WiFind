package com.alberto.Intensidad.model

import org.jetbrains.annotations.NotNull
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "TIntensidad")
data class Intensidad (
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0,

        @NotNull
        @ManyToOne
        @JoinColumn(name = "id_localizacion")
        val localizacion: Localizacion ?= null,

        @NotNull
        @ManyToOne
        @JoinColumn(name = "id_wifi")
        val wifi:Wifi ?= null,

        @NotBlank
        val intensidad: Double = 0.0
)