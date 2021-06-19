package com.example.testapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class tab4 extends Fragment {

    public Fragment ship,delivered,torate;
    public BottomNavigationView navbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab4, container, false);
        navbar=rootView.findViewById(R.id.topnavbar);
        ship=new Tab1ship();
        delivered=new Tab2ship();
        torate=new Tab3ship();

        Fragment selected=ship;
        getFragmentManager().beginTransaction().replace(R.id.innerfrag,selected).commit();

        navbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selected=null;
                switch(item.getItemId()){
                    //DEPENDING ON THE TAB THE FRAGMENT CHANGES TO THE DESIRED NEW CLASS
                    case R.id.shippingstatus:
                        selected=new Tab1ship();
                        break;
                    case R.id.delivered:
                        selected=new Tab2ship();
                        break;
                    case R.id.torate:
                        selected=new Tab3ship();
                    default:
                        break;
                }
                //COMMITS THE NEW FRAGMENT
                getFragmentManager().beginTransaction().replace(R.id.innerfrag,selected).commit();
                return true;
            }
        });


        return rootView;
    }
}