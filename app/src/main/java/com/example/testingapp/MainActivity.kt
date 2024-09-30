package com.example.testingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.testingapp.ui.theme.TestingAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestingAppTheme {
                ExpandableList(
                    expandableContentInList =
                    listOf(
                        Pair(
                            {
                                Text(
                                    text = "Заголовок 1",
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            },
                            List(10) { index ->
                                { Text(index.toString()) }
                            }
                        ),
                        Pair(
                            {
                                Text(
                                    text = "Заголовок 2",
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            },
                            List(10) { index ->
                                { Text(index.toString()) }
                            }
                        ),
                        Pair(
                            {
                                Text(
                                    text = "Заголовок 3",
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            },
                            List(10) { index ->
                                { Text(index.toString()) }
                            }
                        )
                    )

                )
            }
        }
    }
}

@Composable
fun ExpandableList(
    onExpandOrCollapseClick: (() -> Unit)? = null,
    expandableContentInList: List<Pair<@Composable () -> Unit, List<@Composable () -> Unit>>>,
) {
    val expanded = remember { List(expandableContentInList.size){false}.toMutableStateList() }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        expanded.forEachIndexed { index, value ->
            item(key = "title $index") {
                Row(modifier = Modifier.animateItem()) {
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .align(Alignment.CenterVertically)
                            .weight(1f)
                    ) {
                        expandableContentInList[index].first()
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
                    items = expandableContentInList[index].second,
                    key = { _, item -> "$item$index" }) { _, item ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .animateItem(
                            )
                    ) {
                        item()
                    }
                }
            }
        }
    }
}

private const val AnimationDurationMillis = 200

@Composable
@Preview(showBackground = true)
internal fun ExpandableCardPreview() {
    TestingAppTheme {
        ExpandableList(
            expandableContentInList = listOf(
                Pair(
                    {
                        Text(
                            text = "Заголовок 1",
                            modifier = Modifier.fillMaxWidth(),
                        )
                    },
                    List(10) { index ->
                        { Text(index.toString()) }
                    }
                ),
                Pair(
                    {
                        Text(
                            text = "Заголовок 2",
                            modifier = Modifier.fillMaxWidth(),
                        )
                    },
                    List(10) { index ->
                        { Text(index.toString()) }
                    }
                ),
                Pair(
                    {
                        Text(
                            text = "Заголовок 3",
                            modifier = Modifier.fillMaxWidth(),
                        )
                    },
                    List(10) { index ->
                        { Text(index.toString()) }
                    }
                )
            )
        )
    }
}

@Composable
@Preview(showBackground = true)
internal fun ExpandableCardSkeletonPreview() {
    TestingAppTheme {
        ExpandableList(
            expandableContentInList = listOf()
        )
    }
}