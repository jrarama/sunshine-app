package com.jrarama.android.sunshine.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.concurrent.ExecutionException;


/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

    private static final String TAG = ForecastFragment.class.getSimpleName();
    private static final int FORECAST_DAYS = 7;

    private ArrayAdapter<String> forecastAdapter;

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        forecastAdapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textview
        );
        forecastAdapter.setNotifyOnChange(false);

        ListView view = (ListView) rootView.findViewById(R.id.listview_forecast);
        view.setAdapter(forecastAdapter);
        populateForecastAdapter();
        return rootView;
    }

    private void populateForecastAdapter() {
        String[] forecasts = fetchForecast("Singapore", FORECAST_DAYS);

        forecastAdapter.clear();
        for(String item: forecasts) {
            forecastAdapter.add(item);
        }
        forecastAdapter.notifyDataSetChanged();
    }

    private String[] fetchForecast(String q, int days) {
        AsyncTask<String, Void, String[]> task = new FetchWeatherTask().execute(q, days + "");
        try {
            String[] result = task.get();
            return result;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            populateForecastAdapter();
        }
        return super.onOptionsItemSelected(item);
    }
}
