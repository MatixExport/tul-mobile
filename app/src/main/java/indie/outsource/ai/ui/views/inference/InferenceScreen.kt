package indie.outsource.ai.ui.views.inference

import android.content.res.Resources.Theme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import indie.outsource.ai.model.Message
import indie.outsource.ai.ui.theme.PurpleGrey80


@Composable
fun InferenceScreen(modifier: Modifier,viewModel: InferenceViewModel = hiltViewModel(),modelId:String){
    //This is messy
    viewModel.modelId = modelId
    ConstraintLayoutContent(
        viewModel,
        modifier = Modifier
            .then(modifier)
            .padding(16.dp, 32.dp)
    )
}

@Composable
fun ConstraintLayoutContent(
    viewModel: InferenceViewModel,
    modifier: Modifier
) {
    val homeUiState by viewModel.uiState.collectAsState()

    ConstraintLayout {
        val (msgList, textInput) = createRefs()
        val bottomGuideline = createGuidelineFromBottom(0.3f)
        val topGuideline = createGuidelineFromTop(150.dp)

        MessageList(
            homeUiState.messages,
            modifier = Modifier
                .then(modifier)
                .constrainAs(msgList) {
                    top.linkTo(topGuideline)
                    bottom.linkTo(textInput.top, margin = 32.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
//                    height = Dimension.fillToConstraints
                }
        )

        TextInput(
            onClick = { text: String -> viewModel.addUserMessage(text) },
            Modifier
                .then(modifier)
                .constrainAs(textInput) {
                    bottom.linkTo(parent.bottom)
                }
        )
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
fun MessageList(messages: List<Message>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(10.dp)

    ) {
        items(messages, itemContent = {msg ->
            MessageBox(msg)
        })
    }
}

@Composable
fun MessageBox(msg: Message, modifier: Modifier = Modifier){
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
            if(msg.text.contains("```")){
                Column{
                    val splitStrings = msg.text.split("```")
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
                Text(text = msg.text, color = MaterialTheme.colorScheme.onSecondaryContainer)
            }


        }
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
            color = Color.White,
            fontWeight = FontWeight.Normal
        ),
        modifier = Modifier
            .background(Color(0xFF2E3440))
            .padding(12.dp)
    )
}
