package com.practicecoding.sallonapp.appui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.material.CircularProgressIndicator

@Composable
fun CommonDialog() {

    Dialog(onDismissRequest = { }) {
        CircularProgressIndicator()
    }

}