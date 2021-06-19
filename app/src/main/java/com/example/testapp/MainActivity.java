package com.example.testapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    private FirebaseAuth mAuth;
    TextView clickabletext;
    EditText email,password;
    ProgressBar progress;
    private FirebaseUser user;
    private DatabaseReference ref;
    private String uid;
    ImageView image;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        //isntantiation
        email=findViewById(R.id.loginuser);
        password=findViewById(R.id.loginpass);
        clickabletext=findViewById(R.id.loginclickabletext);
        login=findViewById(R.id.loginbutton);
        progress=findViewById(R.id.progressbarlogin);
        mAuth = FirebaseAuth.getInstance();
        image=findViewById(R.id.loginimage);

        String text="New here? Sign up here";
        SpannableString click=new SpannableString(text);
        ClickableSpan heretext=new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent=new Intent(MainActivity.this,registerv1.class);
                startActivity(intent);
            }
        };
        click.setSpan(heretext,18,22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        clickabletext.setText(click);
        clickabletext.setMovementMethod(LinkMovementMethod.getInstance());

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userlogin();
            }
        });

    }

    private void userlogin() {
        String emailinput=email.getText().toString().trim();
        String passinput=password.getText().toString().trim();
        if(emailinput.isEmpty()){
            email.setError("Email is required");
            email.requestFocus();
            if(!Patterns.EMAIL_ADDRESS.matcher(emailinput).matches()){
                email.setError("Please set a valid email");
                email.requestFocus();
                if (passinput.isEmpty()){
                    password.setError("Password is required");
                    password.requestFocus();
                }
            }

        }else{
            progress.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(emailinput,passinput).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();

                        user= FirebaseAuth.getInstance().getCurrentUser();
                        ref= FirebaseDatabase.getInstance().getReference("users");
                        uid=user.getUid();

                        ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                usercred profile= snapshot.getValue(usercred.class);

                                if(profile!=null){
                                    if((profile.name.equals("none"))||(profile.gender.equals("none"))||(profile.phone.equals("none"))){
                                        progress.setVisibility(View.GONE);
                                        startActivity(new Intent(MainActivity.this,profilecreation.class));
                                    }else{
                                        progress.setVisibility(View.GONE);
                                        if(profile.type.equals("Customer")){
                                            startActivity(new Intent(MainActivity.this,customerhome.class));
                                        }else if(profile.type.equals("Seller")){
                                            Intent sellerpage=new Intent(MainActivity.this,sellerhome.class);
                                            sellerpage.putExtra("page","1");
                                            startActivity(sellerpage);

                                        }
                                    }
                                }else{
                                    System.out.print("none");
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                System.out.print("mistake");
                            }
                        });
                    }else{
                        Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        progress.setVisibility(View.GONE);
                    }
                }
            });
        }
    }


}