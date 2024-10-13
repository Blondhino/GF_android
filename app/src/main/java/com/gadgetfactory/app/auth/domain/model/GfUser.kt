package com.gadgetfactory.app.auth.domain.model

data class GfUser(
    val id: String,
    val email: String,
    val firstName: String,
    val profileImageUrl: String,
)
