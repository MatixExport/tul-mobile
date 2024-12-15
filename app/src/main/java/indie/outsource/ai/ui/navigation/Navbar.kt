package indie.outsource.ai.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController


val items = listOf(Routes.Home.name,Routes.ModelList.name)
val selectedIcons = listOf(Icons.Filled.Home, Icons.AutoMirrored.Filled.List)
val unselectedIcons =listOf(Icons.Outlined.Home, Icons.AutoMirrored.Outlined.List)

@Composable
fun Navbar(onClick: (text:String) -> Unit) {
    var selectedItem by remember { mutableIntStateOf(0) }

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                        contentDescription = item
                    )
                },
                label = { Text(item) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    onClick(items[selectedItem])
                }
            )
        }
    }
}

//@Composable
//fun TopBarWithBackButton(
//    title: String,
//    navController: NavHostController,
//    modifier: Modifier = Modifier
//) {
//    TopAppBar(
//        title = { Text(text = "") },
//        navigationIcon = {
//            IconButton(onClick = { navController.navigateUp() }) {
//                Icon(
//                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                    contentDescription = "Back"
//                )
//            }
//        },
//        modifier = modifier
//    )
//}