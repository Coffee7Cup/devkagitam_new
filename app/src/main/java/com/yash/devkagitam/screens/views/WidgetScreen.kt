package com.yash.devkagitam.screens.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Replay
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yash.dev.Widget
import com.yash.devkagitam.__dev__.DevPaperEntity
import com.yash.devkagitam.registries.ContextRegistry
import com.yash.devkagitam.registries.PaperInstanceRegistry.getWidgetInstance
import com.yash.devkagitam.db.widgets.WidgetEntity
import com.yash.devkagitam.screens.viewModels.WidgetScreenViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WidgetScreen() {
    val wsVm: WidgetScreenViewModel = viewModel()
    val showAvailable = rememberSaveable { mutableStateOf(false) }
    val focusWidget = remember { mutableStateOf<WidgetEntity?>(null) }

    val allWidgets by wsVm.allWidgets
    val selectedWidgets = remember(allWidgets) { allWidgets.filter { it.selected } }
    val notSelectedWidgets = remember(allWidgets) { allWidgets.filter { !it.selected } }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)
    ) {
        // Focused widget overlay
        focusWidget.value?.let { widget ->
            val ctx = ContextRegistry.getPluginContext(widget.owner)
            val widInstance = getWidgetInstance(widget)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.8f))
                    .clickable { focusWidget.value = null },
                contentAlignment = Alignment.Center
            ) {
                widInstance.OnHold(ctx)
            }

            Box(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(
                        onClick = { focusWidget.value = null },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.tertiary // 🔹 use tertiary for text
                        )
                    ) {
                        Icon(Icons.Rounded.Close, contentDescription = "Close Focus")
                        Spacer(Modifier.width(4.dp))
                        Text("Close")
                    }

                    Button(
                        onClick = {
                            wsVm.removeWidget(widget)
                            focusWidget.value = null
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,   // 🔹 custom error red
                            contentColor = MaterialTheme.colorScheme.onError
                        )
                    ) {
                        Text("Remove Widget")
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 22.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (showAvailable.value) "Available" else "My Widgets",
                    fontSize = 32.sp,
                    color = Color.White
                )

                Row {
                    IconButton(onClick = { wsVm.refreshWidgets() }) {
                        Icon(
                            imageVector = Icons.Rounded.Replay,
                            contentDescription = "Refresh",
                            tint = MaterialTheme.colorScheme.error // 🔹 primary for action icons
                        )
                    }

                    IconButton(onClick = { showAvailable.value = !showAvailable.value }) {
                        Icon(
                            imageVector = if (showAvailable.value)
                                Icons.Rounded.ArrowBackIosNew else Icons.Rounded.Add,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Dev Widget Section",
                color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f)
            )
            Spacer(Modifier.height(8.dp))

            val devWidInstance = wsVm.devWidgets()
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(minSize = 150.dp),
                contentPadding = PaddingValues(0.dp),
                verticalItemSpacing = 10.dp,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                items(devWidInstance) { widget ->
                    WidgetCardDev(widget, {}, { /* focus dev widget */ })
                }
            }

            Spacer(Modifier.height(16.dp))

            if (showAvailable.value) {
                if (notSelectedWidgets.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            "No new widgets available.",
                            color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f)
                        )
                    }
                } else {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Adaptive(minSize = 150.dp),
                        contentPadding = PaddingValues(0.dp),
                        verticalItemSpacing = 10.dp,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(notSelectedWidgets) { widget ->
                            WidgetCard(widget, { wsVm.addWidget(widget) }, {})
                        }
                    }
                }
            } else {
                if (selectedWidgets.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            "No widgets selected. Tap '+' to add some.",
                            color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f)
                        )
                    }
                } else {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Adaptive(minSize = 150.dp),
                        contentPadding = PaddingValues(0.dp),
                        verticalItemSpacing = 10.dp,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(selectedWidgets) { widget ->
                            WidgetCard(widget, {}, { focusWidget.value = widget })
                        }
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WidgetCard(widget: WidgetEntity, onClick: () -> Unit, onLongClick: () -> Unit) {
    val ctx = ContextRegistry.getPluginContext(widget.owner)
    val widInstance = getWidgetInstance(widget)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .combinedClickable(onClick = onClick, onLongClick = onLongClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.tertiary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            widInstance.Render(ctx)

            if (!widget.selected) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.6f)),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = onClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        Text("Tap to Add")
                    }
                }
            }
        }
    }
}


// -----------------------------------------------------------------------------

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WidgetCardDev(widget: Widget, onClick: () -> Unit, onLongClick: () -> Unit) {
    val ctx = ContextRegistry.getPluginContext(DevPaperEntity.name)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .combinedClickable(onClick = onClick, onLongClick = onLongClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,        // 🔹 slight contrast for dev
            contentColor = MaterialTheme.colorScheme.tertiary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            widget.Render(ctx)
            Box(
                modifier = Modifier
                    .padding(6.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Text("DEV", color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f))
            }
        }
    }
}
