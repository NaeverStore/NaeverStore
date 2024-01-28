package com.naever.store.domain.store.repository

import com.naever.store.domain.store.model.Store
import org.springframework.data.jpa.repository.JpaRepository

interface StoreJpaRepository : JpaRepository<Store, Long> {

    fun existsByUserId(userId: Long): Boolean

}