package com.foo.umbrella.ui;

import android.app.Application;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.foo.umbrella.R;
import com.foo.umbrella.data.ApiServicesProvider;
import com.foo.umbrella.data.AppData;
import com.foo.umbrella.data.model.CurrentObservation;
import com.foo.umbrella.data.model.ForecastCondition;
import com.foo.umbrella.data.model.HourlyForecastDataForDay;
import com.foo.umbrella.data.model.WeatherData;
import com.foo.umbrella.utility.AndroidUtility;
import com.foo.umbrella.utility.UmbrellaConstants;
import com.foo.umbrella.utility.Utility;

import org.threeten.bp.format.TextStyle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

  private RecyclerView recyclerView;
  private ApiServicesProvider sp;
  String currentZipCode = null;
  int currentMetrics = UmbrellaConstants.METRICS_CENTRIGRADE;
  private HourlyForecastAdapter adapter;
  private List<HourlyForecastDataForDay> list;
  private CurrentObservation currentObservation;
  private boolean mHideMenu = false;
  private Toolbar myToolbar;
  private ImageView themeImageView;
  private TextView cityName;
  private TextView conditionView;
  private TextView temperatureView;
  private ProgressDialog progressDialog;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    myToolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(myToolbar);
    getSupportActionBar().setTitle(null);

    themeImageView = (ImageView) findViewById(R.id.expandedImage);
    cityName = (TextView) findViewById(R.id.cityName);
    conditionView = (TextView) findViewById(R.id.condition);
    temperatureView = (TextView) findViewById(R.id.temperatureView);

    recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    recyclerView.setHasFixedSize(true);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(layoutManager);

    sp = new ApiServicesProvider(this.getApplication());
    currentMetrics = AppData.instance(this).getMetricsSetting();
    if (AppData.instance(this).getZipCode() != null && !AppData.instance(this).getZipCode().trim().equals("")) {
      currentZipCode = AppData.instance(this).getZipCode();
      callWeatherUpdate(currentZipCode);
    } else {
      launchZipCodeFragment();
    }
    getSupportFragmentManager().addOnBackStackChangedListener(this);
  }

  private void launchZipCodeFragment() {
    FragmentManager fm = getSupportFragmentManager();
    ZipCodeSelector zipCodeSelector = ZipCodeSelector.newInstance("");
    zipCodeSelector.setDismissListener(new DialogFragmentDismissListener() {
      @Override
      public void onDimiss(boolean toUpdate) {
        if (toUpdate) {
          String updatedZipCode = AppData.instance(MainActivity.this).getZipCode();
          if (currentZipCode == null || !currentZipCode.equals(updatedZipCode)) {
            currentZipCode = updatedZipCode;
            callWeatherUpdate(currentZipCode);
          }
        }
      }
    });
    zipCodeSelector.show(fm, "main_zipcode");
  }

  private void launchSettingsFragment() {
    this.getSupportFragmentManager()
            .beginTransaction()
            .add(R.id.root_layout, SettingsFragment.newInstance(), "settings")
            .addToBackStack(null)
            .commit();
  }


  public void callWeatherUpdate(String zip) {
    if(progressDialog == null)
      progressDialog = AndroidUtility.getProgressDialog(this);
    progressDialog.show();

    Call<WeatherData> call = sp.getWeatherService().forecastForZipCallable(zip);
    call.enqueue(new Callback<WeatherData>() {
      @Override
      public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
        if (response.isSuccessful()) {
          // request successful (status code 200, 201)
          WeatherData result = response.body();
          if(result.getCurrentObservation() != null && result.getForecast() != null && result.getForecast().size() > 0) {
            currentObservation = result.getCurrentObservation();
            updateCurrentObservationUI(result.getCurrentObservation());
            list = Utility.parseForecastData(result.getForecast());
            adapter = new HourlyForecastAdapter(list, MainActivity.this);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            progressDialog.dismiss();
          }
        } else {
          progressDialog.dismiss();
          AndroidUtility.showErrorDialog(AndroidUtility.getStringFromResource(MainActivity.this, R.string.errordialogtitle),
                  AndroidUtility.getStringFromResource(MainActivity.this, R.string.fetcherrormessage)+": "+zip, MainActivity.this);
          //request not successful (like 400,401,403 etc)
          //Handle errors
        }
      }

      @Override
      public void onFailure(Call<WeatherData> call, Throwable t) {
        progressDialog.dismiss();
        AndroidUtility.showErrorDialog(AndroidUtility.getStringFromResource(MainActivity.this, R.string.errordialogtitle),
                AndroidUtility.getStringFromResource(MainActivity.this, R.string.fetcherrormessage)+": "+zip, MainActivity.this);
      }
    });
  }

  private void updateCurrentObservationUI(CurrentObservation currentObservation) {
    updateTemperatureUI(currentObservation);
    updateConditionsUI(currentObservation);
    updateCityUI(currentObservation);
    updateBackground(currentObservation);
  }

  private void updateBackground(CurrentObservation currentObservation) {
    if(currentObservation != null) {
      if (Utility.isTemperatureWarm(currentObservation.getTempFahrenheit())) {
        themeImageView.setBackgroundColor(ContextCompat.getColor(this, R.color.weather_warm));
        myToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.weather_warm));
      }
    }
  }

  private void updateCityUI(CurrentObservation currentObservation) {
    if(currentObservation != null) {
      cityName.setText(currentObservation.getDisplayLocation().getFullName());
    }
  }

  private void updateConditionsUI(CurrentObservation currentObservation) {
    if(currentObservation != null) {
      conditionView.setText(currentObservation.getWeatherDescription());
    }
  }

  private void updateTemperatureUI(CurrentObservation currentObservation) {
    if(currentObservation != null) {
      String temp = currentObservation.getTempCelsius();
      if (AppData.instance(this).getMetricsSetting() == UmbrellaConstants.METRICS_FARENHEIT) {
        temp = currentObservation.getTempFahrenheit();
      }
      temperatureView.setText(Utility.formTemperatureDisplayString(temp));
    }
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_scrolling, menu);
    MenuItem item = menu.findItem(R.id.action_settings);
    if(mHideMenu) {
      item.setVisible(false);
    }
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.action_settings) {
      mHideMenu = true;
      ActivityCompat.invalidateOptionsMenu(MainActivity.this);
      launchSettingsFragment();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }


  @Override
  public boolean onSupportNavigateUp() {
    getSupportFragmentManager().popBackStack();
    return true;
  }

  private void checkForSettingsChanges() {
    String updatedZipCode = AppData.instance(this).getZipCode();
    if (currentZipCode == null || !currentZipCode.equals(updatedZipCode)) {
      currentZipCode = updatedZipCode;
      callWeatherUpdate(currentZipCode);
    } else if (currentMetrics != AppData.instance(this).getMetricsSetting()) {
      currentMetrics = AppData.instance(this).getMetricsSetting();
      updateMetricsUI();
    }

  }

  private void updateMetricsUI() {
    updateTemperatureUI(currentObservation);
    adapter.notifyDataSetChanged();
  }


  @Override
  public void onBackStackChanged() {
      if(getSupportFragmentManager().getBackStackEntryCount() == 0) {
          mHideMenu = false;
          ActivityCompat.invalidateOptionsMenu(MainActivity.this);
          checkForSettingsChanges();
      }
  }

}
