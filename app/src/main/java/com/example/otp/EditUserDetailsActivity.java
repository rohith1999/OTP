package com.example.otp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditUserDetailsActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_details);

        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();
        sysEditDeatils = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        Log.d("uuuuiiiidddd", uid);

        sharedPreferences = getSharedPreferences("checker", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        backHome = findViewById(R.id.submit_edit_details);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        companyName = findViewById(R.id.company);
        companyAddress = findViewById(R.id.address);


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
                                                Toast.makeText(EditUserDetailsActivity.this, "Details Submitted Successfully!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(EditUserDetailsActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                editor.putBoolean("checkEmail", true);
                                                editor.apply();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(EditUserDetailsActivity.this, "Enter Valid Company Address!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(EditUserDetailsActivity.this, "Enter Valid Company Name!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(EditUserDetailsActivity.this, "Enter Valid Email!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(EditUserDetailsActivity.this, "Enter Valid Phone!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditUserDetailsActivity.this, "Enter Valid Name!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EditUserDetailsActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}