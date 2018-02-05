package com.moviesinfo.www.signindemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.jar.Attributes;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {

    private LinearLayout prof_section;
    private Button signout;
    private SignInButton signInButton;
    private TextView name;
    private TextView email;
    private ImageView prof_pic;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE=9;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prof_section=(LinearLayout)findViewById(R.id.prof_section);
        signout=(Button)findViewById(R.id.signout);
        signInButton=(SignInButton)findViewById(R.id.login);
        name=(TextView)findViewById(R.id.name);
        email=(TextView)findViewById(R.id.email_id);
        prof_pic=(ImageView)findViewById(R.id.profile_pic);

        signInButton.setOnClickListener(this);
        signout.setOnClickListener(this);
        prof_section.setVisibility(View.GONE);
        GoogleSignInOptions signInOptions= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
    googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();

    }

    @Override
    public void onClick(View v) {
      switch (v.getId())
      {
          case R.id.login:
              Signin();
              break;
          case R.id.signout:
              SignOut();
              break;
      }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private void Signin()
    {
        Intent intent=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);
    }
    private void SignOut()
    {
    Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
        @Override
        public void onResult(@NonNull Status status) {
            updateUi(false);
        }
    });
    }
    private void handle_result(GoogleSignInResult result)
    {
   if(result.isSuccess())
   {
       GoogleSignInAccount account=result.getSignInAccount();
       String Name=account.getDisplayName();
       String Email=account.getEmail();
       String img_url=account.getPhotoUrl().toString();
       name.setText(Name);
       email.setText(Email);
       Glide.with(this).load(img_url).into(prof_pic);
       updateUi(true);
   }
   else
   {
       updateUi(false);
   }
    }
    private void updateUi(boolean isLogin)
    {
       if(isLogin)
       {
           prof_section.setVisibility(View.VISIBLE);
           signInButton.setVisibility(View.GONE);

       }
       else
       {
           prof_section.setVisibility(View.GONE);
           signInButton.setVisibility(View.VISIBLE);
       }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ_CODE)
        {
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handle_result(result);
        }
    }
}
