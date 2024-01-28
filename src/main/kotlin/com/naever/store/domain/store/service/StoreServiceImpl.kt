package com.naever.store.domain.store.service

import com.naever.store.domain.exception.ForbiddenException
import com.naever.store.domain.exception.ModelNotFoundException
import com.naever.store.domain.product.repository.IProductRepository
import com.naever.store.domain.store.dto.StoreRequest
import com.naever.store.domain.store.dto.StoreResponse
import com.naever.store.domain.store.model.Store
import com.naever.store.domain.store.repository.IStoreRepository
import com.naever.store.domain.user.repository.UserRepository
import com.naever.store.infra.security.SecurityUtil
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StoreServiceImpl(
    private val userRepository: UserRepository,
    private val storeRepository: IStoreRepository,
    private val productRepository: IProductRepository
) : StoreService {

    override fun createStore(request: StoreRequest): StoreResponse {

        val user = userRepository.findByIdOrNull(SecurityUtil.getLoginUserId())
            ?: throw ModelNotFoundException("User", SecurityUtil.getLoginUserId())

        if (storeRepository.existsByUserId(user.id!!)) {
            throw IllegalStateException("already created a store")
        }

        return Store(
            name = request.name,
            introduction = request.introduction,
            user = user
        ).let {
            storeRepository.save(it)
        }.let {
            StoreResponse.from(it)
        }
    }

    override fun getStore(storeId: Long): StoreResponse {

        val store = storeRepository.findById(storeId) ?: throw ModelNotFoundException("Store", storeId)

        return StoreResponse.from(store)
    }

    override fun updateStore(storeId: Long, request: StoreRequest): StoreResponse {

        val store = getStoreIfAuthorized(SecurityUtil.getLoginUserId(), storeId)

        store.update(request)

        return storeRepository.save(store)
            .let { StoreResponse.from(it) }
    }

    @Transactional
    override fun deleteStore(storeId: Long) {

        val store = getStoreIfAuthorized(SecurityUtil.getLoginUserId(), storeId)

        productRepository.deleteAllByStoreId(storeId)

        store.delete()
        storeRepository.save(store)
    }

    override fun getStoreIfAuthorized(userId: Long?, storeId: Long): Store {

        userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("User", userId)

        val store = storeRepository.findById(storeId) ?: throw ModelNotFoundException("Store", storeId)

        if (!store.matchUserId(userId!!)) {
            throw ForbiddenException(userId, "Store", storeId)
        }

        return store
    }

}
