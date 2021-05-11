package com.dnc.wallhub.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dnc.wallhub.Activities.HomeActivity;
import com.dnc.wallhub.Activities.MainActivity;
import com.dnc.wallhub.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

import org.w3c.dom.Text;


public class SettingsFragment extends Fragment {
    SignInButton signInButton;
    GoogleSignInClient googleSignInClient;
    private static final int GOOGLE_SIGN_IN_CODE = 212;
    TextView name, mail;
    ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return inflater.inflate(R.layout.fragment_settings_default, container, false);
        }
        return inflater.inflate(R.layout.fragment_settings_logged_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name = view.findViewById(R.id.text_view_name);
        mail = view.findViewById(R.id.text_view_email);
        imageView = view.findViewById(R.id.imageView);
        signInButton = view.findViewById(R.id.button_google_sign_in);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();

        googleSignInClient = GoogleSignIn.getClient(getActivity(),gso);

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            ImageView imageView = view.findViewById(R.id.image_view);
            TextView textViewName = view.findViewById(R.id.text_view_name);
            TextView textViewEmail = view.findViewById(R.id.text_view_email);

            Glide.with(getActivity())
                    .load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString())
                    .into(imageView);

            textViewName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            textViewEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

            view.findViewById(R.id.text_view_logout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    googleSignInClient.signOut();
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                }
            });
        }else {
            view.findViewById(R.id.button_google_sign_in).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = googleSignInClient.getSignInIntent();
                    startActivityForResult(intent,GOOGLE_SIGN_IN_CODE);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN_CODE){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            }catch (ApiException e){
e.printStackTrace();
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(getActivity(), "Login Failure", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}