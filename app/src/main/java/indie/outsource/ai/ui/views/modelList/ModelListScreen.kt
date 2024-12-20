package indie.outsource.ai.ui.views.modelList

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.gson.Gson


data class ListItem(val name: String, val owner: String)

@Composable
fun ModelListScreen(modifier: Modifier,viewModel: ModelListViewModel = hiltViewModel(),onQuery: (id:String) -> Unit = {}){

    val modelListUiState by viewModel.uiState.collectAsState()

    ConstraintLayout {
        val (list, confirmButton) = createRefs()

        Column(
            modifier = Modifier
                .constrainAs(list){
                    top.linkTo(parent.top)
                }
        ) {
            SwitchWithTextToggle()
            ModelList(
                modifier = modifier,
                models = modelListUiState.models,
                onItemClick = { _:String, index:Int ->
                    viewModel.onModelClick(index)
                }
            )
        }

        if(viewModel.isAnyModelSelected()){
            QueryButton(
                modifier = Modifier
                    .padding(0.dp,0.dp,0.dp,16.dp)
                    .constrainAs(confirmButton){
                        bottom.linkTo(parent.bottom)
                    },
                onClick = {
                    onQuery(
                        Gson().toJson(viewModel.getSelectedModelsIds())
                    )
                }
            )
        }
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
fun ModelList(modifier: Modifier,models:List<ModelListModel>,onItemClick: (id:String,index:Int) -> Unit = { s: String, i: Int -> }){
    ItemList(
        models,
        onItemClick = onItemClick
    )
}


@Composable
fun QueryButton(modifier: Modifier = Modifier,onClick:()->Unit){
    Row(
        horizontalArrangement = Arrangement.Absolute.Center,
        modifier = modifier
            .fillMaxWidth()
            .then(modifier)
    ){
        Button(
            onClick = onClick,
        ) {
            Text("Query")
        }
    }
}

@Composable
fun ItemList(items: List<ModelListModel>,onItemClick: (id:String,index:Int) -> Unit = { s: String, i: Int -> }) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items.size) { index ->
            ListItemView(
                index = index,
                item = items[index],
                onClick = onItemClick
            )
        }
    }
}

@Composable
fun ListItemView(item: ModelListModel,index:Int,onClick: (id:String,index:Int) -> Unit = { s: String, i: Int -> }) {
    Column(
        modifier = Modifier

            .fillMaxWidth()
            .padding(12.dp)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .border(
                width = if (item.isSelected) 2.dp else 0.dp,
                color = if (item.isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainer,
                shape = MaterialTheme.shapes.medium
            )

            .clip(shape = MaterialTheme.shapes.medium)
            .clickable { onClick(item.model.name,index) }
            .padding(16.dp),
    ) {
        Text(
            text = item.model.name,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = item.model.owner,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}