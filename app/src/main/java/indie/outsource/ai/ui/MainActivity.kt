package indie.outsource.ai.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import indie.outsource.ai.ui.navigation.Navbar
import indie.outsource.ai.ui.navigation.Routes
import indie.outsource.ai.ui.theme.TestAppTheme
import indie.outsource.ai.ui.views.auth.signIn.SignInScreen
import indie.outsource.ai.ui.views.auth.singUp.SignUpScreen
import indie.outsource.ai.ui.views.home.HomeScreen
import indie.outsource.ai.ui.views.inference.InferenceScreen
import indie.outsource.ai.ui.views.modelList.ModelListScreen


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestAppTheme {
                NavComposable()
            }
        }
    }
}

@Composable
fun NavComposable(modifier: Modifier = Modifier){
    val viewModel : MainViewModel = hiltViewModel()
        val navController: NavHostController = rememberNavController()

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            bottomBar = {
                println("MainActivity:BottonBar")
                if((navController.currentDestination?.route != Routes.SignIn.name)&&
                    (navController.currentDestination?.route != Routes.SignUp.name)) {
                    Navbar(
                        navController = navController,
                        onClick = { dest: String ->
                            navController.navigate(dest)
                        }
                    )
                }
            }
        ) { innerPadding ->
            NavHost(
                modifier = Modifier.padding(innerPadding),
                navController = navController,
                startDestination = Routes.SignIn.name,
            ) {
                composable(route = Routes.Home.name) {
                    HomeScreen(modifier, mainViewModel = viewModel)
                }
                composable(route = Routes.ModelList.name) {
                    ModelListScreen(
                        modifier,
                        onItemClick = { id ->
                            navController.navigate("${Routes.Inference.name}/$id")
                        }
                    )
                }
                composable("${Routes.Inference.name}/{modelId}") { backStackEntry ->
                    InferenceScreen(
                        modifier = modifier,
                        modelId = backStackEntry.arguments?.getString("modelId")!!
                    )
                }
                composable(route = Routes.SignIn.name) {
                    SignInScreen(
                        modifier = modifier,
                        onSignUpClick = {
                            navController.navigate(Routes.SignUp.name)
                        },
                        onSignInComplete = { loadedUser: FirebaseUser ->
                            navController.navigate(Routes.Home.name)
                            viewModel.user = loadedUser

                        }
                    )
                }
                composable(route = Routes.SignUp.name){
                    SignUpScreen(
                        onSignInClick = {
                            navController.navigate(Routes.SignIn.name)
                        },
                        onSignUpComplete = {loadedUser: FirebaseUser ->
                            navController.navigate(Routes.Home.name)
                            viewModel.user = loadedUser
                        }
                    )
                }
            }
        }
}




