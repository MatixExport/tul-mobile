package indie.outsource.ai.ui.views.modelList

import indie.outsource.ai.model.AccountData
import indie.outsource.ai.model.Model
import indie.outsource.ai.model.ModelData

data class ModelListModel (
    var model: Model,
    var isSelected: Boolean = false,
    var wasUsed:Boolean = false,
    var modelData: ModelData = ModelData(),
    var desc:String = "Short but insightful and helpful description.",
    var year:String = "2020"
)
