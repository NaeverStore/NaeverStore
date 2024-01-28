package com.naever.store.domain.store.repository

import com.naever.store.domain.store.model.Store
import org.springframework.data.repository.findByIdOrNull

class StoreRepositoryImpl(
    private val storeJpaRepository: StoreJpaRepository
) : IStoreRepository {

    override fun save(store: Store): Store {
        return storeJpaRepository.save(store)
    }

    override fun findById(id: Long): Store? {
        return storeJpaRepository.findByIdOrNull(id)
    }

    override fun existsByUserId(userId: Long): Boolean {
        return storeJpaRepository.existsByUserId(userId)
    }

}