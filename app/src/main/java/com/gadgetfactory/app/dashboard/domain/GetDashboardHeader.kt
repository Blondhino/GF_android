package com.gadgetfactory.app.dashboard.domain

import arrow.core.Either
import arrow.core.raise.either
import com.gadgetfactory.app.core.networking.NetworkError
import com.gadgetfactory.app.dashboard.data.RoomRepository
import com.gadgetfactory.app.dashboard.data.UserRepository
import com.gadgetfactory.app.dashboard.data.model.RoomResponse
import com.gadgetfactory.app.dashboard.data.model.UserResponse
import com.gadgetfactory.app.dashboard.ui.model.DashboardHeaderUiState
import com.gadgetfactory.app.dashboard.ui.model.HeaderOption
import com.gadgetfactory.app.dashboard.ui.model.ProfileInfoUiComponentData
import com.gadgetfactory.app.dashboard.ui.model.RoomUiComponentData
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetDashboardHeader(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository,
) {
    operator fun invoke(): Flow<DashboardHeaderUiState> = flow {
        emit(DashboardHeaderUiState.Loading)
        coroutineScope {
            val user = async { userRepository.getUser() }
            val rooms = async { roomRepository.getRooms() }
            val result = mapUserAndRoomsIntoHeaderData(
                userData = user.await(),
                roomsData = rooms.await(),
            )
            emit(
                result.fold(
                    ifLeft = { it },
                    ifRight = { it },
                ),
            )
        }
    }
}

private fun mapUserAndRoomsIntoHeaderData(
    userData: Either<NetworkError, UserResponse>,
    roomsData: Either<NetworkError, RoomResponse>,
): Either<DashboardHeaderUiState.Error, DashboardHeaderUiState.Content> = either {
    val user = userData.mapLeft { DashboardHeaderUiState.Error("") }.bind()
    val rooms = roomsData.mapLeft { DashboardHeaderUiState.Error("") }.bind()

    DashboardHeaderUiState.Content(
        profileInfo = ProfileInfoUiComponentData(
            profileImageUrl = user.profileImageUrl.orEmpty(),
            profileTitle = user.name.orEmpty().substringBefore(" ").plus("'s House"),
        ),
        availableRooms = rooms.rooms?.map { room ->
            RoomUiComponentData(
                roomName = room.roomName.orEmpty(),
                roomId = room.roomId.orEmpty(),
                isSelected = false,
            )
        } ?: listOf(),

        addNewDevice = HeaderOption.AddDevice,
        notificationsOption = HeaderOption.Notifications,
        editRoomsOption = HeaderOption.EditRooms,
    )
}
