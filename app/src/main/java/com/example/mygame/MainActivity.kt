package com.example.mygame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mygame.ui.theme.MyGameTheme
import kotlin.math.abs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyGameTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    PreviewScreen()
                }
            }
        }
    }
}
class Game : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyGameTheme {
                // A surface container using the 'background' color from the theme
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


@Composable
fun PreviewScreen() {
//    val navController = rememberNavController()
//    NavHost(navController = navController, startDestination = "menu") {
//        composable("menu") { MainActivity() }
//        composable("Board") { Board() }
//    }
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
                         MainActivity().finish()
            /* navController.navigate("Board")  {
            popUpTo("menu") { inclusive = true }
        }*/ },
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
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SquareGrid() {
    val grid = Grid().createGrid()
    val values = mutableListOf<Int?>()
    grid.forEach { values.add(it.getValue()) }
    LazyVerticalGrid(cells = GridCells.Fixed(4),
        contentPadding = PaddingValues(
        start = 12.dp,
        top = 16.dp,
        end = 12.dp,
        bottom = 16.dp
    ),
        content = {
            items(values.size) {
                index ->
                Card(
                    backgroundColor = Color(0xEDFD5F5F),
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(),
                    elevation = 8.dp,
                ) {
                    Text(
                        text = values[index].toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        color = Color(0xFFFFFFFF),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        } )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Board() {
    Box(modifier = Modifier.fillMaxSize()) {
        var grid by remember { mutableStateOf(Grid().createGrid()) }
        var cell by remember { mutableStateOf(grid[0].getValue().toString())}
        var direction by remember { mutableStateOf(-1) }
        var color by remember { mutableStateOf(Color(0xFFFFFFFF)) }
        Box(
            Modifier/*.offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }*/
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
                                    //right swipe code here
                                    grid = Grid().moveGrid(Direction.RIGHT, grid)
                                }
                                1 -> {
                                    grid = Grid().moveGrid(Direction.LEFT, grid)
                                    // left swipe code here
                                }
                                2 -> {
                                    grid = Grid().moveGrid(Direction.DOWN, grid)
                                    // down swipe code here
                                }
                                3 -> {
                                    grid = Grid().moveGrid(Direction.UP, grid)
                                    // up swipe code here
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
                        items(grid.size) {
                                index ->
                            val cellValue = grid[index].getValue()
                            //var Color = Color(0xFFAD3B3B)
                            when(cellValue) {
                                0 -> color = Color.White
                                2 -> color = Color(0xFFDB9BFD)
                                4 -> color = Color(0xFFC352FF)
                                8 -> color = Color(0xFFA038D8)
                                16 -> color = Color(0xFF8817C5)
                                32 -> color = Color(0xFF7300B1)
                                64 -> color = Color(0xFF4F0F70)
                                128 -> color = Color(0xFF390A52)
                                256 -> color = Color(0xFF29083A)
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
                    } )
                Text( text = direction.toString())
            }

        }

    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GamingBoard() {
    var grid by remember { mutableStateOf(Grid().createGrid() )}
    var direction by remember { mutableStateOf(-1) }
    //val values = mutableListOf<Int?>()
    //grid.forEach { values.add(it.getValue()) }
    Box(
        Modifier
            .background(Color(0xFF738DAD))
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
                                //right swipe code here
                                grid = Grid().moveGrid(Direction.RIGHT, grid)
                            }
                            1 -> {
                                grid = Grid().moveGrid(Direction.LEFT, grid)
                                // left swipe code here
                            }
                            2 -> {
                                grid = Grid().moveGrid(Direction.DOWN, grid)
                                // down swipe code here
                            }
                            3 -> {
                                grid = Grid().moveGrid(Direction.UP, grid)
                                // up swipe code here
                            }
                        }
                    }
                )
            }
        ,
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier
            .border(1.dp, Color.Black, RoundedCornerShape(5.dp))
        )

        {
            Text(text = grid.toString())
        }
        LazyVerticalGrid(cells = GridCells.Fixed(4),
            contentPadding = PaddingValues(
                start = 12.dp,
                top = 16.dp,
                end = 12.dp,
                bottom = 16.dp
            ),
            content = {
                items(grid.size) {
                        index ->
                    Card(
                        backgroundColor = Color(0xFFFF5C5C),
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth(),
                        elevation = 8.dp,
                    ) {
                        Text(
                            text = grid[index].getValue().toString(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            color = Color(0xFFFFFFFF),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            } )
        }
}


//https://stackoverflow.com/questions/65573008/detect-swipe-direction-on-jetpack-compose
@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun SwipeTest() {
    Box(modifier = Modifier.fillMaxSize()) {
        var text by remember { mutableStateOf("Box") }
        var grid by remember { mutableStateOf(Grid().createGrid()) }
        var cell by remember { mutableStateOf(grid[0].getValue().toString())}
        var direction by remember { mutableStateOf(-1) }
        Box(
            Modifier/*.offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }*/
                .size(500.dp, 500.dp)
                .background(Color.Gray)
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
                                    //right swipe code here
                                    text = "Right"
                                    grid = Grid().moveGrid(Direction.RIGHT, grid)
                                }
                                1 -> {
                                    text = "Left"
                                    grid = Grid().moveGrid(Direction.LEFT, grid)
                                    // left swipe code here
                                }
                                2 -> {
                                    text = "Down"
                                    grid = Grid().moveGrid(Direction.DOWN, grid)
                                    // down swipe code here
                                }
                                3 -> {
                                    text = "Up"
                                    grid = Grid().moveGrid(Direction.UP, grid)
                                    // up swipe code here
                                }
                            }
                        }
                    )
                }
        ) {
            Column() {
                Text(text = text, modifier = Modifier.background(Color.Green))
                Text(text = grid.toString(), modifier = Modifier.background(Color.Cyan))
                LazyVerticalGrid(cells = GridCells.Fixed(4),
            contentPadding = PaddingValues(
                start = 12.dp,
                top = 16.dp,
                end = 12.dp,
                bottom = 16.dp
            ),
            content = {
                items(grid.size) {
                        index ->
                    Card(
                        backgroundColor = Color(0xFFFF5C5C),
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth(),
                        elevation = 8.dp
                    ) {
                        cell = grid[index].getValue().toString()
                        Text(text = cell)

                    }/*{
                        Text(
                            text = grid[index].getValue().toString(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            color = Color(0xFFFFFFFF),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    }*/
                }
            } )
            }

        }

    }
}

@Composable
fun Playground() {
    Card(modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Color(0xFF738DAD),
        elevation = 10.dp
         ) {
        GamingBoard()
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
//        SwipeTest()
}