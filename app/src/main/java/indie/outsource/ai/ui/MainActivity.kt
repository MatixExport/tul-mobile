package indie.outsource.ai.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import indie.outsource.ai.ui.navigation.Navbar
import indie.outsource.ai.ui.navigation.Routes
import indie.outsource.ai.ui.theme.TestAppTheme
import indie.outsource.ai.ui.views.auth.SignInScreen
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
    val navController : NavHostController = rememberNavController()
    var user by remember { mutableStateOf<FirebaseUser?>(null) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        bottomBar = {
            Navbar(
                onClick = {dest : String ->
                    navController.navigate(dest)
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = Routes.SignIn.name,
        ){
            composable(route = Routes.Home.name){
                HomeScreen(modifier,user)
            }
            composable(route = Routes.ModelList.name){
                ModelListScreen(
                    modifier,
                    onItemClick = {
                        id ->
                        navController.navigate("${Routes.Inference.name}/$id")
                    }
                )
            }
            composable("${Routes.Inference.name}/{modelId}"){backStackEntry ->
                InferenceScreen(
                    modifier=modifier,
                    modelId = backStackEntry.arguments?.getString("modelId")!!
                )
            }
            composable(Routes.SignIn.name){
                SignInScreen(
                    modifier = modifier,
                    onSignInComplete = {loadedUser:FirebaseUser->
                    navController.navigate(Routes.Home.name)
                        println(loadedUser.displayName)
//                    user = loadedUser

                }
                )
            }
        }
    }



}
