package com.naever.store.domain.user.repository

import com.naever.store.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.history.RevisionRepository

interface UserRepository: JpaRepository<User, Long>, RevisionRepository<User, Long, Long> {

    fun existsByEmail(email: String): Boolean

    fun findByEmail(email: String): User?
}