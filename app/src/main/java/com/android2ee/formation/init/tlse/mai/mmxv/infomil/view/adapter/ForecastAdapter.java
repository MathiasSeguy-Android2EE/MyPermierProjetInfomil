/**<ul>
 * <li>ForecastRestYahooSax</li>
 * <li>com.android2ee.formation.restservice.sax.forecastyahoo</li>
 * <li>28 mai 2014</li>
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

package com.android2ee.formation.init.tlse.mai.mmxv.infomil.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android2ee.formation.init.tlse.mai.mmxv.infomil.R;
import com.android2ee.formation.init.tlse.mai.mmxv.infomil.transverse.model.Forecast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Mathias Seguy - Android2EE on 04/05/2015.
 */
public class ForecastAdapter extends ArrayAdapter<Forecast>{
    LayoutInflater inflater;
    Drawable sun;
    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
    /**
     * Constructor
     *
     * @param context  The current context.
     */
    public ForecastAdapter(Context context, ArrayList<Forecast> items) {
        super(context, R.layout.activity_main,items);
        inflater=LayoutInflater.from(context);
        sun=context.getResources().getDrawable(R.drawable.ic_sun);

    }
    View rowView;
    Forecast forecast;
    ViewHolder vh;
    /**
     * {@inheritDoc}
     *
     * @param position
     * @param convertView
     * @param parent
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        rowView=convertView;
         forecast=getItem(position);
        if(rowView==null){
            rowView=inflater.inflate(R.layout.today_forecast,parent, false);
            vh=new ViewHolder(rowView);
            rowView.setTag(vh);
        }
        vh= (ViewHolder) rowView.getTag();
        vh.getTxvDate().setText(sdf.format(forecast.getDate().getTime()));
        vh.getTxvTemp().setText(forecast.getTemp());
        vh.getTxvTendence().setText(forecast.getTendence());
        vh.getImvPicture().setBackgroundDrawable(sun);
        return rowView;
    }

    private class ViewHolder{
        TextView txvDate;
        TextView txvTemp;
        TextView txvTendence;
        ImageView imvPicture;
        View view;

        private ViewHolder(View view) {
            this.view = view;
        }

        public TextView getTxvDate() {
            if(txvDate==null){
                txvDate=(TextView)view.findViewById(R.id.txvDate);
            }
            return txvDate;
        }

        public TextView getTxvTemp() {
            if(txvTemp==null){
                txvTemp=(TextView)view.findViewById(R.id.txvTemp);
            }
            return txvTemp;
        }

        public TextView getTxvTendence() {
            if(txvTendence==null){
                txvTendence=(TextView)view.findViewById(R.id.txvTendence);
            }
            return txvTendence;
        }

        public ImageView getImvPicture() {
            if(imvPicture==null){
                imvPicture=(ImageView)view.findViewById(R.id.imvPicture);
            }
            return imvPicture;
        }
    }
}
