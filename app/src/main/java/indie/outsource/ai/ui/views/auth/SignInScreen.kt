package indie.outsource.ai.ui.views.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseUser
import indie.outsource.ai.data.AuthRepository
import indie.outsource.ai.data.AuthRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


fun useCredentialManager(authRepository: AuthRepository,onSignInComplete:(user:FirebaseUser) -> Unit){
    CoroutineScope(Dispatchers.Default).launch {
        try{
            val user : FirebaseUser = authRepository.signIn()
            onSignInComplete(user)
        }
        catch (e : Exception){
            println(e.message)
        }
    }
}

@Composable
fun SignInScreen(modifier: Modifier,onSignInComplete:(user:FirebaseUser) -> Unit) {
    val authRepository : AuthRepository = AuthRepositoryImpl(
        LocalContext.current,
    )

    useCredentialManager(authRepository,onSignInComplete)
}