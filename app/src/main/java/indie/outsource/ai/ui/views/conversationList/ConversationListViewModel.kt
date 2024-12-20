package indie.outsource.ai.ui.views.conversationList

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import indie.outsource.ai.data.ConversationRepository
import indie.outsource.ai.model.Model
import indie.outsource.ai.ui.views.modelList.ModelListModel
import indie.outsource.ai.ui.views.modelList.ModelListState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversationListViewModel @Inject constructor(
    private val repository: ConversationRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ConversationListState())
    val uiState: StateFlow<ConversationListState> = _uiState.asStateFlow()


    private fun getAllConversations(){
        CoroutineScope(Dispatchers.Default).launch {
            try{
                _uiState.value = ConversationListState(
                     conversationsList = repository.loadAllConversationsHeaders()
                )
            }
            catch (e : Exception){
                println(e.message)
            }
        }
    }

    init {
        getAllConversations()
    }

}


