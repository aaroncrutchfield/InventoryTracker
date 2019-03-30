package com.acrutchfield.inventorytracker.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.acrutchfield.inventorytracker.R;
import com.acrutchfield.inventorytracker.data.Inventory;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class DetailsActivity extends AppCompatActivity {

    private static final String ITEM_NUMBER = "item_number";
    InventoryEntryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Get itemNumber from intent
        Intent detailsIntent = getIntent();
        String itemNumber = detailsIntent.getStringExtra(ITEM_NUMBER);

        // Set title on ActionBar to itemNumber
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(itemNumber);

        RecyclerView rvItemDetails = findViewById(R.id.rv_item_details);

        Query query = FirebaseFirestore.getInstance()
                .collection("INVENTORY")
                .whereEqualTo("item", itemNumber);

        FirestoreRecyclerOptions<Inventory> options = new FirestoreRecyclerOptions.Builder<Inventory>()
                .setQuery(query, Inventory.class)
                .build();


        adapter = new InventoryEntryAdapter(options);

        rvItemDetails.setLayoutManager(new LinearLayoutManager(this));
        rvItemDetails.setAdapter(adapter);
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
