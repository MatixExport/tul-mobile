package indie.outsource.ai.ui.views.auth.singUp

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    var auth:FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpState())
    val uiState: StateFlow<SignUpState> = _uiState.asStateFlow()

    fun setEmail(email: TextFieldValue){
        _uiState.update { previousState ->
            previousState.copy(
                email = email
            )
        }
    }

    fun setPassword(password: TextFieldValue){
        _uiState.update { previousState ->
            previousState.copy(
                password = password
            )
        }
    }

    fun setUsername(username: TextFieldValue){
        _uiState.update { previousState ->
            previousState.copy(
                username = username
            )
        }
    }

    private fun setIsLoading(isLoading: Boolean){
        _uiState.update { previousState ->
            previousState.copy(
                isLoading = isLoading
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

    fun signUp(){
        setIsLoading(true)
        setError("")

        auth.createUserWithEmailAndPassword(_uiState.value.email.text,_uiState.value.password.text)
            .addOnFailureListener { e:Exception ->
                setError(e.message.toString())
            }

            .addOnCompleteListener { result: Task<AuthResult> ->
                if(result.isSuccessful){
                    println("ViewModel: Register Success")
                    setIsDone()
                }

            }
    }



}