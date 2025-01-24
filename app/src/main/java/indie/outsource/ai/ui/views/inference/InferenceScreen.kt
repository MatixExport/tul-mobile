package indie.outsource.ai.ui.views.inference

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import indie.outsource.ai.model.Message
import indie.outsource.ai.ui.views.auth.signIn.FormField
import kotlin.math.max
import kotlin.math.min


@Composable
fun InferenceScreen(modifier: Modifier,
                    viewModel: InferenceViewModel = hiltViewModel(),
                    models:List<String> = listOf(),
                    conversationUuid:String = ""

){
    //This is messy
    viewModel.models = models

    if(conversationUuid.isNotEmpty()){
        viewModel.loadConversation(conversationUuid)
    }

    ConstraintLayoutContent(
        viewModel,
        modifier = Modifier
            .then(modifier)
            .padding(16.dp, 32.dp),
        onGoToPreviousModel = {index:Int,msg:Message ->
            viewModel.setMessageResponse(index, max(msg.currentResponseIndex-1,0))
        },
        onGoToNextModel = {index:Int,msg:Message ->
            viewModel.setMessageResponse(index,min((msg.currentResponseIndex+1),msg.responses.size-1))
        }
    )
}

@Composable
fun ConstraintLayoutContent(
    viewModel: InferenceViewModel,
    modifier: Modifier,
    onGoToPreviousModel : (index:Int,msg:Message)->Unit,
    onGoToNextModel : (index:Int,msg:Message)->Unit
) {
    val homeUiState by viewModel.uiState.collectAsState()

    ConstraintLayout {
        val (msgList, textInput,saveButton) = createRefs()
        val bottomGuideline = createGuidelineFromBottom(0.3f)
        val topGuideline = createGuidelineFromTop(150.dp)

        MessageList(
            homeUiState.messages,
            onGoToPreviousModel = onGoToPreviousModel,
            onGoToNextModel = onGoToNextModel,
            modifier = Modifier
                .then(modifier)
                .constrainAs(msgList) {
                    top.linkTo(topGuideline)
                    bottom.linkTo(textInput.top, margin = 32.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
//                    height = Dimension.fillToConstraints
                },

        )

        FloatingActionButton(
            onClick = {viewModel.showDialog()},
            modifier = Modifier
                .padding(8.dp,16.dp,0.dp,0.dp)
                .constrainAs(saveButton){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        ) {
            Icon(Icons.Rounded.Create, "Save conversation")
        }

        TextInput(
            onClick = { text: String -> viewModel.addUserMessage(text) },
            Modifier
                .then(modifier)
                .constrainAs(textInput) {
                    bottom.linkTo(parent.bottom)
                }
        )

        if(homeUiState.isDialogOpen){
            SaveConversationDialog(
                value = homeUiState.conversationTitle,
                onChange = {viewModel.setTitle(it)},
                onConfirmRequest = {viewModel.onConfirmSave()}
            ) {
                viewModel.hideDialog()
            }
        }
    }
}


@Composable
fun TextInput(onClick: (text:String) -> Unit,modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf(TextFieldValue("")) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .then(modifier),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = text,
                onValueChange = { newText: TextFieldValue ->
                    text = newText
                },
                placeholder = {
                    Text(text = "Type something")
                },
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.small,
            )
            IconButton(
                onClick = {
                    onClick(text.text)
                    text = TextFieldValue()
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send"
                )
            }
        }
    }
}




@Composable
fun MessageList(messages: List<Message>,
            modifier: Modifier = Modifier,
            onGoToPreviousModel : (index:Int,msg:Message)->Unit,
            onGoToNextModel : (index:Int,msg:Message)->Unit) {

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(10.dp)

    ) {
        itemsIndexed(messages, itemContent = {index,msg ->
            MessageBox(
                msg,
                index=index,
                onGoToPreviousModel = onGoToPreviousModel,
                onGoToNextModel = onGoToNextModel
            )
        })
    }
}

@Composable
fun MessageBox(msg: Message,
               index:Int,
               modifier: Modifier = Modifier,
               onGoToPreviousModel : (index:Int,msg:Message)->Unit,
               onGoToNextModel : (index:Int,msg:Message)->Unit

){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =  if (msg.isUserMessage) Arrangement.End else Arrangement.Start

    ) {
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 48f,
                        topEnd = 48f,
                        bottomStart = if (msg.isUserMessage) 48f else 0f,
                        bottomEnd = if (msg.isUserMessage) 0f else 48f
                    )
                )
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .padding(16.dp),

            ) {
            if(!msg.isUserMessage){
                val groqMsg = msg.responses[msg.currentResponseIndex]
                Column(){
                    Row(
                        Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        IconButton(
                            enabled = msg.currentResponseIndex > 0,
                            onClick = {
                                onGoToPreviousModel(index,msg)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = "Go back to previous model"
                            )
                        }
                        Text(groqMsg.modelId)
                        IconButton(
                            enabled = msg.currentResponseIndex < (msg.responses.size-1),
                            onClick = {
                                onGoToNextModel(index,msg)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Go to next model"
                            )
                        }
                    }
                    MessageContent(groqMsg.text)
                }
            }else{
                MessageContent(msg.text)
            }
        }
    }

}

@Composable
fun MessageContent(text:String){
    if(text.contains("```")){
        Column{
            val splitStrings = text.split("```")
            splitStrings.forEachIndexed { index, result ->
                if(index % 2 == 0){
                    Text(text = result,color = MaterialTheme.colorScheme.onSecondaryContainer)
                }
                else{
                    CodeSnippet(result)
                }
            }
        }
    }
    else{
        Text(text = text, color = MaterialTheme.colorScheme.onSecondaryContainer)
    }
}

@Composable
fun CodeSnippet(
    text: String
) {
    Text(
        text = text,
        style = TextStyle(
            fontFamily = FontFamily.Monospace,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Normal
        ),
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .padding(12.dp)
            .clip(RoundedCornerShape(12.dp))
    )
}

@Composable
fun SaveConversationDialog(value:TextFieldValue = TextFieldValue(),
                           onChange:(text:TextFieldValue)->Unit = {},
                           onConfirmRequest: () ->Unit = {},
                           onDismissRequest:()->Unit) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(20.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                Text(
                    text = "Save conversation",
                    modifier = Modifier
                        .padding(4.dp),
                    textAlign = TextAlign.Center,
                )
                FormField(
                    label = "Name",
                    placeholder = "Conversation name",
                    keyboardType = KeyboardType.Text,
                    value = value

                ) {
                    onChange(it)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Dismiss")
                    }
                    TextButton(
                        onClick = onConfirmRequest,
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}
