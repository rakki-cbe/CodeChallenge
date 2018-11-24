package com.example.radhakrishnan.codechallenge;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.radhakrishnan.codechallenge.database.WeatherTable;

import java.util.List;

public class WeatherDetailActivity extends AppCompatActivity implements ServiceConnection, InterFaces.WeatherDetailView {
    private static final String CITY_NAME = "name";
    WeatherDetailPresenter presenter;
    WeatherDetailAdapter adapter;
    RecyclerView list;

    public static Intent getIntentForActivity(Context context, String city) {
        Intent in = new Intent(context, WeatherDetailActivity.class);
        in.putExtra(CITY_NAME, city);
        return in;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        presenter = new WeatherDetailPresenter(this);
        adapter = new WeatherDetailAdapter();
        list = findViewById(R.id.recyclerView);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        presenter.setCity(getCityNameFromIntent());
        setTitle(getCityNameFromIntent());
        Intent in = new Intent(this, WeatherFetchService.class);
        startService(in);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent in = new Intent(this, WeatherFetchService.class);
        bindService(in, this, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(this);
    }

    public String getCityNameFromIntent() {
        if (getIntent() != null && getIntent().getExtras() != null)
            return getIntent().getExtras().getString(CITY_NAME);
        else return "";
    }

    @Override
    public void showProgress(Boolean b) {

    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        presenter.setWeatherService(((WeatherFetchService.MyBinder) service).getService());
        new loadList().execute();


    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        presenter.service = null;

    }

    @Override
    public void onBindingDied(ComponentName name) {
        presenter.service = null;
    }

    @Override
    public void onNullBinding(ComponentName name) {
        presenter.service = null;
    }

    class loadList extends AsyncTask<Void, Void, List<WeatherTable>> {

        @Override
        protected List<WeatherTable> doInBackground(Void... voids) {
            return presenter.getDataFromDb();

        }

        @Override
        protected void onPostExecute(List<WeatherTable> weatherTables) {
            super.onPostExecute(weatherTables);
            adapter.data = weatherTables;
            adapter.notifyDataSetChanged();
        }
    }
}
