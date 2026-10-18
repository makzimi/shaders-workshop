package dev.maxkach.shaderworkshop

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ExerciseLauncher(onSelect: (WorkshopExample) -> Unit) {
    Surface(Modifier.fillMaxSize()) {
        Column(
            Modifier.safeDrawingPadding().verticalScroll(rememberScrollState()).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("Choose an exercise", style = MaterialTheme.typography.headlineMedium)
            Text("Start: live coding. Finish: working example.", style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(12.dp))
            Text("Exercise 1 · Spotlight", style = MaterialTheme.typography.titleLarge)
            OutlinedButton(onClick = { onSelect(WorkshopExample.SpotlightStart) }, modifier = Modifier.fillMaxWidth()) {
                Text("Spotlight: Start")
            }
            Button(onClick = { onSelect(WorkshopExample.SpotlightFinish) }, modifier = Modifier.fillMaxWidth()) {
                Text("Spotlight: Finish")
            }
            Spacer(Modifier.height(12.dp))
            Text("Exercise 2 · Upside Down", style = MaterialTheme.typography.titleLarge)
            OutlinedButton(onClick = { onSelect(WorkshopExample.UpsideDownStart) }, modifier = Modifier.fillMaxWidth()) {
                Text("Upside Down: Start")
            }
            Button(onClick = { onSelect(WorkshopExample.UpsideDownFinish) }, modifier = Modifier.fillMaxWidth()) {
                Text("Upside Down: Finish")
            }
        }
    }
}
