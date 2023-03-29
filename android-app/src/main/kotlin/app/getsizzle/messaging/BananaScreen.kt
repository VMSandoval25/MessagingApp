package app.getsizzle.messaging

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.*
import app.getsizzle.shared.Api.getMessage
import app.getsizzle.shared.Greeting
import app.getsizzle.shared.ShoppingListItem
import app.getsizzle.shared.data.model.Dietitian
import app.getsizzle.shared.data.model.Message
import kotlinx.coroutines.launch


@Composable
fun BananaScreen()
{
    val temp: Dietitian = Dietitian("name", "1", "222", "email")
    val scope = rememberCoroutineScope()
    var messageElement by  remember { mutableStateOf(emptyList<Message>())}
    LaunchedEffect(true) {
        scope.launch {
            try {
                messageElement = getMessage(-1467791774)
            } catch (e: Exception) {
                e.localizedMessage ?: "error"
            }
        }
    }
//     Just to show that the :shared module is working as expected
    val shoppingListItem = ShoppingListItem(
        desc = "Bananas 🍌",
        priority = 0,
        creationTime = null,
        lastEditTime = null,
        owners = listOf()
    )
    Column() {
        Text(text = "Hello world! Look, I bought some ${shoppingListItem.desc}.")
        Text(text = "Hello world! Look, I bought some ${messageElement}.")
        var text by remember { mutableStateOf("Loading") }
        LaunchedEffect(true) {
            scope.launch {
                text = try {
                    Greeting().greeting()
                } catch (e: Exception) {
                    e.localizedMessage ?: "error"
                }
            }
        }
        Text(text = "Hello world! Look, I bought some ${text}.")
    }
}