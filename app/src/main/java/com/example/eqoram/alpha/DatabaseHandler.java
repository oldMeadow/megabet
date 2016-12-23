package com.example.eqoram.alpha;
/**
 * Created by Sven on 11/8/2016.
 */
import java.util.LinkedHashMap;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(Context context) {
        super(context, MySQLiteHelper.DATABASE_NAME, null, MySQLiteHelper.DATABASE_VERSION);
    }
    /* Allgemeine Datenbankbefehle */

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MySQLiteHelper.CREATE_USER_TABLE);
        db.execSQL(MySQLiteHelper.CREATE_MESSAGES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MySQLiteHelper.TABLE_MESSAGES);
        db.execSQL("DROP TABLE IF EXISTS " + MySQLiteHelper.TABLE_USER);
        onCreate(db);
    }


    /* Methoden für Messages */

    public long addMessage(Message message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //Werte setzen
        values.put(MySQLiteHelper.FK_FROM, message.getFrom());
        values.put(MySQLiteHelper.FK_TO, message.getTo());
        values.put(MySQLiteHelper.MESSAGE, message.getMessage());
        values.put(MySQLiteHelper.TIME, message.getTime().toString());
        values.put(MySQLiteHelper.IS_READ, message.getIs_read());
        //Nachricht hinzufügen
        return db.insert(MySQLiteHelper.TABLE_MESSAGES, null, values);
    }

    public LinkedHashMap<Integer, Message> getMessageList(String sender, String receiver) {
        LinkedHashMap<Integer, Message> linkedHashMap = new LinkedHashMap<Integer, Message>();
        SQLiteDatabase db = this.getReadableDatabase();

                Cursor cursor = db.query(MySQLiteHelper.TABLE_MESSAGES,
                new String[]{MySQLiteHelper.PK_ID},
                MySQLiteHelper.FK_FROM + "=? AND " + MySQLiteHelper.FK_TO + " =? OR " + MySQLiteHelper.FK_FROM + " =? AND " + MySQLiteHelper.FK_TO + " =?",
                new String[]{String.valueOf(sender), String.valueOf(receiver), String.valueOf(receiver), String.valueOf(sender)},
                null, null, MySQLiteHelper.TIME + " ASC", null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Message message = getMessage(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.PK_ID))));
                linkedHashMap.put(message.getId(), message);
            }
        }else{
            return null;
        }
        return linkedHashMap;
    }


    public int updateMessage(Message message){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(MySQLiteHelper.FK_FROM, message.getFrom());
        values.put(MySQLiteHelper.FK_TO, message.getTo());
        values.put(MySQLiteHelper.MESSAGE, message.getMessage());
        values.put(MySQLiteHelper.TIME, message.getTime().toString());
        values.put(MySQLiteHelper.IS_READ, message.getIs_read());

        return db.update(MySQLiteHelper.TABLE_MESSAGES, values, MySQLiteHelper.PK_ID + " = ?",
                new String[] { String.valueOf(message.getId()) });
    }

    public long deleteMessage(Message message){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(MySQLiteHelper.TABLE_MESSAGES, MySQLiteHelper.PK_ID + " = ?",
                                    new String[] { String.valueOf(message.getId()) });
    }




    public Message getMessage(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(MySQLiteHelper.TABLE_MESSAGES,
                new String[]{MySQLiteHelper.PK_ID, MySQLiteHelper.FK_FROM, MySQLiteHelper.FK_TO,
                             MySQLiteHelper.MESSAGE, MySQLiteHelper.TIME, MySQLiteHelper.IS_READ},
                             MySQLiteHelper.PK_ID + " =?", new String[]{String.valueOf(id)},
                             null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }else{
            return null;
        }
        Message message = new Message(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.PK_ID))),
                cursor.getString(cursor.getColumnIndex(MySQLiteHelper.FK_FROM)),
                cursor.getString(cursor.getColumnIndex(MySQLiteHelper.FK_TO)),
                cursor.getString(cursor.getColumnIndex(MySQLiteHelper.MESSAGE)),
                ConversionHelper.stringToTimeStamp(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.TIME))),
                ConversionHelper.intToBool(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.IS_READ)))));
        return message;
    }


    /* User Datenbank zugriffe */
    public long addUser(User user){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(MySQLiteHelper.KEY_EMAIL, user.getEmail());
        values.put(MySQLiteHelper.KEY_NAME, user.getName());

        return db.insert(MySQLiteHelper.TABLE_USER, null, values);
    }
    public User getUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery( "SELECT * FROM " + MySQLiteHelper.TABLE_USER + " WHERE " +
                MySQLiteHelper.KEY_EMAIL + "=?", new String[] {email} );

        cursor.moveToFirst();
        User usr = new User(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.KEY_EMAIL)), cursor.getString(cursor.getColumnIndex(MySQLiteHelper.KEY_NAME)));
        return usr;
    }

    public LinkedHashMap<String, User> getAllUsers() {
        LinkedHashMap<String, User> linkedHashMap = new LinkedHashMap<String, User>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery( "SELECT * FROM " + MySQLiteHelper.TABLE_USER, null );

        if(cursor != null){
            while(cursor.moveToNext()) {
                User usr = new User(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.KEY_EMAIL)), cursor.getString(cursor.getColumnIndex(MySQLiteHelper.KEY_NAME)));
                linkedHashMap.put(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.KEY_EMAIL)), usr);
            }
        }else{
            return null;
        }
        return linkedHashMap;
    }
    //updateUser
    public long updateUserName(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(MySQLiteHelper.KEY_NAME, user.getName());

        return db.update(MySQLiteHelper.TABLE_USER, values, MySQLiteHelper.KEY_EMAIL + " = ?",
                new String[] { user.getEmail()});
    }
    public long deleteUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(MySQLiteHelper.TABLE_USER, MySQLiteHelper.KEY_EMAIL + " = ?",
                new String[] { String.valueOf(user.getEmail())});
    }
    /*Testingpurpose*/
    public void testDB(String tablename){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + tablename+";", null);

        if( c != null){
            while(c.moveToNext()){
                String s = "";
                for(int i = 0; i < c.getColumnCount(); i++){
                    s = s + " | " + c.getString(i);
                }
                Log.d(tablename,s);
            }
            Log.d(tablename,"      ENDE");
        }
    }
}