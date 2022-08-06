package com.example.otp.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.otp.EditUserDetailsActivity;
import com.example.otp.MainActivity;
import com.example.otp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class EditUserDetailsFragment extends Fragment {


    View main_view;
    NavController navController;
    private Button backHome;
    private DatabaseReference sysEditDeatils;
    private FirebaseAuth auth;
    private String uid;


    private EditText name;
    private EditText phone;
    private EditText email;
    private EditText companyName;
    private EditText companyAddress;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Toolbar user_toolbar;

    public EditUserDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable  Bundle savedInstanceState) {
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
        main_view = inflater.inflate(R.layout.fragment_edit_user_details, container, false);
        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();
        sysEditDeatils = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        Log.d("uuuuiiiidddd", uid);

        user_toolbar = main_view.findViewById(R.id.user_toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(user_toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        sharedPreferences = requireActivity().getSharedPreferences("checker", MODE_PRIVATE);
        editor = sharedPreferences.edit();





        backHome = main_view.findViewById(R.id.submit_edit_details);
        name = main_view.findViewById(R.id.name);
        phone = main_view.findViewById(R.id.phone);
        email = main_view.findViewById(R.id.email);
        companyName = main_view.findViewById(R.id.company);
        companyAddress = main_view.findViewById(R.id.address);


        sysEditDeatils.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.d("qqqqqqqaaaaa", snapshot.child("User Name").getValue().toString());
                    String name1 = snapshot.child("User Name").getValue().toString();
                    String phone1 = snapshot.child("User Phone").getValue().toString();
                    String email1 = snapshot.child("User Email").getValue().toString();
                    String company_name1 = snapshot.child("Company Name").getValue().toString();
                    String company_address1 = snapshot.child("Company Address").getValue().toString();

                    name.setText(name1);
                    phone.setText(phone1);
                    email.setText(email1);
                    companyName.setText(company_name1);
                    companyAddress.setText(company_address1);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Name = name.getText().toString();
                String Phone = phone.getText().toString();
                String Email = email.getText().toString();
                String Company_name = companyName.getText().toString();
                String Company_address = companyAddress.getText().toString();

                if (!Name.isEmpty()) {
                    if (!Phone.isEmpty()) {
                        if (!Email.isEmpty()) {
                            if (!Company_name.isEmpty()) {
                                if (!Company_address.isEmpty()) {
                                    Map detailsEntry = new HashMap();
                                    detailsEntry.put("User Name", Name);
                                    detailsEntry.put("User Phone", Phone);
                                    detailsEntry.put("User Email", Email);
                                    detailsEntry.put("Company Name", Company_name);
                                    detailsEntry.put("Company Address", Company_address);
                                    sysEditDeatils.updateChildren(detailsEntry, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                            if (error == null) {
                                                Toast.makeText(requireActivity(), "Details Submitted Successfully!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(requireActivity(), MainActivity.class);
                                                startActivity(intent);
                                                editor.putBoolean("checkEmail", true);
                                                editor.apply();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(requireActivity(), "Enter Valid Company Address!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(requireActivity(), "Enter Valid Company Name!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(requireActivity(), "Enter Valid Email!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(requireActivity(), "Enter Valid Phone!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireActivity(), "Enter Valid Name!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return  main_view;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            requireActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);

    }
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(requireActivity(, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//    }


}
