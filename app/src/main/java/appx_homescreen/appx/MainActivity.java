package appx_homescreen.appx;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText inputAddr;
    EditText inputPass;
    AppX_Users dbHandler;
    /** These are the optional text field to see the entries in database (uncomment it out to show them) */
    TextView testVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputAddr = (EditText) findViewById(R.id.editUsername);
        inputPass = (EditText) findViewById(R.id.editPassword);
        testVal = (TextView) findViewById(R.id.testVal);
        dbHandler = new AppX_Users(this, null, null, 4);
        printDatabase();

        if (dbHandler.mostRecentEntry()!= null) {
            inputAddr.setText(dbHandler.mostRecentEntry());
            inputPass.requestFocus();
        }
    }

    public void printDatabase(){
        String dString = dbHandler.databaseToString();
        //testVal.setText(dString);
        inputAddr.setText("");
        inputPass.setText("");
    }


    public void onClick (View view) {
        switch (view.getId()){
            case R.id.createButton:
                if (TextUtils.isEmpty(inputAddr.getText().toString()) || !android.util.Patterns.EMAIL_ADDRESS.matcher(inputAddr.getText().toString()).matches()){
                    Toast.makeText(this, "Please enter a valid email.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (inputPass.getText().length() <3 ) {
                    Toast.makeText(this, "Please enter a valid password.\n (3 char. minimum)",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dbHandler.checkAddr(inputAddr.getText().toString())){
                    Toast.makeText(this, "This email already exists in the database!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                InputData user = new InputData(inputAddr.getText().toString(),inputPass.getText().toString());
                dbHandler.addUser(user);
                Toast.makeText(this, "Your information has been saved.", Toast.LENGTH_SHORT).show();
                printDatabase();
                break;

            case R.id.valButton:
                if (dbHandler.ValidateDataEntry(inputAddr.getText().toString(),inputPass.getText().toString())) {
                    Toast.makeText(this, "A successful match was found for the email/password combination.",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(this, "Email/password does not exist, or no such match was not found.",
                            Toast.LENGTH_LONG).show();
                }
                break;


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
