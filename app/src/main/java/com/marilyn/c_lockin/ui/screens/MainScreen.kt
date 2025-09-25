package com.marilyn.c_lockin.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import java.time.LocalTime
import com.marilyn.c_lockin.R

data class AlarmEvent(
    val name: String,
    val date: LocalDate,
    val time: String
)

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToCreateAlarm: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToNotifications: () -> Unit,
) {
    var currentYearMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    val events = remember {
        listOf(
            AlarmEvent("Reunión de trabajo", LocalDate.now(), "10:00 AM"),
            AlarmEvent("Cita médica", LocalDate.now().plusDays(1), "2:30 PM"),
            AlarmEvent("Llamada importante", LocalDate.now().plusDays(2), "4:00 PM"),
            AlarmEvent("Ejercicio", LocalDate.now().plusDays(3), "7:00 AM")
        )
    }

    // Filtrado
    val selectedDateEvents = events.filter { it.date == selectedDate }
    val upcomingEvents = events.filter { it.date >= LocalDate.now() }.take(4)

    Scaffold(
        containerColor = Color(0xFFFFFFFF),
        topBar = {
            TopAppBar(
                title = {
                    val titleFont = FontFamily(Font(R.font.adlam_display))
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "C-Lock In",
                            fontSize = 32.sp,
                            fontFamily = titleFont,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6B63FF)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFFFFF)
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.LightGray.copy(alpha = 0.8f),
                contentColor = Color.Black
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    IconButton(onClick = onNavigateToNotifications) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                    FloatingActionButton(
                        onClick = onNavigateToCreateAlarm,
                        containerColor = Color(0xFF6C63FF),
                        contentColor = Color.White
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Alarm")
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(start = 16.dp,
                    top = 16.dp,
                    end = 16.dp,
                    bottom = 0.dp)
                .background(Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            // Calendario
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Month/Year navigation
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            currentYearMonth = currentYearMonth.minusMonths(1)
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous month")
                        }

                        Text(
                            text = "${currentYearMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentYearMonth.year}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )

                        IconButton(onClick = {
                            currentYearMonth = currentYearMonth.plusMonths(1)
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next month")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Days of week header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf("Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb").forEach { day ->
                            Text(
                                text = day,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Grid
                    CalendarGrid(
                        yearMonth = currentYearMonth,
                        selectedDate = selectedDate,
                        onDateSelected = { selectedDate = it },
                        eventsMap = events.groupBy { it.date }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (selectedDateEvents.isNotEmpty()) {
                Text(
                    text = "Eventos del ${selectedDate.format(DateTimeFormatter.ofPattern("d 'de' MMMM", Locale.getDefault()))}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(selectedDateEvents) { event ->
                        EventCard(event = event)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            } else {
                Text(
                    text = "Próximos eventos",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(upcomingEvents) { event ->
                        EventCard(event = event)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarGrid(
    yearMonth: YearMonth,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    eventsMap: Map<LocalDate, List<AlarmEvent>>
) {
    val firstDayOfMonth = yearMonth.atDay(1)
    val firstWeekday = firstDayOfMonth.dayOfWeek.value % 7 // Sunday = 0, Monday = 1, etc.
    val daysInMonth = yearMonth.lengthOfMonth()

    val calendarDates = mutableListOf<LocalDate?>()

    repeat(firstWeekday) {
        calendarDates.add(null)
    }

    for (day in 1..daysInMonth) {
        calendarDates.add(yearMonth.atDay(day))
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.height(250.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(calendarDates.size) { index ->
            val date = calendarDates[index]
            if (date != null) {
                CalendarDay(
                    date = date,
                    isSelected = date == selectedDate,
                    isToday = date == LocalDate.now(),
                    hasEvents = eventsMap.containsKey(date),
                    onClick = { onDateSelected(date) }
                )
            } else {
                // Empty cell
                Spacer(modifier = Modifier.size(40.dp))
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarDay(
    date: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    hasEvents: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(
                color = when {
                    isSelected -> Color(0xFF6C63FF)
                    isToday -> Color(0xFF6C63FF).copy(alpha = 0.3f)
                    else -> Color.Transparent
                },
                shape = CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                color = when {
                    isSelected -> Color.White
                    isToday -> Color(0xFF6C63FF)
                    else -> Color.Black
                },
                fontSize = 14.sp,
                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
            )

            if (hasEvents) {
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .background(
                            color = if (isSelected) Color.White else Color(0xFF6C63FF),
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventCard(event: AlarmEvent) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = event.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = event.date.format(DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM", Locale.getDefault())),
                    fontSize = 14.sp,
                    color = Color(0xFF6C63FF)
                )
            }

            Text(
                text = event.time,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@SuppressLint("DefaultLocale")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAlarmScreen(
    onNavigateBack: () -> Unit,
    onCreateAlarm: (String, LocalDate, Int, Int, String) -> Unit = { _, _, _, _, _ -> }
) {
    var alarmName by remember { mutableStateOf("") }
    var selectedHour by remember { mutableStateOf(LocalTime.now().hour) }
    var selectedMinute by remember { mutableStateOf(0) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var repetition by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Color(0xFFFFFFFF),
        topBar = {
            TopAppBar(
                title = { Text("Crear Alarma") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Name field
            OutlinedTextField(
                value = alarmName,
                onValueChange = { alarmName = it },
                label = { Text("Nombre") },
                placeholder = { Text("Nombre del evento") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4A90E2),
                    focusedLabelColor = Color(0xFF4A90E2),
                    unfocusedBorderColor = Color.Gray,
                    unfocusedLabelColor = Color.Gray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Time picker
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Hour picker
                Card(
                    modifier = Modifier
                        .size(120.dp, 80.dp)
                        .clickable {
                            selectedHour = (selectedHour + 1) % 24
                        },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF6C63FF)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = String.format("%02d", selectedHour),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Text(
                    text = ":",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

                // Minute picker
                Card(
                    modifier = Modifier
                        .size(120.dp, 80.dp)
                        .clickable {
                            selectedMinute = (selectedMinute + 15) % 60
                        },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF6C63FF)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = String.format("%02d", selectedMinute),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = "Hora",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Minuto",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Día
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    var selectedDay by remember { mutableStateOf("Lun") } // <- Aquí con remember

                    Text(
                        text = "Día seleccionado: $selectedDay",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    WeekDaySelector(
                        selectedDay = selectedDay,
                        onDaySelected = { selectedDay = it }
                    )
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Repetition field
            OutlinedTextField(
                value = repetition,
                onValueChange = { repetition = it },
                label = { Text("Repetición") },
                placeholder = { Text("Diario, Semanal, etc.") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4A90E2),
                    focusedLabelColor = Color(0xFF4A90E2),
                    unfocusedBorderColor = Color.Gray,
                    unfocusedLabelColor = Color.Gray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            // Create button
            Button(
                onClick = {
                    onCreateAlarm(alarmName, selectedDate, selectedHour, selectedMinute, repetition)
                    onNavigateBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6C63FF)
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = "Crear Alarma",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

}

@Composable
fun WeekDaySelector(
    selectedDay: String,
    onDaySelected: (String) -> Unit
) {
    val daysOfWeek = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        daysOfWeek.forEach { day ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(if (day == selectedDay) Color(0xFF6C63FF) else Color.Transparent)
                    .clickable { onDaySelected(day) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day,
                    color = if (day == selectedDay) Color.White else Color.Gray,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp
                )
            }
        }
    }
}



