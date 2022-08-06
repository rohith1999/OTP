package com.example.otp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetCallBackActivity extends AppCompatActivity {


    private DatabaseReference sysGetCallback;
    private DatabaseReference sysGetCallbackStaff;
    private FirebaseAuth auth;
    private String uid;
    private String pid;
    private Button getCallbackButton;
    private EditText callBackEditText;
    private String staffName;
    private int status=0;
    private AlertDialog.Builder alertDialog;
    private List<String> categories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_call_back);
        getCallbackButton = findViewById(R.id.submit_get_callback);
        final Spinner dropdown = findViewById(R.id.spinner);




        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();

        pid = FirebaseDatabase.getInstance().getReference().child("Complaints").child(uid).push().getKey();
        sysGetCallback = FirebaseDatabase.getInstance().getReference().child("Complaints").child(uid).child(pid);
        sysGetCallbackStaff = FirebaseDatabase.getInstance().getReference().child("Complaints").child("Staff").push();
        callBackEditText = findViewById(R.id.callBackProblem);

                alertDialog = new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_baseline_confirmation_number_24)
                .setTitle("Are you sure to register this complaint ticket?")
                .setMessage("Dismissing will return you back to HOME page!")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(staffName.equals("Choose Staff")){
                            Toast.makeText(GetCallBackActivity.this, "Choose Staff To Register Call Back!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            sysGetCallback.child("problemStatement").setValue(callBackEditText.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        sysGetCallback.child("staffName").setValue(staffName).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {

                                                    sysGetCallback.child("lastSeen").setValue("Not Yet Seen!").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                        }
                                                    });
                                                    sysGetCallback.child("timeStamp").setValue("" + Calendar.getInstance().getTime()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if (task.isSuccessful()) {

                                                                status=1;
                                                                sysGetCallback.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                        if (task.isSuccessful()) {
                                                                            Map detailsEntry= new HashMap();
                                                                            detailsEntry.put("problemStatement", callBackEditText.getText().toString().trim());
                                                                            detailsEntry.put("staffName", staffName);
                                                                            detailsEntry.put("lastSeen", "Not Yet Seen!");
                                                                            detailsEntry.put("timeStamp", "" + Calendar.getInstance().getTime());
                                                                            detailsEntry.put("status", status);
                                                                            detailsEntry.put("cuid", uid);
                                                                            detailsEntry.put("pid", pid);
                                                                            sysGetCallbackStaff.updateChildren(detailsEntry, new DatabaseReference.CompletionListener() {
                                                                                @Override
                                                                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                                                    if(error==null){

                                                                                        Toast.makeText(GetCallBackActivity.this, "Complaint Successful!\nWe will reach out to you soon!", Toast.LENGTH_LONG).show();
                                                                                        Intent intent = new Intent(GetCallBackActivity.this, MainActivity.class);
                                                                                        startActivity(intent);
                                                                                    }
                                                                                }
                                                                            });
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                })
                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(GetCallBackActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

        getCallbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!callBackEditText.getText().toString().isEmpty()) {

                    alertDialog.show();

                } else {
                    Toast.makeText(GetCallBackActivity.this, "Enter Valid Particulars!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        categories = new ArrayList<String>();
        categories.add("Choose Staff");
        categories.add("Anyone");
        FirebaseDatabase.getInstance().getReference().child("Staff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Log.d("hahaha",snapshot1.child("Staff Name").getValue().toString());
                    categories.add(snapshot1.child("Staff Name").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        categories.add("Ravi");
//        categories.add("Rohith");
//        categories.add("Nischai");
//        categories.add("Mighty Raju");
//        categories.add("Ganesh Gaithonde");
//        categories.add("Pankaj Tripati");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(dataAdapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                staffName = adapterView.getItemAtPosition(i).toString();

                // Showing selected spinner item
                //Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(GetCallBackActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}