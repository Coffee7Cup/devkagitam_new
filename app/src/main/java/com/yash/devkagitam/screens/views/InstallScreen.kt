package com.yash.devkagitam.screens.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Replay
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp // Note: Prefer MaterialTheme.typography over hardcoded sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yash.devkagitam.screens.viewModels.GitHubFile
import com.yash.devkagitam.screens.viewModels.InstallScreenViewModel
import com.yash.devkagitam.screens.viewModels.PluginCardViewModel
import com.yash.devkagitam.screens.viewModels.PluginCardViewModelFactory


@Composable
fun InstallScreen(viewModel: InstallScreenViewModel = viewModel()) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 22.dp),
        ) {
            Text(
                "Install",
                fontSize = 32.sp,
                color = Color.White
            )
            IconButton(onClick = { viewModel.loadPlugins() }) {
                Icon(
                    imageVector = Icons.Outlined.Replay,
                    contentDescription = "Reload",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        // --- Content Section ---

        if (viewModel.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Loading...")
            }
        } else if (viewModel.error != null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Error: ${viewModel.error}",
                        color = MaterialTheme.colorScheme.error,
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { viewModel.loadPlugins() }) {
                        Text("Retry")
                    }
                }
            }
        } else {
            LazyColumn(
                contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 4.dp)
            ) {
                items(viewModel.plugins.size) { index ->
                    val plugin = viewModel.plugins[index]
                    PluginCard(
                        data = plugin,
                        isInstalled = viewModel.installedPlugins.contains(plugin.name),
                    )
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------

@Composable
fun PluginCard(data: GitHubFile, isInstalled: Boolean) {
    val cardVM: PluginCardViewModel = viewModel(
        key = "plugin-card-${data.name}",
        factory = remember(data) { PluginCardViewModelFactory(data) }
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant, // Using surfaceVariant
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        data.name,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "${data.size} KB",
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    )
                }

                // Status/Action Button
                if (isInstalled) {
                    Text(
                        "Installed",
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                } else if(cardVM.isInstalling){
                    Column {
                        Text(
                            "Downloading",
                            color = MaterialTheme.colorScheme.tertiary,
                        )
                        Button(onClick = { cardVM.cancelDownload() })
                        {
                            Text("Cancel", color = MaterialTheme.colorScheme.error)
                        }
                    }
                } else {
                    IconButton(
                        onClick = { cardVM.startDownload() },
                        // Disable if a download is already in progress (progress != null)
                        enabled = cardVM.progress.value == null,
                        colors = if (cardVM.progress.value == null) {
                            IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                        } else {
                            IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Download,
                            contentDescription = "Download",
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }

            // Progress Indicator Section
            cardVM.progress.value?.let { progress ->
                Spacer(Modifier.height(10.dp))

                if (progress in 1..99 && !isInstalled) {
                    // Linear Progress Indicator
                    LinearProgressIndicator(
                        progress = { progress / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp), // Thinner indicator
                        color = MaterialTheme.colorScheme.primary, // Use primary for progress color
                        trackColor = MaterialTheme.colorScheme.surface, // Use surface for track color
                        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "$progress%",
                        color = MaterialTheme.colorScheme.primary,
                    )
                } else if (progress == 100) {
                    Text(
                        "Download complete",
                        color = MaterialTheme.colorScheme.tertiary, // Use tertiary for completion
                    )
                }
            }

            // Error Section
            cardVM.error.value?.let { err ->
                Spacer(Modifier.height(8.dp))
                Text(
                    "Error: $err",
                    color = MaterialTheme.colorScheme.error,
                )
                // Use a TextButton or OutlinedButton for a less prominent action than the main download button
                Button(
                    onClick = { cardVM.resetError() },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Dismiss error") // More descriptive
                }
            }
        }
    }
}