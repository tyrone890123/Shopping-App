package com.example.testapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class sellerhome extends AppCompatActivity {

    public Fragment analytics, orders, add, reviews;
    public BottomNavigationView navbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellerhome);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        navbar=findViewById(R.id.navbarSeller);
        analytics= new tab1Seller();
        orders= new tab2Seller();
        add= new tab3Seller();
        reviews= new tab4Seller();

        String choice=getIntent().getStringExtra("page");

        Fragment selected=choice.equals("1")?analytics:add;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragSeller,selected).commit();
        navbar.setSelectedItemId(choice.equals("1")?R.id.analytics:R.id.edit);



        navbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //SETS THE FRAGMENT TO NULL
                Fragment selected=null;
                switch(item.getItemId()){
                    //DEPENDING ON THE TAB THE FRAGMENT CHANGES TO THE DESIRED NEW CLASS
                    case R.id.analytics:
                        selected=analytics;
                        break;
                    case R.id.orders:
                        selected=orders;
                        break;
                    case R.id.edit:
                        selected=add;
                        break;
                    case R.id.review:
                        selected=reviews;
                        break;
                    default:
                        break;
                }
                //COMMITS THE NEW FRAGMENT
                getSupportFragmentManager().beginTransaction().replace(R.id.fragSeller,selected).commit();
                return true;
            }
        });


    }


}