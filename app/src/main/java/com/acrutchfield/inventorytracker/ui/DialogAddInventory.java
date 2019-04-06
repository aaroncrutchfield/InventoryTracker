package com.acrutchfield.inventorytracker.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.acrutchfield.inventorytracker.R;
import com.acrutchfield.inventorytracker.Utils;
import com.acrutchfield.inventorytracker.data.Inventory;
import com.acrutchfield.inventorytracker.data.MetaData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DialogAddInventory extends DialogFragment {

    private EditText etPartNumber;
    private EditText etContainers;
    private EditText etQuantity;
    private EditText etLocation;
    private EditText etSpot;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_inventory, null);

        etPartNumber = view.findViewById(R.id.et_part_number);
        etContainers = view.findViewById(R.id.et_containers);
        etQuantity = view.findViewById(R.id.et_quantity);
        etLocation = view.findViewById(R.id.et_location);
        etSpot = view.findViewById(R.id.et_spot);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Add Inventory")
                .setView(view)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Add item to database
                        addItemToDatabase();

                        DialogAddInventory.this.getDialog().dismiss();
                    }
                })
                .setNeutralButton("Next", null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DialogAddInventory.this.getDialog().cancel();
                    }
                });

        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = (dialog).getButton(AlertDialog.BUTTON_NEUTRAL);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // Add item to database
                        addItemToDatabase();

                        //Clear Part number, containers, and quantity
                        clearFields();
                    }
                });
            }
        });

        return dialog;
    }

    private void clearFields() {
        etPartNumber.setText("");
        etContainers.setText("");
        etQuantity.setText("");
    }

    private void addItemToDatabase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final int quantity = Integer.valueOf(etQuantity.getText().toString());
        int containers = Integer.valueOf(etContainers.getText().toString());

        final int total = containers * quantity;
        final String partnumber = etPartNumber.getText().toString();
        final String location = etLocation.getText().toString();
        final String spot = etSpot.getText().toString();

        // Check if item exists at location
        final DocumentReference inventoryEntryRef =
                db.collection("INVENTORY").document(Utils.getDocumentId(location, spot, partnumber));

        final DocumentReference metaDataRef =
                db.collection("META_DATA").document(partnumber);

        inventoryEntryRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        Inventory oldEntry = document.toObject(Inventory.class);
                        inventoryEntryRef.update("total", oldEntry.getUpdatedTotal(total));
                    } else {
                        Inventory newEntry = new Inventory(partnumber, location, spot, total);
                        inventoryEntryRef.set(newEntry);
                    }
                }
            }
        });

        metaDataRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        MetaData oldMetaData = document.toObject(MetaData.class);
                        metaDataRef.update("inventoryTotal", oldMetaData.getUpdatedTotal(total));
                    } else {
                        MetaData newMetaData = new MetaData();
                        newMetaData.setItemProperties(partnumber);
                        newMetaData.setInventoryTotal(total);
                        newMetaData.setStandardPack(quantity);
                        newMetaData.setDefaultLocation(location);
                        metaDataRef.set(newMetaData);
                    }
                }
            }
        });


        // Update if it does, create new entry otherwise


        // Check if it exists in metadata


        // Update if it does, create new entry otherwise
    }

    private String getDocumentId(String spot, String partnumber) {
        return spot + "_" + partnumber;
    }
}
