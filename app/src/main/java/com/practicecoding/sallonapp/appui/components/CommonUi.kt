package com.practicecoding.sallonapp.appui.components

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarHalf
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.practicecoding.sallonapp.R
import com.practicecoding.sallonapp.appui.Screens
import com.practicecoding.sallonapp.ui.theme.Purple40
import com.practicecoding.sallonapp.ui.theme.purple_200
import com.practicecoding.sallonapp.ui.theme.sallonColor
import kotlinx.coroutines.delay



@Composable
fun CommonDialog(title: String = "Loading") {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Box(
            modifier = Modifier
                .size(200.dp, 120.dp) // Set the size of the dialog
                .clip(RoundedCornerShape(12.dp)) // Optional: rounded corners
                .background(Color.White) // Background color of the dialog
                .padding(16.dp), // Padding inside the dialog
            contentAlignment = Alignment.Center // Center content
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(40.dp),
                    color = Color(sallonColor.toArgb()),
                    trackColor = Color(purple_200.toArgb()),
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = title,
                    color = Purple40,
                    fontSize = 16.sp // Adjust font size for better fit
                )
            }
        }
    }
}

@Composable
fun SuccessfulDialog(navController: NavController) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success))
        var isPlaying by remember { mutableStateOf(true) }
        val progress by animateLottieCompositionAsState(
            composition = composition,
            isPlaying = isPlaying,
            restartOnPlay = true,
            iterations = 1,  // Play once
            speed = 1.0f
        )
        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .size(300.dp, 300.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Slot Booked Successfully!", color = sallonColor, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top=8.dp))
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier
                        .size(width = 300.dp, height = 130.dp)
                        ,
                    alignment = Alignment.Center
                )
                Row(horizontalArrangement = Arrangement.SpaceAround) {
                    Button(
                        onClick = { navController.navigate(Screens.MainScreen.route){
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        } },
                        modifier = Modifier
                            .background(Color.White)
                            .border(1.dp, sallonColor, RoundedCornerShape(8.dp))

                            ,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Text(text = "Home Screen", color = sallonColor, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Button(
                        onClick = { navController.navigate(Screens.MainScreen.route) {
                        }
                                 },
                        modifier = Modifier
                            .border(1.dp, sallonColor, RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                sallonColor
                            )
                            ,
                        colors = ButtonDefaults.buttonColors(containerColor = sallonColor),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "View Details", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                    }
                     }
                Spacer(modifier = Modifier.height(14.dp))
                Text(text = "Your slot details are sent to your barber. Please wait or revisit the app to check if the barber has accepted your booking.",color = Color.Red, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, lineHeight = 16.sp)

            }
        }
    }
}

@Composable
fun SuccessfulDialog() {
    val showDialog= remember { mutableStateOf(true) }

    Dialog(
        onDismissRequest = {showDialog.value=false },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success))
        var isPlaying by remember { mutableStateOf(true) }
        val progress by animateLottieCompositionAsState(
            composition = composition,
            isPlaying = isPlaying,
            restartOnPlay = true,
            iterations = 1,  // Play once
            speed = 1.0f
        )
        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .size(300.dp, 300.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Slots update Successfully!", color = sallonColor, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top=8.dp))
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier
                        .size(width = 300.dp, height = 130.dp)
                    ,
                    alignment = Alignment.Center
                )

                Button(
                    onClick = {showDialog.value=false},
                    modifier = Modifier
                        .background(Color.White)
                        .border(1.dp, sallonColor, RoundedCornerShape(8.dp))

                    ,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(text = "OK", color = sallonColor, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                }

            }
        }
    }
}

@Composable
fun CircularProgressWithAppLogo() {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
        var isPlaying by remember { mutableStateOf(true) }
        val progress by animateLottieCompositionAsState(
            composition = composition,
            isPlaying = isPlaying, restartOnPlay = true, iterations = 10, speed = 0.75f
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.White
                ),
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier
                        .size(250.dp)
                        .padding(start = 100.dp, top = 40.dp),
                    alignment = Alignment.Center
                )
            }
        }
    }
}

@Composable
fun BackButtonTopAppBar(
    onBackClick: () -> Unit,
    title: String
) {
    Row(
        modifier = Modifier.fillMaxWidth().background(purple_200),
        horizontalArrangement = Arrangement.Start,

    ) {
        Surface(
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .padding(20.dp)
                .wrapContentSize(align = Alignment.BottomEnd)
                .clip(RoundedCornerShape(20.dp))
                .size(width = 40.dp, height = 40.dp),
            color = purple_200
        ) {

            androidx.compose.material.IconButton(
                onClick = {
                    onBackClick()

                },
                modifier = Modifier.background(color = Color.White)
            ) {
                androidx.compose.material.Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Next",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        androidx.compose.material.Text(
            text = title,
            modifier = Modifier.fillMaxWidth().align(Alignment.CenterVertically)
                .padding(end=70.dp)
            ,
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Composable
fun IconButtonWithTriangle(
    onClick: () -> Unit,
    enabled: Boolean,
    isSelected: Boolean,
    icon: ImageVector
) {
    Column(
        modifier = Modifier.clickable(enabled = enabled) { onClick() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CurvyTriangleWithCircle(
            color = if (isSelected) Color.White else Color.Transparent,
            alignment = Alignment.TopStart
        )

        androidx.compose.material.Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) Color.White else Color.Gray,
            modifier = Modifier.size(34.dp)
        )
    }
}

@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier,
    circleSize: Dp = 25.dp,
    circleColor: Color = sallonColor,
    spaceBetween: Dp = 10.dp,
    travelDistance: Dp = 20.dp
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White, shape = RoundedCornerShape(16.dp)),
        color = Color.White,
        tonalElevation = 20.dp,
        shape = RoundedCornerShape(16.dp)
    ) {

        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            val circles = listOf(
                remember { Animatable(initialValue = 0f) },
                remember { Animatable(initialValue = 0f) },
                remember { Animatable(initialValue = 0f) }
            )

            circles.forEachIndexed { index, animatable ->

                LaunchedEffect(key1 = animatable) {
                    delay(index * 100L)
                    animatable.animateTo(
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = keyframes {
                                durationMillis = 1200
                                0.0f at 0 using LinearOutSlowInEasing
                                1.0f at 300 using LinearOutSlowInEasing
                                0.0f at 600 using LinearOutSlowInEasing
                                0.0f at 1200 using LinearOutSlowInEasing
                            },
                            repeatMode = RepeatMode.Restart
                        )
                    )
                }
            }

            val circleValues = circles.map { it.value }
            val distance = with(LocalDensity.current) { travelDistance.toPx() }

            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(spaceBetween)
            ) {
                circleValues.forEach { value ->
                    Box(
                        modifier = Modifier
                            .size(circleSize)
                            .graphicsLayer {
                                translationY = -value * distance
                            }
                            .background(
                                color = circleColor,
                                shape = CircleShape
                            )
                    )
                }
            }
        }

    }
}

@Composable
private fun CurvyTriangleWithCircle(
    color: Color,
    alignment: Alignment
) {
    Box(
        modifier = Modifier.size(16.dp),
        contentAlignment = alignment
    ) {
        Canvas(modifier = Modifier.size(16.dp)) {
            drawPath(
                path = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(size.width, 0f)
                    lineTo(size.width / 2, size.height)
                    lineTo(0f, 0f)
                    close()
                },
                color = color
            )
        }
    }
}

@Composable
fun BottomAppNavigationBar1(
    currentScreen: BottomNavItems,
    onHomeClick: () -> Unit,
    onLocationClick: () -> Unit,
    onBookClick: () -> Unit,
    onMessageClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BottomAppBar(
        modifier = modifier.height(56.dp),
        contentPadding = PaddingValues(8.dp),
        containerColor = sallonColor,
        tonalElevation = 8.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp, start = 16.dp, bottom = 2.dp)
                .clip(RoundedCornerShape(50)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButtonWithTriangle(
                onClick = onHomeClick,
                enabled = currentScreen != BottomNavItems.Home,
                isSelected = currentScreen == BottomNavItems.Home,
                icon = Icons.Default.Home
            )
            IconButtonWithTriangle(
                onClick = onLocationClick,
                enabled = currentScreen != BottomNavItems.Location,
                isSelected = currentScreen == BottomNavItems.Location,
                icon = Icons.Default.LocationOn
            )
            IconButtonWithTriangle(
                onClick = onBookClick,
                enabled = currentScreen != BottomNavItems.Book,
                isSelected = currentScreen == BottomNavItems.Book,
                icon = Icons.Default.ShoppingCart
            )
            IconButtonWithTriangle(
                onClick = onMessageClick,
                enabled = currentScreen != BottomNavItems.Message,
                isSelected = currentScreen == BottomNavItems.Message,
                icon = Icons.AutoMirrored.Filled.Send
            )
            IconButtonWithTriangle(
                onClick = onProfileClick,
                enabled = currentScreen != BottomNavItems.Profile,
                isSelected = currentScreen == BottomNavItems.Profile,
                icon = Icons.Default.Person
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar() {
    var text by remember { mutableStateOf("") }
    var active by remember {
        mutableStateOf(false)
    }
    val shape = if (active) RoundedCornerShape(16.dp) else SearchBarDefaults.dockedShape

    androidx.compose.material3.SearchBar(
        query = text,
        onQueryChange = { text = it },
        onSearch = { active = false },
        active = active,
        onActiveChange = { active = it },
        placeholder = { Text(text = "Search", color = Color.Gray) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = Purple40

            )
        },
        trailingIcon = {
            if (active) {
                Icon(
                    modifier = Modifier.clickable {
                        if (text.isNotEmpty()) {
                            text = ""
                        } else {
                            active = false
                        }
                    },
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear Icon",
                    tint = Color.Gray
                )
            }
        }, modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 50.dp, max = 180.dp)
            .padding(start = 24.dp, end = 24.dp),
        shape = shape, // Adjust the corner radius as needed
        colors = SearchBarDefaults.colors(
            containerColor = Color.White,
            inputFieldColors = TextFieldDefaults.colors(
                unfocusedTextColor = Color.Gray,
                disabledTextColor = Color.Gray,
                focusedTextColor = Color.Black,
                focusedIndicatorColor = sallonColor,
                unfocusedIndicatorColor = sallonColor,
                disabledIndicatorColor = sallonColor,
            )
        )
    ) {}
}

enum class BottomNavItems {
    Home, Location, Book, Message, Profile
}

@Composable
fun Categories(image: Int, categories: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = "Categories", // We don't need content description for images used as buttons
            modifier = Modifier
                .size(48.dp)
                .clickable {onClick() }
        )
        Text(
            text = categories,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,


            )
    }
}

@Composable
fun DotIndicator(selected: Boolean) {
    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .size(8.dp)
            .background(
                color = if (selected) Color(sallonColor.toArgb()) else Color(purple_200.toArgb()),
                shape = CircleShape
            )
    )
}

@Composable
fun HeadingText(
    bodyText: String
) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp),
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        androidx.compose.material.Text(
            text = bodyText,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun CircularCheckbox(
    isServiceSelected: Boolean,
    onClick: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier,
    color: Color = Color.Green,
    size: Dp = 48.dp
) {
    var checked by remember { mutableStateOf(isServiceSelected) }
    Box(
        modifier = modifier
            .size(size)
            .clickable {
                checked = !checked
                onClick(isServiceSelected)
            }
    ) {
        // Outer circle
        Box(
            modifier = Modifier
                .size(size)
                .background(
                    color = if (checked) color else Color.White,
                    shape = CircleShape
                )
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = CircleShape
                )
        ) {
            // Inner circle (check mark)
            if (checked) {
                androidx.compose.material.Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(size * 0.6f)
                        .align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun LaunchPhotoPicker(singlePhotoPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>) {

    singlePhotoPickerLauncher.launch(
        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
    )

}
@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Double = 0.0,
    stars: Int = 5,
    onRatingChanged: (Double) -> Unit,
    starsColor: Color = Color.Yellow
) {

    var isHalfStar = (rating % 1) != 0.0

    Row {
        for (index in 1..stars) {
            Icon(
                imageVector =
                if (index <= rating) {
                    Icons.Rounded.Star
                } else {
                    if (isHalfStar) {
                        isHalfStar = false
                        Icons.Rounded.StarHalf
                    } else {
                        Icons.Rounded.StarOutline
                    }
                },
                contentDescription = null,
                tint = starsColor,
                modifier = modifier
                    .clickable { onRatingChanged(index.toDouble()) }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Preview() {
    val context = LocalContext.current
   // CommonDialog()
    CircularProgressWithAppLogo()
}

