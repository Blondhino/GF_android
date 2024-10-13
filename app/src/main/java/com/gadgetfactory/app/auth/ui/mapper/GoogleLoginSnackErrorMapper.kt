package com.gadgetfactory.app.auth.ui.mapper

import com.gadgetfactory.app.R
import com.gadgetfactory.app.core.dictionary.Dictionary
import com.gadgetfactory.app.ui.global.snack.SnackbarPayload
import com.gadgetfactory.app.ui.global.snack.SnackbarType.ErrorSnackbar

class GoogleLoginSnackErrorMapper(
    private val dictionary: Dictionary,
) {
    fun map() = SnackbarPayload(
        title = dictionary.getString(R.string.general_something_went_wrong),
        message = dictionary.getString(R.string.general_something_went_wrong_message),
        type = ErrorSnackbar,
        cancelable = true,
    )
}
