package ws.billy.app.Code.LoginSignup.LoginSignup;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ws.billy.app.Code.Activities.JournalList.JournalList;
import ws.billy.app.Code.LoginSignup.SplashScreen.SplashScreen;
import ws.billy.app.MainActivity;
import ws.billy.app.R;

public class Login extends AppCompatActivity implements View.OnClickListener {
    View bg;
    private FirebaseAuth mAuth;

    // define the email & password text edit
    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signinlayout);

        // link the gui elements to the code
        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.LoginEmailAddress);
        passwordEditText = findViewById(R.id.LoginPassword);

        // if they're already logged in, let them skip this
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            Intent intent = new Intent(this, JournalList.class);
            startActivity(intent);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle("Log In");
        }

        bg = findViewById(R.id.activity_loginsignup_style2);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.loginsignup_menu, menu);
        return false;
    }

    // making sure back pressed directs properly
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnLogin) {

            mAuth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                // login, send user to the main page
                                Intent intent = new Intent(Login.this, JournalList.class);
                                startActivity(intent);

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Login.this, "Invalid login details!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }


}
