package com.acrutchfield.inventorytracker.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acrutchfield.inventorytracker.R;
import com.acrutchfield.inventorytracker.Utils;
import com.acrutchfield.inventorytracker.data.Inventory;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.google.firebase.firestore.DocumentSnapshot;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class InventoryEntryAdapter extends FirestoreRecyclerAdapter<Inventory, InventoryEntryAdapter.InventoryEntryViewHolder> {

    private FirestoreRecyclerOptions<Inventory> options;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    InventoryEntryAdapter(@NonNull FirestoreRecyclerOptions<Inventory> options) {
        super(options);
        this.options = options;
    }

    @Override
    protected void onBindViewHolder(@NonNull InventoryEntryViewHolder inventoryEntryViewHolder, int i, @NonNull Inventory inventory) {
        ObservableSnapshotArray<Inventory> snapshots = options.getSnapshots();
        DocumentSnapshot snapshot = snapshots.getSnapshot(i);
        String id = snapshot.getId();

        inventoryEntryViewHolder.bind(inventory, id);
    }

    @NonNull
    @Override
    public InventoryEntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inventory_entry, parent, false);

        return new InventoryEntryViewHolder(view);
    }

    class InventoryEntryViewHolder extends RecyclerView.ViewHolder {

        TextView tvLocation;
        TextView tvSpot;
        TextView tvTotal;

        InventoryEntryViewHolder(@NonNull View itemView) {
            super(itemView);

            tvLocation = itemView.findViewById(R.id.tv_location);
            tvSpot = itemView.findViewById(R.id.tv_spot);
            tvTotal = itemView.findViewById(R.id.tv_total);
        }

        void bind(Inventory inventory, String id) {
            String spot = inventory.getSpot();
            String itemNumber = inventory.getItem();
            String location = inventory.getLocation();
            int total = inventory.getTotal();

            tvLocation.setText(location);
            tvSpot.setText(spot);
            tvTotal.setText(String.valueOf(total));
            itemView.setTag(R.id.tv_spot, Utils.getDocumentId(location, spot, itemNumber));
            itemView.setTag(R.id.tv_total, total);
        }
    }
}
