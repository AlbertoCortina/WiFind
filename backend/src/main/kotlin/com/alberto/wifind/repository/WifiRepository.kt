package com.alberto.wifind.repository

import com.alberto.wifind.model.Wifi
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WifiRepository: JpaRepository<Wifi, Long>