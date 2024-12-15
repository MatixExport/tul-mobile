package indie.outsource.ai.model


enum class ModelType(val text: String) {
    TEXT("Text"),
    IMAGE("Image"),
    AUDIO("Audio")
}

data class Model (
    var name: String,
    var owner: String,
    var type: ModelType
)
