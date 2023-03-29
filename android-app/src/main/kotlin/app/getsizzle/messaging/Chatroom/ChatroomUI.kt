package app.getsizzle.messaging.Chatroom


import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.getsizzle.messaging.*
import app.getsizzle.messaging.R
import app.getsizzle.messaging.ui.SizzleTheme
import app.getsizzle.shared.*
import app.getsizzle.shared.Api.addMessage
import app.getsizzle.shared.data.model.Message
import kotlinx.coroutines.launch


@Composable
fun MyMenu(viewModel: ChatroomViewModel, expanded: Boolean) {
    var expanded by remember { mutableStateOf(false) }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier.wrapContentSize()
    ) {
        DropdownMenuItem(text = { Text("Patient Profile")}, onClick = viewModel::onProfileClicked)
    }

    IconButton(
        onClick = { expanded = !expanded }
    ) {
        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Suppress("UnusedMaterialScaffoldPaddingParameter")
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ChatroomUI(
    viewModel: ChatroomViewModel,
    lazyColumnState: LazyListState,
    showLastScreen: () -> Unit,
    showProfileScreen: () -> Unit,
    patient: UserInfo
) {
    var expanded by remember { mutableStateOf(false) }


    viewModel.collectEvents{
        when(it)
        {
            ChatroomViewModel.ChatroomEvent.ShowLastScreen -> { viewModel.isCollecting.value=false
                socketClient.close()
                showLastScreen()}
            ChatroomViewModel.ChatroomEvent.ShowProfileScreen -> {showProfileScreen()}
        }
    }
    Scaffold(
        topBar = {
            Surface(
                modifier=Modifier.shadow(4.dp),
            ) {
                CenterAlignedTopAppBar(
                    title = { Text(patient.dietitian.fullName) },
                    navigationIcon =
                    {
                        IconButton(onClick = viewModel::onBackClicked) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "BackButton"
                            )
                        }
                    },
                    actions = { MyMenu(viewModel, expanded) },
                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = SizzleTheme.colors.background)
                )
            }
                 },


        bottomBar = { SendMessageContent(viewModel,patient)   }   ) { paddingValues ->
        Box(modifier = Modifier
            .padding(paddingValues)
            .alpha(1.0f)
        )
        {
            MessagesContent(viewModel, lazyColumnState,patient)
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendMessageContent(viewModel: ChatroomViewModel, patient: UserInfo) {

    val scope = rememberCoroutineScope()

    val chatroomState by viewModel.collectState()
    Box(
        Modifier.shadow(1.dp),
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically, // Should center all the elements
            ) {
            Box(
                modifier = Modifier.weight(1f)
                    .padding(8.dp),
            ) {
                OutlinedTextField(
                    value = chatroomState.typedMessage,
                    onValueChange = { viewModel.updateMessage(it) },
                    label = { Text("Type Message Here") },
                    maxLines = 4,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusTarget()
                        .padding(8.dp),
                    trailingIcon = {}
                )
            }
            IconButton(
                onClick = {
                    var newMessage = Message(
                        patient.chatRoomId,
                        chatroomState.typedMessage,
                        currentMoment(),
                        patient.username,
                        false
                    )
                    scope.launch {
                        addMessage(newMessage)
                        viewModel.addMessageList(newMessage)
                    }
                    socketClient.send(newMessage.messageText)
                    viewModel.resetMessageField()

                },
                enabled = chatroomState.typedMessage.isNotBlank(),
                modifier = Modifier
                    .padding(end = 16.dp, top = 16.dp, bottom = 8.dp)
            ) {
                var color2 = SizzleTheme.colors.onBackgroundVariantTwo
                var color1 = SizzleTheme.colors.onBackgroundVariant
                if (chatroomState.typedMessage.isNotBlank()) {
                    color2 = SizzleTheme.colors.primary
                    color1 = SizzleTheme.colors.onBackgroundVariantTwo
                }
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(color2)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = color1,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MessagesContent(
    viewModel: ChatroomViewModel,
    lazyColumnState: LazyListState,
    patient: UserInfo
) {
    val scope = rememberCoroutineScope()
    val chatroomState by viewModel.collectState()
    LaunchedEffect(true)
    {
        scope.launch {
            if(!socketClient._job.isActive){
                println("WEBSOCKET RECONNECTS")
                socketClient._job.start()
            }
        }
    }
    LaunchedEffect(true )
    {
        viewModel.updateMessageList(Api.getMessage(patient.chatRoomId))

        scope.launch {
            while(true)
            {
                socketClient.chatMessage.collect { value->
                    //This uses api call
                    viewModel.updateMessageList(Api.getMessage(patient.chatRoomId))
                    // This uses websocket but buggy
//                    viewModel.addMessageList(Message(patient.chatRoomId,value, currentMoment(),patient.dieticianId, false))
                }
            }
        }
        viewModel.isCollecting.value=true
    }
    if (chatroomState.initialMessages.isNotEmpty()) {
        //there are messages
        LazyColumn(
            state = lazyColumnState,
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Top,
        ) {
            itemsIndexed(chatroomState.initialMessages) { position, message ->
                var date: String = displayDate(message.sentTime!!)
                if ( position==0 || position<chatroomState.initialMessages.size &&date != displayDate(chatroomState.initialMessages[position-1].sentTime!!)) {
                    DateBanner(date)
                }
                if (message.senderID==patient.username) {
                    MessageContentRight(
                        message=message,
                    )
                } else {
                    MessageContentLeft(
                        message=message,
                    )
                }
            }
        }
    } else {
        //no messages
        Text(
            text = ("No Messages"),
            modifier = Modifier
                .padding(top = 50.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
    scope.launch {
        lazyColumnState.animateScrollToItem(index = chatroomState.initialMessages.size)
    }
}

@Composable
fun DateBanner(date: String) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .padding(all=16.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(size = 50.dp),
            shadowElevation = 1.dp,
            color=SizzleTheme.colors.onBackgroundVariantTwo,
        )
        {
            Text(
                text = date,
                modifier = Modifier
                    .padding(8.dp),
                style = SizzleTheme.typography.bodySmall
            )
        }
    }

}


@Composable
fun MessageContentLeft(
    message: Message,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start //LEFT
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .align(Alignment.Start),
//            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            DefaultUserProfilePicture()

            Column(
//                verticalArrangement = Arrangement.Top
            ) {
                //message surface
                Row(modifier = Modifier
                    .align(Alignment.Start)
                )
                {
                    BoxWithConstraints {

                        Surface(
                            shape = RoundedCornerShape(size = 8.dp),
                            color = SizzleTheme.colors.onBackgroundVariantTwo,
                            modifier = Modifier//.width(maxWidth*0.87f)
                                .widthIn(max=maxWidth*0.87f)

                        ) {
                            Text(
                                text = message.messageText,
                                style = SizzleTheme.typography.bodyLarge,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                        Text(
                            text = message.sentTime?.let { displayTime(it) }.toString(),
                            modifier = Modifier
                                .padding(bottom = 4.dp,start=4.dp,end=4.dp)
                                .align(Alignment.Bottom)
                                .weight(1f),
                            style = SizzleTheme.typography.labelSmall
                        )
                }
            }
        }
    }
}
@Composable
fun MessageContentRight(
    message: Message,
    ) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .align(Alignment.End),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
        ) {
            Column(verticalArrangement = Arrangement.Top,
            modifier=Modifier.weight(1f)
            ) {
                Row(modifier = Modifier
                    .align(Alignment.End)
                ) {
                    Text(
                        text = message.sentTime?.let { displayTime(it) }.toString(),
                        modifier = Modifier
                            .align(Alignment.Bottom)
                            .padding(bottom = 4.dp,end=4.dp),
                        style = SizzleTheme.typography.labelSmall

                    )
                    Surface(
                        shape = RoundedCornerShape(size = 8.dp),
                        color = SizzleTheme.colors.primaryVariant,
//                        modifier = Modifier.padding(horizontal = 24.dp)

                    ) {
                        Text(
                            color = SizzleTheme.colors.background,
                            text = message.messageText,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

//                    Spacer(Modifier.width(50.dp))
                }


            }
//            Box(modifier = Modifier.weight(1f))
//            {
//                DefaultUserProfilePicture()
//            }
        }
    }
}

@Composable
fun DefaultProfilePicture() {
    Icon(
        imageVector = Icons.Default.Person,
        contentDescription = "UserImage",
        modifier = Modifier
            .padding(end = 8.dp, top = 8.dp, bottom = 8.dp)
            .clip(CircleShape)
            .requiredSize(40.dp)
    )
}
@Composable
fun DefaultUserProfilePicture() {
    Image(
        painter = painterResource(id = R.drawable.default_icon),
        contentDescription = "UserImage",
        modifier = Modifier
            .padding(end = 8.dp, top = 8.dp, bottom = 8.dp)
            .clip(CircleShape)
            .requiredSize(40.dp)
    )
}

