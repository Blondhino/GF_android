package com.gadgetfactory.app.dashboard.model.network

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: String?,
    val email: String?,
    val profileImageUrl: String?,
    val name: String?,
)
