package com.example.growreminder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.growreminder.sign_in.AuthViewModel
import com.example.growreminder.sign_in.AuthState
import com.example.growreminder.ui.screens.DailyMotivationScreen
import com.example.growreminder.ui.screens.HealthChoiceScreen
import com.example.growreminder.ui.screens.LoginPage
import com.example.growreminder.ui.screens.NewSkillChoiceScreen
import com.example.growreminder.ui.screens.PersonalDevelopmentScreen
import com.example.growreminder.ui.screens.ProfileScreen
import com.example.growreminder.ui.screens.ScheduleListScreen
import com.example.growreminder.ui.screens.ScheduleScreen
import com.example.growreminder.ui.screens.SignupPage
import com.example.growreminder.ui.screens.StudyChoiceScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    isFromNotification: Boolean = false,
    notificationDestination: String? = null
) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    // Xác định điểm bắt đầu dựa trên trạng thái xác thực và nguồn mở app
    val startDestination = remember {
        // Nếu từ thông báo và người dùng đã đăng nhập
        if (isFromNotification && FirebaseAuth.getInstance().currentUser != null) {
            notificationDestination ?: "profile"
        } else if (FirebaseAuth.getInstance().currentUser != null) {
            "profile"
        } else {
            "login"
        }
    }

    // Xử lý đặc biệt khi từ thông báo nhưng chưa đăng nhập
    LaunchedEffect(authState) {
        if (isFromNotification &&
            FirebaseAuth.getInstance().currentUser == null &&
            authState is AuthState.Authenticated) {
            // Sau khi đăng nhập thành công, chuyển đến điểm đến từ thông báo
            navController.navigate(notificationDestination ?: "schedule_list") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login") {
            LoginPage(
                navController = navController,
                authViewModel = authViewModel,
                notificationDestination = if (isFromNotification) notificationDestination else null
            )
        }

        composable("signup") {
            SignupPage(navController = navController, authViewModel = authViewModel)
        }

        composable("home") {
            DailyMotivationScreen(navController)
        }

        composable("personalDevelopment") {
            PersonalDevelopmentScreen(navController, authViewModel)
        }

        composable("profile") {
            ProfileScreen(navController, authViewModel)
        }

        composable(
            route = "schedule/{taskName}",
            arguments = listOf(navArgument("taskName") { type = NavType.StringType })
        ) { backStackEntry ->
            val taskName = backStackEntry.arguments?.getString("taskName") ?: "Đọc sách"
            ScheduleScreen(navController = navController, taskName = taskName)
        }

        composable("schedule") {
            ScheduleScreen(navController)
        }

        composable("schedule_list") {
            ScheduleListScreen(navController)
        }

        composable("studyChoice") {
            StudyChoiceScreen(navController)
        }

        composable("healthChoice") {
            HealthChoiceScreen(navController)
        }

        composable("newSkillChoice") {
            NewSkillChoiceScreen(navController)
        }
    }
}