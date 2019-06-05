package com.android2ee.formation.init.tlse.mai.mmxv.infomil.com;

import android.util.Log;

import com.android2ee.formation.init.tlse.mai.mmxv.infomil.transverse.model.Forecast;

import junit.framework.TestCase;

import java.util.List;

public class ForecastServiceUpdaterTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();

    }

    public void tearDown() throws Exception {

    }

    public void testUpdateForecastFromServer() throws Exception {
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
        }
    }
}