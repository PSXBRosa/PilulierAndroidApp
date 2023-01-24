package com.example.app_mob_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.database.Cursor;
import android.widget.CursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    // view
    private RecyclerView contactRv;
    private Cursor dataFetched;
    private MyCursorAdapter cursorAdapter;
    private ArrayList<String> arr;


    // database
    private DbHelper dbHelper;

    private Button button_new_sched;

    // Helper api Class
    private ApiConnectivity apiHelper;

    protected void loadData(){
        dataFetched = dbHelper.fetchData();
        cursorAdapter = new MyCursorAdapter(getApplication().getApplicationContext(), dataFetched);
        contactRv.setAdapter(cursorAdapter);
    }

    @Override
    protected void onResume(){
        super.onResume();
        loadData();
    }

    public ArrayList<String> getArr() {
        return arr;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialization
        contactRv = findViewById(R.id.contactRv);


        // Initialize DataBase
        dbHelper = new DbHelper(this);
        apiHelper = new ApiConnectivity(dbHelper, this);

        // Initialize button
        button_new_sched = findViewById(R.id.button_new_sched);

        // add listener
        button_new_sched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                // move to new activity to create new schedule
                arr = cursorAdapter.getArrSlots();
                if (arr.size() < 7){
                    Intent i = new Intent();
                    i.putExtra("slots", arr);
                    i.putExtra("isEditMode",false);
                    if(v == button_new_sched){
                        i.setClass(getApplicationContext(), AddEditSchedules.class);
                        startActivity(i);

                }

                }else{
                    Toast.makeText(getApplicationContext(), "Le pilulier est complet, veuillez suprimer un créneau", Toast.LENGTH_LONG).show();

                }

            }
            });

        this.loadData();

    }

    @Override
    protected void onStart() {
        super.onStart();
        apiHelper.copyFromRemote();
    }

}