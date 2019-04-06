package com.acrutchfield.inventorytracker.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.acrutchfield.inventorytracker.R;
import com.acrutchfield.inventorytracker.data.Inventory;
import com.acrutchfield.inventorytracker.data.MetaData;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class DetailsActivity extends AppCompatActivity {

    private static final String ITEM_NUMBER = "item_number";
    InventoryEntryAdapter adapter;
    RecyclerView rvItemDetails;
    String itemNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Get itemNumber from intent
        Intent detailsIntent = getIntent();
        itemNumber = detailsIntent.getStringExtra(ITEM_NUMBER);

        Toolbar toolbar = findViewById(R.id.toolbar_inventory_details);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(itemNumber);

        rvItemDetails = findViewById(R.id.rv_item_details);

        Query query = FirebaseFirestore.getInstance()
                .collection("INVENTORY")
                .whereEqualTo("item", itemNumber);

        FirestoreRecyclerOptions<Inventory> options = new FirestoreRecyclerOptions.Builder<Inventory>()
                .setQuery(query, Inventory.class)
                .build();


        adapter = new InventoryEntryAdapter(options);

        rvItemDetails.setLayoutManager(new LinearLayoutManager(this));
        rvItemDetails.setAdapter(adapter);
        addItemTouchHelper();
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

    private void addItemTouchHelper() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //Get the id of the item being swiped
                String id = (String) viewHolder.itemView.getTag(R.id.tv_spot);
                int locationTotal = -(int) viewHolder.itemView.getTag(R.id.tv_total);
                switch(direction) {
                    case ItemTouchHelper.RIGHT:
                        promptToDeleteItem(id, locationTotal);
                        break;
                    case ItemTouchHelper.LEFT:
                        promptToEditItem(id);
                }

            }
        }).attachToRecyclerView(rvItemDetails);
    }

    private void promptToEditItem(String tag) {
        Toast.makeText(this, tag, Toast.LENGTH_SHORT).show();
        adapter.notifyDataSetChanged();
    }

    private void promptToDeleteItem(String documentId, final int locationTotal) {

        FirebaseFirestore.getInstance()
                .collection("INVENTORY").document(documentId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(DetailsActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
                        updateInventoryTotal(locationTotal);
                    }
                });
        adapter.notifyDataSetChanged();
    }

    private void updateInventoryTotal(final int locationTotal) {
        // Task is not complete
        FirebaseFirestore.getInstance()
                .collection("META_DATA").document(itemNumber)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                MetaData metaData;
                metaData = task.getResult().toObject(MetaData.class);
                int newTotal = metaData.getUpdatedTotal(locationTotal);

                FirebaseFirestore.getInstance()
                        .collection("META_DATA").document(itemNumber)
                        .update("inventoryTotal", newTotal);
            }
        });
    }
}
