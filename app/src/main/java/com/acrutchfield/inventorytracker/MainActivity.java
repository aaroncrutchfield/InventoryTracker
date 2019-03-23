package com.acrutchfield.inventorytracker;

import android.os.Bundle;

import com.acrutchfield.inventorytracker.data.MetaData;
import com.acrutchfield.inventorytracker.ui.MetaDataAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvTotalInventory;
    private MetaDataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvTotalInventory = findViewById(R.id.rv_total_inventory);

        Query query = FirebaseFirestore.getInstance()
                .collection("META_DATA")
                .orderBy("itemNumber");

        FirestoreRecyclerOptions<MetaData> options = new FirestoreRecyclerOptions.Builder<MetaData>()
                .setQuery(query, MetaData.class)
                .build();

        adapter = new MetaDataAdapter(options);

        rvTotalInventory.setLayoutManager(new LinearLayoutManager(this));
        rvTotalInventory.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
