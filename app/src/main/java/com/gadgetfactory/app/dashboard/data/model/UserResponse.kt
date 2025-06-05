package com.gadgetfactory.app.dashboard.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: String?,
    val email: String?,
    val profileImageUrl: String?,
    val name: String?,
)
