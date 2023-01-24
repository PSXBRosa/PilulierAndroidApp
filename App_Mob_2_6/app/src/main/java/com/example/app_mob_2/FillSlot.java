package com.example.app_mob_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FillSlot extends AppCompatActivity {

    private Button button_home;
    private TextView slot;
    private String slot_number;
    private Button notificationBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_slot);

        button_home = findViewById(R.id.button_home);

        // fetching intent number
        Intent intent_final = getIntent();
        slot_number = intent_final.getStringExtra("slot");
        slot = findViewById(R.id.slotMed);
        slot.setText(slot_number);


        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                // move to new activity to create new schedule
                Intent home = new Intent();
                if(v == button_home){
                    home.setClass(FillSlot.this, MainActivity.class);
                    startActivity(home);
                }

            }
        });


        notificationBTN = findViewById(R.id.notificationBtn);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("MyNotification", "MyNotification", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        notificationBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplication().getApplicationContext(), "MyNotification");
                builder.setContentTitle("Prends les médicaments");
                builder.setContentText("C'est le moment de prendre vos pilules!");
                builder.setCategory(NotificationCompat.CATEGORY_ALARM);
                builder.setSmallIcon(R.drawable.ic_launcher_background);
                builder.setAutoCancel(true);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplication().getApplicationContext());
                managerCompat.notify(1, builder.build());

            }
        });

    }
}