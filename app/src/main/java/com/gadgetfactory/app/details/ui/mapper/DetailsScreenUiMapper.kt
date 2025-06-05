package com.gadgetfactory.app.details.ui.mapper

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import arrow.core.Either
import com.gadgetfactory.app.R
import com.gadgetfactory.app.core.dictionary.Dictionary
import com.gadgetfactory.app.core.networking.NetworkError
import com.gadgetfactory.app.core.ui.components.ImageType.Resource
import com.gadgetfactory.app.details.domain.model.DeviceInfographic
import com.gadgetfactory.app.details.domain.model.MeasurementUnit.Humidity
import com.gadgetfactory.app.details.domain.model.MeasurementUnit.Pressure
import com.gadgetfactory.app.details.domain.model.MeasurementUnit.Temperature
import com.gadgetfactory.app.details.domain.model.MeasurementUnit.Unknown
import com.gadgetfactory.app.details.domain.model.SensorData
import com.gadgetfactory.app.details.domain.model.SensorMessage
import com.gadgetfactory.app.details.domain.model.toMeasurementUnit
import com.gadgetfactory.app.details.ui.interaction.DetailsScreenState
import com.gadgetfactory.app.details.ui.interaction.DetailsScreenState.Content
import com.gadgetfactory.app.details.ui.interaction.DetailsScreenState.Error

class DetailsScreenUiMapper(
    private val dictionary: Dictionary,
) {
    fun map(measurements: Either<NetworkError, SensorMessage>): DetailsScreenState = measurements.fold(
        ifLeft = { Error },
        ifRight = { Content(infographics = it.data.toInfographics()) },
    )

    private fun List<SensorData>.toInfographics(): List<DeviceInfographic> = map { data ->
        DeviceInfographic(
            value = "${data.value} ${data.unit}",
            description = mapDescription(data.name),
            icon = mapIcon(data.name),
        )
    }

    private fun mapIcon(name: String): Resource = when (name.toMeasurementUnit()) {
        is Temperature -> Resource(R.drawable.ic_temperature)
        is Humidity -> Resource(R.drawable.ic_humidity)
        is Pressure -> Resource(R.drawable.ic_pressure)
        is Unknown -> Resource(R.drawable.ic_unknown_value)
    }

    private fun mapDescription(name: String): String = when (name.toMeasurementUnit()) {
        is Temperature -> dictionary.getString(R.string.measure_unit_temperature)
        is Humidity -> dictionary.getString(R.string.measure_unit_humidity)
        is Pressure -> dictionary.getString(R.string.measure_unit_pressure)
        is Unknown -> name.capitalize(Locale.current)
    }
}
