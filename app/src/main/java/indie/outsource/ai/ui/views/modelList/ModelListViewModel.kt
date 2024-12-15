package indie.outsource.ai.ui.views.modelList

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import indie.outsource.ai.data.ModelRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModelListViewModel @Inject constructor(
    private val repository: ModelRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ModelListState())
    val uiState: StateFlow<ModelListState> = _uiState.asStateFlow()


    private fun getAllModels(){
        CoroutineScope(Dispatchers.Default).launch {
            try{
                _uiState.value = ModelListState(
                    models = repository.getAllModels(),
                    isLoading = false
                )
            }
            catch (e : Exception){
                println(e.message)
            }
        }
    }

    fun onModelClick(){

    }

    init {
        getAllModels()
    }


}