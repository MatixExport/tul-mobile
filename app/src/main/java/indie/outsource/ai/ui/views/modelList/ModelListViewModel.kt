package indie.outsource.ai.ui.views.modelList

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import indie.outsource.ai.data.ModelRepository
import indie.outsource.ai.model.Model
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.internal.filterList
import okhttp3.internal.toImmutableList
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
                    models = repository.getAllModels().map { model: Model ->
                        ModelListModel(model)
                    },
                    isLoading = false
                )
            }
            catch (e : Exception){
                println(e.message)
            }
        }
    }

    fun onModelClick(index:Int){
        _uiState.update { previousState ->
            val listOfModels = previousState.models.toMutableList()
            listOfModels[index] = listOfModels[index].copy(isSelected = !listOfModels[index].isSelected)
            previousState.copy(
                models = listOfModels.toImmutableList()
            )
        }
    }

    fun isAnyModelSelected() : Boolean{
        _uiState.value.models.forEach { model:ModelListModel->
            if(model.isSelected){
                return true
            }
        }
        return false
    }

    fun getSelectedModelsIds() : List<String>{
        val selectedModels: List<ModelListModel> = _uiState.value.models.filter { model : ModelListModel ->
            model.isSelected
        }
        return selectedModels.map { it.model.name }
    }

    init {
        getAllModels()
    }


}