package io.github.droidkaigi.confsched.profilecard

import conference_app_2024.feature.profilecard.generated.resources.Res
import conference_app_2024.feature.profilecard.generated.resources.card_bg_back_default
import conference_app_2024.feature.profilecard.generated.resources.card_bg_back_flamingo
import conference_app_2024.feature.profilecard.generated.resources.card_bg_back_giraffe
import conference_app_2024.feature.profilecard.generated.resources.card_bg_back_hedgehog
import conference_app_2024.feature.profilecard.generated.resources.card_bg_back_jellyfish
import conference_app_2024.feature.profilecard.generated.resources.card_bg_back_none
import conference_app_2024.feature.profilecard.generated.resources.card_bg_front_default
import conference_app_2024.feature.profilecard.generated.resources.card_bg_front_flamingo
import conference_app_2024.feature.profilecard.generated.resources.card_bg_front_giraffe
import conference_app_2024.feature.profilecard.generated.resources.card_bg_front_hedgehog
import conference_app_2024.feature.profilecard.generated.resources.card_bg_front_jellyfish
import conference_app_2024.feature.profilecard.generated.resources.card_bg_front_none

object ProfileCardRes {
    val drawable = Res.drawable
    val string = Res.string
}

/**
 * get background drawable resources for profile card by profileCardTheme string.
 * @return pair of drawable resources (for front/back)
 */
internal fun cardBackgroundsOf(profileCardTheme: String): ProfileCardBackground {
    return when (profileCardTheme) {
        "Iguana" -> ProfileCardBackground(
            ProfileCardRes.drawable.card_bg_front_default,
            ProfileCardRes.drawable.card_bg_back_default,
        )

        "Hedgehog" -> ProfileCardBackground(
            ProfileCardRes.drawable.card_bg_front_hedgehog,
            ProfileCardRes.drawable.card_bg_back_hedgehog,
        )

        "Giraffe" -> ProfileCardBackground(
            ProfileCardRes.drawable.card_bg_front_giraffe,
            ProfileCardRes.drawable.card_bg_back_giraffe,
        )

        "Flamingo" -> ProfileCardBackground(
            ProfileCardRes.drawable.card_bg_front_flamingo,
            ProfileCardRes.drawable.card_bg_back_flamingo,
        )

        "Jellyfish" -> ProfileCardBackground(
            ProfileCardRes.drawable.card_bg_front_jellyfish,
            ProfileCardRes.drawable.card_bg_back_jellyfish,
        )

        else -> ProfileCardBackground(
            ProfileCardRes.drawable.card_bg_front_none,
            ProfileCardRes.drawable.card_bg_back_none,
        )
    }
}
