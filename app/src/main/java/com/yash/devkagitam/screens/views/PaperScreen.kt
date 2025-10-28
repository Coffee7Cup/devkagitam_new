package com.yash.devkagitam.screens.views

import android.graphics.fonts.FontStyle
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yash.dev.PaperEntryPoint
import com.yash.devkagitam.__dev__.DevPaperEntity
import com.yash.devkagitam.db.plugins.MetaDataPluginEntity
import com.yash.devkagitam.registries.AppRegistry
import com.yash.devkagitam.registries.ContextRegistry
import com.yash.devkagitam.screens.viewModels.PaperElementViewModel
import com.yash.devkagitam.screens.viewModels.PaperElementViewModelFactory
import com.yash.devkagitam.screens.viewModels.PaperScreenViewModel
@Composable
fun PaperScreen(vm: PaperScreenViewModel = viewModel()) {

    val allPapers = vm.allPapers
    val devError = remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 22.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Papers",
                color = Color.White,
                fontSize = 32.sp
            )
            IconButton(
                onClick = { vm.refreshPapers() }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Refresh,
                    contentDescription = "Refresh",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        LazyColumn(
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            //------ Dev Paper Section ----------
            fun launchDevPaper() {
                try {
                    val instance = Class
                        .forName(DevPaperEntity.entryPoint)
                        .getDeclaredConstructor()
                        .newInstance() as PaperEntryPoint
                    AppRegistry.setCurrentPlugin(instance, DevPaperEntity.name)
                    AppRegistry.getNavController().navigate("paper") {
                        launchSingleTop = true
                    }
                } catch (e: Error) {
                    devError.value = e.message
                }
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable(onClick = { launchDevPaper() }),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.tertiary
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(DevPaperEntity.name)
                                Text(DevPaperEntity.author)
                                Text(DevPaperEntity.version)
                            }
                            Text(
                                "Dev mode",
                                color = MaterialTheme.colorScheme.tertiary // 🔹 highlight label
                            )
                        }

                        devError.value?.let {
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Error: $it",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }

            //------------ Standard Papers ------------
            items(allPapers.value.size) { index ->
                val paper = allPapers.value[index]
                PaperCard(paper)
            }
        }
    }
}

// -----------------------------------------------------------------------------
@Composable
fun PaperCard(paper: MetaDataPluginEntity) {

    val cardVm: PaperElementViewModel = viewModel(
        factory = remember(paper) { PaperElementViewModelFactory(paper) }
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = { cardVm.loadPaper() }),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.tertiary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(paper.name,)
                    Text(
                        paper.author,
                        color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f)
                    )
                    Text(
                        "Version: ${paper.version}",
                        color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f)
                    )
                }

                IconButton(
                    onClick = { cardVm.deletePaper() },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.error // 🔹 delete icon in error red
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = "Delete"
                    )
                }
            }

            // 🔹 Handle Error Messages
            cardVm.error.value?.let { errorText ->
                Spacer(Modifier.height(8.dp))
                Text(
                    "Error loading paper: $errorText",
                    color = MaterialTheme.colorScheme.error
                )

                TextButton(
                    onClick = { cardVm.removeError() },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "Dismiss Error",
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Remove Error",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CurrentPaperScreen(){
    Box(
        Modifier.fillMaxSize()
    ){
        val (instance, name) = AppRegistry.getCurrentPlugin()
        val navController = AppRegistry.getNavController()
        if (instance != null && name != null) {
            val ctx = ContextRegistry.getPluginContext(name)
            instance.RenderWithHome(ctx, navController)
        } else {
            Text("No paper loaded")
        }

    }
}