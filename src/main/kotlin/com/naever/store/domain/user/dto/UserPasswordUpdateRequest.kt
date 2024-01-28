package com.naever.store.domain.user.dto

import jakarta.validation.constraints.Pattern
import org.hibernate.validator.constraints.Length

data class UserPasswordUpdateRequest(
    val passwordConfirm: String,
    @field:Length(min = 8, max = 15, message = "비밀번호는 8자 이상, 15자 이하여야합니다.")
    @field:Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,15}$",
        message = "비밀번호는 영문 대소문자, 숫자, 특수문자를 사용하세요.")
    val newPassword: String
)