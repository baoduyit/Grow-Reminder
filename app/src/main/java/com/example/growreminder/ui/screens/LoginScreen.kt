package com.example.growreminder.ui.screens


import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.growreminder.sign_in.AuthViewModel
import com.example.growreminder.sign_in.AuthState
import androidx.compose.ui.text.style.TextAlign



@SuppressLint("ResourceType")
@Composable
fun LoginPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    notificationDestination: String? = null
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by authViewModel.authState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                // Nếu có điểm đến từ thông báo, chuyển đến đó
                if (notificationDestination != null) {
                    navController.navigate(notificationDestination) {
                        popUpTo("login") { inclusive = true }
                    }
                } else {
                    navController.navigate("profile") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }
            is AuthState.Error -> {
                Toast.makeText(context, (authState as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6FC))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp, vertical = 40.dp), // Thay vì chỉ center
            verticalArrangement = Arrangement.Top, // Đẩy nội dung lên trên
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Welcome text
            Text(
                text = "Welcome to",
                fontSize = 25.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF4B0082),
                modifier = Modifier.padding(top = 70.dp, bottom = 4.dp)
            )

            // Logo text GrowMind
            Text(
                text = "GrowReminder",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4B0082),
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            // Main motivational text
            Text(
                text = "Tôi ở đây để cho bạn sự\nKỈ LUẬT. Bạn đã sẵn sàng\ncho sự thay đổi chưa?",
                textAlign = TextAlign.Center,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4B0082),
                modifier = Modifier.padding(top = 80.dp, bottom = 60.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { authViewModel.login(email, password) },
                enabled = authState != AuthState.Loading,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A5ACD)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Login", fontSize = 18.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { navController.navigate("signup") }) {
                Text(
                    text = "Don't have an account? Sign up",
                    color = Color(0xFF6A5ACD)
                )
            }
        }
    }
}