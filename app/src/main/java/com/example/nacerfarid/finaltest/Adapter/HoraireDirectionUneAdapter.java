package com.example.nacerfarid.finaltest.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.nacerfarid.finaltest.R;

import java.util.List;

public class HoraireDirectionUneAdapter extends BaseAdapter
{
    private final Context context;
    private final List<String> arrets;
    private final List<String> delay;

    public HoraireDirectionUneAdapter(Context context, List<String> arrets, List<String> delay)
    {
        this.context = context;
        this.arrets = arrets;
        this.delay = delay;
    }

    @Override
    public int getCount() {
        return arrets.size();
    }

    @Override
    public Object getItem(int i) {
        return arrets.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup)
    {
        final String arret = arrets.get(position);
        final String duree = delay.get(position);
        if(view == null)
        {
            view = LayoutInflater.from(context).inflate(R.layout.item_direction_une_layout_textview, viewGroup, false);
        }

        final TextView arretTextView = (TextView) view.findViewById(R.id.itemArretTextView);
        final TextView horaireTextView = (TextView) view.findViewById(R.id.itemHoraireTextView);

        arretTextView.setText(arret);
        horaireTextView.setText(String.valueOf(duree));

        return view;
    }
}
