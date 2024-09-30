package com.example.testingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.testingapp.ui.theme.TestingAppTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestingAppTheme {
                ExpandableList(
                    modifier = Modifier.fillMaxWidth(),
                    fixedContent = {
                        Text(
                            text = "Смогу ли я просматривать видео с камер, находясь в другом городе или за границей?",
                            modifier = Modifier.fillMaxWidth(),
                        )
                    },
                    expandableContentInList = List(10) { index ->
                        index.toString()
                        /*{
                            Text(
                                text = index.toString(),
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }*/
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpandableListV2(
    modifier: Modifier = Modifier,
    onExpandOrCollapseClick: (() -> Unit)? = null,
    fixedContent: @Composable () -> Unit,
    expandableContentInList: List<String>,
) {
    var expanded by remember { mutableStateOf(false) }//TODO верни как было
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    Card(
        modifier = modifier.bringIntoViewRequester(bringIntoViewRequester),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, top = 12.dp, end = 12.dp, bottom = 12.dp)
                .fillMaxWidth()
        ) {
            Row {
                Box(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .align(Alignment.CenterVertically)
                        .weight(1f)
                ) {
                    fixedContent()
                }
                Crossfade(
                    expanded,
                    animationSpec = tween(durationMillis = AnimationDurationMillis),
                    label = "ExpandableCardArrowAnimation"
                ) {
                    val icon =
                        if (it) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown
                    Icon(
                        imageVector = icon,
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(36.dp)
                            .clickable {
                                expanded = !(expanded)
                                onExpandOrCollapseClick?.invoke()
                            }
                            .background(Color.Gray)
                            .padding(8.dp),
                        contentDescription = "",
                        tint = Color.Red,
                    )
                }
            }
            LazyColumn {
                if (expanded) {
                    itemsIndexed(
                        items = expandableContentInList,
                        key = { _, item -> item }) { index, item ->
                        var visible by remember { mutableStateOf(!expanded) }
                        LaunchedEffect(Unit) {
                            delay(100L * index)
                            visible = !visible
                        }
                        AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically(),
                            modifier = Modifier.animateItem()
                        ) {
                            Text(item)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpandableList(
    modifier: Modifier = Modifier,
    onExpandOrCollapseClick: (() -> Unit)? = null,
    fixedContent: @Composable () -> Unit,
    expandableContentInList: List<String>,
) {
    val expanded = remember { mutableStateListOf(false, false, false) }//TODO верни как было
    var speedVariant by remember { mutableIntStateOf(0) }
    var ratioVariant by remember { mutableIntStateOf(0) }
    var animationVariant by remember { mutableIntStateOf(0) }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        item {
            Row {
                Button(onClick = { speedVariant = 0 }) {
                    Text("Speed 0")
                }
                Button(onClick = { speedVariant = 1 }) {
                    Text("Speed 1")
                }
                Button(onClick = { speedVariant = 2 }) {
                    Text("Speed 2")
                }
                Text("Speed $speedVariant")
            }
        }
        item {
            Row {
                Button(onClick = { ratioVariant = 0 }) {
                    Text("ratio 0")
                }
                Button(onClick = { ratioVariant = 1 }) {
                    Text("ratio 1")
                }
                Button(onClick = { ratioVariant = 2 }) {
                    Text("ratio 2")
                }
                Text("ratio $ratioVariant")
            }
        }
        item {
            Row {
                Button(onClick = { animationVariant = 0 }) {
                    Text("spring")
                }
                Button(onClick = { animationVariant = 1 }) {
                    Text("tween")
                }
                Text(if(animationVariant == 0) "spring" else "tween")
            }
        }
        expanded.forEachIndexed{index, value ->
            item {
                Row {
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .align(Alignment.CenterVertically)
                            .weight(1f)
                    ) {
                        fixedContent()
                    }
                    Crossfade(
                        expanded,
                        animationSpec = tween(durationMillis = AnimationDurationMillis),
                        label = "ExpandableCardArrowAnimation"
                    ) {
                        val icon =
                            if (it[index]) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown
                        Icon(
                            imageVector = icon,
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(36.dp)
                                .clickable {
                                    expanded[index] = !(expanded[index])
                                    onExpandOrCollapseClick?.invoke()
                                }
                                .background(Color.Gray)
                                .padding(8.dp),
                            contentDescription = "",
                            tint = Color.Red,
                        )
                    }
                }
            }
            if (value) {
                itemsIndexed(
                    items = expandableContentInList,
                    key = { _, item -> "$item$index" }) { _, item ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .animateItem(
                                fadeInSpec =
                                when (animationVariant) {
                                     0 -> spring(
                                        dampingRatio = setAnimationRatio(ratioVariant),
                                        stiffness = setAnimationSpeed(speedVariant)
                                    )
                                    else -> tween(durationMillis = setAnimationSpeed(speedVariant).toInt(), easing = FastOutSlowInEasing)
                                },
                                placementSpec =
                                when (animationVariant) {
                                    0 -> spring(
                                        dampingRatio = setAnimationRatio(ratioVariant),
                                        stiffness = setAnimationSpeed(speedVariant)
                                    )
                                    else -> tween(durationMillis = setAnimationSpeed(speedVariant).toInt(), easing = FastOutSlowInEasing)
                                }
                            )
                    ) {
                        Text(text = item)
                    }
                }
            }
        }
    }
}

private fun setAnimationRatio(ratioIndex:Int) = when (ratioIndex) {
    0 -> Spring.DampingRatioLowBouncy
    1 -> Spring.DampingRatioMediumBouncy
    else -> Spring.DampingRatioHighBouncy
}

private fun setAnimationSpeed(speedIndex:Int) = when (speedIndex) {
    0 -> Spring.StiffnessVeryLow
    1 -> Spring.StiffnessMedium
    else -> Spring.StiffnessHigh
}

private const val AnimationDurationMillis = 200

@Composable
@Preview(showBackground = true)
internal fun ExpandableCardPreview() {
    TestingAppTheme {
        ExpandableList(
            modifier = Modifier.fillMaxWidth(),
            fixedContent = {
                Text(
                    text = "Смогу ли я просматривать видео с камер, находясь в другом городе или за границей?",
                )
            },
            expandableContentInList = listOf(
                "Присматривайте за домом и участком. Если кто-то проникнет во двор, вы тут же об этом узнаете.",
                "Присматривайте за домом и участком. Если кто-то проникнет во двор, вы тут же об этом узнаете.",
                "Присматривайте за домом и участком. Если кто-то проникнет во двор, вы тут же об этом узнаете.",
            )
        )
    }
}

@Composable
@Preview(showBackground = true)
internal fun ExpandableCardSkeletonPreview() {
    TestingAppTheme {
        ExpandableList(
            modifier = Modifier.fillMaxWidth(),
            fixedContent = {},
            expandableContentInList = listOf()
        )
    }
}