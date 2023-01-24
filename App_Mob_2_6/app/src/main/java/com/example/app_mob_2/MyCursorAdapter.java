package com.example.app_mob_2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyCursorAdapter extends RecyclerView.Adapter<MyCursorAdapter.AlarmViewHolder>{

    private Context context;
    private Cursor alarmQuery;
    private ArrayList<ModelAlarm> alarmList;
    private ArrayList<String> arrSlots;
    private DbHelper dbHelper;

    public ArrayList<String> getArrSlots() {
        return arrSlots;
    }

    public MyCursorAdapter(Context context, Cursor alarmQuery){
        this.context = context;
        this.alarmQuery = alarmQuery;
        dbHelper = new DbHelper(context);
        arrSlots  = new ArrayList<String>();
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_alarm_item,parent,false);
        AlarmViewHolder vh = new AlarmViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        int nameIndex, timeIndex, dateIndex, idIndex, slotIndex;
        this.alarmQuery.moveToPosition(position);

        // retrieving data fetched from db

        nameIndex = this.alarmQuery.getColumnIndexOrThrow(Constants.M_MED);
        String alarmMedC = this.alarmQuery.getString(nameIndex);
        String alarmMedD = alarmMedC.replace("[", "");
        String alarmMed = alarmMedD.replace("]", "");

        nameIndex = this.alarmQuery.getColumnIndexOrThrow(Constants.QUANT);
        String alarmQuantC = this.alarmQuery.getString(nameIndex);
        String alarmQuantD = alarmQuantC.replace("[", "");
        String alarmQuant = alarmQuantD.replace("]", "");

        dateIndex = this.alarmQuery.getColumnIndexOrThrow(Constants.DATE);
        String date = this.alarmQuery.getString(dateIndex);

        timeIndex = this.alarmQuery.getColumnIndexOrThrow(Constants.TIME);
        String time = this.alarmQuery.getString(timeIndex);

        idIndex = this.alarmQuery.getColumnIndexOrThrow(Constants.M_ID);
        String id = this.alarmQuery.getString(idIndex);

        slotIndex = this.alarmQuery.getColumnIndexOrThrow(Constants.SLOT);
        String slot = this.alarmQuery.getString(slotIndex);

        holder.alarmName.setText(alarmMed);
        holder.dateMed.setText(date + " | " + time);

        arrSlots.add(id);
        // handle editBtn click

        holder.buttonModif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // create intent to move AddEditActivity to update data

                Intent intent = new Intent(context,AddEditSchedules.class);

                //pass the value of current position

                intent.putExtra("list_meds",alarmMed);
                intent.putExtra("list_quants",alarmQuant);
                intent.putExtra("date",date);
                intent.putExtra("time",time);
                intent.putExtra("id",id);
                intent.putExtra("slot",slot);
                // pass a boolean data to define it is for edit purpose
                intent.putExtra("isEditMode",true);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //start intent
                context.startActivity(intent);

            }
        });

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // create intent
                Intent intent = new Intent(context,ConfirmDelete.class);
                //pass the value of index
                intent.putExtra("id",id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //start intent
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount()  {
        return this.alarmQuery.getCount();
    }

    class AlarmViewHolder extends RecyclerView.ViewHolder{
        //view for row_contact_item
        ImageView alarmImage, alarmIcon;
        TextView alarmName, alarmEdit, alarmDelete, dateMed;
        RelativeLayout relativeLayout;
        Button buttonModif;
        Button buttonDelete;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);

            //init view
            alarmName = itemView.findViewById(R.id.alarmName);
            dateMed = itemView.findViewById(R.id.dateMed);
            buttonModif = itemView.findViewById(R.id.button_modif);
            buttonDelete = itemView.findViewById(R.id.button_delete);

        }
    }
        
}