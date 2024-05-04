package com.practicecoding.sallonapp.appui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.practicecoding.sallonapp.ui.theme.sallonColor


enum class NavigationItem(val icon: ImageVector) {
    Home(Icons.Default.Home),
    Location(Icons.Default.LocationOn),
    Book(Icons.Default.ShoppingCart),
    Message(Icons.AutoMirrored.Filled.Send),
    Profile(Icons.Default.Person)
}

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        onClick()
    }
}

//@Composable
//fun BottomAppNavigationBar(
//    onHomeClick: () -> Unit,
//    onLocationClick: () -> Unit,
//    onBookClick: () -> Unit,
//    onMessageClick: () -> Unit,
//    onProfileClick: () -> Unit,
//){
//    val bottomBarItems = remember { NavigationItem.values() }
//    var selectedIndex by remember { mutableStateOf(0) }
//    AnimatedNavigationBar(
//        modifier = Modifier
//            .height(64.dp)
//            .fillMaxWidth(),
//        barColor = Color.White,
//        ballAnimation = Parabolic(tween(durationMillis = 200)),
//        ballColor = Color(sallonColor.toArgb()),
//        selectedIndex = selectedIndex,
//        cornerRadius = shapeCornerRadius(36.dp),
//        indentAnimation = Height(tween(durationMillis = 200)),
//    ) {
//        bottomBarItems.forEach {item->
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .noRippleClickable { selectedIndex = item.ordinal },
//                contentAlignment = Alignment.Center
//            ) {
//                Icon(
//                    imageVector = item.icon,
//                    contentDescription = "Icon",
//                    modifier = Modifier.size(24.dp),
//                    tint = if (selectedIndex == item.ordinal) Color(sallonColor.toArgb()) else Color.Gray
//                )
//            }
//        }
//    }
//}

///*Top App Bars*/

@Composable
fun TransparentTopAppBar(
    onBackClick: () -> Unit,
    onLikeClick: () -> Unit,
    onShareClick: () -> Unit,
    isFavorite: Boolean,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        color = Color.Transparent,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(36.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(30.dp)
                            .padding(4.dp)
                    )
                }
            }
            Row {
                IconButton(
                    onClick = onLikeClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color.White
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Like",
                            tint = Color(sallonColor.toArgb()),
                            modifier = Modifier
                                .size(30.dp)
                                .padding(4.dp)
                        )
                    }
                }
                IconButton(
                    onClick = onShareClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color.White
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.Black,
                            modifier = Modifier
                                .size(30.dp)
                                .padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}