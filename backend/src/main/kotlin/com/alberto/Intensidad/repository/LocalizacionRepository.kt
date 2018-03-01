package com.alberto.Intensidad.repository

import com.alberto.Intensidad.model.Localizacion
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LocalizacionRepository: JpaRepository<Localizacion, Long>