package com.example.bookYourSeat.user

object UserConst {
    const val INVALID_LOGIN_REQUEST: String = "이메일 또는 비밀번호가 올바르지 않습니다."
    const val INVALID_EMAIL: String = "가입되지 않은 이메일입니다."
    const val ADDRESS_NOT_FOUND: String = "해당 주소를 찾을 수 없습니다."
    const val ALREADY_JOIN_EMAIL: String = "이미 가입한 이메일입니다."
    const val ADDRESS_NOT_OWNED: String = "로그인한 유저의 주소가 아닙니다."
    const val MAIL_CREATION_FAILED: String = "메일을 생성할 수 없습니다."
    const val INVALID_CERT_CODE: String = "인증 코드가 올바르지 않습니다."
    const val EMAIL_NOT_VERIFIED: String = "인증되지 않은 이메일입니다."

    const val EMAIL_CERT_CODE_KEY: String = "emailCertCode:"
    const val VERIFIED_EMAIL_KEY: String = "email:"
}
