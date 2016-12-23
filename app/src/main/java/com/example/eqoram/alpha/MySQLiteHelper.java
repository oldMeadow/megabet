package com.example.eqoram.alpha;

/**
 * Created by Sven on 11/9/2016.
 */

public final class MySQLiteHelper {

    // Datenbankversion
    public static final int DATABASE_VERSION = 4;

    // name der Datenbank
    public static final String DATABASE_NAME = "ChatHelper";
    // Tabellennamen
    public static final String TABLE_MESSAGES = "messages";
    public static final String TABLE_USER = "user";
    // Spalten für Nachrichtentabelle
    public static final String PK_ID = "id";
    public static final String FK_FROM = "sender";
    public static final String FK_TO = "receiver";
    public static final String MESSAGE = "message";
    public static final String TIME = "time";
    public static final String IS_READ = "is_read";
    // Spalten für Usertabelle
    public static final String KEY_EMAIL = "email";
    public static final String KEY_NAME = "name";

    //Statemens für die Tabellenerzeugung

    public static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USER
            +" (" +KEY_EMAIL + " text primary key, "
            + KEY_NAME + " text not null);";

    public static final String CREATE_MESSAGES_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_MESSAGES + " ("
            + PK_ID + " INTEGER PRIMARY KEY, "
            + FK_FROM + " TEXT, "
            + FK_TO + " TEXT, "
            + MESSAGE + " TEXT, "
            + TIME + " DATETIME, "
            + IS_READ + " INTEGER, "
            + " FOREIGN KEY ("+FK_FROM+") REFERENCES "+TABLE_USER+"("+KEY_EMAIL+"), "
            + " FOREIGN KEY ("+FK_TO+") REFERENCES "+TABLE_USER+"("+KEY_EMAIL+"));";
}
