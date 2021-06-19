package com.example.testapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.FirebaseDatabase;

public class registerv1 extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView clickabletext;
    public Button signup;
    public EditText email,pass,confirmpass;
    public RadioGroup type;
    public ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerv1);

        //initialization
        mAuth = FirebaseAuth.getInstance();
        clickabletext=findViewById(R.id.signclickabletext);
        signup=findViewById(R.id.signbutton);
        email=findViewById(R.id.signemail);
        pass=findViewById(R.id.signpassword);
        confirmpass=findViewById(R.id.signrepassword);
        type=findViewById(R.id.groupedradiobuttonsregister);
        progress=findViewById(R.id.progreessbarsign);

        //remove statusbar
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        //set which word can be clicked
        String text="Already have an account? Click Here";
        SpannableString click=new SpannableString(text);
        ClickableSpan heretext=new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent=new Intent(registerv1.this,MainActivity.class);
                startActivity(intent);
            }
        };
        click.setSpan(heretext,31,35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //set what happens when the botton is clicked
        clickabletext.setText(click);
        clickabletext.setMovementMethod(LinkMovementMethod.getInstance());

        //set what happens upon successful sign up
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registeruser();
//                Intent intent=new Intent(registerv1.this,profilecreation.class);
//                startActivity(intent);
            }
        });
    }

    private void registeruser() {
        String emailinput=email.getText().toString().trim();
        String passinput=pass.getText().toString().trim();
        String confirmpassinput=confirmpass.getText().toString().trim();
        if(type.getCheckedRadioButtonId()==-1){
            type.requestFocus();
            Toast.makeText(this, "Type not selected", Toast.LENGTH_SHORT).show();
        }else{
            String typeinput=((RadioButton) findViewById(type.getCheckedRadioButtonId())).getText().toString().trim();
            if(emailinput.isEmpty()){
                email.setError("Email is required");
                email.requestFocus();
                if(!Patterns.EMAIL_ADDRESS.matcher(emailinput).matches()){
                    email.setError("Please set a valid email");
                    email.requestFocus();
                    if (passinput.isEmpty()){
                        pass.setError("Password is required");
                        pass.requestFocus();
                        if (confirmpassinput.isEmpty()){
                            confirmpass.setError("Confirm Password is required");
                            confirmpass.requestFocus();
                            if(!passinput.equals(confirmpassinput)){
                                pass.setError("Password is not the same as Confirm Password");
                                pass.requestFocus();
                                confirmpass.setError("Password is not the same as Confirm Password");
                                confirmpass.requestFocus();
                            }
                        }
                    }
                }
            }else{
                progress.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(emailinput,passinput)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    usercred user= new usercred(emailinput,typeinput,"none","none","none","none","none");
                                    Intent intent=new Intent(registerv1.this,profilecreation.class);
                                    FirebaseDatabase.getInstance().getReference("users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(registerv1.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                                progress.setVisibility(View.GONE);
                                                startActivity(intent);
                                            }else{
                                                FirebaseAuthException e = (FirebaseAuthException)task.getException();
                                                System.out.print(e);
                                                Toast.makeText(registerv1.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                                Log.e("LoginActivity", "Failed Registration", e);
                                                progress.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                                }else{
                                    FirebaseAuthException e = (FirebaseAuthException)task.getException();
                                    Toast.makeText(registerv1.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                    System.out.print(e);
                                    Log.e("LoginActivity", "Failed Registration", e);
                                    progress.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        }
    }
}