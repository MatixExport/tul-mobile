package indie.outsource.ai.ui.views.modelList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import indie.outsource.ai.data.AccountDataRepository
import indie.outsource.ai.data.ModelDataRepository
import indie.outsource.ai.data.ModelRepository
import indie.outsource.ai.model.AccountData
import indie.outsource.ai.model.Model
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList
import javax.inject.Inject

@HiltViewModel
class ModelListViewModel @Inject constructor(
    private val repository: ModelRepository,
    private val accountRepository: AccountDataRepository,
    private val modelDataRepository: ModelDataRepository,
    private val externalScope: CoroutineScope
) : ViewModel() {
    private var accountData : AccountData? = null
    private val _uiState = MutableStateFlow(ModelListState())
    val uiState: StateFlow<ModelListState> = _uiState.asStateFlow()



    private fun getAllModels(){
        viewModelScope.launch {
            try{
                _uiState.value = ModelListState(
                    models = repository.getAllModels().sortedBy { it.name }.map { model: Model ->
                        ModelListModel(model)
                    },
                    isLoading = false
                )
            }
            catch (e : Exception){
                println(e.message)
            }
        }
        viewModelScope.launch {
            try{
                accountData = accountRepository.loadAccountData()
                _uiState
                    .asStateFlow()
                    .filter { !it.isLoading }
                    .first()
                _uiState.update { previousState ->
                    previousState.copy(
                       models = previousState.models.map { modelListModel: ModelListModel ->
                           modelListModel.copy(
                               wasUsed = modelListModel.model.name in accountData!!.usedModels
                           )
                       },
                        accountData = accountData!!,
                        isLoading = false
                    )
                }
            }
            catch (e : Exception){
                println(e.message)
            }
        }
        viewModelScope.launch {
            try{
                val modelData = modelDataRepository.loadModelsData();
                _uiState
                    .asStateFlow()
                    .filter { !it.isLoading }
                    .first()
                _uiState.update { previousState ->
                    previousState.copy(
                        models = previousState.models.map { modelListModel: ModelListModel ->
                            val foundModelData = modelData.find { it.modelId == modelListModel.model.name }
                            if (foundModelData != null) {
                                modelListModel.copy(
                                    modelData = foundModelData
                                )
                            } else {
                                modelListModel
                            }
                        },
                        isLoading = false
                    )
                }
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

    fun onLikeClick(index:Int){
        _uiState.update { previousState ->
            val listOfModels = previousState.models.toMutableList()
            if(accountData == null){
                return;
            }
            var newLikedBy: List<String>? = null
            if(accountData!!.userId in listOfModels[index].modelData.likedBy){
                newLikedBy = listOfModels[index].modelData.likedBy.filter{it != accountData!!.userId}

            }
            else{
                newLikedBy = listOfModels[index].modelData.likedBy + accountData!!.userId
            }
            listOfModels[index] = listOfModels[index].copy(
                modelData = listOfModels[index].modelData.copy(
                    likedBy = newLikedBy
                )
            )
            listOfModels[index] = listOfModels[index].copy(isSelected = !listOfModels[index].isSelected)
            previousState.copy(
                models = listOfModels.toImmutableList()
            )

        }
        viewModelScope.launch {
           modelDataRepository.toggleLikeModel(
               uiState.value.models[index].modelData.documentId,
               uiState.value.models[index].model.name
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

    fun handleOnQuery() {
        externalScope.launch {
            accountData?.let {
                accountRepository.updateAccountData(
                    it.copy(
                        usedModels = it.usedModels.union(getSelectedModelsIds()).toList()
                    )
                )
            }
        }
    }
    init {
        getAllModels()
    }

}