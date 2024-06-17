package com.practicecoding.sallonapp.appui.screens.MainScreens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.practicecoding.sallonapp.appui.Screens
import com.practicecoding.sallonapp.appui.components.GeneralButton
import com.practicecoding.sallonapp.appui.components.RowofDate
import com.practicecoding.sallonapp.appui.viewmodel.GetBarberDataViewModel
import com.practicecoding.sallonapp.data.model.BarberModel
import com.practicecoding.sallonapp.data.model.Service
import com.practicecoding.sallonapp.data.model.TimeSlot
import com.practicecoding.sallonapp.ui.theme.purple_200
import com.practicecoding.sallonapp.ui.theme.sallonColor
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.math.ceil

enum class SlotStatus {
    AVAILABLE, BOOKED, NOT_AVAILABLE
}


@Composable
fun TimeSelection(
    time: Int,
    date: LocalDate,
    navController: NavController,
    service: List<Service>,
    barber: BarberModel,
    genders: List<Int>,
    viewModel: GetBarberDataViewModel= hiltViewModel()
) {
    BackHandler {
        navController.popBackStack()
    }
//    var slotTime by remember {
//        mutableStateOf(restScreenViewModel.slots.value)
//    }
    val dayOfWeek = date.dayOfWeek
    val dayNameFull = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val context = LocalContext.current
    val slotTime = viewModel._slots.value
    LaunchedEffect(date) {
//        slotTime = restScreenViewModel.getSlots(dayNameFull, barber.uid, slotsViewModel)
        viewModel.getSlots(dayNameFull,barber.uid)
    }
    val startTime = LocalTime.parse(slotTime.StartTime, timeFormatter)
    val endTime = LocalTime.parse(slotTime.EndTime, timeFormatter)

    // Retrieve the booked and not available times for the selected date
    val bookedTimes = remember {
        mutableStateListOf<LocalTime>()
    }

    val notAvailableTimes = remember {
        mutableStateListOf<LocalTime>()
    }
if (date==LocalDate.parse(slotTime.date)) {
    bookedTimes.clear()
    for (timeString in slotTime.Booked!!) {
        val localTime = LocalTime.parse(timeString, timeFormatter)
        bookedTimes.add(localTime)
    }
}
    if (date==LocalDate.parse(slotTime.date)) {
        notAvailableTimes.clear()
    for (timeString in slotTime.NotAvailable!!) {
        val localTime = LocalTime.parse(timeString, timeFormatter)
        notAvailableTimes.add(localTime)
    }}

    val slots = generateTimeSlots(date,startTime, endTime, 30L, bookedTimes, notAvailableTimes)
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    val requiredSlots = ceil(time / 30.0).toInt()

    if (viewModel.selectedSlots.size > requiredSlots) {
        showDialog=true
        dialogMessage= "You doesn't need more than ${ceil(time/30.0).toInt()}"
        viewModel.selectedSlots.removeAt(viewModel.selectedSlots.size - 1)
    }

    Box(modifier = Modifier.fillMaxSize()){
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Time",
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "AVAILABLE",
                        color = Color.Gray,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "NOT AVAILABLE",
                        color = Color.Red,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "BOOKED",
                        color = sallonColor, // Update to match `sallonColor` if defined
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "YOUR BOOKING",
                        color = Color.Green,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
        slots.chunked(4).forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { slot ->
                    TimeSlotBox(
                        slot = slot,
                        timeFormatter = timeFormatter,
                        isSelected = viewModel.selectedSlots.contains(slot),
                        onClick = {
                            when (slot.status) {
                                SlotStatus.AVAILABLE -> {
                                    if (viewModel.selectedSlots.contains(slot)) {
                                        viewModel.selectedSlots.remove(slot)
                                    } else if(viewModel.selectedSlots.size>0&&viewModel.selectedSlots[0].date!=date.toString()){
                                        showDialog=true
                                        dialogMessage = "You can select Slot for only one day"
                                    }else {
                                        viewModel.selectedSlots.add(slot)
                                    }
                                }

                                SlotStatus.BOOKED, SlotStatus.NOT_AVAILABLE -> {
                                    showDialog = true
                                    dialogMessage = if (slot.status == SlotStatus.BOOKED) {
                                        "This slot is already booked."
                                    } else {
                                        "This slot is not available."
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }

        AnimatedVisibility(showDialog, enter = fadeIn(animationSpec = tween(500)),exit = fadeOut(animationSpec = tween(500))) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Selection Error") },
                text = { Text(dialogMessage) },
                confirmButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }


        Text(
            text = "Estimated total time you need according to your selection is approx $time mins",
            color = sallonColor, // Update to match `sallonColor` if defined
            fontWeight = FontWeight.SemiBold,
            textDecoration = TextDecoration.Underline,
            fontSize = 12.sp
        )
    }
        GeneralButton(text = "Continue", width = 300, modifier = Modifier.align(Alignment.BottomCenter)) {
            // Handle continue button click
            if (viewModel.selectedSlots.size < requiredSlots) {
                showDialog = true
                dialogMessage =
                    "Your selected time slots don't match the required time for your service."
            } else {
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    key = "selectedDateSlots",
                    value = mutableStateOf(viewModel.selectedSlots.toList())
                )
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    key = "date",
                    value = date
                )
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    key = "services",
                    value = service
                )
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    key = "barber",
                    value = barber
                )
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    key = "genders",
                    value = genders
                )
                navController.navigate(Screens.Appointment.route)
            }
        }}
}


@Composable
fun TimeSlotBox(
    slot: TimeSlot,
    timeFormatter: DateTimeFormatter,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isSelected -> Color.Green
        slot.status == SlotStatus.AVAILABLE -> Color.Gray
        slot.status == SlotStatus.BOOKED -> sallonColor // Update to match `sallonColor` if defined
        slot.status == SlotStatus.NOT_AVAILABLE -> Color.Red
        else -> Color.Transparent
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardColors(purple_200, purple_200, purple_200, purple_200),
        modifier = Modifier
            .size(width = 80.dp, height = 40.dp)
            .padding(4.dp)
            .clickable { onClick() }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = slot.time.format(timeFormatter),
                color = backgroundColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun daySelection(): LocalDate {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val currentDate = LocalDate.now()
    val monthName = currentDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
    val year = currentDate.year
    val daysToShow = (0..6).map { currentDate.plusDays(it.toLong()) }

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$monthName $year",
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            for (date in daysToShow) {
                val isSelected = date == selectedDate
                RowofDate(
                    isSelected = isSelected,
                    date = date.dayOfMonth.toString(),
                    day = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                ) {
                    selectedDate = date
                }
            }
        }

    }
    return selectedDate

}

fun generateTimeSlots(
    date:LocalDate,
    startTime: LocalTime,
    endTime: LocalTime,
    intervalMinutes: Long,
    bookedTimes: List<LocalTime>,
    notAvailableTimes: List<LocalTime>
): List<TimeSlot> {
    val slots = mutableListOf<TimeSlot>()
    var currentTime = startTime

    while (currentTime.isBefore(endTime) || currentTime == endTime) {
        val status = when {
            notAvailableTimes.contains(currentTime) -> SlotStatus.NOT_AVAILABLE
            bookedTimes.contains(currentTime) -> SlotStatus.BOOKED
            else -> SlotStatus.AVAILABLE
        }
        slots.add(TimeSlot(currentTime.toString(), date.toString(),status))
        currentTime = currentTime.plus(intervalMinutes, ChronoUnit.MINUTES)
    }

    return slots
}

