package indie.outsource.ai.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState


val items = listOf(Routes.ConversationList.name,Routes.Home.name,Routes.ModelList.name)
val selectedIcons = listOf(Icons.Rounded.Edit,Icons.Filled.Home, Icons.AutoMirrored.Filled.List)
val unselectedIcons =listOf(Icons.Outlined.Edit,Icons.Outlined.Home, Icons.AutoMirrored.Outlined.List)



@Composable
fun Navbar(navController: NavHostController, onClick: (text:String) -> Unit) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination
    val hideBottomBar = currentDestination?.route in listOf(Routes.SignIn.name,Routes.SignUp.name)
    var selectedItem by remember { mutableIntStateOf(1) }

    if(hideBottomBar){
        return
    }

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
