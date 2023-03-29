package app.getsizzle.messaging.Profile

//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.style.TextOverflow
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.getsizzle.messaging.R
import app.getsizzle.messaging.UserInfo
import app.getsizzle.messaging.collectEvents
import app.getsizzle.messaging.collectState


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileUI(
    viewModel: ProfileViewModel,
    showLastScreen: () -> Unit,
    patient: UserInfo
) {


    LaunchedEffect(true )
    {
        viewModel.updateEmail(patient.email)
        viewModel.updateName(patient.fullname)
    }
    viewModel.collectEvents{
        when(it)
        {
            ProfileViewModel.ProfileEvent.ShowLastScreen -> { showLastScreen()}
        }
    }
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = {snackbarHostState},
        topBar = { TopAppBar(
            title = { Text("Sizzle") },
            navigationIcon = {
                IconButton(onClick = viewModel::onBackClicked) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "BackButton"
                )
            }
            },
        )
        },
        //drawerContent = { Text(text = "drawerContent") },
        //bottomBar = { BottomAppBar() { Text("BottomAppBar") } }
    ){ paddingValues ->
        Box(modifier = Modifier
            .padding(paddingValues)
            .alpha(1.0f)
        )
        {
             ProfileBody(viewModel,patient)
        }
    }


}

@Composable
fun ProfileBody(viewModel: ProfileViewModel, patient: UserInfo)
{
    var isEditing by remember { mutableStateOf(false) }
    var context=LocalContext.current.applicationContext
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {

            item {
                // User's image, name, email and edit button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // User's image
                    Image(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(shape = CircleShape),
                        painter = painterResource(id = R.drawable.pfp),
                        contentDescription = "Your Image"
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(weight = 3f, fill = false)
                                .padding(start = 16.dp)
                        ) {

                            // User's name]
                            if (!isEditing)
                                Texts(viewModel)
                            else
                                TextFields(viewModel)
                        }

                        // Edit button
                        IconButton(
                            modifier = Modifier
                                .weight(weight = 1f, fill = false),
                            onClick = {
                                isEditing = !isEditing;
                            }) {
                            if (!isEditing) {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    imageVector = Icons.Outlined.Edit,
                                    contentDescription = "Edit Details",
                                )
                            } else {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    imageVector = Icons.Outlined.Save,
                                    contentDescription = "Save Details",
                                )
                            }
                        }

                    }
                }

            }
            item() {
                OptionsList("Account", "Manage your account", Icons.Default.Face)
            }
            item() {
                OptionsList("Settings", "Manage your settings", Icons.Default.Settings)
            }

        }

    Spacer(modifier = Modifier.height(8.dp))
//    ProfileDetails()
}
@Composable
fun Texts(viewModel: ProfileViewModel) {
    val profileUIState by viewModel.collectState()

    Text(
        text = profileUIState.name,
        maxLines = 1,
        //overflow = TextOverflow.Ellipsis
    )

    Spacer(modifier = Modifier.height(2.dp))

    // User's email
    Text(
        text = profileUIState.email,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFields( viewModel: ProfileViewModel) {
    val profileUIState by viewModel.collectState()


    OutlinedTextField(
        value = profileUIState.name,
        onValueChange ={ viewModel.updateName(it)},
        maxLines = 1,

    )

    Spacer(modifier = Modifier.height(2.dp))

    // User's email
    OutlinedTextField(
        value = profileUIState.email,
        onValueChange ={ viewModel.updateEmail(it)}

    )
}

@Composable
fun ProfileDetails() {
    Column() {


        Text(
            text = "Email: john.doe@example.com",
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Phone: (555) 555-5555",
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Address: 123 Main St, Anytown USA",
        )
    }
}
object Option{
    var image:ImageVector?=null
    var optionName:ImageVector?=null
    var subtext:ImageVector?=null
}
@Composable
fun OptionsList(optionText:String,subtext:String,icon:ImageVector){
    var context=LocalContext.current.applicationContext

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = true) {
                Toast
                    .makeText(context, "Clicked", Toast.LENGTH_SHORT)
                    .show()
            }
            .padding(all = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(32.dp),
            imageVector = icon,
            contentDescription = "Account",
        )
        Spacer(modifier = Modifier.width(20.dp))
        Column() {
            Row() {
                Text(text=optionText)

            }
            Row() {
                Text(text=subtext)
            }
        }
    }
}
