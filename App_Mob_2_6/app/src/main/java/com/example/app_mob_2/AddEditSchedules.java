package com.example.app_mob_2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;

import java.util.ArrayList;
import java.util.Calendar;

public class AddEditSchedules extends AppCompatActivity {

    private Button button_conf, button_add;
    private EditText list_med, list_qtt, hourEt;
    private TextView dateEt;
    private ListView lv_1, lv_qtt;
    private String date, hour;
    private LinearLayout layout;
    private AlertDialog dialog;
    private ArrayList<String> arrayListMed, arrayListQuant, arMeds, arQuants, slots;
    private ArrayAdapter<String> adapterMed, adapterQuant;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private static final String TAG = "MainActivity";
    private Boolean isEditMode;
    private String idInt, slotInt;

    //private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_schedules);

        //init actionBar
        // TODO actionbar
        // actionBar = getSupportActionBar();


        //back button
        // actionBar.setDisplayHomeAsUpEnabled(true);
        // actionBar.setDisplayShowHomeEnabled(true);

        // Initialize button, list of medicaments and list view
        button_conf = findViewById(R.id.button_conf);
        button_add = findViewById(R.id.button_add);

        dateEt = findViewById(R.id.date);
        hourEt = findViewById(R.id.hour);
        arrayListMed = new ArrayList<String>();
        arrayListQuant = new ArrayList<String>();
        arMeds = new ArrayList<String>();
        arQuants = new ArrayList<String>();

        // creating the dialog

        layout = findViewById(R.id.container);
        button_add = findViewById(R.id.button_add);
        buildDialog();

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("isEditMode",false);

        if (isEditMode) {

            // get intent data from edit
            String list_meds = intent.getStringExtra("list_meds");

            //get the other value from intent


            String listMedInt = intent.getStringExtra("list_meds");
            String listQuantInt = intent.getStringExtra("list_quants");
            String dateInt = intent.getStringExtra("date");
            String timeInt = intent.getStringExtra("time");
            idInt = intent.getStringExtra("id");
            idInt = intent.getStringExtra("id");
            slotInt = intent.getStringExtra("slot");
            System.out.println(slotInt);
            System.out.println("_________----------____--");
            dateEt.setText(dateInt);
            hourEt.setText(timeInt);
            arrayListMed = new ArrayList<String>();
            arrayListQuant = new ArrayList<String>();

            // transforming listMedInt and listQuantInt to arrays

            String[] listMedArr = listMedInt.split(",");
            String[] listQuantArr = listQuantInt.split(",");

            // adding loaded items to list view

            for (int j = 0; j < listMedArr.length; j = j + 1) {
                String result = (String) Array.get(listMedArr, j);
                String result_2 = (String) Array.get(listQuantArr, j);
                addCard(result, result_2);
            }


        }else{
            slots = intent.getStringArrayListExtra("slots");
            System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOO");
            System.out.println(slots);// TODO enlever ça

        }


        // add listener confirmation button
        button_conf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // take what's written

                date = dateEt.getText().toString();
                hour = hourEt.getText().toString();
                Intent intent_med = new Intent(AddEditSchedules.this, ConfirmAddEdit.class);
                intent_med.putExtra("list_med", arMeds);
                intent_med.putExtra("list_quants", arQuants);
                intent_med.putExtra("date", date);
                intent_med.putExtra("hour", hour);
                intent_med.putExtra("slots", slots);

                //intent_med.putExtra("hour", slot);
                if (isEditMode){
                    intent_med.putExtra("isEditMode", true);
                    intent_med.putExtra("id", idInt);
                    intent_med.putExtra("slot", slotInt);
                    intent_med.putExtra("isEditMode",true);
                }
                // check if info not empty
                if (arMeds.isEmpty() || arQuants.isEmpty() || date.isEmpty() || hour.isEmpty()) {

                    // move to new activity to create new schedule
                    Toast.makeText(getApplicationContext(), "Il y a des champs vides", Toast.LENGTH_SHORT).show();


                } else {
                    if (v == button_conf) {

                        startActivity(intent_med);


                    }
                }


            }
        });

        // put date picker
        dateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddEditSchedules.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
            });

            mDateSetListener = new DatePickerDialog.OnDateSetListener(){
                @Override
                public void onDateSet(DatePicker datePicker, int year , int month, int day){
                    month = month + 1;
                    Log.d(TAG, "onDateSet: date: " + day + "/" + month + "/" + year);

                    String date = day + "/" + month + "/" + year;
                    dateEt.setText(date);
                }

            };
        }
    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog, null);

        final EditText name = view.findViewById(R.id.nameEdit);
        final EditText number = view.findViewById(R.id.numberEdit);

        builder.setView(view);
        builder.setTitle("Insérer le nom")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (name.getText().toString().matches("") || (number.getText().toString().matches(""))){

                            Toast.makeText(getApplicationContext(), "Il y a des champs vides", Toast.LENGTH_SHORT).show();
                            name.setText("");
                            number.setText("");
                        }else{
                            addCard(name.getText().toString(), number.getText().toString());
                            name.setText("");
                            number.setText("");
                        }

                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        name.setText("");
                        number.setText("");
                    }
                });

        dialog = builder.create();
    }

    private void addCard(String name, String quantity) {
        final View view = getLayoutInflater().inflate(R.layout.card, null);

        TextView nameView = view.findViewById(R.id.name);
        TextView quantView = view.findViewById(R.id.quant);
        Button delete = view.findViewById(R.id.delete);

        nameView.setText(name);
        quantView.setText(quantity);
        arMeds.add(name);
        arQuants.add(quantity);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arMeds.remove(nameView.getText().toString());
                arQuants.remove(quantView.getText().toString());
                layout.removeView(view);

            }
        });

        layout.addView(view);

    }
    }




