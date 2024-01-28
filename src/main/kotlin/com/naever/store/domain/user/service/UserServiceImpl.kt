package com.naever.store.domain.user.service

import com.naever.store.domain.exception.InvalidCredentialException
import com.naever.store.domain.exception.ModelNotFoundException
import com.naever.store.domain.exception.UniqueAttributeValueAlreadyExistException
import com.naever.store.domain.user.dto.*
import com.naever.store.domain.user.repository.UserRepository
import com.naever.store.infra.security.jwt.TokenProvider
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val tokenProvider: TokenProvider,
): UserService {
    @Transactional
    override fun registerUser(userRegisterRequest: UserRegisterRequest): UserResponse {
        if(userRepository.existsByEmail(userRegisterRequest.email))
            throw UniqueAttributeValueAlreadyExistException("email", userRegisterRequest.email)
        if(userRegisterRequest.passwordConfirm != userRegisterRequest.password) throw InvalidCredentialException()
        val user = userRepository.save(userRegisterRequest.to(passwordEncoder))
        return UserResponse.from(user)
    }

    override fun loginUser(loginRequest: UserLoginRequest): UserLoginResponse {
        val user = userRepository.findByEmail(loginRequest.email) ?: throw ModelNotFoundException("User", null)
        if(!passwordEncoder.matches(loginRequest.password, user.password)) throw InvalidCredentialException()
        return UserLoginResponse(user.email, user.authority,
            tokenProvider.generateToken(user.id.toString(), user.email, user.authority.name))
    }
    @Transactional

    override fun updateUser(userId: Long, updateRequest: UserUpdateRequest): UserResponse {
        val user = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("User", userId)
        if(userRepository.existsByEmail(updateRequest.email ?: user.email) && user.email != updateRequest.email)
            throw UniqueAttributeValueAlreadyExistException("email" , updateRequest.email!!)
        user.updateProfile(updateRequest)
        return UserResponse.from(userRepository.save(user))
    }

    @Transactional
    override fun updatePassword(userId: Long, userPasswordUpdateRequest: UserPasswordUpdateRequest): UserResponse {
        val user = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("User", userId)
        if(!passwordEncoder.matches(userPasswordUpdateRequest.passwordConfirm, user.password)) throw InvalidCredentialException()
        val list = userRepository.findRevisions(userId).content
            .toList().map{it.entity.password}.reversed().let { it.subList(0, it.size.coerceAtMost(3)) }
        if(list.find { passwordEncoder.matches(userPasswordUpdateRequest.newPassword, it)} != null)
            throw IllegalArgumentException("Already Used Password")
        user.updatePassword(passwordEncoder.encode(userPasswordUpdateRequest.newPassword))
        return UserResponse.from(userRepository.save(user))
    }
}