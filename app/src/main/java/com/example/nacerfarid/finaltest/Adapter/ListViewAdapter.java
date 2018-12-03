package com.example.nacerfarid.finaltest.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.nacerfarid.finaltest.R;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class ListViewAdapter extends BaseAdapter {

    ArrayList<String> shortNames,arretsLabel,bgColors,txtColors;
    Context context;


    public ListViewAdapter(Context context, ArrayList<String> shortNames,ArrayList<String> arretsLabel,ArrayList<String> bgColors, ArrayList<String> txtColors)
    {
        this.context = context;
        this.arretsLabel = arretsLabel;
        this.bgColors = bgColors;
        this.shortNames = shortNames;
        this.txtColors = txtColors;
    }



    @Override
    public int getCount() {
        return arretsLabel.size();
    }

    @Override
    public Object getItem(int position) {
        return arretsLabel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        String arret = arretsLabel.get(position);
        String shortName = shortNames.get(position);
        String couleur = bgColors.get(position);
        String txtCoul = txtColors.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_arret, parent, false);
        }
        // Lookup view for data population
        TextView tvName = convertView.findViewById(R.id.arretFavoris);
        // Populate the data into the template view using the data object
        tvName.setText(shortName+"|"+arret);
        tvName.setTextColor(Color.parseColor("#"+couleur));
        tvName.setBackgroundColor(Color.parseColor("#"+txtCoul));
        // Return the completed view to render on screen

        return convertView;


    }
}