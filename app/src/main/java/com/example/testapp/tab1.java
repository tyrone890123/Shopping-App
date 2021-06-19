package com.example.testapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class tab1 extends Fragment {

    private FirebaseUser user;
    private DatabaseReference ref,ref2;
    private String uid;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    public ImageView image;
    public TextView name;
    public RecyclerView hotitems,stores;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tab1, container, false);
        setRetainInstance(true);
        stores= rootView.findViewById(R.id.storeslist);
        hotitems= rootView.findViewById(R.id.hotitemslist);

        name=rootView.findViewById(R.id.name);
        user= FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference("users");
        ref2=FirebaseDatabase.getInstance().getReference("items");
        uid=user.getUid();
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        image=rootView.findViewById(R.id.imageView);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), openProfile.class);
                startActivity(intent);
            }
        });

        LinearLayoutManager layoutManager =new LinearLayoutManager(getActivity());
        LinearLayoutManager layoutManager2 =new LinearLayoutManager(getActivity());

        stores.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        hotitems.setLayoutManager(layoutManager2);
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);

        //filter by type
        Query query = FirebaseDatabase.getInstance().getReference("users").orderByChild("type").equalTo("Seller");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String,Map<String,String>> storeProf= (Map)snapshot.getValue();
                Set<String> keys=storeProf.keySet();
                String[] myArray = new String[keys.size()];
                keys.toArray(myArray);

                ArrayList<stores> store=new ArrayList<>();
                ArrayList<String> storeuuid=new ArrayList<>();

                for(int i=0;i<(myArray.length<6?myArray.length:6);i++){
                    Map <String, String> fields = storeProf.get(myArray[i]);
                    store.add(new stores(fields.get("name"),fields.get("pic"),fields.get("rating"),fields.get("adress")));
                    storeuuid.add(myArray[i]);
                }

                storeAdapter test=new storeAdapter(rootView.getContext());
                test.setStore(store,storeuuid);
                stores.setAdapter(test);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usercred profile= snapshot.getValue(usercred.class);
                String nameuser=profile.name;
                name.setText(nameuser);

                if(profile!=null){
                    String pic=profile.pic;

                    if(!pic.equals("none")){
                        // Reference to an image file in Cloud Storage
                        storageReference.child("images/"+uid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
//                                System.out.print(uri.toString().trim());
                                Glide.with(rootView.getContext())
                                        .load(uri.toString().trim())
                                        .into(image);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                System.out.print(exception);
                            }
                        });
                    }
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.print("mistake");
            }
        });


//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Map <String, Map<String,String>> map = (Map)snapshot.getValue();
//                Map <String, String> map2 = map.get("type").entrySet().stream()
//                        .filter(mapCond -> "Seller".equals(mapCond.getValue()))
//                        .collect(Collectors.toMap(mapCond -> mapCond.getKey(), mapCond -> mapCond.getValue()));
//                Set<String> mapkeys=map2.keySet();
//                String[] myArray = new String[mapkeys.size()];
//                mapkeys.toArray(myArray);
//
//                Log.d("uuid sellers:", Arrays.toString(myArray));
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });



//        store.add(new stores("SM","https://pngimage.net/wp-content/uploads/2018/06/sm-mall-logo-png-7.png","4","Quezon City"));
//        store.add(new stores("PURE GOLD","https://www.clickthecity.com/img/restaurants/original/5747.jpg","5","Quezon City"));
//        store.add(new stores("WALTER MART","https://assets.bossjob.com/companies/6830/logo/tcX2I61NBjgU5vaDTl00cOHspHsKmhca2EWenldp.png","3","Quezon City"));
//        store.add(new stores("LANDMARK","https://static1.eyellowpages.ph/uploads/yp_business/photo/20364/thumb_The_LandMark_Logo.gif","2","Malabon City"));
//        store.add(new stores("ROBINSONS","https://upload.wikimedia.org/wikipedia/en/f/ff/Robinsons_Supermarket_logo.jpg","4","Malabon City"));
//        store.add(new stores("SUPER 8","https://ph.top10place.com/img_files/294917943966629","3","Malabon City"));




        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map <String, Map<String, Object>> map = (Map)snapshot.getValue();
                Set<String> test=map.keySet();
                String[] myArray = new String[test.size()];
                test.toArray(myArray);

                ArrayList<items> item=new ArrayList<>();
                ArrayList<String> itemuuid=new ArrayList<>();

//                Log.d("items", map.values().toString());
                for(int i=0;i<myArray.length;i++){
                    Map<String, Object> fields = map.get(myArray[i]);
                    itemuuid.add(myArray[i]);
                    item.add(new items(fields.get("name").toString(), fields.get("category").toString(), fields.get("url").toString(), fields.get("rating").toString(), fields.get("price").toString(),fields.get("stock").toString(), fields.get("brand").toString(), fields.get("description").toString(), fields.get("sellerUUID").toString(),((Long) fields.get("quantitySold")).intValue()));
                }

//                Log.d("items", String.valueOf(item.size()));
                itemAdapter test2=new itemAdapter(rootView.getContext());
                test2.setItem(item,itemuuid);
                hotitems.setAdapter(test2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//



//        item.add(new items("Cupcakes","Bakery Goods","https://randomwordgenerator.com/img/picture-generator/52e1d64a435aae14f1dc8460962e33791c3ad6e04e507441722a72dd904ac1_640.jpg","4","100.00","1000","monde","Lorem ipsum \ndolor sit \namet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."));
//        item.add(new items("Tomato","Fresh Produce","https://www.generatorslist.com/public/img/misc/objects/tomato.jpg","5","1200.00","1200","delmonte","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."));
//        item.add(new items("Soap","Hygiene","https://www.generatorslist.com/public/img/misc/objects/soap.jpg","3","300.00","500","safeguard","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."));
//        item.add(new items("Coloring Pencil","Stationary","https://www.generatorslist.com/public/img/misc/objects/colored-pencil.jpg","5","330.00","1200","crayola","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."));
//        item.add(new items("Gloves","Cleaning","https://www.generatorslist.com/public/img/misc/objects/glove.jpg","1","564.00","200","latex","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."));
//        item.add(new items("Controller","Gaming","https://www.generatorslist.com/public/img/misc/objects/controller.jpg","2","1000.00","500","Xbox","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."));
//        item.add(new items("Butter Knife","Kitchen","https://www.generatorslist.com/public/img/misc/objects/knife.jpg","5","120.00","100","kitchenware","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."));
//        item.add(new items("Hand Drill","Hardware","https://www.generatorslist.com/public/img/misc/objects/drill.jpg","3","110.00","100","homedepot","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."));
//        item.add(new items("Needle","Sewing","https://www.generatorslist.com/public/img/misc/objects/needle.jpg","4","130.00","100","sew","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."));
//        item.add(new items("Apron","Kitchen","https://www.generatorslist.com/public/img/misc/objects/apron.jpg","2","112.00","300","kitenware","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."));


//        test2.setItem(item,itemuuid);






        return rootView;
    }
}