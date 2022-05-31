package com.example.coursework

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coursework.core.Cell
import com.example.coursework.core.Directions
import com.example.coursework.logic.*
import com.example.coursework.ui.theme.CourseworkTheme
import com.example.coursework.ui.theme.Shapes
import kotlinx.coroutines.launch
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


@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DrawGrid() {
    val game by remember { mutableStateOf(Game()) }
    var gameOver by remember { mutableStateOf(false) }
    var gameScore by remember { mutableStateOf(0) }
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
                            0 -> {
                                game.moveGrid(Directions.RIGHT)
                            }
                            1 -> {
                                game.moveGrid(Directions.LEFT)
                            }
                            2 -> {
                                game.moveGrid(Directions.DOWN)

                            }
                            3 -> {
                                game.moveGrid(Directions.UP)
                            }
                        }
                        gameScore = game.getScore()
                        gameOver = game.getGameOver()
                    }
                )
            }
        ) {
            Column {
                LazyVerticalGrid(
                    cells = GridCells.Fixed(4),
                    contentPadding = PaddingValues(
                        start = 10.dp,
                        top = 16.dp,
                        end = 10.dp,
                        bottom = 16.dp
                    ),
                    content = {
                        items(game.getGrid().size) { index ->
                            val value = game.getGrid().toList()[index].second
                            color = Game.getCellColor(value)
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    ScoreBox(gameScore = gameScore)

                    Button(
                        onClick = {
                            game.createStartGrid()
                            gameScore = 0
                            game.setScore(0)
                            gameOver = false
                            game.setGameOver(false)
                        },
                        modifier = Modifier
                            .wrapContentSize(Alignment.Center),
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xffffb278)
                        )
                    ) {
                        Text(
                            text = "Restart",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
                if (gameOver) GameOverAttention()
            }
    }
}


@Composable
fun ScoreBox(gameScore: Int) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .background(Color(0xfff77b61))
            .wrapContentSize(
                Alignment.Center
            )
    ) {
        Text(
            text = "$gameScore",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
fun GameOverAttention() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
           modifier = Modifier
               .fillMaxSize()
               .padding(10.dp)
               .background(Color(0xffffc22e))
               .wrapContentSize(Alignment.Center)
        ) {
            Text(
                text = "GAME OVER! Press the restart button to start over!",
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}




@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DrawGrid()
}