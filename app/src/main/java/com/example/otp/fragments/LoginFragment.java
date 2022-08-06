package com.example.otp.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.otp.EditUserDetailsActivity;
import com.example.otp.LoginActivity;
import com.example.otp.MainActivity;
import com.example.otp.OtpActivity;
import com.example.otp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;


public class LoginFragment extends Fragment {


    View main_view;
    NavController navController;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference syslogin;
    private String uid;
    private boolean checkEmail;

    private EditText mCountryCode;
    private EditText mPhoneNumber;

    private Button mGenerateBtn;
    private ProgressBar mLoginProgress;

    private TextView mLoginFeedbackText;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        main_view = inflater.inflate(R.layout.fragment_login, container, false);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        sharedPreferences = requireActivity().getSharedPreferences("checker", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        checkEmail = sharedPreferences.getBoolean("checkEmail", false);


        mCountryCode = main_view.findViewById(R.id.country_code_text);
        mPhoneNumber = main_view.findViewById(R.id.phone_number_text);
        mGenerateBtn = main_view.findViewById(R.id.generate_btn);
        mLoginProgress = main_view.findViewById(R.id.login_progress_bar);
        mLoginFeedbackText = main_view.findViewById(R.id.login_form_feedback);

        mCountryCode.setText("+91");
        mCountryCode.setEnabled(false);

        mGenerateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String country_code = mCountryCode.getText().toString();
                String phone_number = mPhoneNumber.getText().toString();

                String complete_phone_number = "+" + country_code + phone_number;

                if (country_code.isEmpty() || phone_number.isEmpty()) {
                    mLoginFeedbackText.setText("Please fill in the form to continue.");
                    mLoginFeedbackText.setVisibility(View.VISIBLE);
                } else {
                    mLoginProgress.setVisibility(View.VISIBLE);
                    mGenerateBtn.setEnabled(false);

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            complete_phone_number,
                            60,
                            TimeUnit.SECONDS,
                            requireActivity(),
                            mCallbacks
                    );

                }
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                mLoginFeedbackText.setText("Verification Failed, please try again.");
                mLoginFeedbackText.setVisibility(View.VISIBLE);
                mLoginProgress.setVisibility(View.INVISIBLE);
                mGenerateBtn.setEnabled(true);
            }

            @Override
            public void onCodeSent(final String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);


//                Intent otpIntent = new Intent(LoginActivity.this, OtpActivity.class);
//                otpIntent.putExtra("AuthCredentials", s);
//                startActivity(otpIntent);

                LoginFragmentDirections.ActionLoginFragmentToOtpFragment action= LoginFragmentDirections.actionLoginFragmentToOtpFragment(s);
                navController.navigate(action);
            }
        };
        return main_view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mCurrentUser != null) {
            if (checkEmail) {
                sendUserToHome();
            }
            else if (!checkEmail) {
                uid = mCurrentUser.getUid();
                syslogin = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                syslogin.child("User Email").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            checkEmail = true;
                            editor.putBoolean("checkEmail", checkEmail);
                            editor.commit();
                            sendUserToHome();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            else {
//                Intent intent = new Intent(LoginActivity.this, EditUserDetailsActivity.class);
//                startActivity(intent);
                  navController.navigate(R.id.action_loginFragment_to_editUserDetailsFragment);

            }
        }

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //sendUserToHome();
                            // ...
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                mLoginFeedbackText.setVisibility(View.VISIBLE);
                                mLoginFeedbackText.setText("There was an error verifying OTP");
                            }
                        }
                        mLoginProgress.setVisibility(View.INVISIBLE);
                        mGenerateBtn.setEnabled(true);
                    }
                });
    }

    private void sendUserToHome() {
//        Intent homeIntent = new Intent(requireActivity(), MainActivity.class);
//        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(homeIntent);
//        finish();
        navController.navigate(R.id.action_loginFragment_to_mainFragment);
    }
}