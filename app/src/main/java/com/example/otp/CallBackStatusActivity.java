package com.example.otp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CallBackStatusActivity extends AppCompatActivity {

    private Button backhome;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_back_status);

        backhome = findViewById(R.id.back_home_cbs);
        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();
        sysStatusReference = FirebaseDatabase.getInstance().getReference().child(uid);
        callRegisteredImg = findViewById(R.id.call_registerd_img);
        callAckImg = findViewById(R.id.call_ack_img);
        inTouchImg = findViewById(R.id.in_touch_img);

        feedback=findViewById(R.id.feedback);

        problemResolvedImg = findViewById(R.id.problem_resolved_img);
        complaintClosedImg = findViewById(R.id.complaint_closed_img);
        problemBox = findViewById(R.id.problem_box_cbcla);
        problemBox.setMovementMethod(new ScrollingMovementMethod());
        timeStampLastSeen = findViewById(R.id.last_seen);


        callRegisteredImg.setImageResource(R.drawable.ic_baseline_close_24);
        callAckImg.setImageResource(R.drawable.ic_baseline_close_24);
        inTouchImg.setImageResource(R.drawable.ic_baseline_close_24);
        problemResolvedImg.setImageResource(R.drawable.ic_baseline_close_24);
        complaintClosedImg.setImageResource(R.drawable.ic_baseline_close_24);

        problemStatement=getIntent().getStringExtra("problemStatement");
        lastSeen=getIntent().getStringExtra("lastSeen");
        status=getIntent().getIntExtra("itemStatus",0);

        if (getIntent().getExtras().containsKey("feedback")){

            feedBack=getIntent().getStringExtra("feedback");
            feedback.setText(feedBack);

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

        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CallBackStatusActivity.this, CallBackComplaintListActivity.class);
                startActivity(intent);

            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(CallBackStatusActivity.this,CallBackComplaintListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}