package com.practicecoding.sallonapp.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.practicecoding.sallonapp.R
import com.practicecoding.sallonapp.screens.initiatorScreens.OnBoardingPageText
import com.practicecoding.sallonapp.screens.initiatorScreens.OnBoardingText
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingBottomTextCard(
    navController: NavController,
    onBoardingTextList : List<OnBoardingPageText>,
    onNextClick: () -> Unit,
){
    Card(
        modifier = Modifier
            .fillMaxWidth().wrapContentHeight(),
        shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth().wrapContentHeight()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
                )
                .padding(top = 16.dp, start = 18.dp, end = 18.dp, bottom = 16.dp),
        ) {
            val pagerState = rememberPagerState(0,0f) {onBoardingTextList.size }
            val scope = rememberCoroutineScope()
            HorizontalPager(
                modifier = Modifier
                    .fillMaxWidth().wrapContentHeight().align(Alignment.TopCenter),
                state = pagerState
            ) {text ->
                OnBoardingText(
                    mainHeading = onBoardingTextList[text].mainHeading,
                    bodyText = onBoardingTextList[text].bodyText,
                )
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(16.dp).align(Alignment.BottomStart)
            ) {
                repeat(3) { index ->
                    DotIndicator(selected = index == pagerState.currentPage)
                }
            }
            Surface(
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 32.dp, end = 32.dp, start = 32.dp)
                    .wrapContentSize(align = Alignment.BottomEnd).clip(RoundedCornerShape(50.dp)),
                color = MaterialTheme.colorScheme.primary
            ) {

                IconButton(onClick ={
                    scope.launch {
                        if(pagerState.currentPage < 3){
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }else{
                            /*TODO*/
                            //open the welcome screen
                           // navController.navigate("welcome")
                        }
                    }
                    onNextClick()
                },
                    modifier = Modifier.background(color =  Color.Blue)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Next",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(45.dp)
                    )
                }
            }

        }
    }
}

@Composable
fun DotIndicator(selected: Boolean) {
    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .size(8.dp)
            .background(
                color = if (selected) Color.Gray else Color.LightGray,
                shape = CircleShape
            )
    )
}

@Preview(showBackground = true)
@Composable
fun OnBoardingBottomTextCardPreview(){
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
    OnBoardingBottomTextCard(
        navController = navController,
        onBoardingTextList = onBoardingTextList,
        onNextClick = {}
    )
}
