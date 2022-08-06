package com.example.otp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.otp.CallBackComplaintListActivity;
import com.example.otp.CallBackStatusActivity;
import com.example.otp.ComplaintAdapter;
import com.example.otp.ComplaintsDatabaseModel;
import com.example.otp.MainActivity;
import com.example.otp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class CallBackComplaintListFragment extends Fragment {


    View main_view;
    NavController navController;

    private ImageView backHome;
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
    private Toolbar user_toolbar;


    public CallBackComplaintListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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
        main_view = inflater.inflate(R.layout.fragment_call_back_complaint_list, container, false);

        progressBar=main_view.findViewById(R.id.progressBar);
        problemBox=main_view.findViewById(R.id.problem_box_cbcla);
        recyclerView=main_view.findViewById(R.id.recyclerView);
        linearLayoutManager=new LinearLayoutManager(requireContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        user_toolbar = main_view.findViewById(R.id.list_issues_toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(user_toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        auth=FirebaseAuth.getInstance();
        rootRef= FirebaseDatabase.getInstance().getReference();
        complaintsRef=rootRef.child("Complaints").child(auth.getCurrentUser().getUid());
        loadComplaints();


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

        return  main_view;
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent=new Intent(requireActivity(),MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//    }

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
//                            if (snapshot.child("feedback").exists()){
//                                final String feedbackText=snapshot.child("feedback").getValue().toString();
//                                intent.putExtra("feedback",feedbackText);
//                            }

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

//                                    intent.putExtra("itemStatus",itemStatus);
//                                    intent.putExtra("problemStatement",itemName);
//                                    intent.putExtra("lastSeen",lastSeen);
//                                    Log.d("dddddddd",String.valueOf(itemStatus));
//                                    startActivity(intent);
                                    CallBackComplaintListFragmentDirections.ActionCallBackComplaintListFragmentToCallBackStatusFragment action=
                                            CallBackComplaintListFragmentDirections.actionCallBackComplaintListFragmentToCallBackStatusFragment(
                                             itemStatus,itemName,lastSeen
                                            );
                                    navController.navigate(action);
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
    public void onStart() {
        super.onStart();
        recyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        recyclerAdapter.stopListening();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            requireActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);

    }
}