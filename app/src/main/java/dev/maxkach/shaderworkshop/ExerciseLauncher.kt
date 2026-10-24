package dev.maxkach.shaderworkshop

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private object LauncherColor {
    val Background = Color(0xFF101014)
    val Card = Color(0xFF1B1B21)
    val Text = Color(0xFFF2F2F5)
    val Muted = Color(0xFFA6A6B0)
    val Outline = Color(0xFF4A4A55)
    val Accent = Color(0xFF4A63F0)
}

@Composable
fun ExerciseLauncher(onSelect: (WorkshopExample) -> Unit) {
    Surface(Modifier.fillMaxSize(), color = LauncherColor.Background, contentColor = LauncherColor.Text) {
        Column(
            Modifier.safeDrawingPadding().verticalScroll(rememberScrollState()).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("Choose an exercise", fontSize = 30.sp, lineHeight = 36.sp, fontWeight = FontWeight.SemiBold)
            Text(
                "Start: live coding. Finish: working example.",
                fontSize = 15.sp,
                lineHeight = 22.sp,
                color = LauncherColor.Muted,
                modifier = Modifier.padding(bottom = 20.dp),
            )
            ExerciseCard(
                number = 1,
                title = "Spotlight",
                preview = R.drawable.launcher_preview_spotlight,
                start = WorkshopExample.SpotlightStart,
                finish = WorkshopExample.SpotlightFinish,
                onSelect = onSelect,
            )
            ExerciseCard(
                number = 2,
                title = "Upside Down",
                preview = R.drawable.launcher_preview_upside_down,
                start = WorkshopExample.UpsideDownStart,
                finish = WorkshopExample.UpsideDownFinish,
                onSelect = onSelect,
            )
        }
    }
}

@Composable
private fun ExerciseCard(
    number: Int,
    title: String,
    @DrawableRes preview: Int,
    start: WorkshopExample,
    finish: WorkshopExample,
    onSelect: (WorkshopExample) -> Unit,
) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(LauncherColor.Card).padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Image(
            painterResource(preview),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(96.dp, 128.dp).clip(RoundedCornerShape(14.dp)),
        )
        Column(Modifier.weight(1f).height(128.dp)) {
            Text("Exercise $number", fontSize = 12.sp, color = LauncherColor.Muted)
            Text(title, fontSize = 20.sp, lineHeight = 26.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 2.dp))
            Spacer(Modifier.weight(1f))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = { onSelect(start) },
                    modifier = Modifier.weight(1f).height(44.dp).semantics { contentDescription = "$title: Start" },
                    border = BorderStroke(1.dp, LauncherColor.Outline),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = LauncherColor.Text),
                    contentPadding = PaddingValues(0.dp),
                ) {
                    Text("Start", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
                Button(
                    onClick = { onSelect(finish) },
                    modifier = Modifier.weight(1f).height(44.dp).semantics { contentDescription = "$title: Finish" },
                    colors = ButtonDefaults.buttonColors(containerColor = LauncherColor.Accent, contentColor = Color.White),
                    contentPadding = PaddingValues(0.dp),
                ) {
                    Text("Finish", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
