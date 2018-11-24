package com.example.radhakrishnan.codechallenge;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.radhakrishnan.codechallenge.database.WeatherTable;

import java.util.List;

public class MainActivity extends AppCompatActivity implements InterFaces.WeatherView ,ServiceConnection,
        InterFaces.WeatherAdapterClick,View.OnClickListener {
    WeatherPresenter weatherPresenter;
    RecyclerView list;
    WeatherAdapter adapter;
    Dialog dialog;
    TextInputLayout cityNew;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter =new WeatherAdapter(this);
        list=findViewById(R.id.recyclerView);
        list.setLayoutManager(new GridLayoutManager(this,2));
        list.setAdapter(adapter);
        Intent in = new Intent(this,WeatherFetchService.class);
        startService(in);

        weatherPresenter = new WeatherPresenter(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent in = new Intent(this,WeatherFetchService.class);
        bindService(in,this,BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(this);
    }

    @Override
    public void showProgress(Boolean b) {
            findViewById(R.id.progress).setVisibility((b)? View.VISIBLE: View.GONE);


    }

    @Override
    public void updateView(List<Weather> list) {

    }

    @Override
    public void getDataFromDb() {
        new GetDataFromDB().execute();

    }

    @Override
    public void errorInvalidCity() {
        Toast.makeText(this,getString(R.string.CityNameIsNotValid),Toast.LENGTH_LONG).show();
        if(dialog.isShowing()){
            dialog.dismiss();
        }
    }

    @Override
    public void validateCityAdd(String city) {
        new validateCityForExisting(city).execute(city);
    }

    @Override
    public boolean isInterNetAvailable() {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo() != null;

    }

    @Override
    public void pleaseEableInterNetToAddCity() {
        Toast.makeText(this,getString(R.string.EnableInternetConnection),Toast.LENGTH_LONG).show();
        showProgress(false);
    }

    @Override
    public void sorryWeCouldNotProcessYourRequest() {
        Toast.makeText(this,getString(R.string.SorryWeCouldNot),Toast.LENGTH_LONG).show();
        showProgress(false);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        weatherPresenter.setWeatherService(((WeatherFetchService.MyBinder) service).getService());
        weatherPresenter.forceDataUpdate();


    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        weatherPresenter.service=null;

    }

    @Override
    public void onBindingDied(ComponentName name) {
        weatherPresenter.service=null;
    }

    @Override
    public void onNullBinding(ComponentName name) {
        weatherPresenter.service=null;
    }

    @Override
    public void itemClicked(WeatherTable item) {

    }

    @Override
    public void addNewItemClicked() {

        if(dialog==null){
            dialog=new Dialog(this);
            dialog.setContentView(R.layout.add_new_city);
            dialog.setCancelable(false);
            dialog.findViewById(R.id.cancel).setOnClickListener(this);
            dialog.findViewById(R.id.add).setOnClickListener(this);
            cityNew=dialog.findViewById(R.id.newCity);
        }
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                dialog.dismiss();
                break;
            case R.id.add:
                weatherPresenter.addCity(cityNew.getEditText().getText().toString());
                break;
        }
    }

    class GetDataFromDB extends AsyncTask<Void,Integer,List<WeatherTable>>{

    @Override
    protected List<WeatherTable> doInBackground(Void... voids) {
        return weatherPresenter.getDataFromDb(weatherPresenter.service.getReadDataBase());

    }

    @Override
    protected void onPostExecute(List<WeatherTable> weatherTables) {
        super.onPostExecute(weatherTables);
        adapter.setData(weatherTables);
        adapter.notifyDataSetChanged();
        showProgress(false);


    }
}

    class validateCityForExisting extends AsyncTask<String,Integer,Boolean>{
            String city;

        public validateCityForExisting(String city) {
            this.city = city;
        }

        @Override
        protected Boolean doInBackground(String... s) {
            return weatherPresenter.isCityPresent(weatherPresenter.service.getReadDataBase(),s[0]);

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean) {
                showProgress(false);
                Toast.makeText(MainActivity.this,getString(R.string.CityNameIsNotValid),
                        Toast.LENGTH_LONG).show();
            }else{
                weatherPresenter.addNewCity(city);
                dialog.dismiss();
                showProgress(true);
            }

        }


    }

}
