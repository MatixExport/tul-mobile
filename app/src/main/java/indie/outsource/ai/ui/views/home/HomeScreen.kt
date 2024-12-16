package indie.outsource.ai.ui.views.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R

@Composable
fun HomeScreen(modifier: Modifier){
    Text(modifier = modifier,text="Witajicie")
    HomeScreenItems()
}

@Composable
fun HomeScreenItems(
    modifier: Modifier = Modifier,
    onCardClicked: (String) -> Unit = {},
) {

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
    ){


        item {

            UserHomeSection()

        }
        item{
            WelcomeText()
        }
        item{
            BrowseModelsBox()
        }


        item{



        }


    }

}


@Composable
fun UserHomeSection(modifier: Modifier = Modifier){
    val image = painterResource(R.drawable.blank_profile_picture)
    Row(

    ){
        Image(
            painter = image,
            contentDescription = "profile picture",
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(30.dp))
        )
        Column(
            modifier = Modifier
                .padding(8.dp,8.dp,0.dp,0.dp)
        ) {
            Text(
                text = "Hello",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Random User"
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
fun RecentConversations(){

}





