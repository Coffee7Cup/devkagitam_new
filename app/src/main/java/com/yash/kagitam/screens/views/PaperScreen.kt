package com.yash.kagitam.screens.views

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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yash.deps.PaperContext
import com.yash.kagitam.BuildConfig
import com.yash.kagitam.db.plugins.MetaDataPluginEntity
import com.yash.kagitam.registries.AppRegistry
import com.yash.kagitam.registries.ContextRegistry
import com.yash.kagitam.screens.viewModels.PaperElementViewModel
import com.yash.kagitam.screens.viewModels.PaperElementViewModelFactory
import com.yash.kagitam.screens.viewModels.PaperScreenViewModel

@Composable
fun PaperScreen() {

    val vm: PaperScreenViewModel = viewModel()

    val allPapers = vm.allPapers

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


            //-------------dev---------------------

            if (BuildConfig.IS_DEV_BUILD) {
                item {
                    PaperCard(DevObject.getManifest((AppRegistry.getAppContext())), true)
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
fun PaperCard(paper: MetaDataPluginEntity,isDev: Boolean = false) {

    val cardVm: PaperElementViewModel = viewModel(
        key = "paper-card-${paper.name}",
        factory = remember(paper) { PaperElementViewModelFactory(paper) }
    )

    val psvm : PaperScreenViewModel = viewModel()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = { cardVm.loadPaper() }),
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

                if(!isDev){
                    IconButton(
                        onClick = { cardVm.deletePaper(psvm) },
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.error // 🔹 delete icon in error red
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = "Delete"
                        )
                    }
                }else{
                    Text("Dev", color = Color.White)
                }
            }

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
    
    val error = remember { mutableStateOf<String?>(null) }
    
    Box(
        Modifier.fillMaxSize()
    ){
        val (instance, name) = AppRegistry.getCurrentPlugin()
        val navController = AppRegistry.getNavController()
        if (instance != null && name != null) {

            val ctx : PaperContext =  ContextRegistry.getPluginContext(name)

            CompositionLocalProvider(LocalContext provides ctx) {
                instance.RenderWithHome( navController)
            }
        } else {
            Text("No paper loaded")
        }

        error.value?.let {
            Text("error : ${error.value}")
        }

    }
}