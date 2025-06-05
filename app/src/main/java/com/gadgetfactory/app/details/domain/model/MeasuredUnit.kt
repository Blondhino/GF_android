package com.gadgetfactory.app.details.domain.model

import com.gadgetfactory.app.core.ui.components.ImageType
import com.gadgetfactory.app.details.domain.model.MeasurementUnit.Humidity
import com.gadgetfactory.app.details.domain.model.MeasurementUnit.Pressure
import com.gadgetfactory.app.details.domain.model.MeasurementUnit.Temperature
import com.gadgetfactory.app.details.domain.model.MeasurementUnit.Unknown

data class MeasuredUnit(
    val unitName: String,
    val value: String,
    val unitType: MeasurementUnit,
    val image: ImageType.Resource,
)

sealed class MeasurementUnit(val name: String) {
    data object Temperature : MeasurementUnit("temperature")
    data object Humidity : MeasurementUnit("humidity")
    data object Pressure : MeasurementUnit("pressure")
    data object Unknown : MeasurementUnit("unknown")
}

fun String.toMeasurementUnit(): MeasurementUnit = when (this) {
    Temperature.name -> Temperature
    Humidity.name -> Humidity
    Pressure.name -> Pressure
    else -> Unknown
}
