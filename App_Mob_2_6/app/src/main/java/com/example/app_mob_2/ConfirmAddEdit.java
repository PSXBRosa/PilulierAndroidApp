package com.example.app_mob_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ConfirmAddEdit extends AppCompatActivity {

    private Button button_save;
    private ListView lv_conf, lv_qtt;
    private ArrayList<String> arrayListMed, arrayListQuant, list_med, list_qtt, slots;
    private String date, time, slot, idd;
    private Boolean isEditMode;
    private TextView dateEt, hourEt;
    private ArrayAdapter<String> adapterMed, adapterQuant;
    private String name;

    //permission constant
    private static final int STORAGE_PERMISSION_CODE = 100;

    //string array of permission
    private String[] storagePermission;

    //database
    private DbHelper dbHelper;
    private ApiConnectivity apiHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_add_edit2);

        //init db
        dbHelper = new DbHelper(this);
        apiHelper = new ApiConnectivity(dbHelper, this);


        //init intent to fetch data take from AddEditSchedules
        Intent intent_med = getIntent();

        // getting list of medicaments provided by user in AddEditSchedules
        name = intent_med.getStringExtra("name");
        list_med = intent_med.getStringArrayListExtra("list_med");
        list_qtt = intent_med.getStringArrayListExtra("list_quants");
        date = intent_med.getStringExtra("date");
        time = intent_med.getStringExtra("hour");
        isEditMode = intent_med.getBooleanExtra("isEditMode",false);
        idd = intent_med.getStringExtra("id");
        slots = intent_med.getStringArrayListExtra("slots");
        ArrayList<String> slotArr = new ArrayList<String>();
        slotArr.add("1");
        slotArr.add("2");
        slotArr.add("3");
        slotArr.add("4");
        slotArr.add("5");
        slotArr.add("6");
        slotArr.add("7");
        if(isEditMode == false){
            for (int k=0; k<slots.size(); k = k + 1 ) {
                slotArr.remove(slots.get(k));
            }
        }
        String finSlot = slotArr.get(0);
        slot = finSlot;
        // initialize list view of medicaments
        lv_conf = findViewById(R.id.lvConfName);
        arrayListMed = new ArrayList<String>();
        adapterMed = new ArrayAdapter<String>(ConfirmAddEdit.this, android.R.layout.simple_expandable_list_item_1,
                arrayListMed);
        lv_conf.setAdapter(adapterMed);

        // initialize list view of quantities
        lv_qtt = findViewById(R.id.lvConfQtt);
        arrayListQuant = new ArrayList<String>();
        adapterQuant = new ArrayAdapter<String>(ConfirmAddEdit.this, android.R.layout.simple_expandable_list_item_1,
                arrayListQuant);
        lv_qtt.setAdapter(adapterQuant);


        // adding to list view
        for(int i = 0; i< list_med.size(); i++){

            // changing list view with medicaments
            String result = list_med.get(i);
            arrayListMed.add(result);
            adapterMed.notifyDataSetChanged();

            String result_2 = list_qtt.get(i);
            arrayListQuant.add(result_2);
            adapterQuant.notifyDataSetChanged();

        }

        // initialize and set date and hour
        dateEt = findViewById(R.id.show_date);
        hourEt = findViewById(R.id.show_hour);


        dateEt.setText(date);
        hourEt.setText(time);

        //init permission
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //initialize button
        button_save= findViewById(R.id.button_save);

        // add listener
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEditMode){
                    updateData();
                    sendUpdatedata();

                    Intent update_intent = new Intent(ConfirmAddEdit.this, MainActivity.class);
                    startActivity(update_intent);
                }else{
                    saveData();
                    sendSaveData();

                    Intent saved_intent = new Intent(ConfirmAddEdit.this, FillSlot.class);
                    saved_intent.putExtra("slot", slot);
                    startActivity(saved_intent);
                }


            }
        });


    }

    private void sendSaveData(){
        apiHelper.postToRemote("name", ""+slot, ""+time,""+date,
                ""+list_med, ""+list_qtt);
    }

    private void sendUpdatedata(){
        apiHelper.putToRemote(""+idd,"name", ""+slot, ""+time,""+date,
                ""+list_med, ""+list_qtt);
    }

    //check storage permission
    private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_PERMISSION_CODE);
    }

    //handle request permission

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case STORAGE_PERMISSION_CODE:
                if(grantResults.length >0){

                    //if all permission allowed return true
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if(storageAccepted){

                        //pickFromGallery();

                    }else {
                        Toast.makeText(getApplicationContext(), "Storage Perm Denied", Toast.LENGTH_SHORT).show();

                    }
                }
                break;
        }
    }

    private void  saveData() {

        //save db
        // TODO Insert the good slot
        long id = dbHelper.insertContact(""+list_med, ""+list_qtt,""+date,
                ""+time, ""+slot);

        // to check insert data, show a toast
        Toast.makeText(getApplicationContext(), "Créneau " + id + " inséré", Toast.LENGTH_SHORT).show();


    }
    private void  updateData() {

        //save db
        // TODO Insert the good slot
        dbHelper.updateContact(""+idd,""+list_med, ""+list_qtt,""+date,
                ""+time, ""+slot);

        // to check insert data, show a toast
        Toast.makeText(getApplicationContext(), "Créneau " + idd + " modifié", Toast.LENGTH_SHORT).show();

    }
}
