package com.example.coursework

import android.os.Bundle
import android.util.Size
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.DrawTransform
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coursework.core.Directions
import com.example.coursework.logic.*
import com.example.coursework.ui.theme.CourseworkTheme
import com.example.coursework.ui.theme.Shapes
import java.lang.Math.abs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CourseworkTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = Color.White
                ) {
                    DrawGrid()
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DrawGrid() {
        var gameGrid by remember { mutableStateOf(createStartGrid()) }
        var color by remember { mutableStateOf(Color(0xffebe7e1)) }
        var direction by remember { mutableStateOf(-1) }
        Box (
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            change.consumeAllChanges()
                            val (x, y) = dragAmount
                            if (kotlin.math.abs(x) > kotlin.math.abs(y)) {
                                when {
                                    x > 0 -> direction = 0
                                    x < 0 -> direction = 1
                                }
                            } else when {
                                y > 0 -> direction = 2
                                y < 0 -> direction = 3
                            }
                        },
                        onDragEnd = {
                            when (direction) {
                                0 -> gameGrid = moveGrid(Directions.RIGHT)
                                1 -> gameGrid = moveGrid(Directions.LEFT)
                                2 -> gameGrid = moveGrid(Directions.DOWN)
                                3 -> gameGrid = moveGrid(Directions.UP)
                            }
                        }
                    )
                }
        ) {
            Column() {
                LazyVerticalGrid(
                    cells = GridCells.Fixed(4),
                    contentPadding = PaddingValues(
                        start = 10.dp,
                        top = 16.dp,
                        end = 10.dp,
                        bottom = 16.dp
                    ),
                    content = {
                        items(gameGrid.size) { index ->
                            val value = gameGrid.toList()[index].second
                            color = getCellColor(value)
                            Card(
                                backgroundColor = color,
                                modifier = Modifier
                                    .size(90.dp)
                                    .padding(2.dp)
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    modifier = Modifier.wrapContentSize(Alignment.Center),
                                    text = if (value != null) "$value" else "",
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                )
            }
        }
    }




@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DrawGrid()
}