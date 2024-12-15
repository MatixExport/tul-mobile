package indie.outsource.ai.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import indie.outsource.ai.model.Message
import indie.outsource.ai.ui.theme.PurpleGrey80
import indie.outsource.ai.ui.theme.TestAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ConstraintLayoutContent(modifier = Modifier
                          .padding(innerPadding)
                          .padding(16.dp,32.dp))
                }
            }
        }
    }
}

@Composable
fun ConstraintLayoutContent(modifier: Modifier) {
    ConstraintLayout {
        val (msgList, textInput) = createRefs()
        val bottomGuideline = createGuidelineFromBottom(0.4f)

        MessageList(
                listOf<Message>(
                    Message("Start",false),
                    Message("Test2",true),
                    Message("Test3",false),
                    Message("Test1",false),
                    Message("Test2",true),
                    Message("Test3",false),
                    Message("Test1",false),
                    Message("Test2",true),
                    Message("Test3",false),
                    Message("Test1",false),
                    Message("Test2",true),
                    Message("Test3",false),
                    Message("Test3",false),
                    Message("Test1",false),
                    Message("Test2",true),
                    Message("Koniec",false)
                ),
            modifier = Modifier
                .then(modifier)
                .constrainAs(msgList) {
                top.linkTo(parent.top, margin = 64.dp)
                bottom.linkTo(textInput.top, margin = 32.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        TextInput(
            Modifier
                .then(modifier)
                .constrainAs(textInput) {
                bottom.linkTo(parent.bottom)
            }
        )
    }
}



@Composable
fun TextInput(modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf(TextFieldValue("")) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .then(modifier),
        contentAlignment = Alignment.Center
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
                    // TODO: Send the message
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
                .background(PurpleGrey80)
                .padding(16.dp),

        ) {
            Text(text = msg.text)
        }
    }

}


@Preview(showBackground = true, name = "wasd")
@Composable
fun GreetingPreview() {
    TestAppTheme {

    }
}