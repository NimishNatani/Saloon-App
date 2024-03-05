package com.practicecoding.sallonapp.appui.components

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.practicecoding.sallonapp.R
import com.practicecoding.sallonapp.ui.theme.purple_200
import com.practicecoding.sallonapp.ui.theme.sallonColor

@Composable
fun CommonDialog() {

    Dialog(onDismissRequest = { }) {
        CircularProgressIndicator()
    }

}
@Composable
fun LaunchPhotoPicker(singlePhotoPickerLauncher:ManagedActivityResultLauncher<PickVisualMediaRequest,Uri?>) {

        singlePhotoPickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )

}

