package com.gadgetfactory.app.gadgetcenter.domain

import arrow.core.Either
import arrow.core.raise.either
import com.gadgetfactory.app.core.networking.NetworkError
import com.gadgetfactory.app.gadgetcenter.data.RoomRepository
import com.gadgetfactory.app.gadgetcenter.data.UserRepository
import com.gadgetfactory.app.gadgetcenter.model.network.RoomResponse
import com.gadgetfactory.app.gadgetcenter.model.network.UserResponse
import com.gadgetfactory.app.gadgetcenter.model.ui.GadgetCenterHeaderUiState
import com.gadgetfactory.app.gadgetcenter.model.ui.HeaderOption
import com.gadgetfactory.app.gadgetcenter.model.ui.ProfileInfoUiComponentData
import com.gadgetfactory.app.gadgetcenter.model.ui.RoomUiComponentData
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetGadgetCenterHeader(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository,
) {
    operator fun invoke(): Flow<GadgetCenterHeaderUiState> = flow {
        emit(GadgetCenterHeaderUiState.Loading)
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
): Either<GadgetCenterHeaderUiState.Error, GadgetCenterHeaderUiState.Content> = either {
    val user = userData.mapLeft { GadgetCenterHeaderUiState.Error("") }.bind()
    val rooms = roomsData.mapLeft { GadgetCenterHeaderUiState.Error("") }.bind()

    GadgetCenterHeaderUiState.Content(
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
