package indie.outsource.ai.model

data class Message (
    var text : String,
    var currentResponseIndex:Int = 0,
    var responses: MutableList<GroqMessage> = mutableListOf(),
    var isUserMessage: Boolean
);

