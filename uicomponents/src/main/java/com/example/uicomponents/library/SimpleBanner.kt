package com.example.uicomponents.library

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.uicomponents.R
import com.example.uicomponents.models.BannerState

@Composable
fun SimpleBanner(bannerState: BannerState, onDismissRequest: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colors.primaryVariant),
        backgroundColor = MaterialTheme.colors.primaryVariant.copy(alpha = 0.3f)
    ) {
        Box {
            IconButton(modifier = Modifier.align(Alignment.TopEnd), onClick = onDismissRequest) {
                Icon(painter = painterResource(id = R.drawable.ic_baseline_close_24), contentDescription = "Close")
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = bannerState.title, style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = bannerState.message, style = MaterialTheme.typography.body1)
            }
        }
    }
}