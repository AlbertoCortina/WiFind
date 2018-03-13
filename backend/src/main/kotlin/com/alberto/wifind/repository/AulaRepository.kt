package com.alberto.wifind.repository

import com.alberto.wifind.model.Aula
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AulaRepository: JpaRepository<Aula, Long>