/**<ul>
 * <li>ForecastRestYahooSax</li>
 * <li>com.android2ee.formation.restservice.sax.forecastyahoo.service</li>
 * <li>22 nov. 2013</li>
 *
 * <li>======================================================</li>
 *
 * <li>Projet : Mathias Seguy Project</li>
 * <li>Produit par MSE.</li>
 *
 /**
 * <ul>
 * Android Tutorial, An <strong>Android2EE</strong>'s project.</br> 
 * Produced by <strong>Dr. Mathias SEGUY</strong>.</br>
 * Delivered by <strong>http://android2ee.com/</strong></br>
 *  Belongs to <strong>Mathias Seguy</strong></br>
 ****************************************************************************************************************</br>
 * This code is free for any usage but can't be distribute.</br>
 * The distribution is reserved to the site <strong>http://android2ee.com</strong>.</br>
 * The intelectual property belongs to <strong>Mathias Seguy</strong>.</br>
 * <em>http://mathias-seguy.developpez.com/</em></br> </br>
 *
 * *****************************************************************************************************************</br>
 *  Ce code est libre de toute utilisation mais n'est pas distribuable.</br>
 *  Sa distribution est reservée au site <strong>http://android2ee.com</strong>.</br> 
 *  Sa propriété intellectuelle appartient à <strong>Mathias Seguy</strong>.</br>
 *  <em>http://mathias-seguy.developpez.com/</em></br> </br>
 * *****************************************************************************************************************</br>
 */
package com.android2ee.formation.init.tlse.mai.mmxv.infomil.com;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android2ee.formation.init.tlse.mai.mmxv.infomil.com.parser.ForcastSaxHandler;
import com.android2ee.formation.init.tlse.mai.mmxv.infomil.transverse.model.Forecast;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals This class aims to retrieve YahooForecast from the internet and then save them in DB
 */
public class ForecastServiceUpdater {

    /******************************************************************************************/
    /** Attributes **************************************************************************/
    /******************************************************************************************/

    /**
     * The url to use
     */
    private String url;

    /**
     * The object used to communicate with http
     */
    private HttpClient client;
    /**
     * The raw xml answer
     */
    private String responseBody;
    /**
     * The forecasts to display
     */
    private List<Forecast> forecasts;
    /**
     * The logCat's tag
     */
    private final String tag = "ForecastServiceUpdater";
    /**
     * The callBack to update activity
     */
    private ForecastCallBack callback;


    /**
     * The callBack to update activity
     */
    private ForecastPictureCallBack pictureCallBack;
    /******************************************************************************************/
    /** Public method **************************************************************************/
    /******************************************************************************************/

    /**
     * Return the forecast
     *
     * @param callback The callback to use to deliver the data when data updated
     */
    public void updateForecastFromServer(ForecastCallBack callback) {
        Log.e(tag, "updateForecastFromServer called");
        this.callback = callback;
        // retrieve the url
        url = "http://weather.yahooapis.com/forecastrss?w=628886&u=c";
        //then link the Handler with the handler of the runnable
        if (restCallRunnable.restCallHandler == null) {
            restCallRunnable.restCallHandler = restCallHandler;
        }
        restCallRunnable.run();
    }

    public void downloadPicture(ForecastPictureCallBack pictureCallBack, String pictureCode) {
        //do a new AsyncTask, do not reuse because it can be called n times at the same time
        new DownloadPictureAsync(pictureCallBack).execute(pictureCode);
    }

    private class DownloadPictureAsync extends AsyncTask<String, Void, Bitmap> {
        /**
         * The callback to use
         */
        ForecastPictureCallBack pictureCallBack;
        /**
         * The url to use
         */
        private String pictureUrl;

        private DownloadPictureAsync(ForecastPictureCallBack pictureCallBack) {
            this.pictureCallBack = pictureCallBack;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            pictureUrl = "http://l.yimg.com/a/i/us/we/52/" + params[0] + ".gif";
            HttpURLConnection conn=null;
            // retrieve the URL
            URL myFileUrl = null;
            try {
                myFileUrl = new URL(pictureUrl);
                    //Define the HttpConnection and open it
                conn = (HttpURLConnection) myFileUrl.openConnection();
                //Define that connection is an input
                conn.setDoInput(true);
                //connect
                conn.connect();
                //retrieve the input stream returned by the connection
                InputStream is = conn.getInputStream();
                //Use this input stream to build your bitmpa
                Bitmap bmImg = BitmapFactory.decodeStream(is);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //disconnect
                if(conn!=null){
                    conn.disconnect();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            pictureCallBack.onPictureLoaded(bitmap);
        }
    }
    /******************************************************************************************/
    /** Loading Forecast **************************************************************************/
    /******************************************************************************************/


    /**
     * The runnable to execute when requesting update from the server
     */
    private RestCallRunnable restCallRunnable = new RestCallRunnable();

    /**
     * @author Mathias Seguy (Android2EE)
     * @goals This class aims to implements a Runnable with an Handler
     */
    private class RestCallRunnable implements Runnable {
        /**
         * The handler to use to communicate outside the runnable
         */
        public Handler restCallHandler = null;

        @Override
        public void run() {
            // Do the rest http call
            // Parse the element
            buildForecasts(getForecast());
            restCallHandler.sendMessage(restCallHandler.obtainMessage());
        }

        /**
         * Retrieve the forecast
         */
        private String getForecast() {
            Log.e(tag, "getForecast called");
            // The HTTP get method send to the URL
            HttpGet getMethod = new HttpGet(url);
            // The basic response handler
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            // instantiate the http communication
            client = new DefaultHttpClient();
            // Call the URL and get the response body
            try {
                responseBody = client.execute(getMethod, responseHandler);
                Log.e(tag, "getForecast returned " + responseBody);
            } catch (ClientProtocolException e) {
                Log.e(tag, "ClientProtocolException", e);
                //ExceptionManager.manage(new ExceptionManaged(this.getClass(), R.string.exc_client_protocol, e));
            } catch (IOException e) {
                Log.e(tag, "IOException", e);
                //ExceptionManager.manage(new ExceptionManaged(this.getClass(), R.string.exc_http_get_error, e));
            }
            if (responseBody != null) {
                Log.d(tag, responseBody);
            }
            // parse the response body
            return responseBody;
        }

        /**
         * Build the Forecasts list by parsing the xml response using SAX
         *
         * @param raw the xml response of the web server
         */
        private void buildForecasts(String raw) {

            Log.e(tag, "buildForecasts called");
            try {
                // Create a new instance of the SAX parser
                SAXParserFactory saxPF = SAXParserFactory.newInstance();
                SAXParser saxP = saxPF.newSAXParser();
                // The xml reader
                XMLReader xmlR = saxP.getXMLReader();
                // Create the Handler to handle each of the XML tags.
                ForcastSaxHandler forecastHandler = new ForcastSaxHandler();
                xmlR.setContentHandler(forecastHandler);
                // then parse
                xmlR.parse(new InputSource(new StringReader(raw)));
                // and retrieve the parsed forecasts
                forecasts = forecastHandler.getForecasts();
                Log.e(tag, "buildForecasts finished");
            } catch (ParserConfigurationException e) {
                Log.e(tag, "ParserConfigurationException", e);
//				ExceptionManager.manage(new ExceptionManaged(this.getClass(), R.string.exc_parsing, e));
            } catch (SAXException e) {
                Log.e(tag, "SAXException", e);
//				ExceptionManager.manage(new ExceptionManaged(this.getClass(), R.string.exc_parsing, e));
            } catch (IOException e) {
                Log.e(tag, "IOException", e);
//				ExceptionManager.manage(new ExceptionManaged(this.getClass(), R.string.exc_parsing, e));
            }
        }
    }

    /**
     * The handler awoke when the Runnable has finished it's execution
     */
    private Handler restCallHandler = new Handler() {
        /* (non-Javadoc)
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        @Override
        public void handleMessage(Message msg) {
            returnForecast();
        }

    };

    /**
     * Called when the forecast are built
     * Return that list to the calling Activity using the ForecastCallBack
     */
    private void returnForecast() {
        // use the callback to prevent the client
        for (Forecast forcast : forecasts) {
            Log.e("ForcastServiceUpdater ", "Found " + forcast);
        }
        callback.forecastLoaded(forecasts);
    }
}
