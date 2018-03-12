package com.alberto.Intensidad.model

import org.jetbrains.annotations.NotNull
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "TRelacion")
data class Relacion (
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0,

        val comentario: String = "",

        @NotNull
        @ManyToOne
        @JoinColumn(name = "id_aula")
        val aula: Aula ?= null,

        @NotNull
        @ManyToOne
        @JoinColumn(name = "id_wifi")
        val wifi:Wifi ?= null
)