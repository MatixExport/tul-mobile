package indie.outsource.ai.ui.views.conversationList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import indie.outsource.ai.model.Conversation

@Composable
fun ConversationListScreen(viewModel: ConversationListViewModel = hiltViewModel(),onConversationClick:(Conversation)->Unit = {}) {
    val conversationListUiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
    ){
        items(conversationListUiState.conversationsList, itemContent = { item ->
            ConversationRow(item){conv ->
                onConversationClick(conv)
            }
        })
    }
}



@Composable
fun ConversationRow(conversation: Conversation, onClick: (Conversation) -> Unit = {}){
    Row(
        modifier = Modifier
            .padding(0.dp,0.dp,0.dp,12.dp)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick(conversation) },

        ){
        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {
            Text(
                text = conversation.title,
            )
            Text(
                text = conversation.models[0],
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
                onClick = {onClick(conversation)},
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                    contentDescription = "Load Conversation"
                )
            }
        }
    }
}