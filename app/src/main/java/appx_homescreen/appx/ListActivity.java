package appx_homescreen.appx;

import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ListActivity extends AppCompatActivity {
    TableLayout listLayout;
    TableRow tr;
    TextView c1,c2;
    EditText listTitle_new, listDate_new, listDesc_new;
    Button editButton, delButton, listButton;
    LayoutParams params, params2;
    ListData newList;
    Appx_ListEntries dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //dbHandler = new Appx_ListEntries(this, null, null, 4);
        //newList = new ListData("Header1, we might add some additional information here", "Date1 for this event (expected date)", "This is just a simple description for this event but it can be as long as you want it to be.");
        //dbHandler.addList(newList);
        //dbHandler.addList(new ListData("Header2", "Date2", "You can also add database entries this way"));


        listLayout = (TableLayout) findViewById(R.id.listLayout);
        AddRowEntry("Header1, we might add some additional information here", "Date1 for this event (expected date)", "This is just a simple description for this event but it can be as long as you want it to be.");
        AddRowEntry("Header2", "Date2", "Add some more entries to fill the screen so that you can see that the table is indeed scrollable");
        AddRowEntry("Header3", "Date3", "Description3");
        AddRowEntry("Header4", "Date4", "Description4");


        createListEntryMethod();
      // listLayout.removeView((ViewGroup)c1.getParent());

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
        delButton.setOnClickListener(onClick(delButton));

        LayoutParams pen = new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.6f);
        pen.setMargins(0, 0, 10, 0);
        LayoutParams pen2 = new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.4f);
        LayoutParams pen3 = new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.4f);
        c1.setLayoutParams(pen);
        c2.setLayoutParams(pen2);
        delButton.setLayoutParams(pen3);
        c1.setBackgroundColor(0x8c4f75ed);
        params2 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);


        //editButton.setWidth(2);
        delButton.setWidth(4);
        tr.addView(c1);
        tr.addView(c2);
        //tr.addView(editButton);
        tr.addView(delButton);

        listLayout.addView(tr);

        tr = new TableRow(this);


        params = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(60, 20, 20, 20);

        c1 = new TextView(this);
        c1.setText(String.format("%s\n\n", list_item3));
        tr.addView(c1, params);
        listLayout.addView(tr);
    }


    public void createListEntryMethod(){
        tr = new TableRow(this);
        params = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(60, 20, 20, 20);

        listTitle_new = new EditText(this);
        listTitle_new.setSingleLine(true);
        listTitle_new.setWidth(1);
        listTitle_new.setEms(4);

        LayoutParams pen = new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.4f);
        listTitle_new.setLayoutParams(pen);

        listDate_new= new EditText(this);
        listDate_new.setLayoutParams(pen);
        listDate_new.setSingleLine(true);

        listDesc_new = new EditText(this);

        listButton = new Button(this);
        listButton.setText("Create"); listButton.setOnClickListener(onClick(listButton));
        tr.addView(listTitle_new);
        tr.addView(listDate_new);
        listLayout.addView(tr);

        tr = new TableRow(this);
        tr.addView(listDesc_new);
        listLayout.addView(tr);
        tr = new TableRow(this);
        tr.addView(listButton);
        listLayout.addView(tr);


    }



    public View.OnClickListener onClick (final Button button) {
        return new View.OnClickListener() {
            public void onClick(View view) {

                String tmp = button.getText().toString().trim();
                switch(tmp) {
                    case "Edit":
                        TextView test = (TextView) ((ViewGroup) view.getParent()).getChildAt(0);
                        Toast.makeText(getApplicationContext(), test.getText().toString(), Toast.LENGTH_LONG).show();
                    break;
                    case "Delete":
                        ViewGroup ListRow = (ViewGroup) view.getParent();
                        ViewGroup ListDesc = (ViewGroup) (listLayout.getChildAt(listLayout.indexOfChild(ListRow)+1));
                        listLayout.removeView(ListRow);
                        listLayout.removeView(ListDesc);
                    break;
                    case "Create":
                        listLayout.removeAllViews();
                        AddRowEntry("Changed Header1, we might add some additional information here", "Date1 for this event (expected date)", "This is just a simple description for this event but it can be as long as you want it to be.");
                        AddRowEntry("Changed Header2", "Date2", "Add some more entries to fill the screen so that you can see that the table is indeed scrollable");
                        AddRowEntry("Changed Header3", "Date3", "Description3");
                        AddRowEntry("Changed Header4", "Date4", "Description4");
                    break;
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
