package com.practicecoding.sallonapp.appui.screens.MainScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.practicecoding.sallonapp.R
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.practicecoding.sallonapp.appui.Screens
import com.practicecoding.sallonapp.appui.components.BackButtonTopAppBar
import com.practicecoding.sallonapp.appui.components.Categories
import com.practicecoding.sallonapp.appui.components.DoubleCard
import com.practicecoding.sallonapp.appui.components.TransparentTopAppBar

data class Category(val image: Int, val name: String)

val categories = listOf(
    Category(image = R.drawable.shaving, name = "Shaves"),
    Category(image = R.drawable.haircut, name = "Hair Cut"),
    Category(image = R.drawable.down, name = "Nail Cut"),
    Category(image = R.drawable.haircolor, name = "Hair Color"),
    Category(image = R.drawable.makeup, name = "Makeup"),
    Category(image = R.drawable.nails, name = "Nail Cut")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    navController: NavController
){
    BackHandler {
        navController.popBackStack()
    }
    DoubleCard(
        midCardAble = false,
        midCarBody ={},
        mainScreen = { CategoriesLinedUp(navController) },
        topAppBar = {
            BackButtonTopAppBar(
            onBackClick = { navController.popBackStack() },
            title = "Categories"
        )}
    )
}
@Composable
fun CategoriesLinedUp(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(categories.chunked(4)) { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    rowItems.forEach { category ->
                        Categories(
                            image = category.image,
                            categories = category.name
                        ) {
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                key = "service",
                                value = category.name
                            )
                            navController.navigate(Screens.CatBarberList.route)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoriesScreenPreview() {
//    CategoriesScreen()
}