package indie.outsource.ai.ui.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import indie.outsource.ai.data.ModelRepository
import indie.outsource.ai.model.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ModelRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()


    private fun completeText(text : String){
        CoroutineScope(Dispatchers.Default).launch {
            try{
                val response : String = repository.getCompletion(text)
                println(response);
                addMessage(
                    Message(response,false)
                )
            }
            catch (e : Exception){
                println(e.message)
            }
        }
    }

    private fun addMessage(item: Message) {
        _uiState.value = HomeState(messages = uiState.value.messages + item)
    }

    fun addUserMessage(text : String) {
        addMessage(
            Message(text,true)
        )
        completeText(text)
    }
}


