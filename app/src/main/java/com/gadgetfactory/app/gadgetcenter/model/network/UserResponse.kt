package com.gadgetfactory.app.gadgetcenter.model.network

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: String?,
    val email: String?,
    val profileImageUrl: String?,
    val name: String?,
)
