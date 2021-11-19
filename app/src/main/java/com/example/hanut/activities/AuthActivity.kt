package com.example.hanut.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.hanut.R
import com.example.hanut.providers.AuthProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.auth_activity.*


class AuthActivity : AppCompatActivity() {

    var btnLoginGoogle: SignInButton? = null
    var mDialog: android.app.AlertDialog? = null
    val GOOGLE_SIGN_IN:Int = 100
    lateinit var mAuthProvider: AuthProvider;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_activity)

        val analytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("Mensaje", "Integracin de firebase completa")
        analytics.logEvent("InitScreen", bundle)

        mAuthProvider = AuthProvider();

        btnLoginGoogle = findViewById(R.id.btnLoginGoogle)
        mDialog = SpotsDialog.Builder().setContext(this).setMessage("Espere").build()

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)

        setUp()
        //session()
    }

    // Esto es para que si el usuario cierrar la app pero no cierra sesion al brirla no deba usar iniciar sesion nuevamente
    override fun onStart() {
        super.onStart()
        if( mAuthProvider.userSession != null){
            var intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

     fun setUp(){
        title = "Autenticación"
        signUpBtn.setOnClickListener{
            if (emailText.text.isNotEmpty() && passwordText.text.isNotEmpty()){
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(emailText.text.toString(),
                    passwordText.text.toString()).addOnCompleteListener{
                    if(it.isSuccessful){
                        showHome(it.result?.user?.email?:"", HomeActivity.ProviderType.BASIC)

                    }else{
                        showAlert("Error","No se pudo ingresar")
                    }
                }
            }
        }
        logInBtn.setOnClickListener{
            if (emailText.text.isNotEmpty() && passwordText.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailText.text.toString(),
                    passwordText.text.toString()).addOnCompleteListener{
                    if(it.isSuccessful){
                        showHome(it.result?.user?.email?:"", HomeActivity.ProviderType.BASIC)
                    }else{
                        showAlert("Error","No se pudo ingresar")
                    }
                }
            }
        }
         btnLoginGoogle?.setOnClickListener {
             // Configuracion
             val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                 .requestIdToken(getString(R.string.default_web_client_id))
                 .requestEmail()
                 .build()

             val googleClient = GoogleSignIn.getClient(this, googleConf)
             startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
         }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == GOOGLE_SIGN_IN){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)
                if(account != null){
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if(it.isSuccessful){
                            showHome(account.email ?: "", HomeActivity.ProviderType.GOOGLE)
                        }else{
                            showAlert("Error", "Ocurrió un error de autenticación con cuenta google")
                        }
                    }
                }
            } catch (e:ApiException){
                showAlert("Error", "${e}")
                Log.v("Error", "${e}" )
            }
        }
    }

    fun showAlert(title:String, message:String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        val dialog = builder.create()
        dialog.show()
    }
     fun showHome(email:String, provider: HomeActivity.ProviderType){
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
        finish()
    }
}