package indie.outsource.ai.ui.views.modelList

import indie.outsource.ai.model.Model
import indie.outsource.ai.model.response.GroqModel

data class ModelListState (
    val isLoading : Boolean = true,
    val models : List<ModelListModel> = listOf()
)