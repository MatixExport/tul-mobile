package indie.outsource.ai.ui.views.inference

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import indie.outsource.ai.data.ModelRepository
import indie.outsource.ai.model.GroqMessage
import indie.outsource.ai.model.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList
import javax.inject.Inject

@HiltViewModel
class InferenceViewModel @Inject constructor(
    private val repository: ModelRepository
) : ViewModel() {
    //This is really messy
    var modelId : String = ""
    var models : List<String> = listOf()
    private val _uiState = MutableStateFlow(InferenceState())
    val uiState: StateFlow<InferenceState> = _uiState.asStateFlow()


    private fun completeText(text : String){
        CoroutineScope(Dispatchers.Default).launch {
            try{

                val msg : Message = Message("",isUserMessage = false)

                models.forEach { id:String->
                    val response : String = repository.getCompletion(text,id)
                    println(response);
                    msg.responses.add(GroqMessage(id,response))
                }
                addMessage(
                    msg
                )

            }
            catch (e : Exception){
                println(e.message)
            }
        }
    }

    private fun addMessage(item: Message) {
        _uiState.value = InferenceState(messages = uiState.value.messages + item)
    }

    fun addUserMessage(text : String) {
        addMessage(
            Message(text, isUserMessage = true)
        )
        completeText(text)
    }

    fun setMessageResponse(messageIndex:Int,responseIndex:Int){
        _uiState.update { previousState ->
            val messages = previousState.messages.toMutableList()
            messages[messageIndex] = messages[messageIndex].copy(currentResponseIndex = responseIndex)
            previousState.copy(
                messages = messages.toImmutableList()
            )
        }
    }

}


