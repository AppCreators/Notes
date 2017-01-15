package com.example.kartikay.notes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    Button signUp;
    EditText username,Name,password;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog mpro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mpro=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        mDatabase=FirebaseDatabase.getInstance().getReference().child("Users");
        username=(EditText)findViewById(R.id.username);
        Name=(EditText)findViewById(R.id.name);
        password=(EditText)findViewById(R.id.password);
        signUp=(Button)findViewById(R.id.register);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });

    }

    private void startRegister() {
        final String mName=Name.getText().toString().trim();
        String mUsername=username.getText().toString().trim();
        String mPassword=password.getText().toString().trim();
        if(!TextUtils.isEmpty(mName)||!TextUtils.isEmpty(mUsername)||!TextUtils.isEmpty(mPassword)) {
            mpro.setMessage("Signing in");
            mpro.show();
            mAuth.createUserWithEmailAndPassword(mUsername,mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String user_id=mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user=mDatabase.child(user_id);
                        current_user.child("name").setValue(mName);
                        mpro.dismiss();
                        Intent i=new Intent(RegisterActivity.this,ExploreActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                }
            });
        }
    }
}
