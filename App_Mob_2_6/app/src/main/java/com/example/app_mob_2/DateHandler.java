package com.example.app_mob_2;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHandler {

    private String dateTime;
    private String date;
    private String time;

    public DateHandler(String date, String time){
        try {
            this.date = date; // In the DD/MM/YYYY Format
            this.time = time; // In the HH:MM format

            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date formattedDate = formatter.parse(date + " " + time);
            DateFormat newFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            this.dateTime = newFormatter.format(formattedDate);
        } catch (ParseException e){
            e.printStackTrace();
        }
    }

    public DateHandler(String dateTime) {
        this.dateTime = dateTime;

        try{
            DateFormat newFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date dateObjectDateTime = newFormatter.parse(dateTime);
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat timeFormat = new SimpleDateFormat("HH:mm");
            this.date = dateFormat.format(dateObjectDateTime);
            this.time = timeFormat.format(dateObjectDateTime);
        } catch (ParseException e){
            e.printStackTrace();
        }

    }

    public String getDateTime() {
        return dateTime;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

}
