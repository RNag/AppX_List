package appx_homescreen.appx;

import android.graphics.Color;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    TableLayout listLayout;
    TableRow tr;
    TextView c1,c2;
    EditText edit_listName, edit_listDate, edit_listAbout;
    Button editButton, delButton, listButton;
    LayoutParams tr_params;

    Appx_ListEntries dbHandler;
    String[] str = new String[2];

    List<String> new_listName = new ArrayList<String>();
    List<String> new_listDate = new ArrayList<String>();
    List<String> new_listAbout = new ArrayList<String>();
    ListData newList;
    String formatName, formatDate = null;
    SimpleDateFormat toFormat, fromFormat;

    boolean[] Switch_sortOrder = new boolean[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);



        new_listName.add("VCU Event");
        new_listDate.add("7/18/13");
        //new_listDate.add("2013-07-18");
        new_listAbout.add("This is just a simple description for the event to be added, but feel free to make this as long as you want it to be.");

        new_listName.add("Another event");
        new_listDate.add("12/20/14");
       // new_listDate.add("2014-12-20");
        new_listAbout.add("Add some more entries to fill the screen so that you can see that the table is indeed scrollable");


        new_listName.add("Random meeting");
       // new_listDate.add("2015-11-04");
        new_listDate.add("11/4/15");
        new_listAbout.add("Description3");

        new_listName.add("The last one listed");
        //new_listDate.add("2015-11-01");
        new_listDate.add("11/1/15");
        new_listAbout.add("Description4");

        fromFormat = new SimpleDateFormat("M/d/yy", java.util.Locale.US);
        toFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US);

        dbHandler = new Appx_ListEntries(this, null, null, 6);
        for (int i = 0; i < new_listName.size(); i++) {
            try {
                formatDate = toFormat.format(fromFormat.parse(new_listDate.get(i)));
            }
            catch (ParseException e){
                Toast.makeText(this, "The date '"+ new_listDate.get(i) + "' was unparseable, and the list entry could not be added.",Toast.LENGTH_LONG).show();
                continue;
            }
            newList = new ListData(new_listName.get(i), formatDate, new_listAbout.get(i));
            dbHandler.addList(newList);
        }


        listLayout = (TableLayout) findViewById(R.id.listLayout);

        /** Please pay close attention to how the values getting passed to the function arguments are defined here!
         *  By default we want to sort the list by the _id tag (which is the string name for the column in our list database
         *  that we want to reference). This identifier is automatically created, or incremented, each time a new entry
         *  is added to the running list of entries that is stored. The _id tag can therefore be thought of as a pre-defined
         *  identifier column which directly references the new row in the database that is added (the row that contains all other
         *  column entries which describe the individual entry more in detail). Therefore, suppose we want to sort the entries by
         *  the order in which they were created and which is supposedly the sorting-by order that the database will default to,
         *  then we should opt to select all the stored entries and sort them by increasing order and by the '_id' tag. This is
         *  in fact what the argument values specify and what will be returned in this example function call. The function
         *  declaration or description which includes its parameters information as well as what would be deemed acceptable
         *  (or valid) argument values for this function call are listed below. Please note that, if in doubt or if further
         *  information are needed, one can always find and reference the java .class file titled 'Appx_ListEntries' listed
         *  under the main directory or folder of this activity declaration for more details referencing the function
         *  declaration statements.
         *
         * public List<ListData> returnListEntries_byOrder(String COLUMN_NAME, Integer sortByOrder)
         *  Takes values as (Remember, CaSe-SENSITIVE):
         *
         *  BEGIN
         *      SELECT [COLUMN_NAME]:
         *          (String) _id        IS      Column 'Id' {Autoincrement Integer identifier}
         *          (String) listname   IS      List Title {Name}
         *          (String) date       IS      Event Date {Expected}
         *          (String) about      IS      Description {Optional}
         *          (String) author     IS      Contributor {Thread Starter}
         *
         *      SELECT [sortByOrder]:
         *          (Integer) 0         IS      ASCENDING {Order}
         *          (Integer) 1         IS      DESCENDING {Order}
         *  DONE
         * */

        PopulateTableElements("_id",0);

    }

    public void PopulateTableElements(String SortByColumnName, Integer Order){
        AddTableHeaders();

        List<ListData> sortedList = dbHandler.returnListEntries_byOrder(SortByColumnName,Order);

        toFormat = new SimpleDateFormat("M/d/yy", java.util.Locale.US);
        fromFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US);

        for (ListData fetchListItem : sortedList) {
            try {
                formatDate = toFormat.format(fromFormat.parse(fetchListItem.get_listDate()));
            }
            catch (ParseException e) {
                Toast.makeText(this, "The date '"+ fetchListItem.get_listDate() + "' was unparseable, and the list entry could not be fetched.",Toast.LENGTH_LONG).show();
                continue;
            }
            AddRowEntry(fetchListItem.get_listTitle(), formatDate, fetchListItem.get_listAbout());
        }
        createListEntryMethod();
    }

    public void AddTableHeaders(){
        LayoutParams c_params[] = new LayoutParams[2];

        tr_params = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        c_params[0] = new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.2f);
        c_params[1] = new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.8f);
        c_params[0].setMargins(0, 0, 0, 0);
        c_params[1].setMargins(0,0,280,0);

        tr = new TableRow(this);
        tr.setLayoutParams(tr_params);

        c1 = new TextView(this);
        c1.setBackgroundColor(0x8c1a46ed);
        c1.setText("Title");
        c1.setOnClickListener(onClick());

        c1.setPadding(100, 0, 0, 0);
        c1.setLayoutParams(c_params[0]);
        c1.setTextAppearance(android.R.style.TextAppearance_Large);

        c2 = new TextView(this);
        c2.setText("Date");
        c2.setOnClickListener(onClick());
        c2.setLayoutParams(c_params[1]);
        c2.setTextAppearance(android.R.style.TextAppearance_Large);
        c2.setGravity(Gravity.CENTER);
        tr.addView(c1);
        tr.addView(c2);
        listLayout.addView(tr);

    }

    public void AddRowEntry(String list_item1, String list_item2, String list_item3) {
        LayoutParams c_params[] = new LayoutParams[3];

        tr = new TableRow(this);
        c1 = new TextView(this);
        c2 = new TextView(this);
        editButton = new Button(this);
        delButton = new Button(this);
        c1.setText(list_item1);
        c2.setText(list_item2);

        //editButton.setText("Edit"); editButton.setOnClickListener(onClick(editButton));

        delButton.setText("Delete");
        delButton.setOnClickListener(onClick());

        c_params[0] = new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.6f);
        c1.setPadding(30, 0, 10, 0);
        c_params[1] = new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.4f);
        c_params[2] = new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.4f);
        c1.setLayoutParams(c_params[0]);
        c2.setLayoutParams(c_params[1]);
        c2.setGravity(Gravity.CENTER);
        delButton.setLayoutParams(c_params[2]);
        //c1.setBackgroundColor(0x8c4f75ed);
        c1.setBackgroundColor(0xe5dee7e8);

        delButton.setWidth(4);
        tr.addView(c1);
        tr.addView(c2);
        //tr.addView(editButton);
        tr.addView(delButton);
        listLayout.addView(tr);

        tr = new TableRow(this);
        tr_params = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        tr_params.setMargins(60, 10, 20, 0);
        c1 = new TextView(this);
        c1.setText(String.format("%s\n\n", list_item3));
        tr.addView(c1, tr_params);
        listLayout.addView(tr);
    }


    public void createListEntryMethod(){
        LayoutParams c_params[] = new LayoutParams[2];

        tr = new TableRow(this);
        c_params[0] = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        c_params[0].setMargins(20, 0, 20, 20);
        c1 = new TextView(this);
        c1.setText(String.format("%s\n\n", "If you would like to list an event, please add the following information below (also note that the description is an optional field)."));
        tr.addView(c1, c_params[0]);
        listLayout.addView(tr);

        tr = new TableRow(this);
        tr_params = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        tr_params.setMargins(60, 20, 20, 20);

        edit_listName = new EditText(this);
        edit_listName.setHint("Event Name");
        edit_listName.setSingleLine(true);
        edit_listName.setWidth(1);
        edit_listName.setEms(4);

        c_params[0] = new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.4f);
        edit_listName.setLayoutParams(c_params[0]);

        edit_listDate= new EditText(this);
        edit_listDate.setHint("Date of the Event");
        edit_listDate.setLayoutParams(c_params[0]);
        edit_listDate.setSingleLine(true);
        edit_listDate.setHighlightColor(Color.RED);

        edit_listAbout = new EditText(this);
        edit_listAbout.setHint("Add a description for this event (optional)");
        edit_listAbout.setMaxLines(3);

        listButton = new Button(this);
        listButton.setText("Create"); listButton.setOnClickListener(onClick());
        tr.addView(edit_listName);
        tr.addView(edit_listDate);
        listLayout.addView(tr);

        tr = new TableRow(this);
        tr.addView(edit_listAbout);
        listLayout.addView(tr);
        tr = new TableRow(this);
        tr.addView(listButton);
        listLayout.addView(tr);


    }



    public View.OnClickListener onClick () {
        return new View.OnClickListener() {
            public void onClick(View view) {
                ViewGroup ListRow = (ViewGroup) view.getParent();

                if (view instanceof TextView) {
                    View FirstRow = listLayout.getChildAt(0);
                    if (FirstRow.equals(ListRow)) {
                        Integer c = ListRow.indexOfChild(view);
                        listLayout.removeAllViews();
                        switch(c) {
                            case 0:
                                PopulateTableElements("listname", Switch_sortOrder[c]? 1: 0);
                                Switch_sortOrder[c+1] ^= Switch_sortOrder[c+1];
                                break;
                            case 1:
                                PopulateTableElements("date", Switch_sortOrder[c]? 1: 0);
                                Switch_sortOrder[c-1] &= !Switch_sortOrder[c-1];
                                break;
                        }
                        Switch_sortOrder[c] ^= Boolean.TRUE;
                    }

                }
                if (view instanceof Button) {
                    //Toast.makeText(getApplicationContext(), "TRUE BUTTON", Toast.LENGTH_LONG).show();


                String tmp = ((Button) view).getText().toString().trim();
                switch (tmp) {
                    case "Edit":
                        TextView test = (TextView) ((ViewGroup) view.getParent()).getChildAt(0);
                        Toast.makeText(getApplicationContext(), test.getText().toString(), Toast.LENGTH_LONG).show();
                        break;
                    case "Delete":
                        ViewGroup ListDesc = (ViewGroup) (listLayout.getChildAt(listLayout.indexOfChild(ListRow) + 1));
                        listLayout.removeView(ListRow);
                        listLayout.removeView(ListDesc);
                        break;
                    case "Create":

                        fromFormat = new SimpleDateFormat("M/d/yy", java.util.Locale.US);
                        toFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US);

                            try {

                                formatName = edit_listName.getText().toString().trim();
                                if (formatName.length() < 2) {
                                    Toast.makeText(getApplicationContext(), "Please enter a valid name for the event (2 char. minimum)", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                formatName = formatName.substring(0,1).toUpperCase() + formatName.substring(1);
                                formatDate = toFormat.format(fromFormat.parse(edit_listDate.getText().toString()));
                                newList = new ListData(formatName, formatDate, edit_listAbout.getText().toString().trim());
                                dbHandler.addList(newList);
                                listLayout.removeAllViews();
                                PopulateTableElements("_id", 0);
                                Toast.makeText(getApplicationContext(),"The event '"+ formatName +"' was successfully added!",Toast.LENGTH_LONG).show();
                            }
                            catch (ParseException e){
                                Toast.makeText(getApplicationContext(), "The date '"+ edit_listDate.getText().toString() + "' was unparseable.\nPlease enter in the format: " + fromFormat.toLocalizedPattern(),Toast.LENGTH_LONG).show();
                            }

                        break;
                }
            }

            }
        };

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
