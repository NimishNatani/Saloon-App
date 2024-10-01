package com.practicecoding.sallonapp.appui.components

import android.app.AlertDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AlertDialogBox(
    dialogMessage:List<String>,
    onDismiss: ()-> Unit,
    onClick :()->Unit
){
    AnimatedVisibility(true) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(text = dialogMessage[0]) },
            text = { Text(text = dialogMessage[1]) },
            confirmButton = {
                TextButton(onClick = { onClick() }) {
                    Text("OK")
                }
            }
        )
    }
}