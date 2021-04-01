package com.anni.socialmediaplatform;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends Fragment {

    RecyclerView recyclerView;
    AdapterUsers adapterUsers;
    List<ModelUsers> usersList;
    FirebaseAuth firebaseAuth;

    public UsersFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_users, container, false);
        recyclerView=view.findViewById(R.id.recyclep);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        usersList=new ArrayList<>();
        firebaseAuth= FirebaseAuth.getInstance();
        getAllUsers();
        return view;
    }

    private void getAllUsers() {
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();//geetig current user
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");//retrieve all node value
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                    ModelUsers modelUsers = dataSnapshot1.getValue(ModelUsers.class);
                    //if uid is not equl is current user uid
                    //the add modelusers
                    if (dataSnapshot1.exists()) {
                        if (!modelUsers.getUid().equals(firebaseUser.getUid())) {
                            usersList.add(modelUsers);
                        }
                        adapterUsers = new AdapterUsers(getActivity(), usersList);
                        recyclerView.setAdapter(adapterUsers);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

}
