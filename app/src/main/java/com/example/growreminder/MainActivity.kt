package com.example.growreminder

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.growreminder.ui.theme.GrowReminderTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.material3.Surface


import com.example.growreminder.ui.theme.GrowReminderTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        // Đã xóa dòng đăng xuất người dùng: FirebaseAuth.getInstance().signOut()
        enableEdgeToEdge()

        try {
            FirebaseApp.initializeApp(this)
            Log.d("Firebase", "Firebase initialized successfully")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val requestPermissionLauncher = registerForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted: Boolean ->
                    if (isGranted) {
                        Log.d("Permissions", "Notification permission granted")
                    } else {
                        Log.d("Permissions", "Notification permission denied")
                    }
                }

                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            // Test Firestore connection
            val db = FirebaseFirestore.getInstance()
            db.collection("test").document("test")
                .set(mapOf("test" to "value"))
                .addOnSuccessListener {
                    Log.d("Firebase", "Firestore connection successful")
                }
                .addOnFailureListener { e ->
                    Log.e("Firebase", "Firestore connection failed", e)
                }
        } catch (e: Exception) {
            Log.e("Firebase", "Firebase initialization failed", e)
        }

        // Kiểm tra xem app có được mở từ thông báo không
        val isFromNotification = intent.getBooleanExtra("is_from_notification", false)
        val notificationDestination = intent.getStringExtra("notification_destination")

        setContent {
            GrowReminderTheme {
                Scaffold { innerPadding ->
                    AppNavigation(
                        modifier = Modifier.padding(innerPadding),
                        isFromNotification = isFromNotification,
                        notificationDestination = notificationDestination
                    )
                }
            }
        }
    }
}