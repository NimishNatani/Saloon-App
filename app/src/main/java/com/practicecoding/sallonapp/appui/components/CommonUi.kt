package com.practicecoding.sallonapp.appui.components

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun BackButtonTopAppBar(
    onBackClick:()->Unit,
    title:String
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Surface(
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .padding(20.dp)
                .wrapContentSize(align = Alignment.BottomEnd)
                .clip(RoundedCornerShape(20.dp))
                .size(width = 40.dp, height = 40.dp),
            color = MaterialTheme.colorScheme.primary
        ) {

            androidx.compose.material.IconButton(
                onClick = {
                    onBackClick()

                },
                modifier = Modifier.background(color = Color.White)
            ) {
                androidx.compose.material.Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Next",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        androidx.compose.material.Text(
            text = title,
            modifier = Modifier
                .padding(40.dp, 26.dp),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun LaunchPhotoPicker(singlePhotoPickerLauncher:ManagedActivityResultLauncher<PickVisualMediaRequest,Uri?>) {

        singlePhotoPickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )

}

