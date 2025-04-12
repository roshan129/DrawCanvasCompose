package com.roshanadke.drawcanvascompose

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class DrawingState(
    val selectedColor: Color = Color.Black,
    val currentPath: PathData? = null,
    val paths: List<PathData> = emptyList(),
)

val allColors = listOf(
    Color.Red,
    Color.Green,
    Color.Blue,
    Color.Yellow,
    Color.Cyan,
    Color.Magenta,
    Color.Black,
)

data class PathData(
    val id: String,
    val color: Color,
    val path: List<Offset>
)

sealed interface DrawingAction {
    data object OnNewPathStart : DrawingAction
    data class OnDraw(val offset: Offset) : DrawingAction
    data object OnPathEnd : DrawingAction
    data class OnSelectColor(val color: Color) : DrawingAction
    data object OnClearCanvasClick : DrawingAction
}

class DrawingViewModel : ViewModel() {

    private val _state = MutableStateFlow(DrawingState())
    val state: StateFlow<DrawingState> = _state.asStateFlow()

    fun onAction(action: DrawingAction) {
        when (action) {
            DrawingAction.OnClearCanvasClick -> {
                clearCanvasClick()
            }

            is DrawingAction.OnDraw -> {
                onDraw(action.offset)
            }

            DrawingAction.OnNewPathStart -> {
                onNewPathStart()
            }

            DrawingAction.OnPathEnd -> {
                onPathEnd()
            }

            is DrawingAction.OnSelectColor -> {
                onSelectColor(action.color)
            }
        }
    }

    private fun onSelectColor(color: Color) {
        _state.value = _state.value.copy(selectedColor = color)
    }

    private fun onNewPathStart() {
        _state.update {
            it.copy(
                currentPath = PathData(
                    id = System.currentTimeMillis().toString(),
                    color = it.selectedColor,
                    path = emptyList()
                )
            )
        }
    }

    private fun onDraw(offset: Offset) {
        val currentPath = _state.value.currentPath ?: return
        _state.update {
            it.copy(
                currentPath = currentPath.copy(
                    path = currentPath.path + offset
                )
            )
        }
    }

    private fun onPathEnd() {
        val currentPath = _state.value.currentPath ?: return
        _state.update {
            it.copy(
                currentPath = null,
                paths = it.paths + currentPath
            )
        }
    }

    private fun clearCanvasClick() {
        _state.update {
            it.copy(
                paths = emptyList(),
                currentPath = null
            )
        }
    }
}