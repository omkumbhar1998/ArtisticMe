package com.code_crawler.artisticme.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<AuthUI.IdpConfig> providers = Collections.singletonList(new AuthUI.IdpConfig.GoogleBuilder().build());
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getSupportActionBar().hide();

      Thread thread = new Thread(new Runnable() {
          @Override
          public void run() {

              auth = FirebaseAuth.getInstance();
              createDirectory();
              if (auth.getCurrentUser() == null){
                  Intent signInIntent = AuthUI.getInstance()
                          .createSignInIntentBuilder()
                          .setIsSmartLockEnabled(false)
                          .setAvailableProviders(providers)
                          .build();
                  startActivityForResult(signInIntent,9999);
              }else{
                  launchActivity(auth.getCurrentUser());
              }
          }
      });
      thread.start();

        /*auth = FirebaseAuth.getInstance();
        createDirectory();
        if (auth.getCurrentUser() == null){
            Intent signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setIsSmartLockEnabled(false)
                    .setAvailableProviders(providers)
                    .build();
            startActivityForResult(signInIntent,9999);
        }else{
            launchActivity(auth.getCurrentUser());
        }*/


    }

    private void createDirectory() {
        String DirectoryPath = Environment.getExternalStorageDirectory()+"/Artwork";
        File dir = new File(DirectoryPath);
        if( !dir.exists())
            dir.mkdir();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9999){
            if (resultCode == RESULT_OK){
                launchActivity(auth.getCurrentUser());
            }else{
                //Log.e("LoginActivity", "onActivityResult: ",IdpResponse.fromResultIntent(data).getError());
                Toast.makeText(this, "Login Failed. Try Again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void launchActivity(FirebaseUser currentUser) {
        Intent i = new Intent(this, HomeActivity.class);
        //Intent i = new Intent(this, FingerAuthActivity.class);
        i.putExtra("name",currentUser.getDisplayName());
        i.putExtra("email",currentUser.getEmail());
        startActivity(i);
        finish();
        //Toast.makeText(this, "Already logged in user", Toast.LENGTH_SHORT).show();

    }







}
