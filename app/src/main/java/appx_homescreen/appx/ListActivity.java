package appx_homescreen.appx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    Appx_ListEntries dbHandler; //Public reference to database file

    TableLayout listLayout;     //Table Container
    TableRow tr;                //Table Row
    TextView c1,c2;             //Table Columns
    Button editButton, delButton, listButton;               //Buttons ('Edit' is currently unused)
    EditText edit_listName, edit_listDate, edit_listAbout;  //User-input fields
    LayoutParams tr_params, c_params[]= new LayoutParams[3];     //Table elements layout parameters

    /** List declarations to hold predefined list entries since none exist upon creating or upgrading the database version */
    List<String> new_listName = new ArrayList<>();
    List<String> new_listDate = new ArrayList<>();
    List<String> new_listAbout = new ArrayList<>();

    ListData newList;                                   //Public reference to the list class used to store entries in the database
    SimpleDateFormat fromFormat, toFormat;              //Format strings to convert to/from the 'accepted' SQLite Date-string format
    String formatName, formatDate, formatAbout = null;  //The string values for data in the list entries to format as needed

    boolean[] Switch_sortOrder = new boolean[2];    //Boolean array elements to switch between the sort order for column values
    int UniqueID;                                   //Unique identifiers (ID) for Views that are dynamically created upon startup

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        /** These are where the predefined list entries are defined. They are added dynamically using the 'list' object declaration,
         *  therefore we don't need to worry about declaring or initializing an array size since we will constantly be adding or
         *  removing entries to the list. These values for string literals in each list (essentially they each hold specific types
         *  of data for each list entry to be added) are each stored in a separate list for no other reason than it is easier to read
         *  and modify the values. Since the string values supplied for the description (about) field can be many sentences and word
         *  characters long, it makes sense to declare a separate list for each list entry field to enhance readability rather than
         *  store them in a single aggregated list of type <String[]>, i.e. a String array type. For testing or development purposes,
         *  one can always modify or rearrange these entries to see how they will be displayed on the screen. You can also safely
         *  delete or remove any of these list entries if you find that they contribute to the space on the screen  or UI appearing
         *  too cluttered. However, if such is your intention just remember to delete all the fields for each list entry (i.e. remove
         *  the individual elements appended to each of the separate lists in each case). A complete declaration of each individual
         *  entry added to the running list should clearly follow this format:
         *
         *      new_listName.add({String} Event Name);
         *      new_listDate.add({String} Event Date);
         *      new_listAbout.add({String} Optional description);
         **/

        new_listName.add("VCU Event");
        new_listDate.add("7/18/13");
        //new_listDate.add("2013-07-18"); //This format was used in previous versions, but you can uncomment this to see what will be returned
        new_listAbout.add("This is just a simple description for the event to be added, but feel free to make this as long as you want it to be.");

        new_listName.add("Another event");
        new_listDate.add("12/20/14");
        new_listAbout.add("Add some more entries to fill the screen so that you can see that the table is indeed scrollable");

        new_listName.add("Random meeting");
        new_listDate.add("11/4/15");
        new_listAbout.add("Description3");

        new_listName.add("Cycling Club @VCU");
        new_listDate.add("11/1/15");
        new_listAbout.add("Another simple example of an event meeting listed. Please also note that: \n\n" +
                "This is an actual club that exists, this was not made up for the sake of creating or generating a sample event " +
                "that can be used to introduce an example. ");

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
         * public List<ListData> returnListEntries_byOrder(String COLUMN_NAME, int sortByOrder)
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
         *          (int) 0         IS      ASCENDING {Order}
         *          (int) 1         IS      DESCENDING {Order}
         *  DONE
         * */

        PopulateTableElements("_id",0);

    }

    public void PopulateTableElements(String SortByColumnName, int Order){
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
        tr_params = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        c_params[0] = new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.2f);
        c_params[1] = new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.8f);
        c_params[0].setMargins(0,0,0,0);
        c_params[1].setMargins(0,0,280,0);

        tr = new TableRow(this);
        tr.setLayoutParams(tr_params);

        c1 = new TextView(this);
        c1.setBackgroundColor(0x8c1a46ed);
        c1.setText("Title");
        c1.setPadding(100, 0, 0, 0);
        c1.setLayoutParams(c_params[0]);
        c1.setTextAppearance(android.R.style.TextAppearance_Large);
        c1.setOnClickListener(onClick());

        c2 = new TextView(this);
        c2.setText("Date");
        c2.setLayoutParams(c_params[1]);
        c2.setTextAppearance(android.R.style.TextAppearance_Large);
        c2.setGravity(Gravity.CENTER);
        c2.setOnClickListener(onClick());

        tr.addView(c1);
        tr.addView(c2);
        listLayout.addView(tr);
    }

    public void AddRowEntry(String list_item1, String list_item2, String list_item3) {
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
        c_params[1] = new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.4f);
        c_params[2] = new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.4f);

        c1.setPadding(30, 0, 10, 0);
        c1.setLayoutParams(c_params[0]);
        //c1.setBackgroundColor(0x8c4f75ed);
        c1.setBackgroundColor(0xe5dee7e8);

        c2.setLayoutParams(c_params[1]);
        c2.setGravity(Gravity.CENTER);

        delButton.setLayoutParams(c_params[2]);
        delButton.setWidth(2);

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
        c_params[0] = new TableRow.LayoutParams(500, 5);
        c_params[0].setMargins(0,0,0,40);

        tr = new TableRow(this);
        View c1_view = new View(this);
        c1_view.setBackgroundColor(0xe5386569);
        c1_view.setLayoutParams(c_params[0]);
        tr.addView(c1_view);
        listLayout.addView(tr);


        c_params[0] = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        c_params[0].setMargins(0, 0, 0, 40);

        tr = new TableRow(this);
        c1 = new TextView(this);
        c1.setText("List an Event");
        c1.setTextAppearance(android.R.style.TextAppearance_Large);
        tr.addView(c1, c_params[0]);
        listLayout.addView(tr);


        c_params[0] = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        c_params[0].setMargins(40, 0, 20, 120);

        tr = new TableRow(this);
        c1 = new TextView(this);
        c1.setText(String.format("%s", "If you would like to add an event, please use the form below and supply the following information  (also note that the description is an optional field)."));
        tr.addView(c1, c_params[0]);
        listLayout.addView(tr);


        tr_params = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        tr_params.setMargins(60, 20, 20, 20);

        tr = new TableRow(this);
        edit_listName = new EditText(this);
        edit_listName.setHint("Event Name");
        int temp_id = getUniqueID();
        edit_listName.setNextFocusDownId(temp_id);
        edit_listName.setSingleLine(true);
        c_params[0] = new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.4f);
        edit_listName.setLayoutParams(c_params[0]);

        edit_listDate= new EditText(this);
        edit_listDate.setHint("Date of the Event");
        edit_listDate.setInputType(InputType.TYPE_CLASS_DATETIME);
        edit_listDate.setLayoutParams(c_params[0]);
        edit_listDate.setId(temp_id);
        edit_listDate.setSingleLine(true);

        edit_listAbout = new EditText(this);
        edit_listAbout.setHint("Add a description for this event (optional)");
        edit_listAbout.setMaxLines(3);

        listButton = new Button(this);
        listButton.setText("Create");
        listButton.setOnClickListener(onClick());

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

    public synchronized int getUniqueID(){
    /**
     *   Returns a unique ID (autoincrement value of int type) which in this case initially increments from a default value of '1'.
     *   The purpose for declaring and returning an ID value is for cases when such dynamic, i.e. programmatically created elements
     *   will need to be referenced at some unknown point in the code. Using this method, it is entirely possible for an earlier
     *   partition in the code to reference an undeclared element (at that current point in time that such an individual line is
     *   read in) by using its ID tag value, before reaching that line in the code where that element is even instantiated or
     *   even before it is assigned the identifier value that has been referenced a priori to the declaration statement. This will
     *   be useful particularly if multiple IDs are being assigned to various objects or instances and you want to make sure that
     *   each one of them will have a unique identifier value that can be referenced in any point or section of the code without
     *   worrying overmuch about any particular case of overlap that can result from duplicate IDs being assigned to various
     *   elements.
     *   */

        UniqueID ++;
        return UniqueID;
    }


    public View.OnClickListener onClick () {
        return new View.OnClickListener() {


            public void onClick(View view) {
                ViewGroup ListRow = (ViewGroup) view.getParent();

                if (view instanceof TextView) {
                    View FirstRow = listLayout.getChildAt(0);
                    if (FirstRow.equals(ListRow)) {
                        int c = ListRow.indexOfChild(view);
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
                    String tmp = ((Button) view).getText().toString().trim();
                    switch (tmp) {
                        case "Edit":
                            TextView test = (TextView) ((ViewGroup) view.getParent()).getChildAt(0);
                            Toast.makeText(getApplicationContext(), test.getText().toString(), Toast.LENGTH_LONG).show();
                        break;

                        case "Delete":
                            ViewGroup ListDesc = (ViewGroup) (listLayout.getChildAt(listLayout.indexOfChild(ListRow) + 1));
                            String listName = ((TextView)ListRow.getChildAt(0)).getText().toString();
                            dbHandler.deleteList(listName);
                            listLayout.removeView(ListRow);
                            listLayout.removeView(ListDesc);
                            Toast.makeText(getApplicationContext(),"The listing '"+ listName +"' has been removed.", Toast.LENGTH_LONG).show();
                        break;

                        case "Create":
                            fromFormat = new SimpleDateFormat("M/d/yy", java.util.Locale.US);
                            toFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US);
                            try {
                                formatName = edit_listName.getText().toString().trim();
                                formatName = formatName.replaceAll("\\s+", " ");
                                if (formatName.replaceAll("( )","").length() < 2) {
                                    Toast.makeText(getApplicationContext(), "Please enter a valid name for the event (2 char. minimum)", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                else if (dbHandler.listItem_alreadyExists(formatName)){
                                    Toast.makeText(getApplicationContext(), "There is already a duplicate entry for this event, your listing could not be added.", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                formatName = formatName.substring(0,1).toUpperCase() + formatName.substring(1);
                                formatDate = toFormat.format(fromFormat.parse(edit_listDate.getText().toString()));
                                formatAbout = edit_listAbout.getText().toString().trim();
                                formatAbout = formatAbout.replaceAll("( )+"," ");
                                formatAbout = formatAbout.replaceAll("(\\r?\\n){2,}","\n\n");
                                newList = new ListData(formatName, formatDate, formatAbout);
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
