package com.example.otp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CallBackComplaintListActivity extends AppCompatActivity {

    private Button backHome;
    private ProgressBar progressBar;
    private TextView problemBox;
    private RecyclerView recyclerView;
    //private List<ComplaintsDatabaseModel> complaintsDatabaseModelList=new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private ComplaintAdapter complaintAdapter;
    private DatabaseReference rootRef;
    private DatabaseReference complaintsRef;
    private FirebaseAuth auth;
    private FirebaseRecyclerAdapter recyclerAdapter;
    String itemName;
    String itemTime;
    String timePattern;
    String lastSeen;
    int itemStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_back_complaint_list);

        backHome=findViewById(R.id.back_home_cbcla);
        progressBar=findViewById(R.id.progressBar);
        problemBox=findViewById(R.id.problem_box_cbcla);
        recyclerView=findViewById(R.id.recyclerView);
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        auth=FirebaseAuth.getInstance();
        rootRef= FirebaseDatabase.getInstance().getReference();
        complaintsRef=rootRef.child("Complaints").child(auth.getCurrentUser().getUid());
        loadComplaints();

        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CallBackComplaintListActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });

        complaintsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(CallBackComplaintListActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void loadComplaints() {

        Query query=complaintsRef;
        FirebaseRecyclerOptions<ComplaintsDatabaseModel> options=new FirebaseRecyclerOptions.Builder<ComplaintsDatabaseModel>().setQuery(query,ComplaintsDatabaseModel.class).build();
        recyclerAdapter=new FirebaseRecyclerAdapter<ComplaintsDatabaseModel, ComplaintViewHolder>(options){

            @NonNull
            @Override
            public ComplaintViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_complaint_item,parent,false);
                return new ComplaintViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ComplaintViewHolder holder, int position, @NonNull ComplaintsDatabaseModel model) {

              final   String key=getRef(position).getKey();
                Log.d("qqqqqqqqq",key);
                progressBar.setVisibility(View.INVISIBLE);
                problemBox.setText("");

                complaintsRef.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot snapshot) {
                        if (snapshot.exists()){
                         final String   itemName=snapshot.child("problemStatement").getValue().toString();
                         final String  itemTime=snapshot.child("timeStamp").getValue().toString();
                         final String staffName = snapshot.child("staffName").getValue().toString();
                            final Intent intent=new Intent(CallBackComplaintListActivity.this,CallBackStatusActivity.class);
                         if (snapshot.child("feedback").exists()){
                             final String feedbackText=snapshot.child("feedback").getValue().toString();
                             intent.putExtra("feedback",feedbackText);
                         }

                          final int  itemStatus=Integer.parseInt(snapshot.child("status").getValue().toString());
                           final String lastSeen=snapshot.child("lastSeen").getValue().toString();
                            timePattern=itemTime.substring(0,10)+itemTime.substring(29,itemTime.length())+itemTime.substring(10,19);
                            if (itemName.length()>20){
                                holder.singleItemName.setText(itemName.substring(0,20)+"...");
                                holder.getSingleItemStaff.setText("• " + staffName);
                                holder.singleItemTime.setText("Registered : " + timePattern);
                            }
                            else {
                                holder.singleItemName.setText(itemName);
                                holder.getSingleItemStaff.setText("• " + staffName);
                                holder.singleItemTime.setText("Registered : " + timePattern);
                            }
                            holder.cView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    intent.putExtra("itemStatus",itemStatus);
                                    intent.putExtra("problemStatement",itemName);
                                    intent.putExtra("lastSeen",lastSeen);
                                    Log.d("dddddddd",String.valueOf(itemStatus));
                                    startActivity(intent);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        };

        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.startListening();

    }
    public static class ComplaintViewHolder extends RecyclerView.ViewHolder {

        View cView;
        TextView singleItemName;
        TextView singleItemTime;
        TextView getSingleItemStaff;
        public ComplaintViewHolder( View itemView) {

            super(itemView);
            cView=itemView;
            singleItemName=itemView.findViewById(R.id.single_item_name);
            singleItemTime=itemView.findViewById(R.id.single_item_time);
            getSingleItemStaff = itemView.findViewById(R.id.single_item_staff);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        recyclerAdapter.stopListening();
    }
}