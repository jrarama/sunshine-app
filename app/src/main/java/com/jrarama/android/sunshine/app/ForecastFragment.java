package com.jrarama.android.sunshine.app;

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

        // We will manually notify the adapter for data change
        forecastAdapter.setNotifyOnChange(false);

        ListView view = (ListView) rootView.findViewById(R.id.listview_forecast);
        view.setAdapter(forecastAdapter);

        fetchForecast("Singapore", FORECAST_DAYS);
        return rootView;
    }

    private void populateForecastAdapter(String[] forecasts) {
        forecastAdapter.clear();
        for(String item: forecasts) {
            forecastAdapter.add(item);
        }
        forecastAdapter.notifyDataSetChanged();
    }

    private void fetchForecast(String q, int days) {
        new WeatherFetcher().execute(q, days + "");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            fetchForecast("Singapore", FORECAST_DAYS);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Instead of putting the big FetchWeatherTask,
     * just create a subclass that only overrides the required methods
     **/
    private class WeatherFetcher extends FetchWeatherTask {

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            populateForecastAdapter(strings);
        }
    }
}