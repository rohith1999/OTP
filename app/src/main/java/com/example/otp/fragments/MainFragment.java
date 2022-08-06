package com.example.otp.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.otp.AboutUsActivity;
import com.example.otp.CallBackComplaintListActivity;
import com.example.otp.EditUserDetailsActivity;
import com.example.otp.GetCallBackActivity;
import com.example.otp.LoginActivity;
import com.example.otp.MainActivity;
import com.example.otp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainFragment extends Fragment {

    View main_view;
    NavController navController;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private ImageButton mLogoutBtn;
    private CardView getcallback;
    private CardView aboutbutton;
    private CardView editdetailsbutton;
    private CardView callstatusbutton;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController= Navigation.findNavController(view);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        main_view = inflater.inflate(R.layout.fragment_main, container, false);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mLogoutBtn = main_view.findViewById(R.id.logout_btn);
        getcallback = main_view.findViewById(R.id.register_issues_view);
        aboutbutton = main_view.findViewById(R.id.about_us_card);
        editdetailsbutton = main_view.findViewById(R.id.edit_details);
        callstatusbutton = main_view.findViewById(R.id.your_issues_view);

        getcallback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(MainActivity.this, GetCallBackActivity.class);
//                startActivity(intent);
                navController.navigate(R.id.action_mainFragment_to_getCallBackFragment);

            }
        });

        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(requireActivity())
                        .setIcon(R.drawable.ic_round_warning_24)
                        .setTitle("LOGOUT")
                        .setMessage("Are you sure you want to LOGOUT?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mAuth.signOut();
                                sendUserToLogin();
                            }
                        })
                        .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
            }
        });

        aboutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
//                startActivity(intent);
                    navController.navigate(R.id.action_mainFragment_to_aboutUsFragment);

            }
        });

        editdetailsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, EditUserDetailsActivity.class);
//                startActivity(intent);
                navController.navigate(R.id.action_mainFragment_to_editUserDetailsFragment);

            }
        });

        callstatusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, CallBackComplaintListActivity.class);
//                startActivity(intent);
                navController.navigate(R.id.action_mainFragment_to_callBackComplaintListFragment);


            }
        });
        return  main_view;
    }
//    boolean doubleBackToExitPressedOnce = false;
//
//    @Override
//    public void onBackPressed() {
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();
//
//            return;
//        }
//
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce=false;
//            }
//        }, 2000);
//    }

    @Override
    public void onStart() {
        super.onStart();
        if(mCurrentUser == null){
            sendUserToLogin();
        }
    }

    private void sendUserToLogin() {
//        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
//        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(loginIntent);
//        finish();
        navController.navigate(R.id.action_mainFragment_to_loginFragment);
    }
}