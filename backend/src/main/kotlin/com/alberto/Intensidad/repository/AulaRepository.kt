package com.alberto.Intensidad.repository

import com.alberto.Intensidad.model.Aula
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AulaRepository: JpaRepository<Aula, Long>