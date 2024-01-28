package com.naever.store.domain.user.model

import com.naever.store.common.BaseTimeEntity
import com.naever.store.domain.user.dto.UserUpdateRequest
import jakarta.persistence.*
import org.hibernate.envers.Audited
import org.hibernate.envers.NotAudited

@Table(name = "app_user")
@Entity
@Audited
data class User(
    @Column(name = "nickname")
    var nickname:String,
    @Column(name = "password")
    var password: String,
    @Column(name = "email")
    var email: String,
    @Column(name = "address")
    var address: String,
    @Column(name = "introduction")
    var introduction: String,
    @Column(name = "authority")
    @Enumerated(EnumType.STRING)
    val authority: UserType = UserType.USER
) : BaseTimeEntity()
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    fun updatePassword(newPassword: String)
    {
        password = newPassword
    }
    fun updateProfile(updateRequest: UserUpdateRequest)
    {
        email = updateRequest.email ?: email
        address = updateRequest.address ?: address
        introduction = updateRequest.introduction ?: introduction
        nickname = updateRequest.nickname ?: nickname
    }
}