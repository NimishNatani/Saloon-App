package com.practicecoding.sallonapp.appui.screens.MainScreens
import android.graphics.drawable.Drawable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.practicecoding.sallonapp.R
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.OnBoardingPageText
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.OnBoardingText
import com.practicecoding.sallonapp.ui.theme.purple_200
import com.practicecoding.sallonapp.ui.theme.sallonColor
import kotlinx.coroutines.launch
import androidx.compose.material3.Button
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalMapOf
import coil.compose.rememberAsyncImagePainter
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.exyte.animatednavbar.utils.noRippleClickable
import com.practicecoding.sallonapp.appui.components.GeneralButton
import com.practicecoding.sallonapp.appui.components.TransperentTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarberScreen(
    backgroundDrawableId: Int,
    onBackClick: () -> Unit,
    onLikeClick: () -> Unit,
    onShareClick: () -> Unit,
    firstCardContent: @Composable () -> Unit,
    secondCardContent: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background image
        Image(
            painter = painterResource(id = backgroundDrawableId),
            contentDescription = null,
            modifier = Modifier.aspectRatio(1.0f),
            contentScale = ContentScale.FillBounds
        )
        TransperentTopAppBar(
            onBackClick = { /*TODO*/ },
            onLikeClick = { /*TODO*/ },
            onShareClick = { /*TODO*/ },
            isFavorite = false,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        )

        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(bottom = 16.dp)
                .verticalScroll(rememberScrollState())
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Main card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    firstCardContent()
                    Spacer(modifier = Modifier.height(16.dp))
                    // Second card
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                            secondCardContent()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BarberScreenPreview() {
    BarberScreen(
        backgroundDrawableId =  R.drawable.salon_app_logo,
        onBackClick = {},
        onLikeClick = {},
        onShareClick = {},
        firstCardContent = {
            Text(
                text = "First card content",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        secondCardContent = {
            Text(
                text = "Second card content",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    )
}