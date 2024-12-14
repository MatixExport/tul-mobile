package outsource.indie.ai.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import outsource.indie.ai.model.Message
import outsource.indie.ai.ui.theme.PurpleGrey80
import outsource.indie.ai.ui.theme.TestAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                  MessageList(
                      listOf<Message>(
                          Message("Życze sobie śmierci",false),
                          Message("Rzecz to niesłychana",true),
                          Message("Była nam dana ziemia obiecana, idea naszych stosy ofiarne będą marne",false)
                      ),
                      modifier = Modifier
                          .padding(innerPadding)
                          .padding(16.dp,32.dp)
                  )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}



@Composable
fun MessageList(messages: List<Message>,modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        messages.forEach { message ->
            MessageBox(message)
        }
    }
}

@Composable
fun MessageBox(msg: Message,modifier: Modifier = Modifier){
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
        contentAlignment =  if (msg.isUserMessage) Alignment.CenterStart else Alignment.CenterEnd

    ) {
        Text(text = msg.text)
    }
}


@Composable
fun UserCard(){
    
}

@Preview(showBackground = true, name = "wasd")
@Composable
fun GreetingPreview() {
    TestAppTheme {
        Greeting("Android")
    }
}