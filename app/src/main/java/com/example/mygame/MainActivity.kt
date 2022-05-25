package com.example.mygame

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.mygame.ui.theme.MyGameTheme
import kotlin.math.abs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyGameTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    PreviewScreen()
                }
            }
        }
    }
    @Composable
    fun PreviewScreen() {
        ConstraintLayout {
            val (text, button) = createRefs()
            val guideLine = createGuidelineFromTop(0.5f)
            Text(
                text = "2048",
                fontSize = 100.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(20.dp)
                    .constrainAs(text) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
            Button(onClick = {
                startActivity(Intent(this@MainActivity, Game::class.java))
            },
                modifier = Modifier
                    .width(200.dp)
                    .height(80.dp)
                    .constrainAs(button) {
                        top.linkTo(guideLine)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.wrapContent
                    }) {
                Text(text = "START GAME")
            }
        }
    }
}


class Game : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyGameTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Board()
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Board() {
    Box(modifier = Modifier.fillMaxSize()) {
        var grid by remember { mutableStateOf(Grid().createGrid()) }
        var cell by remember { mutableStateOf(grid[0].getValue().toString())}
        var direction by remember { mutableStateOf(-1) }
        var color by remember { mutableStateOf(Color(0xFFFFFFFF)) }
        var gameLost by remember { mutableStateOf(false) }
        var gameWon by remember { mutableStateOf(false) }
        Column() {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consumeAllChanges()

                                val (x, y) = dragAmount
                                if (abs(x) > abs(y)) {
                                    when {
                                        x > 0 -> {
                                            //right
                                            direction = 0
                                        }
                                        x < 0 -> {
                                            // left
                                            direction = 1
                                        }
                                    }
                                } else {
                                    when {
                                        y > 0 -> {
                                            // down
                                            direction = 2
                                        }
                                        y < 0 -> {
                                            // up
                                            direction = 3
                                        }
                                    }
                                }

                            },
                            onDragEnd = {
                                when (direction) {
                                    0 -> {
                                        grid = Grid().moveGrid(Direction.RIGHT, grid)
                                        if (grid.isEmpty()) gameLost = true
                                        if (grid.any { it.getValue() == 2048 }) gameWon = true
                                    }
                                    1 -> {
                                        grid = Grid().moveGrid(Direction.LEFT, grid)
                                        if (grid.isEmpty()) gameLost = true
                                        if (grid.any { it.getValue() == 2048 }) gameWon = true
                                    }
                                    2 -> {
                                        grid = Grid().moveGrid(Direction.DOWN, grid)
                                        if (grid.isEmpty()) gameLost = true
                                        if (grid.any { it.getValue() == 2048 }) gameWon = true
                                    }
                                    3 -> {
                                        grid = Grid().moveGrid(Direction.UP, grid)
                                        if (grid.isEmpty()) gameLost = true
                                        if (grid.any { it.getValue() == 2048 }) gameWon = true
                                    }
                                }
                            }
                        )
                    }
            ) {
                Column() {
                    LazyVerticalGrid(cells = GridCells.Fixed(4),
                        contentPadding = PaddingValues(
                            start = 12.dp,
                            top = 16.dp,
                            end = 12.dp,
                            bottom = 16.dp
                        ),
                        content = {
                            items(grid.size) { index ->
                                when (grid[index].getValue()) {
                                    0 -> color = Color.White
                                    2 -> color = Color(0xFFDB9BFD)
                                    4 -> color = Color(0xFFC352FF)
                                    8 -> color = Color(0xFFA038D8)
                                    16 -> color = Color(0xFF8817C5)
                                    32 -> color = Color(0xFF7300B1)
                                    64 -> color = Color(0xFF4F0F70)
                                    128 -> color = Color(0xFF390A52)
                                    256 -> color = Color(0xFF29083A)
                                    512 -> color = Color(0xFF160320)
                                    1024 -> color = Color(0xFF11011A)
                                    2048 -> color = Color(0xFF000000)
                                }
                                Card(
                                    backgroundColor = color,
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .fillMaxWidth(),
                                    elevation = 8.dp
                                ) {
                                    cell = grid[index].getValue().toString()
                                    Text(
                                        text = cell,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 30.sp,
                                        color = Color(0xFFFFFFFF),
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(16.dp)
                                    )

                                }
                            }
                        })
                    Button(
                        onClick = {
                            MainActivity().recreate()
                        },
                        modifier = Modifier
                            .width(150.dp)
                            .height(60.dp)
                            .align(CenterHorizontally)
                    ) {
                        Text(text = "RESTART")
                    }
                }
                Box(Modifier.fillMaxSize()) {
                    if (gameLost) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "YOU",
                                fontSize = 60.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(20.dp),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "HAVE",
                                fontSize = 60.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(20.dp),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "LOST",
                                fontSize = 60.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(20.dp),
                                textAlign = TextAlign.Center
                            )
                            Button(
                                onClick = {
                                    MainActivity().recreate()
                                },
                                modifier = Modifier
                                    .width(200.dp)
                                    .height(80.dp)
                            ) {
                                Text(text = "TRY AGAIN")
                            }
                        }
                    }
                    if (gameWon) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "YOU",
                                fontSize = 60.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(20.dp),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "HAVE",
                                fontSize = 60.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(20.dp),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "WON",
                                fontSize = 60.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(20.dp),
                                textAlign = TextAlign.Center
                            )
                            Button(
                                onClick = {
                                    MainActivity().recreate()
                                },
                                modifier = Modifier
                                    .width(200.dp)
                                    .height(80.dp)
                            ) {
                                Text(text = "TRY AGAIN")
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Board()
}