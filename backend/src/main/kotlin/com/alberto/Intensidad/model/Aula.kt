package com.alberto.Intensidad.model

import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "TAulas")
data class Aula (
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0,

        @NotBlank
        val nombre: String = ""
)