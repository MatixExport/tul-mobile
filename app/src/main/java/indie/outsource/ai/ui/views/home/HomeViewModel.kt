package indie.outsource.ai.ui.views.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import indie.outsource.ai.data.ConversationRepository
import indie.outsource.ai.ui.views.conversationList.ConversationListState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val conversationRepo : ConversationRepository
) : ViewModel() {



    private val conversationCount = 5;
    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()


    private fun loadRecentConversations(){
        CoroutineScope(Dispatchers.Default).launch {
            try{
                _uiState.value = HomeState(
                    conversationsList = conversationRepo.loadRecentConversations(conversationCount)
                )
            }
            catch (e : Exception){
                println(e.message)
            }
        }
    }

    init{loadRecentConversations()}

}