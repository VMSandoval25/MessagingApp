@file:OptIn(ExperimentalMaterial3Api::class)

package app.getsizzle.messaging.Register

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import app.getsizzle.messaging.R
import app.getsizzle.messaging.collectEvents
import app.getsizzle.messaging.collectState
import app.getsizzle.messaging.ui.SizzleTheme
import app.getsizzle.shared.Api.addChatRoom
import app.getsizzle.shared.Api.getGroupID
import app.getsizzle.shared.Api.registerPatient
import app.getsizzle.shared.data.model.Gender
import app.getsizzle.shared.data.model.Patient
import app.getsizzle.shared.data.model.UserRole
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(onClickBack: () -> Unit)
{
    Surface(
        shadowElevation = 4.dp,
    ) {
        CenterAlignedTopAppBar(
            title = {
                    Text("Register Screen" )
            },
            navigationIcon = {
                IconButton(onClick =onClickBack ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "BackButton"
                    )
                }
            },
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterScreen(registerViewModel: RegisterViewModel, showLoginScreen:()->Unit) {
    registerViewModel.collectEvents {
        when (it) {
            RegisterViewModel.RegisterEvent.ShowMessagingScreen -> showLoginScreen()
        }
    }


    Scaffold(
        topBar = { TopBar(registerViewModel::onBackClicked) },
        content = {paddingValues ->
            RegisterBox(registerViewModel = registerViewModel, paddingValues=paddingValues)
        },
        bottomBar= {registerBar(registerViewModel = registerViewModel)}
    )
}
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun RegisterBox(registerViewModel: RegisterViewModel, paddingValues: PaddingValues)
{
    val registerUiState by registerViewModel.collectState()
    val lazyColumnState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val focusManager = LocalFocusManager.current




    LazyColumn(
        contentPadding= paddingValues,
        state=lazyColumnState,
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

        )
    {
        item {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier=Modifier.width(350.dp),
                horizontalAlignment=Alignment.CenterHorizontally
            ) {
                val spacing=8.dp
                OutlinedTextFieldBox(registerUiState.fullName, stringResource(R.string.FullNameLabel))
                { registerViewModel.updateName(it) }
                Spacer(modifier=Modifier.height(spacing))
                RegularTextFieldBox(registerUiState.email, stringResource(R.string.EmailLabel))
                { registerViewModel.updateEmail(it) }
                Spacer(modifier=Modifier.height(spacing))
                OutlinedTextFieldBox(registerUiState.userId, stringResource(R.string.UsernameLabel))
                { registerViewModel.updateUsername(it) }
                Spacer(modifier=Modifier.height(spacing))

                RegularTextFieldBox(registerUiState.password, stringResource(R.string.PasswordLabel))
                { registerViewModel.updatePassword(it) }
                Spacer(modifier=Modifier.height(spacing))

                OutlinedTextFieldBox(registerUiState.dietitianEmail, stringResource(R.string.DietitianEmailLabel))
                { registerViewModel.updateDietitianEmail(it) }
                Spacer(modifier=Modifier.height(spacing))


                Row(
                ) {
                    NumberField(registerUiState.age, stringResource(R.string.AgeLabel,),Modifier.weight(1f))
                    { registerViewModel.updateAge(it) }
                    NumberField(registerUiState.weight, stringResource(R.string.WeightLabel) ,Modifier.weight(1f))
                    { registerViewModel.updateWeight(it) }
                    NumberField(registerUiState.feet, stringResource(R.string.FeetLabel),Modifier.weight(1f) )
                    { registerViewModel.updateFeet(it) }
                    NumberField(registerUiState.inches, "Inches",Modifier.weight(1f),)
                    { registerViewModel.updateInches(it) }
                }
                Spacer(modifier=Modifier.height(spacing))

                GenderDropdown(registerViewModel)

                if (registerUiState.error != null) {
                    Text(
                        text = registerUiState.error!!,
                    )

                }
            }
        }

    }

}

@Composable
fun OutlinedTextFieldBox(fullName: String, label:String,updateName:(String)->Unit) {
    OutlinedTextField(
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        value = fullName,
        singleLine = true,
        onValueChange = {updateName(it)} ,
        label = { Text(label) },
        modifier = Modifier
            .focusTarget()
            .fillMaxWidth()
    )
}
@Composable
fun RegularTextFieldBox(fullName: String,label:String, update:(String)->Unit) {
    TextField(
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        value = fullName,
        singleLine = true,
        onValueChange = {update(it)} ,
        label = { Text(label) },
        modifier = Modifier
            .focusTarget()
            .fillMaxWidth()
    )
}
@Composable
fun GenderDropdown(registerViewModel: RegisterViewModel) {
    val genders = listOf("Male", "Female", "Transgender","Non-binary","Other","Prefer not to respond")
    var expanded by remember { mutableStateOf(false) }
    val registerUiState by registerViewModel.collectState()

    Column() {
        Box(
            Modifier
                .fillMaxWidth()
                .clickable(onClick = { expanded = true })
                .background(SizzleTheme.colors.background)
                .shadow(1.dp)
        ) {
            Text(
                text = if (registerUiState.gender.isEmpty()) "Select gender" else registerUiState.gender,
                modifier = Modifier.padding(16.dp)
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth(0.85F)
                .background(SizzleTheme.colors.background)
        ) {
            genders.forEach { gender ->
                Surface(
                    shadowElevation = 12.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DropdownMenuItem(

                        text = { Text(text = gender) },
                        onClick = {
                            registerViewModel.updateGender(gender)
                            expanded = false
                        },
                        modifier=Modifier.background(SizzleTheme.colors.background)
                    )
                }
            }
        }
    }
}
@Composable
fun NumberField(age: String, label: String, modifier:Modifier, update: (String) -> Unit)
{
    OutlinedTextField(
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        value = age,
        onValueChange = { update(it) },
        label = { Text(label) },
        modifier = modifier
            .padding(end = 4.dp)
    )
}

@Composable
fun registerBar(registerViewModel: RegisterViewModel) {
    val scope = rememberCoroutineScope()
    val registerUiState by registerViewModel.collectState()

    Column(
        modifier = Modifier
            .background(SizzleTheme.colors.background)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier=Modifier.height(16.dp))
        Button(
            onClick = {
                scope.launch {
                    try {
                        var temp2=Gender.UNSPECIFIED
                        if(registerUiState.gender=="Male")
                            temp2=Gender.MALE
                        else if(registerUiState.gender=="Female")
                            temp2=Gender.FEMALE
                        else if(registerUiState.gender=="Transgender")
                            temp2=Gender.TRANSGENDER
                        else if(registerUiState.gender=="Non-binary")
                            temp2=Gender.NONBINARY
                        var groupID=getGroupID(registerUiState.dietitianEmail)
                        val registeredPatient = Patient(
                            fullName = registerUiState.fullName,
                            userId = registerUiState.userId,
                            password = registerUiState.password,
                            email = registerUiState.email,
                            groupId = groupID,
                            pfp = null,
                            role = UserRole.PATIENT,
                            lastSentAt = Clock.System.now(),
                            height = (registerUiState.feet.toInt()*12)+registerUiState.inches.toInt(),
                            weight = registerUiState.weight.toDouble(),
                            gender = temp2,
                            age = registerUiState.age.toInt())

                        var statusOfChatroom = addChatRoom(registeredPatient)
                        if ("Successfully added chatRoom" == statusOfChatroom)
                            registerViewModel.updateError(
                                registerPatient(
                                    registeredPatient
                                )
                            )
                        else
                            registerViewModel.updateError(statusOfChatroom)
                    } catch (e: Exception) {
                        e.localizedMessage ?: "error"
                        registerViewModel.updateError("Connection to server absent")
                    }
                }
            },
            modifier = Modifier.width(300.dp)
        )
        {
            Text("Register", color=SizzleTheme.colors.background)
        }
        Spacer(modifier=Modifier.height(16.dp))
    }

}



