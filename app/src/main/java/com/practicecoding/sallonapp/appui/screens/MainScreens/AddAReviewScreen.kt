package com.practicecoding.sallonapp.appui.screens.MainScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.practicecoding.sallonapp.appui.Screens
import com.practicecoding.sallonapp.appui.components.RatingBar
import com.practicecoding.sallonapp.appui.components.ShimmerEffectMainScreen
import com.practicecoding.sallonapp.appui.viewmodel.OrderEvent
import com.practicecoding.sallonapp.appui.viewmodel.OrderViewModel
import com.practicecoding.sallonapp.data.model.OrderModel
import com.practicecoding.sallonapp.data.model.OrderStatus
import com.practicecoding.sallonapp.data.model.ReviewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AddReviewScreen(
    order: OrderModel,
    orderViewModel: OrderViewModel = hiltViewModel(),
    navController: NavController
) {
    var isLoading by remember { mutableStateOf(false) }
    var isReviewAdded by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var rating by remember { mutableStateOf(0.0) }
    var reviewText by remember { mutableStateOf("") }
    if(isLoading){
        ShimmerEffectMainScreen()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Image(
                    painter = rememberImagePainter(data = order.imageUrl),
                    contentDescription = "Order Image",
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = order.barberShopName, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Barber: ${order.barberName}", fontSize = 16.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Order Type: ${order.orderType.joinToString()}", fontSize = 16.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Time Slot: ${order.timeSlot.joinToString()}", fontSize = 16.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Phone: ${order.phoneNumber}", fontSize = 16.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Payment Method: ${order.paymentMethod}", fontSize = 16.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Date: ${order.date}", fontSize = 16.sp, color = Color.Black)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        RatingBar(
            rating= rating,
            onRatingChanged = { rating = it})
        Spacer(modifier = Modifier.height(2.dp))
        OutlinedTextField(
            value = reviewText,
            onValueChange = { reviewText = it },
            label = { Text(text = "Write your review", color = Color.Black) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            textStyle = LocalTextStyle.current.copy(color = Color.Black),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF6200EE),
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isLoading = true
                      val review =ReviewModel(
                            rating = rating,
                            reviewText = reviewText
                      )
                scope.launch(Dispatchers.IO){
                    orderViewModel.onEvent(OrderEvent.AddReview, orderId = order.orderId, review = review,
                        onCompletion = {
                            isReviewAdded = true
                        }
                        )
                }
                if(isReviewAdded){
                    isLoading = false
                    navController.navigate(Screens.MainScreen.route){
                        popUpTo(Screens.AddReviewScreen.route){
                            inclusive = true
                        }
                    }
                }else{
                    //Show error message
                }
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6200EE)),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Submit", color = Color.White)
        }
    }
}
