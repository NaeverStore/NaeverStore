package com.naever.store.domain.store.dto

import com.naever.store.domain.store.model.Store
import java.time.ZonedDateTime

data class StoreResponse(
    val id: Long?,
    val name: String,
    val introduction: String,
    val createdAt: ZonedDateTime,
    val userId: Long?
) {
    companion object {
        fun from(store: Store): StoreResponse {
            return StoreResponse(
                id = store.id,
                name = store.name,
                introduction = store.introduction,
                createdAt = store.createdAt,
                userId = store.user.id
            )
        }
    }
}