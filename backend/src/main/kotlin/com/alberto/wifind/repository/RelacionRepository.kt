package com.alberto.wifind.repository

import com.alberto.wifind.model.Relacion
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RelacionRepository: JpaRepository<Relacion, Long>