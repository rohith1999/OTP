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

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.otp.CallBackComplaintListActivity;
import com.example.otp.CallBackStatusActivity;
import com.example.otp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class CallBackStatusFragment extends Fragment {

    private View main_view;
    NavController navController;

    private ImageView backhome;
    private DatabaseReference sysStatusReference;
    private FirebaseAuth auth;
    private String uid;
    private TextView feedback;
    private ImageView callRegisteredImg;
    private ImageView callAckImg;
    private ImageView inTouchImg;
    private ImageView problemResolvedImg;
    private ImageView complaintClosedImg;
    private TextView problemBox;
    private TextView timeStampLastSeen;
    private String problemStatement;
    private String lastSeen;
    private String timePattern;
    private int status;
    private String feedBack;
    private Toolbar user_toolbar;

    public CallBackStatusFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController= Navigation.findNavController(view);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        main_view = inflater.inflate(R.layout.fragment_call_back_status, container, false);
        backhome = main_view.findViewById(R.id.back_home_cbs);
        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();
        sysStatusReference = FirebaseDatabase.getInstance().getReference().child(uid);
        callRegisteredImg = main_view.findViewById(R.id.call_registerd_img);
        callAckImg = main_view.findViewById(R.id.call_ack_img);
        inTouchImg = main_view.findViewById(R.id.in_touch_img);

        feedback=main_view.findViewById(R.id.feedback);

        user_toolbar = main_view.findViewById(R.id.issue_status_toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(user_toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        problemResolvedImg = main_view.findViewById(R.id.problem_resolved_img);
        complaintClosedImg = main_view.findViewById(R.id.complaint_closed_img);
        problemBox = main_view.findViewById(R.id.problem_box_cbcla);
        problemBox.setMovementMethod(new ScrollingMovementMethod());
        timeStampLastSeen = main_view.findViewById(R.id.last_seen);


        callRegisteredImg.setImageResource(R.drawable.ic_baseline_close_24);
        callAckImg.setImageResource(R.drawable.ic_baseline_close_24);
        inTouchImg.setImageResource(R.drawable.ic_baseline_close_24);
        problemResolvedImg.setImageResource(R.drawable.ic_baseline_close_24);
        complaintClosedImg.setImageResource(R.drawable.ic_baseline_close_24);

        if (getArguments()!=null){
            CallBackStatusFragmentArgs args = CallBackStatusFragmentArgs.fromBundle(getArguments());
            problemStatement=args.getProblemStatement();
            lastSeen=args.getLastSeen();
            status=args.getItemStatus();

        }
//        problemStatement=requireActivity().getIntent().getStringExtra("problemStatement");
//        lastSeen=requireActivity().getIntent().getStringExtra("lastSeen");
//        status=requireActivity().getIntent().getIntExtra("itemStatus",0);

        if (getArguments().getString("feedback")!=null){
            CallBackStatusFragmentArgs args = CallBackStatusFragmentArgs.fromBundle(getArguments());
          //  feedBack=args.
           // feedback.setText(feedBack);

        }
        else {
            feedBack="";
            feedback.setText("No Feedback");
        }

        if (status>=1){
            callRegisteredImg.setImageResource(R.drawable.ic_baseline_done_24);
        }
        if (status>=2){
            callAckImg.setImageResource(R.drawable.ic_baseline_done_24);
        }
        if (status>=3){
            inTouchImg.setImageResource(R.drawable.ic_baseline_done_24);
        }
        if (status>=4){
            problemResolvedImg.setImageResource(R.drawable.ic_baseline_done_24);
        }
        if (status>=5){
            complaintClosedImg.setImageResource(R.drawable.ic_baseline_done_24);
        }

        problemBox.setText("\""+problemStatement+"\"");
        if(lastSeen.length()>13){
            timePattern=lastSeen.substring(0,10)+lastSeen.substring(29)+lastSeen.substring(10,19);
        }
        else {
            timePattern=lastSeen;
        }
        timeStampLastSeen.setText("Problem Last Seen : " + timePattern);

        return main_view;
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
//        Intent intent=new Intent(CallBackStatusActivity.this,CallBackComplaintListActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//    }
}