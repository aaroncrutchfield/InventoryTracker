package com.acrutchfield.inventorytracker.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acrutchfield.inventorytracker.R;
import com.acrutchfield.inventorytracker.data.MetaData;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MetaDataAdapter extends FirestoreRecyclerAdapter<MetaData, MetaDataAdapter.MetaDataViewHolder> {


    private final MetaDataInteractionListener lister;

    public interface MetaDataInteractionListener {
        void onMetaDataInteraction(String itemNumber);
    }
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MetaDataAdapter(@NonNull FirestoreRecyclerOptions options, MetaDataInteractionListener lister) {
        super(options);
        this.lister = lister;
    }

    @Override
    protected void onBindViewHolder(@NonNull MetaDataViewHolder metaDataViewHolder, int i, @NonNull MetaData metaData) {
        metaDataViewHolder.bind(metaData);
    }

    @NonNull
    @Override
    public MetaDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meta_data, parent, false);

        return new MetaDataViewHolder(view);
    }


    class MetaDataViewHolder extends RecyclerView.ViewHolder {

        private TextView tvItemNumber;
        private TextView tvTotalInventory;

        MetaDataViewHolder(@NonNull View itemView) {
            super(itemView);

            tvItemNumber = itemView.findViewById(R.id.tv_item_number);
            tvTotalInventory = itemView.findViewById(R.id.tv_total_inventory);
        }

        void bind(MetaData metaData) {
            final String completeItemNumber = metaData.getCompleteItemNumber();

            tvItemNumber.setText(completeItemNumber);
            tvTotalInventory.setText(String.valueOf(metaData.getInventoryTotal()));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lister.onMetaDataInteraction(completeItemNumber);
                }
            });
        }
    }
}
