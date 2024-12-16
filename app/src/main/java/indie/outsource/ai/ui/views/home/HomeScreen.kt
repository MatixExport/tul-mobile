package indie.outsource.ai.ui.views.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseUser
import indie.outsource.ai.model.Conversation

@Composable
fun HomeScreen(modifier: Modifier = Modifier,user:FirebaseUser?){
    HomeScreenItems(user=user)
}

@Composable
fun HomeScreenItems(
    modifier: Modifier = Modifier,
    onCardClicked: (String) -> Unit = {},
    user:FirebaseUser?
) {

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
    ){


        item {
            UserHomeSection(user = user)
        }
        item{
            WelcomeText()
        }
        item{
            BrowseModelsBox()
        }

        item{
            RecentConversations()
        }

    }

}

@Composable
fun UserHomeSection(modifier: Modifier = Modifier,user:FirebaseUser?){
    val image = painterResource(R.drawable.blank_profile_picture)
    Row(

    ){
        if(user != null){
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user.photoUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(30.dp))
            )
        }
        else{
            Image(
                painter = image,
                contentDescription = "profile picture",
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(30.dp))
            )
        }
        Column(
            modifier = Modifier
                .padding(8.dp,8.dp,0.dp,0.dp)
        ) {
            Text(
                text = "Hello",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = user?.displayName?.toString() ?: "Random User"
            )
        }
    }
}

@Composable
fun WelcomeText(modifier: Modifier = Modifier){
    val gradientColors = listOf(Color.Red, Color.Blue,Color.Magenta)
    Text(
        text = buildAnnotatedString {
            append("Welcome to the AI app made by")
            withStyle(
                SpanStyle(
                    brush = Brush.linearGradient(
                        colors = gradientColors
                    )
                )
            ) {
                append(" indie outsource")
            }
        },
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier
            .then(modifier)
            .padding(4.dp,48.dp,0.dp,48.dp)
    )
}

@Composable
fun BrowseModelsBox(){
    val image = painterResource(R.drawable.network_bg)
    Column{

        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                       56f
                    )
                )
                .padding(0.dp,0.dp,0.dp,0.dp)
        ){
            Image(
                painter = image,
                contentDescription = null
            )
            Text(
                text = "Browse Models",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .padding(8.dp,0.dp,16.dp,24.dp)
                    .align(Alignment.BottomEnd)
            )
        }
    }

}

@Composable
fun RecentConversations(modifier: Modifier = Modifier){
    val conversationList = listOf(
        Conversation(
            "My conversation1",
            listOf(),
            "lambda-8192-b"
        ),
        Conversation(
            "Example Conv",
            listOf(),
            "lambda-8192-b"
        ),
        Conversation(
            "New conv",
            listOf(),
            "lambda-8192-b"
        ),
        Conversation(
            "Lambada v1.2",
            listOf(),
            "lambda-8192-b"
        )
    )
    Column(
        modifier = Modifier
            .padding(0.dp,32.dp,0.dp,0.dp)
            .then(modifier)
    ) {
        Text(
            text = "Recent Conversations",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .padding(0.dp,0.dp,0.dp,16.dp)
                .then(modifier)
        )
        conversationList.forEach{conversation: Conversation ->
            ConversationRow(conversation)
        }

    }
}

@Composable
fun ConversationRow(conversation: Conversation, onClick: () -> Unit = {}){
    Row(
        modifier = Modifier
            .padding(0.dp,0.dp,0.dp,12.dp)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),

    ){
        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {
            Text(
                text = conversation.title,
            )
            Text(
                text = conversation.modelId,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier
                .padding(12.dp),
            horizontalAlignment = Alignment.End

        ) {
            IconButton(
                onClick = onClick,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                    contentDescription = "Load Conversation"
                )
            }
        }
    }
}





