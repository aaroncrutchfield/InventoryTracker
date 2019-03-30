package com.acrutchfield.inventorytracker.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acrutchfield.inventorytracker.R;
import com.acrutchfield.inventorytracker.data.Inventory;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class InventoryEntryAdapter extends FirestoreRecyclerAdapter<Inventory, InventoryEntryAdapter.InventoryEntryViewHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    InventoryEntryAdapter(@NonNull FirestoreRecyclerOptions<Inventory> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull InventoryEntryViewHolder inventoryEntryViewHolder, int i, @NonNull Inventory inventory) {
        inventoryEntryViewHolder.bind(inventory);
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

        void bind(Inventory inventory) {
            tvLocation.setText(inventory.getLocation());
            tvSpot.setText(inventory.getSpot());
            tvTotal.setText(String.valueOf(inventory.getTotal()));
        }
    }
}
