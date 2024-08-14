package io.github.droidkaigi.confsched.profilecard

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.preat.peekaboo.image.picker.toImageBitmap
import conference_app_2024.feature.profilecard.generated.resources.icon_default_user
import conference_app_2024.feature.profilecard.generated.resources.icon_qr
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.designsystem.theme.LocalProfileCardScreenTheme
import io.github.droidkaigi.confsched.designsystem.theme.ProvideProfileCardScreenTheme
import io.github.droidkaigi.confsched.model.ProfileCardTheme
import io.ktor.util.decodeBase64Bytes
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

const val ProfileCardFlipCardTestTag = "ProfileCardFlipCardTestTag"
const val ProfileCardFlipCardFrontTestTag = "ProfileCardFlipCardFrontTestTag"
const val ProfileCardFlipCardBackTestTag = "ProfileCardFlipCardBackTestTag"

@Composable
internal fun FlipCard(
    uiState: ProfileCardUiState.Card,
    modifier: Modifier = Modifier,
    isCreated: Boolean = false,
    isFlipped: Boolean = false,
) {
    var isFlipped by remember { mutableStateOf(isFlipped) }
    var isCreated by rememberSaveable { mutableStateOf(isCreated) }
    var initialRotation by remember { mutableStateOf(if (isFlipped) 180f else 0f) }
    val rotation = animateFloatAsState(
        targetValue = if (isFlipped) 180f else initialRotation,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing,
        ),
    )
    val isBack by remember { derivedStateOf { rotation.value >= 90 } }
    val targetRotation = animateFloatAsState(
        targetValue = 30f,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing,
        ),
    ).value
    val targetRotation2 = animateFloatAsState(
        targetValue = 0f,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing,
        ),
    ).value

    LaunchedEffect(Unit) {
        if (isCreated) {
            initialRotation = targetRotation
            delay(400)
            initialRotation = targetRotation2
            isCreated = false
        }
    }

    ProvideProfileCardScreenTheme(uiState.theme.toString()) {
        val image by remember { derivedStateOf { uiState.image?.decodeBase64Bytes()?.toImageBitmap() } }
        val cardColor = CardDefaults.cardColors(containerColor = LocalProfileCardScreenTheme.current.containerColor)
        val cardBackgrounds by remember { derivedStateOf { cardBackgroundsOf(uiState.theme.toString()) } }

        Card(
            modifier = modifier
                .testTag(ProfileCardFlipCardTestTag)
                .size(width = 300.dp, height = 380.dp)
                .clickable { isFlipped = !isFlipped }
                .graphicsLayer {
                    rotationY = rotation.value
                    cameraDistance = 12f * density
                },
            colors = cardColor,
            elevation = CardDefaults.cardElevation(10.dp),
        ) {
            if (isBack) { // Back
                FlipCardBack(
                    uiState = uiState,
                    backgroundRes = cardBackgrounds.back,
                )
            } else { // Front
                FlipCardFront(
                    uiState = uiState,
                    image = image,
                    backgroundRes = cardBackgrounds.front,
                    cardColor = cardColor,
                )
            }
        }
    }
}

@Composable
private fun FlipCardFront(
    uiState: ProfileCardUiState.Card,
    image: ImageBitmap?,
    backgroundRes: DrawableResource,
    cardColor: CardColors,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .testTag(ProfileCardFlipCardFrontTestTag)
            .fillMaxSize(),
    ) {
        Image(
            bitmap = imageResource(backgroundRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(103.dp))
            Image(
                bitmap = image
                    ?: imageResource(ProfileCardRes.drawable.icon_default_user),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(131.dp),
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = uiState.occupation ?: "",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                maxLines = 1,
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            brush = Brush.verticalGradient(listOf(Color.White, cardColor.containerColor)),
                        ),
                    ) {
                        append(uiState.nickname)
                    }
                },
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun FlipCardBack(
    @Suppress("UnusedParameter")
    uiState: ProfileCardUiState.Card,
    backgroundRes: DrawableResource,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .testTag(ProfileCardFlipCardBackTestTag)
            .fillMaxSize()
            .graphicsLayer {
                rotationY = 180f
            },
    ) {
        Image(
            bitmap = imageResource(backgroundRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(ProfileCardRes.drawable.icon_qr),
                contentDescription = null,
                modifier = Modifier.size(160.dp),
            )
        }
    }
}

internal data class ProfileCardBackground(val front: DrawableResource, val back: DrawableResource)

@Preview
@Composable
fun FlipCardFrontPreview() {
    KaigiTheme {
        Surface {
            FlipCard(
                uiState = ProfileCardUiState.Card(
                    nickname = "nickname",
                    occupation = "occupation",
                    link = null,
                    image = null,
                    theme = ProfileCardTheme.Hedgehog,
                ),
            )
        }
    }
}

@Preview
@Composable
fun FlipCardBackPreview() {
    KaigiTheme {
        Surface {
            FlipCard(
                uiState = ProfileCardUiState.Card(
                    nickname = "nickname",
                    occupation = "occupation",
                    link = null,
                    image = null,
                    theme = ProfileCardTheme.Jellyfish,
                ),
                isFlipped = true,
            )
        }
    }
}
