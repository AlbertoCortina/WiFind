package com.alberto.Intensidad.repository

import com.alberto.Intensidad.model.Wifi
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WifiRepository: JpaRepository<Wifi, Long>