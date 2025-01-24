package indie.outsource.ai.ui.views.modelList

import indie.outsource.ai.model.Model

data class ModelListModel (
    var model: Model,
    var isSelected: Boolean = false,
    var wasUsed:Boolean = false,
    var rating:Int = 0
)
