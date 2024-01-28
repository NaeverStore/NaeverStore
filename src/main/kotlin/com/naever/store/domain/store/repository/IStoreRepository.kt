package com.naever.store.domain.store.repository

import com.naever.store.domain.store.model.Store

interface IStoreRepository {

    fun save(store: Store): Store

    fun findById(id: Long): Store?

    fun existsByUserId(userId: Long): Boolean

}