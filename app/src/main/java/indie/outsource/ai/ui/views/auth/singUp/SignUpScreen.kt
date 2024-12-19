package indie.outsource.ai.ui.views.auth.singUp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseUser
import indie.outsource.ai.ui.views.auth.signIn.FormField
import indie.outsource.ai.ui.views.auth.signIn.PasswordField

@Composable
fun SignUpScreen(modifier: Modifier = Modifier,
                 viewModel: SignUpViewModel = hiltViewModel(),
                 onSignInClick:()->Unit,
                 onSignUpComplete:(user: FirebaseUser)->Unit) {


    val signUpUiState by viewModel.uiState.collectAsState()

    if(signUpUiState.isDone){
        println("isDone: in SingUpScreen")
        viewModel.setIsNotDone()
        viewModel.auth.currentUser?.let { onSignUpComplete(it) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.headlineMedium,
            )

            FormField(
                label = "Username",
                placeholder = "Enter your username",
                keyboardType = KeyboardType.Text,
                value = signUpUiState.username
            ) { mail ->
                viewModel.setUsername(mail)
            }

            FormField(
                label = "Email",
                placeholder = "Enter your email",
                keyboardType = KeyboardType.Email,
                value = signUpUiState.email
            ) { mail ->
                viewModel.setEmail(mail)
            }

            PasswordField(
                value = signUpUiState.password
            ) {pass ->
                viewModel.setPassword(pass)
            }

            if(signUpUiState.error.isNotEmpty()){
                Text(
                    signUpUiState.error,
                    color = MaterialTheme.colorScheme.error
                )
            }
            // Sign In Button
            Button(
                onClick = {
                    viewModel.signUp()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = signUpUiState.email.text.isNotBlank() && signUpUiState.password.text.isNotBlank() &&
                signUpUiState.username.text.isNotBlank()
            ) {
                Text(text = "Sign Up")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Redirect to Sign Up
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Already have an account?")
                Spacer(modifier = Modifier.width(2.dp))
                TextButton(onClick = {onSignInClick()}) {
                    Text("Sign In")
                }
            }
        }
    }
}