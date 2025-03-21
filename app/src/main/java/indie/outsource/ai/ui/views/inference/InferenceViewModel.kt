package indie.outsource.ai.ui.views.inference

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import indie.outsource.ai.data.ConversationRepository
import indie.outsource.ai.data.ModelRepository
import indie.outsource.ai.model.Conversation
import indie.outsource.ai.model.GroqMessage
import indie.outsource.ai.model.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import okhttp3.internal.toImmutableList
import javax.inject.Inject

@HiltViewModel
class InferenceViewModel @Inject constructor(
    private val repository: ModelRepository,
    private val conversationRepository: ConversationRepository
) : ViewModel() {
    //This is really messy
    var models : List<String> = listOf()
    private var conversationUuid : String = ""
    private val _uiState = MutableStateFlow(InferenceState())
    val uiState: StateFlow<InferenceState> = _uiState.asStateFlow()




    private fun completeText(text: String) {
        val addMessageLock = Mutex()
        val msg = Message("",isUserMessage = false);
        var wasAdded : Boolean = false;
        var msgIndex : Int = 0;
        for (model in models) {
            CoroutineScope(Dispatchers.Default).launch {
                try{
                    val response : String = repository.getCompletion(text,model);

                    //First response before the message was even added
                    addMessageLock.lock()
                    if(!wasAdded){
                        wasAdded = true;
                        msgIndex = uiState.value.messages.size
                        msg.responses.add(GroqMessage(model,response))
                        addMessage(msg)
                    }
                    else{
                        addResponseToMessage(msgIndex,GroqMessage(model,response))
                    }
                    addMessageLock.unlock()

                }
                catch (e : Exception){
                    println(e.message)
                }
            }
        }
    }

    private fun addMessage(item: Message) {
        _uiState.update { previousState ->
            previousState.copy(
                messages = uiState.value.messages + item
            )
        }}

    fun addUserMessage(text : String) {
        addMessage(
            Message(text, isUserMessage = true)
        )
        completeText(text)
    }

    private fun updateIsDialogOpen(isShown:Boolean){
        _uiState.update { previousState ->
            previousState.copy(
                isDialogOpen = isShown
            )
        }
    }

    fun showDialog(){
        updateIsDialogOpen(true)
    }

    fun hideDialog(){
        updateIsDialogOpen(false)
    }

    fun setTitle(text: TextFieldValue){
        _uiState.update { previousState ->
            previousState.copy(
                conversationTitle = text
            )
        }
    }

    fun onConfirmSave(){
        CoroutineScope(Dispatchers.Default).launch {
            try{
               conversationRepository.saveConversation(
                   Conversation(
                        documentId = conversationUuid.ifEmpty { "" },
                        title = uiState.value.conversationTitle.text,
                        messages = _uiState.value.messages,
                        models = models,
                        lastVisited = Timestamp.now()
                   )
               )
            }
            catch (e : Exception){
                println(e.message)
            }
        }
        hideDialog()
    }

    fun clearTitle(){
        setTitle(TextFieldValue())
    }

    fun loadConversation(uuid:String){
        this.conversationUuid = uuid
        _uiState.update { previousState ->
            previousState.copy(
                isLoading = true
            )
        }
        CoroutineScope(Dispatchers.Default).launch {
            try{
                val result = conversationRepository.loadConversationDetails(uuid)
                models = result.models
                _uiState.update { previousState ->
                    previousState.copy(
                        messages = result.messages,
                        conversationTitle =  TextFieldValue(result.title),
                        isLoading = false
                    )
                }

            }
            catch (e : Exception){
                println(e.message)
            }
        }
    }

    private fun addResponseToMessage(messageIndex: Int, newResponse: GroqMessage) {
        _uiState.value = _uiState.value.copy(
            messages = _uiState.value.messages.toMutableList().apply {
                this[messageIndex] = this[messageIndex].copy(
                    responses = this[messageIndex].responses.toMutableList().apply {
                        add(newResponse)
                    }
                )
            }
        )
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


