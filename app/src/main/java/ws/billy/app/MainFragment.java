package ws.billy.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import ws.billy.app.Code.LoginSignup.LoginSignup.Login;
import ws.billy.app.Code.LoginSignup.LoginSignup.Signup;

@SuppressLint("ValidFragment")
public class MainFragment extends Fragment {

    // Register the buttons
    private Button btnSignup;
    private Button btnSignin;

    // Called when view is created
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        // Inflate the splash layout
        final View view = inflater.inflate(R.layout.splashlayout, container, false);
        return view;

    }

    // Called after view is created
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Hide action bar
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Connect the buttons so they can be listened to
        btnSignup = view.findViewById(R.id.btnSignUp);
        btnSignin = view.findViewById(R.id.btnSignIn);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

    }

    // Sign in page opening
    public void signIn() {

        // open intent
        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);

    }

    // Sign up page opening
    public void signUp() {

        // open intent
        Intent intent = new Intent(getActivity(), Signup.class);
        startActivity(intent);

    }


}
