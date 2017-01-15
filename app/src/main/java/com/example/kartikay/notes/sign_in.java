package com.example.kartikay.notes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by kartikay on 1/8/17.
 */

public class sign_in extends AppCompatActivity{
    private ProgressDialog mPro;
    private EditText userName,Password;
    private Button sign_in,sign_up,forgotPassword;
    public FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        mPro=new ProgressDialog(this);
        mDatabaseUser= FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUser.keepSynced(true);
        userName=(EditText)findViewById(R.id.username);
        Password=(EditText)findViewById(R.id.password);
        forgotPassword=(Button)findViewById(R.id.reset_password);
        sign_in=(Button)findViewById(R.id.signin);
        sign_up=(Button)findViewById(R.id.register);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(sign_in.this,ResetPassword.class);
                startActivity(i);
            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(sign_in.this,RegisterActivity.class);
                startActivity(i);
            }
        });
        mAuth = FirebaseAuth.getInstance();
        mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Intent i=new Intent(sign_in.this,ExploreActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            }
        };
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    private void checkUser(){
        final String user_id=mAuth.getCurrentUser().getUid();
        mDatabaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user_id)){
                    Intent i=new Intent(sign_in.this,ExploreActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
                else{
                    Toast.makeText(sign_in.this,"You need to register",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void startSignIn(){
        String username,password;
        username=userName.getText().toString();
        password=Password.getText().toString();
        if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password)) {
            Toast.makeText(sign_in.this,"Fields are empty",Toast.LENGTH_SHORT).show();
        }
        else{
            mPro.setMessage("Checking in");
            mPro.show();
            mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        mPro.dismiss();
                        checkUser();
                    }
                    else{
                        mPro.dismiss();
                        Toast.makeText(sign_in.this,"Error Login",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
