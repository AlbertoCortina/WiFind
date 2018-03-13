package com.alberto.wifind.model

import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "TAula")
data class Aula (
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0,

        @NotBlank
        val nombre: String = ""
)