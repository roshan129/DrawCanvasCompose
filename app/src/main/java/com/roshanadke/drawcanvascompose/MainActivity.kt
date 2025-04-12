package com.roshanadke.drawcanvascompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.roshanadke.drawcanvascompose.ui.theme.DrawCanvasComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DrawCanvasComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel: DrawingViewModel by viewModels()
                    val state by viewModel.state.collectAsState()

                    Column(
                        modifier = Modifier.padding(innerPadding).background(Color.LightGray),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        DrawingCanvas(
                            paths = state.paths,
                            currentPath = state.currentPath,
                            onAction = viewModel::onAction,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            allColors.fastForEach { color ->
                                val isSelected = state.selectedColor == color
                                Box(
                                    modifier = Modifier
                                        .graphicsLayer {
                                            val scale = if (isSelected) 1.2f else 1f
                                            scaleX = scale
                                            scaleY = scale
                                        }
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(color = color)
                                        .border(
                                            width = 2.dp,
                                            color = if (isSelected) {
                                                Color.White
                                            } else {
                                                Color.Transparent
                                            },
                                            shape = CircleShape
                                        )
                                        .padding(4.dp)
                                        .clickable {
                                            viewModel.onAction(DrawingAction.OnSelectColor(color))
                                        }
                                )
                            }
                        }

                        Button(
                            onClick = {
                                viewModel.onAction(DrawingAction.OnClearCanvasClick)
                            }
                        ) {
                            Text("Clear Canvas")
                        }
                    }
                }
            }
        }
    }
}