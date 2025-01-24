package indie.outsource.ai.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser

class MainViewModel : ViewModel() {
    var user: FirebaseUser? by mutableStateOf(null)
}