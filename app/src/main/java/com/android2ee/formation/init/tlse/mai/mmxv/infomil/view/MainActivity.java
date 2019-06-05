package com.android2ee.formation.init.tlse.mai.mmxv.infomil.view;

import android.os.Bundle;
import androidx.appcompat.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android2ee.formation.init.tlse.mai.mmxv.infomil.R;
import com.android2ee.formation.init.tlse.mai.mmxv.infomil.com.ForecastCallBack;
import com.android2ee.formation.init.tlse.mai.mmxv.infomil.com.ForecastServiceUpdater;
import com.android2ee.formation.init.tlse.mai.mmxv.infomil.transverse.model.Forecast;
import com.android2ee.formation.init.tlse.mai.mmxv.infomil.view.adapter.ForecastAdapter;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    public static final String RESULT = "RESULT";
    public static final String MESSAGE = "MESSAGE";
    /****************************************************
     *Constants
     ****************************************************/

   private static String tag="MainActivity";

    /****************************************************
     *Attributes
     ****************************************************/
    /**
     * The button to add the string contained in the edt to the result
     */
    private Button btnAdd;
    /**
     * The result
     */
    private ListView lsvResult;
    private ArrayList<Forecast> items;
    private ForecastAdapter adapter;
    /**
     * The editText to set the message to add
     */
    private EditText edtMessage;


    /****************************************************
     *Managing Life cycle
     ****************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(tag,"onCreate called");
        //initialize the view
        setContentView(R.layout.activity_main);
        //initialize the graphical components
        btnAdd= (Button) findViewById(R.id.btnAdd);
        edtMessage= (EditText) findViewById(R.id.edtMessage);
        //initialize the listView
        items=new ArrayList<Forecast>();
        adapter=new ForecastAdapter(this,items);
        lsvResult= (ListView) findViewById(R.id.lsvResult);
        lsvResult.setAdapter(adapter);
        //add listeners
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAddClicked();
            }
        });
        lsvResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemClicked(position);
            }
        });

        testUpdateForecastFromServer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(tag, "onPause called");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(tag, "onRestart called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(tag, "onResume called");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(tag, "onStart called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(tag, "onStop called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(tag, "onDestroy called");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        items.clear();
//        for(String item:savedInstanceState.getStringArrayList(RESULT)){
//            items.add(item);
//        }
//        adapter.notifyDataSetChanged();
        edtMessage.setText(savedInstanceState.getString(MESSAGE));
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putStringArrayList(RESULT,items);
        outState.putString(MESSAGE,edtMessage.getText().toString());
    }

    /****************************************************
     *Business Methods
     ****************************************************/
    /**
     * Called when the button add is clicked
     */
    private void btnAddClicked(){
        String message=edtMessage.getText().toString();
        Forecast forecast=new Forecast(message);
        //first way
//        adapter.add(message);
        //second way
        items.add(forecast);
        adapter.notifyDataSetChanged();
        edtMessage.setText("");
    }


    /**
     *
     * @param position
     */
    private void itemClicked(int position){
        String message=items.get(position).getTendence();
        edtMessage.setText(message);
    }
    public void testUpdateForecastFromServer() {
        ForecastServiceUpdater fsu= new ForecastServiceUpdater();
        fsu.updateForecastFromServer(new ForecastCallBack() {
            @Override
            public void forecastLoaded(List<Forecast> forecasts) {
                logResult(forecasts);
            }
        });
    }

    private void logResult(List<Forecast> forecasts){
        for(Forecast forecast:forecasts){
            Log.e("ForecastServiceUpdaterTest"," Test forecast found : "+forecast);
            items.add(forecast);
        }
        adapter.notifyDataSetChanged();
        Toast.makeText(this,"forecaste found",Toast.LENGTH_LONG).show();
    }
    /****************************************************
     *Managing Menu
     ****************************************************/
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
