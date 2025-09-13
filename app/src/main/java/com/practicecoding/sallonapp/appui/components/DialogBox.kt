package com.practicecoding.sallonapp.appui.components

import android.app.AlertDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.practicecoding.sallonapp.ui.theme.purple_200

@Composable
fun AlertDialogBox(
    dialogMessage:List<String>,
    onDismiss: ()-> Unit,
    onClick :()->Unit
){
    AnimatedVisibility(true) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { SaloonColorText(text = dialogMessage[0]) },
            text = { SaloonColorText(text = dialogMessage[1]) },
            confirmButton = {
                TextButton(onClick = { onClick() }) {
                    SaloonColorText("OK")
                }
            }, containerColor = purple_200,
        )
    }
}