package indie.outsource.ai.model

data class Conversation (
    var title : String,
    var messages : List<Message>,
    var modelId : String
)
