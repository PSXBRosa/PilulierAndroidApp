package com.example.app_mob_2;

public class Constants {

    // db name
    public static final String DATABASE_NAME = "MED_DB";

    // database version
    public static final int DATABASE_VERSION = 1;

    // table name
    public static final String TABLE_NAME = "MED_TABLE";

    // table column names
    public static final String M_ID = "id";
    public static final String SLOT = "mode";
    public static final String NAME = "name";
    public static final String TIME = "cr_t";
    public static final String DATE = "time";

    public static final String M_MED = "m_med";
    public static final String QUANT = "quant";



    // query for create table
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "( "
            + M_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + M_MED + " TEXT, "
            + QUANT + " TEXT,"
            + DATE + " TEXT, "
            + TIME + " TEXT, "
            + NAME + " TEXT, "
            + SLOT + " TEXT "
            + ");";
}

