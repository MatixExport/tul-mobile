package indie.outsource.ai.ui.views.auth.signIn

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import indie.outsource.ai.ui.views.inference.InferenceState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(
    val auth : FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignInState())
    val uiState: StateFlow<SignInState> = _uiState.asStateFlow()

    fun setEmail(email:TextFieldValue){
        _uiState.update { previousState ->
            previousState.copy(
                email = email
            )
        }
    }

    fun setPassword(password:TextFieldValue){
        _uiState.update { previousState ->
            previousState.copy(
                password = password
            )
        }
    }

    private fun setIsDone(){
        _uiState.update { previousState ->
            previousState.copy(
                isLoading = false,
                isDone = true
            )
        }
    }

    fun setIsNotDone(){
        _uiState.update { previousState ->
            previousState.copy(
                isLoading = false,
                isDone = false
            )
        }
    }

    private fun setError(error:String){
        _uiState.update { previousState ->
            previousState.copy(
                error = error
            )
        }
    }

    fun disableCredentialManagerPopUp(){
        _uiState.update { previousState ->
            previousState.copy(
                credentialManagerPopUp = false
            )
        }
    }
    fun signIn(){
        auth.signInWithEmailAndPassword(uiState.value.email.text,uiState.value.password.text)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    setIsDone()
                } else {
                   setError(task.exception?.message.toString())
                }
            }
    }
}