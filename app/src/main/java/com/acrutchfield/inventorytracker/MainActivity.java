package com.acrutchfield.inventorytracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.acrutchfield.inventorytracker.data.MetaData;
import com.acrutchfield.inventorytracker.ui.DetailsActivity;
import com.acrutchfield.inventorytracker.ui.DialogAddInventory;
import com.acrutchfield.inventorytracker.ui.MetaDataAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements MetaDataAdapter.MetaDataInteractionListener {

    private RecyclerView rvTotalInventory;
    private MetaDataAdapter adapter;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvTotalInventory = findViewById(R.id.rv_total_inventory);
        fab = findViewById(R.id.floatingActionButton);

        Query query = FirebaseFirestore.getInstance()
                .collection("META_DATA")
                .orderBy("itemNumber");

        FirestoreRecyclerOptions<MetaData> options = new FirestoreRecyclerOptions.Builder<MetaData>()
                .setQuery(query, MetaData.class)
                .build();

        adapter = new MetaDataAdapter(options, this);

        rvTotalInventory.setLayoutManager(new LinearLayoutManager(this));
        rvTotalInventory.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItems();
            }
        });
    }

    private void addItems() {
        // Create a Dialog prompting for data
        DialogFragment dialogAddInventory = new DialogAddInventory();
        dialogAddInventory.show(getSupportFragmentManager(), "add_inventory");
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

    @Override
    public void onMetaDataInteraction(String itemNumber) {
        // Put item number into intent Extra
        Intent detailsIntent = new Intent(this, DetailsActivity.class);
        detailsIntent.putExtra("item_number", itemNumber);

        // Start DetailsActivity
        startActivity(detailsIntent);
    }
}
