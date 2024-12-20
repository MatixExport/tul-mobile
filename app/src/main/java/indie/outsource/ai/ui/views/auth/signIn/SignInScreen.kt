package indie.outsource.ai.ui.views.auth.signIn

import android.content.Context
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseUser
import indie.outsource.ai.ui.views.auth.credentialManagerSignIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


fun startAuth(context:Context,onSignInComplete: (user: FirebaseUser) -> Unit){
    CoroutineScope(Dispatchers.Main).launch {
        try{
            val user : FirebaseUser = credentialManagerSignIn(context)
            onSignInComplete(user)
        }
        catch (e : Exception){
            println(e.message)
        }
    }
}


@Composable
fun SignInScreen(modifier: Modifier,
                 viewModel: SignInViewModel = hiltViewModel(),
                 onSignUpClick:()->Unit,
                 onSignInComplete:(user:FirebaseUser)->Unit) {


    val signInUiState by viewModel.uiState.collectAsState()

    if(signInUiState.credentialManagerPopUp) {
        startAuth(LocalContext.current) { user: FirebaseUser ->
            onSignInComplete(user)
        }
        viewModel.disableCredentialManagerPopUp()
    }

    if(signInUiState.isDone){
        viewModel.setIsNotDone()
        viewModel.auth.currentUser?.let { onSignInComplete(it) }
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
                text = "Sign In",
                style = MaterialTheme.typography.headlineMedium,
            )

            FormField(
                label = "Email",
                placeholder = "Enter your email",
                keyboardType = KeyboardType.Email,
                value = signInUiState.email
            ) { mail ->
                viewModel.setEmail(mail)
            }

            PasswordField(
                value = signInUiState.password
            ) {pass ->
                viewModel.setPassword(pass)
            }


            if(signInUiState.error.isNotEmpty()){
                Text(
                    signInUiState.error,
                    color = MaterialTheme.colorScheme.error
                )
            }

            // Sign In Button
            Button(
                onClick = { viewModel.signIn() },
                modifier = Modifier.fillMaxWidth(),
                enabled = signInUiState.email.text.isNotBlank() && signInUiState.password.text.isNotBlank()
            ) {
                Text(text = "Sign In")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Redirect to Sign Up
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Don't have an account?")
                Spacer(modifier = Modifier.width(2.dp))
                TextButton(onClick = {onSignUpClick()}) {
                    Text("Sign Up")
                }
            }
        }
    }
}

@Composable
fun FormField(
    modifier: Modifier = Modifier,
    label : String,
    placeholder: String,
    keyboardType: KeyboardType,
    value:TextFieldValue,
    onChange:(text:TextFieldValue)->Unit
){
    OutlinedTextField(
        value = value,
        onValueChange = { onChange(it) },
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Next
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp,0.dp,16.dp,0.dp),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordField(modifier: Modifier = Modifier,value:TextFieldValue,onChange:(text:TextFieldValue)->Unit) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {onChange(it)},
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Preview(showBackground = true)
@Composable
fun SignInScreenPreview() {
    SignInScreen(modifier = Modifier,onSignUpClick={}, onSignInComplete = {})
}