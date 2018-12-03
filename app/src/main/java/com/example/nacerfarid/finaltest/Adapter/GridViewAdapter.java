package com.example.nacerfarid.finaltest.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nacerfarid.finaltest.R;

import java.util.List;

/**
 * Created by Nacer FARID on 19/03/2018.
 */

public class GridViewAdapter extends BaseAdapter
{
    private final Context context;
    private final List<String> items;
    private final List<String> bgColors,textColors;

    public GridViewAdapter(Context context, List<String> items, List<String> bgColors, List<String> textColors)
    {
        this.context = context;
        this.items = items;
        this.bgColors = bgColors;
        this.textColors = textColors;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position)
    {
        return items.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup)
    {
        final String itemsList = items.get(position);

        if(view == null)
        {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.item_layout_textview,null);
        }

        final TextView itemTextView = (TextView) view.findViewById(R.id.itemTextView);
        final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);

        itemTextView.setText(itemsList);
        if(bgColors.get(position) != null)
            linearLayout.setBackgroundColor(Color.parseColor("#"+bgColors.get(position)));

        if(textColors.get(position) != null)
            itemTextView.setTextColor(Color.parseColor("#"+textColors.get(position)));

        return view;
    }
}
