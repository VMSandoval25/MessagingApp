package app.getsizzle.messaging

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import app.getsizzle.messaging.ui.SizzleTheme
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

object MainColor
{
    val color:Color= Color(0xFFFFD3A8)
}

class MainActivity : ComponentActivity() {
    companion object {
        private const val TAG = "MainActivity"
        private const val NOTIFICATION_REQUEST_CODE = 1234
    }

    lateinit var navController: NavHostController
    val context=this

    override fun onCreate(savedInstanceState: Bundle?) {
        askNotificationPermission()
        super.onCreate(savedInstanceState)
        actionBar?.hide()

        setContent {

            SizzleTheme() {
                LaunchedEffect(true) {

                    //askNotificationPermission()
                    val token = FirebaseMessaging.getInstance().token.await()
                    println("token is = $token")
                }
                navController = rememberNavController()
                SetupNavGraph(navController = navController, context)
            }

        }

    }
    // [START ask_post_notifications]
    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.v(TAG,"GRANTED ACCESS")
            // FCM SDK (and your app) can post notifications.
        } else {
            Log.v(TAG,"NOT GRANTED ACCESS")
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    // [END ask_post_notifications]
}





