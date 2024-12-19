package indie.outsource.ai.ui.views.auth.singUp

import androidx.compose.ui.text.input.TextFieldValue

data class SignUpState (
    var email: TextFieldValue = TextFieldValue(),
    var password: TextFieldValue = TextFieldValue(),
    var isPasswordVisible: Boolean = false,
    var username : TextFieldValue = TextFieldValue(),
    var isLoading: Boolean = false,
    var isDone : Boolean = false,
    var error : String = ""
)