package com.wordscroll.core

import com.wordscroll.core.DestinationRoute.PassedKey.POEM_INDEX
import com.wordscroll.core.DestinationRoute.PassedKey.POET_NAME

object DestinationRoute {
    const val HOME_SCREEN_ROUTE = "home_screen_route"
    const val SAVED_ROUTE = "saved_route"
    const val SETTINGS_ROUTE = "settings_route"

    const val POET_PROFILE_ROUTE = "poet_profile_route"
    const val FORMATTED_POET_PROFILE_ROUTE = "$POET_PROFILE_ROUTE/{$POET_NAME}"

    const val POEM_READER_ROUTE = "poem_reader_route"
    const val FORMATTED_POEM_READER_ROUTE = "$POEM_READER_ROUTE/{$POET_NAME}/{$POEM_INDEX}"

    object PassedKey {
        const val POET_NAME = "poet_name"
        const val POEM_INDEX = "poem_index"
    }
}
