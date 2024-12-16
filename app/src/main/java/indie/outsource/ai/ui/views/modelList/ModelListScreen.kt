package indie.outsource.ai.ui.views.modelList

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import indie.outsource.ai.model.Model
import indie.outsource.ai.ui.views.inference.ConstraintLayoutContent
import indie.outsource.ai.ui.theme.TestAppTheme
import indie.outsource.ai.ui.views.inference.InferenceViewModel


data class ListItem(val name: String, val owner: String)


@Composable
fun ModelListScreen(modifier: Modifier,viewModel: ModelListViewModel = hiltViewModel(),onItemClick: (id:String) -> Unit = {}){
    Column {
        SwitchWithTextToggle()
        ModelList(
            modifier = modifier,
            viewModel=viewModel,
            onItemClick = onItemClick
        )
    }

}


@Composable
fun SwitchWithTextToggle() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Local",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Switch(
            checked = true,
            onCheckedChange = { },

        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Remote",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        }

}


@Composable
fun ModelList(modifier: Modifier,viewModel: ModelListViewModel = hiltViewModel(),onItemClick: (id:String) -> Unit = {}){
    val modelListUiState by viewModel.uiState.collectAsState()
    ItemList(
        modelListUiState.models,
        onItemClick = onItemClick
    )
}

@Composable
fun ItemList(items: List<Model>,onItemClick: (id:String) -> Unit = {}) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items.size) { index ->
            ListItemView(
                item = items[index],
                onClick = onItemClick
            )
        }
    }
}

@Composable
fun ListItemView(item: Model,onClick: (id:String) -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .shadow(4.dp, MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
            .clickable { onClick(item.name) }
    ) {
        Text(
            text = item.name,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = item.owner,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}