package appx_homescreen.appx;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppX_Users extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 6;
    private static final String DATABASE_NAME = "AppX_Users.db";
    public static final String TABLE_USERDATA = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ADDR = "email";
    public static final String COLUMN_KEYS = "keys";
    public static final String COLUMN_NAME= "fullName";

    public AppX_Users(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase Database) {
        String query = "create table " +
                TABLE_USERDATA +
                " (" +
                COLUMN_ID + " integer primary key autoincrement not null," +
                COLUMN_ADDR  + " text,"  +
                COLUMN_KEYS  + " text,"  +
                COLUMN_NAME  + " text"   +
                ");";
        Database.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERDATA);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int newVersion, int oldVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERDATA);
        onCreate(db);
    }

    public void addUser(InputData userData){
        ContentValues values = new ContentValues();

        values.put(COLUMN_ADDR, userData.get_userName());
        values.put(COLUMN_KEYS, userData.get_userPass());
        values.put(COLUMN_NAME, userData.get_fullName());
        SQLiteDatabase Database = getWritableDatabase();
        Database.insert(TABLE_USERDATA, null, values);
        Database.close();

    }

    public boolean checkAddr (String userValue){
        SQLiteDatabase Database = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_USERDATA + " WHERE " + COLUMN_ADDR + " = '" + userValue + "'";
        Cursor c =  Database.rawQuery(query, null);
        boolean email_fieldExists = false;
        if (c.moveToFirst()){
            email_fieldExists = true;
        }
        c.close();
        Database.close();
        return email_fieldExists;
    }

    public boolean ValidateDataEntry (String userValue, String passValue){
        SQLiteDatabase Database = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_USERDATA + " WHERE " + COLUMN_ADDR + " = '" + userValue + "' AND " + COLUMN_KEYS + " = '" + passValue + "'";
        Cursor c =  Database.rawQuery(query, null);

        boolean checkIfValid = false;
        if (c.moveToFirst()){
            checkIfValid = true;
        }
        c.close();
        Database.close();
        return checkIfValid;
    }

    public String mostRecentEntry(){
        SQLiteDatabase Database = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_USERDATA + " ORDER BY " + COLUMN_ID + " DESC LIMIT 1";
        Cursor c =  Database.rawQuery(query, null);
        if (c.moveToFirst()) {
            return  c.getString(c.getColumnIndex("email"));
        }
        return null;
    }

    public String databaseToString(){
        String dbString = String.format("%-30s%10s\n","EMAIL","PASSWORD");
        SQLiteDatabase Database = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_USERDATA ;

        Cursor c = Database.rawQuery(query, null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("email")) != null){

                dbString += String.format("%-30s%s\n",c.getString(c.getColumnIndex("email")),c.getString(c.getColumnIndex("keys")));
                c.moveToNext();
            }
        }
        Database.close();
        return dbString;
    }

}
