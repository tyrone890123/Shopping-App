package com.example.testapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class customerhome extends AppCompatActivity {

    public Fragment home,search,shoppingcart,delivery;
    public BottomNavigationView navbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customerhome);
        //MAKES THE APPLICATION FULL SCREEN TO HIDE THE STATUS BAR
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        //INSTANTIATE THE NAVIGATION BAR
        navbar=findViewById(R.id.navbar);
        home=new tab1();
        search=new tab2();
        shoppingcart=new tab3();

        //PLACES A DEFAULT PAGE WHEN THE APPLICATION IS OPENED SPECIFICALLY THE FIRST TAB OF NAV BAR

        if(getIntent().getStringExtra("page")!=null){
            String back=getIntent().getStringExtra("page");
            if(Integer.valueOf(back)==3){
                Fragment selected=shoppingcart;
                getSupportFragmentManager().beginTransaction().replace(R.id.frag,selected).commit();
                navbar.setSelectedItemId(R.id.item3);
            }

        }else{
            Fragment selected=home;
            getSupportFragmentManager().beginTransaction().replace(R.id.frag,selected).commit();
        }




        //ADDS A LISTENER TO CHECK WHICH TAB THE USER HAS CLICKED
        navbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //SETS THE FRAGMENT TO NULL
                Fragment selected=null;
                switch(item.getItemId()){
                    //DEPENDING ON THE TAB THE FRAGMENT CHANGES TO THE DESIRED NEW CLASS
                    case R.id.item1:
                        selected=home;
                        break;
                    case R.id.item2:
                        selected=search;
                        break;
                    case R.id.item3:
                        selected=shoppingcart;
                        break;
                    case R.id.item4:
                        selected=new tab4();
                        break;
                    default:
                        break;
                }
                //COMMITS THE NEW FRAGMENT
                getSupportFragmentManager().beginTransaction().replace(R.id.frag,selected).commit();
                return true;
            }
        });
    }
}