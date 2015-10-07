package appx_homescreen.appx;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class Appx_ListEntries extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 8;
    private static final String DATABASE_NAME = "AppX_Lists.db";
    public static final String TABLE_LISTDATA = "lists";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LIST = "listname";
    public static final String COLUMN_DATE = "date";
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
                COLUMN_LIST + " text," +
                COLUMN_DATE + " date," +
                COLUMN_DESC + " text," +
                COLUMN_CONTRIBUTOR + " text" +
                ");";
        Database.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTDATA);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int newVersion, int oldVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTDATA);
        onCreate(db);
    }

    public void addList(ListData newList) {
        if (listItem_alreadyExists(newList.get_listTitle()) == false) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_LIST, newList.get_listTitle());
            values.put(COLUMN_DATE, newList.get_listDate());
            values.put(COLUMN_DESC, newList.get_listAbout());
            values.put(COLUMN_CONTRIBUTOR, newList.get_listAuthor());
            SQLiteDatabase Database = getWritableDatabase();
            Database.insert(TABLE_LISTDATA, null, values);
            Database.close();
        }
    }

    public void deleteList(String listName){
        SQLiteDatabase Database = getWritableDatabase();
        String query = "DELETE FROM " + TABLE_LISTDATA + " WHERE " + COLUMN_LIST + " = '" + listName + "'";
        Database.execSQL(query);
        Database.close();
    }

    public boolean isThreadCreator(String userValue) {
        SQLiteDatabase Database = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_LISTDATA + " WHERE " + COLUMN_CONTRIBUTOR + " = '" + userValue + "'";
        Cursor c = Database.rawQuery(query, null);
        boolean list_isAuthored = false;
        if (c.moveToFirst()) {
            list_isAuthored = true;
        }
        c.close();
        Database.close();
        return list_isAuthored;
    }

    public boolean listItem_alreadyExists(String listname){
        boolean checkfor_DuplicateEntries = false;
        SQLiteDatabase Database = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_LISTDATA + " WHERE " + COLUMN_LIST + " = '" + listname + "'";
        Cursor c = Database.rawQuery(query, null);
        if (c.moveToFirst()){
            checkfor_DuplicateEntries = true;
        }
        c.close();
        Database.close();
        return checkfor_DuplicateEntries;
    }


    public List<ListData> returnListEntries_byOrder(String COLUMN_NAME, int sortByOrder) {
        List<ListData> return_List = new ArrayList<ListData>();
        String query;
        query = "SELECT * FROM " + TABLE_LISTDATA   + " ORDER BY " + COLUMN_NAME;

        SQLiteDatabase Database = getWritableDatabase();

        if (!(sortByOrder == 0))
                query = "SELECT * FROM " + TABLE_LISTDATA  + " ORDER BY " + COLUMN_NAME + " DESC";

        Cursor c = Database.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("listname")) != null) {
                return_List.add(new ListData(c.getString(c.getColumnIndex("listname")), c.getString(c.getColumnIndex("date")), c.getString(c.getColumnIndex("about")), c.getString(c.getColumnIndex("author"))));
                c.moveToNext();
            }
        }

        c.close();
        Database.close();
        return return_List;
    }
}
