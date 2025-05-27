package com.gadgetfactory.app.core.routes

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Resource("/v1")
object V1 {

    @Serializable
    @Resource("/user")
    class GetUser(
        val parent: V1 = V1,
    )

    @Serializable
    @Resource("/get-rooms")
    class GetRooms(
        val parent: V1 = V1,
    )

    @Serializable
    @Resource("/register-device")
    class RegisterDevice(
        val parent: V1 = V1,
    )
}
