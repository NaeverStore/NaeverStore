package com.naever.store.domain.store.service

import com.naever.store.domain.store.dto.StoreRequest
import com.naever.store.domain.store.dto.StoreResponse
import com.naever.store.domain.store.model.Store

interface StoreService {

    fun createStore(request: StoreRequest): StoreResponse

    fun getStore(storeId: Long): StoreResponse

    fun updateStore(storeId: Long, request: StoreRequest): StoreResponse

    fun deleteStore(storeId: Long)

    fun getStoreIfAuthorized(userId: Long?, storeId: Long): Store

}