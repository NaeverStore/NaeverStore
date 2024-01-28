package com.naever.store.domain.store.model

import com.naever.store.common.BaseTimeEntity
import com.naever.store.domain.store.dto.StoreRequest
import com.naever.store.domain.user.model.User
import jakarta.persistence.*
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "store")
@SQLRestriction("is_deleted <> true")
class Store(

    @Column(name = "name")
    var name: String,

    @Column(name = "introduction")
    var introduction: String,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User

): BaseTimeEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun matchUserId(requestUserId: Long): Boolean {
        return user.id == requestUserId
    }

    fun update(request: StoreRequest) {
        name = request.name
        introduction = request.introduction
    }

    fun delete() {
        isDeleted = true
    }

}