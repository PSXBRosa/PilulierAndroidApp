package com.example.app_mob_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ConfirmDelete extends AppCompatActivity {

    private Button button_save;
    private DbHelper dbHelper;
    private ApiConnectivity apiHelper;
    private String idd;
    private Button button_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_confirm_delete);

        dbHelper = new DbHelper(this);
        apiHelper = new ApiConnectivity(dbHelper, this);

        Intent intent_med = getIntent();

        idd = intent_med.getStringExtra("id");

        button_delete = findViewById(R.id.confirm_delete);

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent();
                if(v == button_delete){
                    dbHelper.deleteContact(idd);
                    apiHelper.deleteToRemote(idd);
                    home.setClass(ConfirmDelete.this, MainActivity.class);
                    startActivity(home);
                }
            }
        });
    }

}