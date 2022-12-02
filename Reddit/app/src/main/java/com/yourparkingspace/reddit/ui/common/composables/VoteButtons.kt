package com.yourparkingspace.reddit.ui.common.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yourparkingspace.domain.model.VoteType
import com.yourparkingspace.reddit.ui.common.Dimensions
import com.yourparkingspace.reddit.ui.theme.AppColors

enum class VoteBarSize(
    val height: Dp,
    val clickableAreaSize: Dp,
    val rippleSize: Dp,
    val iconSize: Dp,
    val textSize: TextUnit
) {
    SMALL(
        height = 24.dp,
        clickableAreaSize = 24.dp,
        rippleSize = 16.dp,
        iconSize = 18.dp,
        textSize = 12.sp
    ),
    NORMAL(
        height = 38.dp,
        clickableAreaSize = 36.dp,
        rippleSize = 18.dp,
        iconSize = 24.dp,
        textSize = 14.sp
    )
}

@Composable
fun VoteButtons(
    score: String,
    vote: VoteType,
    canVote: Boolean,
    size: VoteBarSize = VoteBarSize.NORMAL,
    onVote: (Boolean) -> Unit
) {
    Row(
        Modifier
            .height(size.height)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {}),
        verticalAlignment = Alignment.CenterVertically
    ) {

        CustomIconButton(
            onClick = { if (canVote) onVote(true) },
            modifier = Modifier.size(size.clickableAreaSize),
            rippleRadius = size.rippleSize,
            rippleColor = AppColors.upvote
        ) {
            Icon(
                modifier = Modifier.size(size.iconSize),
                imageVector = Icons.Outlined.ArrowUpward,
                tint = if (vote == VoteType.UPVOTE) AppColors.upvote else Color.Gray,
                contentDescription = "Upvote"
            )
        }

        Text(
            text = score,
            color = when (vote) {
                VoteType.UPVOTE -> AppColors.upvote
                VoteType.DOWNVOTE -> AppColors.downvote
                VoteType.NONE -> Color.Gray
            },
            fontSize = size.textSize,
            modifier = Modifier.padding(horizontal = Dimensions.padding)
        )

        CustomIconButton(
            onClick = { if (canVote) onVote(false) },
            modifier = Modifier.width(size.clickableAreaSize),
            rippleRadius = size.rippleSize,
            rippleColor = AppColors.downvote
        ) {
            Icon(
                modifier = Modifier.size(size.iconSize),
                imageVector = Icons.Outlined.ArrowDownward,
                tint = if (vote == VoteType.DOWNVOTE) AppColors.downvote else Color.Gray,
                contentDescription = "Downvote",
            )
        }
    }
}

@Composable
fun CustomIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    rippleRadius: Dp,
    rippleColor: Color,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalRippleTheme provides RippleCustomTheme) {
        Box(
            modifier = modifier
                .clickable(
                    onClick = onClick,
                    enabled = enabled,
                    role = Role.Button,
                    interactionSource = interactionSource,
                    indication = rememberRipple(
                        bounded = false,
                        radius = rippleRadius,
                        color = rippleColor
                    )
                )
                .size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            val contentAlpha = if (enabled) LocalContentAlpha.current else ContentAlpha.disabled
            CompositionLocalProvider(LocalContentAlpha provides contentAlpha, content = content)
        }
    }
}

private object RippleCustomTheme : RippleTheme {

    @Composable
    override fun defaultColor() = RippleTheme.defaultRippleColor(
        contentColor = LocalContentColor.current,
        lightTheme = MaterialTheme.colors.isLight
    )

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleAlpha(
        draggedAlpha = 1.0f,
        focusedAlpha = 1.0f,
        hoveredAlpha = 1.0f,
        pressedAlpha = 1.0f
    )
}