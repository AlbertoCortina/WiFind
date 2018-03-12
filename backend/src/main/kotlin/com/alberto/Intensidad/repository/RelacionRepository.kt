package com.alberto.Intensidad.repository

import com.alberto.Intensidad.model.Relacion
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RelacionRepository: JpaRepository<Relacion, Long>