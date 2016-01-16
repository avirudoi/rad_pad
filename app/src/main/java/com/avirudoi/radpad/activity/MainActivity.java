package com.avirudoi.radpad.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avirudoi.radpad.R;
import com.avirudoi.radpad.listener.RecycleViewClickListener;
import com.avirudoi.radpad.adapter.RentAdapter;
import com.avirudoi.radpad.module.RentObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements RecycleViewClickListener {

    public RecyclerView recyclerView;
    RentObject rentObject;
    Dialog dialogEditRent;
    EditText etStreet, etCity;
    Button btMoveIn, btMoveOut;
    TextView tvEmpty;

    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    SimpleDateFormat sdf;
    long moveIn, moveOut;

    RentAdapter rentAdapter;
    ArrayList<RentObject> listOfRent;

    //create a hashmap for a better was save data use key for integer and Value as an object
    HashMap<Integer,RentObject> mRentsObjects;


    int Viewposition = -1;

    //item click is 1, add new object is 0
    int viewClick = 0;

    boolean validation = true;

    boolean IsMoveInClick = false;
    boolean IsMoveOutClick = false;

    //a view variable to know witch button click last
    View lastButtonClick;

    int  yearIn,monthIn,dayIn, yearOut,monthOut,dayOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set up the action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initialize the object
        rentObject = new RentObject();

        listOfRent = new ArrayList<RentObject>();
        mRentsObjects = new HashMap<Integer,RentObject>();

        tvEmpty = (TextView) findViewById(R.id.tvEmpty);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        //check if any data available
        checkIfHaveAnyData();

        //set up the recyclerView
        recyclerView.setHasFixedSize(true);
        rentAdapter = new RentAdapter(listOfRent,this);
        recyclerView.setAdapter(rentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //on click on the Floating Action Button to create new rent object
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IsMoveInClick =false;
                IsMoveOutClick=false;
                viewClick = 0;
                dialogAddEditRent(0);
            }
        });
    }

    //check if any rent data available if not show the text for no data
    public void checkIfHaveAnyData(){
        if (listOfRent != null && listOfRent.size() > 0) {
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        }
    }

    //create the custome dialog
    public void dialogAddEditRent(int position) {
        dialogEditRent = new Dialog(this);
        dialogEditRent.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogEditRent.setContentView(R.layout.dialog_update_rent);

        myCalendar = Calendar.getInstance();

        //create a format of a date to show on the button
        String myFormat = "MMM d"; //In which you need put here
        sdf = new SimpleDateFormat(myFormat, Locale.US);

        // set the custom dialog components
        etStreet = (EditText) dialogEditRent
                .findViewById(R.id.etStreet);
        etCity = (EditText) dialogEditRent
                .findViewById(R.id.etCity);
        btMoveIn = (Button) dialogEditRent
                .findViewById(R.id.btMoveIn);
        btMoveOut = (Button) dialogEditRent
                .findViewById(R.id.btMoveOut);
        TextView tvSave = (TextView) dialogEditRent
                .findViewById(R.id.tvSave);
        TextView tvCancel = (TextView) dialogEditRent
                .findViewById(R.id.tvCancel);

        if(viewClick == 1){
            for (int i = 0;mRentsObjects.size()>0;i++){
                /*compare the postion on the view with the postion from the map*/
                if(position == i){
                    rentObject = mRentsObjects.get(i);

                        //get the street address
                        if(mRentsObjects.get(position).getStreet() !=null){
                            etStreet.setText(rentObject.getStreet());
                        }

                        //get the city address
                        if(mRentsObjects.get(position).getCity() !=null){
                            etCity.setText(rentObject.getCity());
                        }

                        //get the move in dates
                        if(mRentsObjects.get(position).getStartDate()>0){
                            btMoveIn.setText(rentObject.getFormatedStartDate());
                        }

                        //get the move out dates
                        if(mRentsObjects.get(position).getEndDate()>0){
                            btMoveOut.setText(rentObject.getformattedEndDate());
                        }

                    break;
                }
            }
        }

        dialogEditRent.show();

        //set up the dates on the dialog Picker
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                //check what button click last based on that set the right dates
                if(lastButtonClick == btMoveIn){
                    dayIn = dayOfMonth;
                    monthIn = monthOfYear;
                    yearIn = year;
                    updateDayIn();
                }else if(lastButtonClick == btMoveOut){
                    dayOut = dayOfMonth;
                    monthOut = monthOfYear;
                    yearOut = year;
                    updateDayOut();
                }
            }
        };

        btMoveIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View moveIn) {
                IsMoveInClick = true;
                if(dayIn ==0){
                    dayIn = myCalendar.get(Calendar.DAY_OF_MONTH) +1;
                    monthIn = myCalendar.get(Calendar.MONTH);
                    yearIn = myCalendar.get(Calendar.YEAR);
                }
                new DatePickerDialog(MainActivity.this, date,yearIn, monthIn,
                        dayIn).show();
                lastButtonClick = btMoveIn;
            }
        });

        btMoveOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View moveOut) {
                IsMoveOutClick = true;
                if(dayOut ==0){
                    dayOut = myCalendar.get(Calendar.DAY_OF_MONTH)+2;
                    monthOut = myCalendar.get(Calendar.MONTH);
                    yearOut = myCalendar.get(Calendar.YEAR);
                }
                new DatePickerDialog(MainActivity.this, date,yearOut,monthOut,
                        dayOut).show();
                lastButtonClick = btMoveOut;
            }
        });

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (viewClick == 0) {
                    rentObject = new RentObject();
                }
                //if the rent object not null set the data
                if (rentObject != null) {

                    //if street is not empty set up street address
                    if (!etStreet.getText().toString().isEmpty()) {
                        rentObject.setStreet(etStreet.getText().toString());
                    }

                    //if city not empty set up city address
                    if (!etCity.getText().toString().isEmpty()) {
                        rentObject.setCity(etCity.getText().toString());
                    }

                    //if move in day not empty set up move in date
                    if (moveIn > 0) {
                        rentObject.setStartDate(moveIn);
                    }

                    //if move out date not empty set up move out date
                    if (moveOut > 0) {
                        rentObject.setEndDate(moveOut);
                    }

                    //method that responsible for making validation
                    UpdateSubmitForm();

                   /* if all the filed pass validation go inside */
                    if (validation) {

                    if (listOfRent != null) {
                        if (viewClick == 0) {
                            mRentsObjects.put(++Viewposition, rentObject);
                            listOfRent.add(mRentsObjects.get(Viewposition));
                        }
                    }
                        checkIfHaveAnyData();
                        //remove the dialog
                        dialogEditRent.dismiss();
                        lastButtonClick = null;


                       /* sync the data with the server,
                        every time you calling async task you need to create a new instance*/
                        DataCall task = new DataCall();
                        task.execute("");

                        rentAdapter.notifyDataSetChanged();
                  }
                  /* end validation field */
                }
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastButtonClick = null;
                dialogEditRent.dismiss();
            }
        });
    }

    //update day in method on the button
    public void updateDayIn(){
        moveIn = myCalendar.getTimeInMillis();
        btMoveIn.setText(sdf.format(myCalendar.getTime()));
    }

    //update day out method on the button
    public void updateDayOut(){
        moveOut = myCalendar.getTimeInMillis();
        btMoveOut.setText(sdf.format(myCalendar.getTime()));
    }

    /**
     * Validating form
     */
    private void UpdateSubmitForm() {

        validation =true;
        View focusView = null;

        //validate street not empty
        if (etStreet.getText().toString().trim().isEmpty()) {
            etStreet.setError(getString(R.string.err_msg_street));
            focusView = etStreet;
            validation = false;
            if (focusView != null) {
                focusView.requestFocus();
            }
            return;
        }

        //validate street
        if (!validateCityStreet(etStreet.getText().toString())){
            etStreet.setError(getString(R.string.invalid_street_address));
            focusView = etStreet;
            validation = false;
            if(focusView != null) {
                focusView.requestFocus();
            }
            return;
        }

        //validate city not empty
        if (etCity.getText().toString().trim().isEmpty()) {
            etCity.setError(getString(R.string.err_msg_city));
            focusView = etCity;
            if(focusView != null) {
                focusView.requestFocus();
            }
            validation = false;
            return;
        }

        //validate city
        if (!validateCityStreet(etCity.getText().toString())){
            etCity.setError(getString(R.string.invalid_city));
            focusView = etCity;
            if(focusView != null) {
                focusView.requestFocus();
            }
            validation = false;
            return;
        }
        //validate dates move in and move out are entered
        if (IsMoveInClick == false && IsMoveOutClick ==false){
            Toast.makeText(this,getResources().getString(R.string.please_select_dates),Toast.LENGTH_SHORT).show();
            validation = false;
            return;
        }

        //validate dates move in is enter
        if(IsMoveInClick == false){
            Toast.makeText(this,getResources().getString(R.string.please_select_day_in),Toast.LENGTH_SHORT).show();
            validation = false;
            return;
        }

        //validate dates move out is enter
        if(IsMoveOutClick == false){
            Toast.makeText(this,getResources().getString(R.string.please_select_day_out),Toast.LENGTH_SHORT).show();
            validation = false;
            return;
        }

        //validate move in date is after move out date
        if (moveIn>moveOut) {
            Toast.makeText(this,getResources().getString(R.string.day_in_after_day_out),Toast.LENGTH_SHORT).show();
            validation = false;
            return;
        }
        if (moveIn<(System.currentTimeMillis())){
            Toast.makeText(this,getResources().getString(R.string.no_move_in_in_a_past_day),Toast.LENGTH_SHORT).show();
            validation = false;
            return;
        }
        if (moveOut<(System.currentTimeMillis())){
            Toast.makeText(this, getResources().getString(R.string.no_move_out_in_a_past_day),Toast.LENGTH_SHORT).show();
            validation = false;
            return;
        }

        //validation successful
    }

    //method that checking validation for city and street
    public static boolean validateCityStreet( String state ){

        return state.matches( "([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)" ) ;
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

    @Override
    public void getViewClick(int postion) {
        viewClick = 1;
        IsMoveInClick =true;
        IsMoveOutClick=true;
        dialogAddEditRent(postion);
    }

            /*
        * AsyncTask to sync the data with the server
        * */
    private class DataCall extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        public void setForceFetch(boolean forceFetch){

        }

        @Override
        protected String doInBackground(String... params) {
            return "Executed";
        }
        /*
        * Set up view widgets when data is accessible
        * */
        @Override
        protected void onPostExecute(String result) {
              /* set up the Media Player that will parse
               The URI and play the song*/

        }
    }
}
