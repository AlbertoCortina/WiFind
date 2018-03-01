package com.alberto.Intensidad.repository

import com.alberto.Intensidad.model.Aula
import com.alberto.Intensidad.model.Intensidad
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface IntensidadRepository: JpaRepository<Intensidad, Long>