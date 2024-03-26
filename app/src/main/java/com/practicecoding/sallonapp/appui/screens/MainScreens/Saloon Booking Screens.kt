package com.practicecoding.sallonapp.appui.screens.MainScreens
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.practicecoding.sallonapp.R
import com.practicecoding.sallonapp.ui.theme.sallonColor
import androidx.compose.material3.Button
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp
import com.exyte.animatednavbar.utils.noRippleClickable
import com.practicecoding.sallonapp.appui.components.BookingScreenShopPreviewCard
import com.practicecoding.sallonapp.appui.components.GenderSelectCard
import com.practicecoding.sallonapp.appui.components.HorizontalPagerWithTabs
import com.practicecoding.sallonapp.appui.components.TransparentTopAppBar
import com.practicecoding.sallonapp.data.model.BarberModel
import com.practicecoding.sallonapp.data.model.ServiceCat
import com.practicecoding.sallonapp.data.model.ServiceModel

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
    val screenHeight = LocalContext.current.resources.displayMetrics.heightPixels
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
        TransparentTopAppBar(
            onBackClick = { /*TODO*/ },
            onLikeClick = { /*TODO*/ },
            onShareClick = { /*TODO*/ },
            isFavorite = false,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        )
        Box(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(bottom = 16.dp)
                    .verticalScroll(rememberScrollState())
                    .align(Alignment.TopCenter),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Main card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight.dp * 0.235f),
                    elevation = 8.dp,
                    shape = RoundedCornerShape(topStart = 45.dp, topEnd = 45.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(2.dp),
                        verticalArrangement = Arrangement.Top,
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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .align(Alignment.BottomCenter)
                    .noRippleClickable { onShareClick() },
                elevation = 8.dp,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            ) {
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(sallonColor.toArgb()),
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Book now",color = Color.White)
                }
            }
        }
    }
}

@Composable
fun GenderSelectOnBook(
){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GenderSelectCard(gender = "Male")
                Spacer(modifier = Modifier.width(8.dp))
                GenderSelectCard(gender = "Female")
            }
            GenderSelectCard(gender = "Other")
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
            backgroundColor = Color.White,
            contentColor = Color.Black,
            elevation = 8.dp
        ) {
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 30.dp, start = 16.dp, end = 16.dp),
                shape = RoundedCornerShape(45),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.sallon_color)
                )
            ) {
                Text(
                    text = "Next",
                    color = Color.White,
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BarberScreenPreview() {
    val previewImages = listOf(
        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636"
    )
    val barberDetails = BarberModel(
        aboutUs = "About Us",
        phoneNumber = "1234567890",
        noOfReviews = "10"
    )
    val serviceCategories = listOf(
        ServiceCat(
            name = "Hair Cut",
            services = listOf(
                ServiceModel(
                    name = "Hair Cut",
                    price = 100.0
                )
            )
        )
    )
    BarberScreen(
        backgroundDrawableId =  R.drawable.salon_app_logo,
        onBackClick = {},
        onLikeClick = {},
        onShareClick = {},
        firstCardContent = {
                BookingScreenShopPreviewCard(
                    shopName = "Shop name",
                    shopAddress = " Shop address",
                    ratings = 4.5f,
                    numberOfReviews = 100,
                ) {

                }
},
        secondCardContent = {
            HorizontalPagerWithTabs(barberDetails, serviceCategories, previewImages)
        }
    )
}