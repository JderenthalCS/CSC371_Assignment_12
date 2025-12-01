@file:OptIn(ExperimentalFoundationApi::class)

package edu.farmingdale.draganddropanim_demo

import android.content.ClipData
import android.content.ClipDescription
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight



import androidx.compose.material3.Button
import androidx.compose.ui.graphics.vector.ImageVector


//private val rotation = FloatPropKey()


@Composable
fun DragAndDropBoxes(modifier: Modifier = Modifier) {
    var isPlaying by remember { mutableStateOf(false) }
    var targetOffset by remember { mutableStateOf(IntOffset(0, 0)) }
    Column(modifier = Modifier.fillMaxSize()) {

        Row(
            modifier = modifier
                .fillMaxWidth()
                .weight(0.2f)
        ) {
            val boxCount = 4
            var dragBoxIndex by remember {
                mutableIntStateOf(0)
            }

            repeat(boxCount) { index ->

                val arrowIcon = when (index) {
                    0 -> Icons.Filled.KeyboardArrowUp
                    1 -> Icons.Filled.KeyboardArrowDown
                    2 -> Icons.Filled.KeyboardArrowLeft
                    3 -> Icons.Filled.KeyboardArrowRight
                    else -> Icons.Filled.KeyboardArrowRight
                }


                val arrowDescription = when (index) {
                    0 -> "Move Up"
                    1 -> "Move Down"
                    2 -> "Move Left"
                    3 -> "Move Right"
                    else -> "Command"
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(10.dp)
                        .border(1.dp, Color.Black)
                        .dragAndDropTarget(
                            shouldStartDragAndDrop = { true },
                            target = remember {
                                object : DragAndDropTarget {
                                    override fun onDrop(event: DragAndDropEvent): Boolean {
                                        dragBoxIndex = index

                                        // Always spin when a command is dropped
                                        isPlaying = !isPlaying

                                        targetOffset = when (index) {
                                            // UP
                                            0 -> {
                                                targetOffset.copy(
                                                    y = (targetOffset.y - 80)
                                                )
                                            }
                                            // DOWN
                                            1 -> {
                                                targetOffset.copy(
                                                    y = targetOffset.y + 80
                                                )
                                            }
                                            // LEFT
                                            2 -> {
                                                targetOffset.copy(
                                                    x = (targetOffset.x - 80)
                                                )
                                            }
                                            // RIGHT
                                            3 -> {
                                                targetOffset.copy(
                                                    x = targetOffset.x + 80
                                                )
                                            }
                                            else -> targetOffset
                                        }

                                        return true
                                    }

                                }
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = arrowIcon,
                        contentDescription = arrowDescription,
                        tint = Color.Red,
                        modifier = Modifier
                            .fillMaxSize()
                            .dragAndDropSource {
                                detectTapGestures(
                                    onLongPress = {
                                        startTransfer(
                                            transferData = DragAndDropTransferData(
                                                clipData = ClipData.newPlainText("text", "")
                                            )
                                        )
                                    }
                                )
                            }
                    )

                }
            }
        }

        Button(
            onClick = {
                targetOffset = IntOffset(0, 0)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Reset to Center")
        }

        val pOffset by animateIntOffsetAsState(
            targetValue = targetOffset,
            animationSpec = tween(3000, easing = LinearEasing),
            label = "rectOffset"
        )


        val rectRotation by animateFloatAsState(
            targetValue = if (isPlaying) 360f else 0f,
            animationSpec = tween(
                durationMillis = 600,
                easing = LinearEasing
            ),
            label = "rectOffset"
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.8f)
                .background(Color.Red)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.Center)
                    .offset(pOffset.x.dp, pOffset.y.dp)
                    .background(Color.Yellow)
                    .rotate(rectRotation)
            )
        }

    }
}

