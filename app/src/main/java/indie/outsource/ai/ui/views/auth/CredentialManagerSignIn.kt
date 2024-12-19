package indie.outsource.ai.ui.views.auth

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

suspend fun signIn(context:Context): FirebaseUser {
    val credentialManager = CredentialManager.create(context)
    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        //move to resources
        .setServerClientId("435938895956-l9avckf0mdr88u0u642jn9646r569gu3.apps.googleusercontent.com")
        .setAutoSelectEnabled(false)
        .setNonce("halohalohalohalohalo")
        .build()

    val request: GetCredentialRequest =  GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    try {
        val result = credentialManager.getCredential(
            request = request,
            context = context,
        )
        return handleSignIn(result)!!

    } catch (e: GetCredentialException) {
        println("SignIn: ${e.errorMessage}")
        throw e
    }


}
private suspend fun handleSignIn(result: GetCredentialResponse): FirebaseUser? {
    val auth = Firebase.auth
    when (val credential = result.credential) {

        is CustomCredential -> {
            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                try {
                    val googleIdTokenCredential = GoogleIdTokenCredential
                        .createFrom(credential.data)

                    val idToken = googleIdTokenCredential.idToken
                    val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                    auth.signInWithCredential(firebaseCredential)
                    val user = auth.currentUser

                    if (user != null) {
                        println("firebase user?: ${user.photoUrl.toString()}")
                    }else{
                        println("firebase user is null")
                    }
                    return user

                } catch (e: GoogleIdTokenParsingException) {
                    Log.e(TAG, "Received an invalid google id token response", e)
                    throw e
                }
            } else {
                Log.e(TAG, "Unexpected type of credential")
            }
        }

        else -> {
            Log.e(TAG, "Unexpected type of credential")
        }
    }
    throw RuntimeException()
}
