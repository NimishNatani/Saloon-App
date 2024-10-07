package com.practicecoding.sallonapp.appui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.practicecoding.sallonapp.data.model.BarberModel
import com.practicecoding.sallonapp.data.model.BookingModel
import com.practicecoding.sallonapp.data.model.ReviewModel
import com.practicecoding.sallonapp.data.model.ServiceCategoryModel
import com.practicecoding.sallonapp.ui.theme.sallonColor
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPagerWithTabs(
    bookingModel: BookingModel,
    serviceCategories: List<ServiceCategoryModel?> = listOf(),
    previewImages: List<String?> = listOf(),
    reviewList: MutableList<ReviewModel>
) {
    val pagerState = rememberPagerState(pageCount = { 4 })

    val titles = listOf("About Us", "Services", "Gallery", "Reviews")
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    color = Color.White
                )
            },
            containerColor = sallonColor,
            contentColor = Color.White
        ) {
            titles.forEachIndexed { index, title ->
                Tab(
                    text = { Text(text = title, color = Color.White) },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                )
            }
        }
        /*TODO: finding a way to make the horizontal pager to take the pager as parameter like providing it in a list or something*/
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
        ) { page ->
            when (page) {
                0 -> AboutUsPage(bookingModel.barber)
                1 -> ServicesPage(serviceCategories)
                2 -> GalleryPage(previewImages)
                3 -> ReviewsPage(reviewList)
                else -> AboutUsPage(bookingModel.barber) // Handle unexpected page index
            }
        }
    }
}

@Composable
fun AboutUsPage(barberDetails: BarberModel) {
    val scrollState = rememberScrollState()
    var isAboutUsExpanded by remember { mutableStateOf(false) }
    var isOpen_ClosedExpanded by remember { mutableStateOf(false) }
    var isContactUsExpanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp, vertical = 15.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExpandableCard(title = "About us", expanded = isAboutUsExpanded) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = barberDetails.aboutUs ?: "Nothing",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        ExpandableCard(title = "Open - Closed", expanded = isOpen_ClosedExpanded) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (barberDetails.open==true)"Open" else "Close",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

        ExpandableCard(title = "Contact Us", expanded = isContactUsExpanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp),
            ) {
                Text(
                    text = "phone number",
                    modifier = Modifier.padding(8.dp),
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                Text(
                    text = barberDetails.phoneNumber ?: "Contact Us",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(60.dp))
    }
}

@Composable
fun ServicesPage(
    serviceCategories: List<ServiceCategoryModel?> = listOf()

) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp, vertical = 15.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        serviceCategories.forEach { service ->
            ExpandableCard(title = service?.type!!, expanded = false) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    service.services.forEach { service ->
                        ServiceNameAndPriceCard(
                            serviceName = service.name!!,
                            serviceTime = service.time,
                            servicePrice = service.price.toString(),
                            count = 0
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(60.dp))
    }
}

@Composable
fun GalleryPage(previewImages: List<String?> = listOf()) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        state = rememberLazyGridState(),
        contentPadding = PaddingValues(16.dp),
        reverseLayout = false,
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.Start,
        userScrollEnabled = true,
    ) {
        items(previewImages.size) { index ->
            val imageUrl = previewImages.getOrNull(index)
            imageUrl?.let { imageUrl ->
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(100.dp)
                        .background(Color.Gray)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = "Gallery Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

@Composable
fun ReviewsPage(reviewList: MutableList<ReviewModel>) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Total ${reviewList.size} Reviews",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = 20.sp,
                fontStyle = FontStyle.Italic
            ),
            modifier = Modifier.padding(8.dp)
        )
        reviewList.sortByDescending { it.reviewTime }
        for (review in reviewList)
        CustomerReviewCard(
            /*TODO: have to add the reviews of barber*/
            review = review
        )
        Spacer(modifier = Modifier.height(60.dp))
    }
}