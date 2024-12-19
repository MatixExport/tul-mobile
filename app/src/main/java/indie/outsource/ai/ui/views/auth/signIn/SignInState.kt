package indie.outsource.ai.ui.views.auth.signIn

import androidx.compose.ui.text.input.TextFieldValue

data class SignInState (
    var email: TextFieldValue = TextFieldValue(),
    var password: TextFieldValue = TextFieldValue(),
    var isPasswordVisible: Boolean = false,
    var isLoading: Boolean = false,
    var credentialManagerPopUp:Boolean = true,
    var isDone : Boolean = false,
    var error : String = ""
)
