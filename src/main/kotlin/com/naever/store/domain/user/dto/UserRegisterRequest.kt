package com.naever.store.domain.user.dto

import com.naever.store.domain.user.model.User
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.hibernate.validator.constraints.Length
import org.springframework.security.crypto.password.PasswordEncoder


data class UserRegisterRequest(
    @field:NotBlank(message = "이메일은 필수입니다.")
    @field:Email(message = "이메일 형식이 아닙니다.")
    val email: String,
    val address: String,
    @field:Length(min = 8, max = 15, message = "비밀번호는 8자 이상, 15자 이하여야합니다.")
    @field:Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,15}\$",
        message = "비밀번호는 영문 대소문자, 숫자, 특수문자를 사용하세요.")
    val password: String,
    val passwordConfirm: String,
    @field:NotBlank(message = "닉네임은 필수입니다.")
    val nickname: String,
    val introduction: String
)
{
    fun to(passwordEncoder: PasswordEncoder): User
    {
        return User(
            email = email,
            address = address,
            nickname = nickname,
            introduction = introduction,
            password = passwordEncoder.encode(password)
        )
    }
}
