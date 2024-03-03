package com.practicecoding.sallonapp.screens.initiatorScreens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.practicecoding.sallonapp.R
import com.practicecoding.sallonapp.Screens
import com.practicecoding.sallonapp.screens.OnBoardingBottomTextCard
import kotlinx.coroutines.launch

@Composable
fun OnBoardingText(
    mainHeading: String,
    bodyText: String,
) {
Box(
    modifier = Modifier.fillMaxWidth().wrapContentHeight(),
    contentAlignment = Alignment.TopCenter
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 18.dp, end = 18.dp, bottom = 16.dp)
            .wrapContentHeight(),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
          Text(
                text = mainHeading,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 39.sp
                ),
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
            )
            Text(
                text = bodyText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp
                ),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
        Spacer(modifier = Modifier.heightIn(120.dp))
    }
}
}

@Composable
fun OnBoardingPageImage(
    image: Int,
    onClickSkip: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ){

        Column(
            modifier = Modifier.fillMaxSize(),
        ){
            Image(
                painter = painterResource(id = image),
                contentDescription = "Onboarding Image",
                modifier = Modifier
                    .fillMaxWidth().size(600.dp)
                    .aspectRatio(0.909f),
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Spacer(Modifier.weight(1f))
            Text(
                text = "Skip",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp
                ),
                modifier = Modifier
                    .padding(20.dp)
                    .clickable { onClickSkip() }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScreen(
    navController: NavController,
    imageList: List<Int>,
    OnBoardingTextList: List<OnBoardingPageText>
) {
    val pagerState = rememberPagerState(0,0f) { imageList.size }
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
          Box(
            modifier = Modifier.fillMaxSize().align(Alignment.TopCenter),
            contentAlignment = Alignment.TopCenter
          ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                OnBoardingPageImage(
                    image = imageList[page],
                    onClickSkip = {
                        navController.navigate(Screens.PhoneNumberScreen.route)
                    }
                )
            }
          }
        OnBoardingBottomTextCard(
            navController = navController,
            onBoardingTextList = OnBoardingTextList,
            onNextClick = {
                scope.launch {
                    if(pagerState.currentPage <= imageList.size - 2){
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }else{
                        navController.navigate(Screens.PhoneNumberScreen.route)
                    }
                }
            }
        )
    }

}

@Preview(showBackground = true)
@Composable
fun OnBoardingScreenPreview() {
//OnBoardingText("Hello", "This is a test message")
    val onBoardingTextList = listOf(
        OnBoardingPageText(
            mainHeading = "Heading 1",
            bodyText = "Body 1"
        ),
        OnBoardingPageText(
            mainHeading = "Heading 2",
            bodyText = "Body 2"
        ),
        OnBoardingPageText(
            mainHeading = "Heading 3",
            bodyText = "Body 3"
        ),
    )
    val navController = rememberNavController()
    OnBoardingScreen(
        navController = navController,
        imageList = listOf(
            R.drawable.onboarding1,
            R.drawable.onboarding2,
            R.drawable.onboarding3
        ),
        OnBoardingTextList = onBoardingTextList
    )
    }
