package appx_homescreen.appx;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Appx_ListEntries extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 7;
    private static final String DATABASE_NAME = "AppX_Lists.db";
    public static final String TABLE_LISTDATA = "lists";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LIST= "listname";
    public static final String COLUMN_DATE = "when";
    public static final String COLUMN_DESC = "about";
    public static final String COLUMN_CONTRIBUTOR = "author";

    public Appx_ListEntries(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase Database) {
        String query = "create table " +
                TABLE_LISTDATA +
                " (" +
                COLUMN_ID + " integer primary key autoincrement not null," +
                COLUMN_LIST  + " text,"  +
                COLUMN_DATE  + " text,"  +
                COLUMN_DESC  + " text"   +
                COLUMN_CONTRIBUTOR  + " text"   +
                ");";
        Database.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTDATA);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int newVersion, int oldVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTDATA);
        onCreate(db);
    }

    public void addList(ListData newList){
        ContentValues values = new ContentValues();

        values.put(COLUMN_LIST, newList.get_listTitle());
        values.put(COLUMN_DATE, newList.get_listDate());
        values.put(COLUMN_DESC, newList.get_listAbout());
        values.put(COLUMN_CONTRIBUTOR, newList.get_listAuthor());
        SQLiteDatabase Database = getWritableDatabase();
        Database.insert(TABLE_LISTDATA, null, values);
        Database.close();

    }

    public boolean isThreadCreator(String userValue){
        SQLiteDatabase Database = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_LISTDATA + " WHERE " + COLUMN_CONTRIBUTOR + " = '" + userValue + "'";
        Cursor c =  Database.rawQuery(query, null);
        boolean list_isAuthored = false;
        if (c.moveToFirst()){
            list_isAuthored = true;
        }
        c.close();
        Database.close();
        return list_isAuthored;
    }

    public String sortListbyOrder(){
        SQLiteDatabase Database = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_LISTDATA + " ORDER BY " + COLUMN_LIST + " DESC";
        Cursor c =  Database.rawQuery(query, null);
        c.moveToFirst();

        return null;
    }

    public String databaseToString(){
        String dbString = String.format("%-30s%10s\n","EMAIL","PASSWORD");
        SQLiteDatabase Database = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_LISTDATA ;

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
