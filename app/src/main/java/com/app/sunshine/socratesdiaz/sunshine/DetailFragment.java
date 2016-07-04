package com.app.sunshine.socratesdiaz.sunshine;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.sunshine.socratesdiaz.sunshine.data.WeatherContract;


public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    static final String DETAIL_URI = "URI";
    private ShareActionProvider mShareActionProvider;
    private static final String FORECAST_SHARE_HASHTAG = "#SunshineApp";

    private String mForecastStr;
    private Uri mUri;

    private static final int DETAIL_LOADER = 0;

    private static final String[] DETAIL_COLUMNS = {
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING
    };


    private static final int COL_WEATHER_ID = 0;
    private static final int COL_WEATHER_DATE = 1;
    private static final int COL_WEATHER_DESC = 2;
    private static final int COL_WEATHER_MAX_TEMP = 3;
    private static final int COL_WEATHER_MIN_TEMP = 4;
    private static final int COL_WEATHER_HUMIDITY = 5;
    private static final int COL_WEATHER_PRESSURE = 6;
    private static final int COL_WEATHER_WIND_SPEED = 7;
    private static final int COL_WEATHER_DEGREES = 8;
    private static final int COL_WEATHER_CONDITION_ID = 9;
    private static final int COL_WEATHER_LOCATION_SETTING = 10;

    private TextView mDayNameView;
    private TextView mDateView;
    private TextView mHighView;
    private TextView mLowView;
    private TextView mDescView;
    private TextView mHumidityView;
    private TextView mWindView;
    private TextView mPressureView;
    private ImageView mIconView;

    public DetailFragment() {
        // Required empty public constructor
        //setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // TODO Create another view for portrait sw600dp screens
        Bundle arguments = getArguments();
        if(arguments != null) {
            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
        }

        View fragmentDetail = inflater.inflate(R.layout.fragment_detail, container, false);
        mDayNameView = (TextView) fragmentDetail.findViewById(R.id.detail_day_textview);
        mDateView = (TextView) fragmentDetail.findViewById(R.id.detail_date_textview);
        mHighView = (TextView) fragmentDetail.findViewById(R.id.detail_high_textview);
        mLowView = (TextView) fragmentDetail.findViewById(R.id.detail_low_textview);
        mDescView = (TextView) fragmentDetail.findViewById(R.id.detail_forecast_textview);
        mHumidityView = (TextView) fragmentDetail.findViewById(R.id.detail_humidity_textview);
        mWindView = (TextView) fragmentDetail.findViewById(R.id.detail_wind_textview);
        mPressureView = (TextView) fragmentDetail.findViewById(R.id.detail_pressure_textview);
        mIconView = (ImageView) fragmentDetail.findViewById(R.id.detail_icon);

        return fragmentDetail;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.detail, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);

        // TODO: Action provider not working. Fix.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if(mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        } else {
            Log.d(LOG_TAG, "Share Action Provider is null?");
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(null != mUri) {
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    DETAIL_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG, "In onLoadFinished");
        if(data != null && data.moveToFirst()) {
            long dateInMilis = data.getLong(COL_WEATHER_DATE);

            // Day name
            String dayNameString = Utility.getDayName(getContext(), dateInMilis);
            mDayNameView.setText(dayNameString);

            // Date
            String dateString = Utility.formatDate(dateInMilis);
            mDateView.setText(dateString);

            boolean isMetric = Utility.isMetric(getActivity());

            // High temperature
            String high = Utility.formatTemperature(getContext(),
                    data.getDouble(COL_WEATHER_MAX_TEMP), isMetric);
            mHighView.setText(high);

            // Low temperature
            String low = Utility.formatTemperature(getContext(),
                    data.getDouble(COL_WEATHER_MIN_TEMP), isMetric);
            mLowView.setText(low);

            // Icon
            int iconId = Utility.getArtResourceForWeatherCondition(data.getInt(COL_WEATHER_CONDITION_ID));
            mIconView.setImageResource(iconId);

            // Short description
            String weatherDescription = data.getString(COL_WEATHER_DESC);
            mDescView.setText(weatherDescription);

            // Humidity
            String humidity = getString(R.string.format_humidity, data.getFloat(COL_WEATHER_HUMIDITY));
            mHumidityView.setText(humidity);

            // Wind
            String wind = Utility.getFormattedWind(getContext(), data.getFloat(COL_WEATHER_WIND_SPEED),
                    data.getFloat(COL_WEATHER_DEGREES));
            mWindView.setText(wind);

            // Pressure
            String pressure = getString(R.string.format_pressure, data.getFloat(COL_WEATHER_PRESSURE));
            mPressureView.setText(pressure);

            mForecastStr = String.format("%s - %s - %s/%s", dateString, weatherDescription, high, low);

            if(mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            }
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void onLocationChanged(String newLocation) {
        Uri uri = mUri;
        if (null != uri) {
            long date = WeatherContract.WeatherEntry.getDateFromUri(uri);
            Uri updatedUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(newLocation,
                    date);
            mUri = updatedUri;
            getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
        }
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                mForecastStr + FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }
}
