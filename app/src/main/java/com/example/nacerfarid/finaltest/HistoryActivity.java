package com.example.nacerfarid.finaltest;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.nacerfarid.finaltest.Adapter.ListViewAdapter;
import com.example.nacerfarid.finaltest.Adapter.ListViewAdapterH;
import com.example.nacerfarid.finaltest.Utils.DataBase.dataBaseTAG;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryActivity extends AppCompatActivity {
    ArrayList<String> shortNames,bgColors, textColors,arretsLabel,arretsCode,lastVisited;
    @BindView(R.id.listeHistorique) ListView listView;
    public int NUM_ITEMS_PAGE   = 10;
    private int noOfBtns;
    private Button[] btns;
    ListViewAdapterH sd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ButterKnife.bind(this);

        shortNames = new ArrayList<String>();
        bgColors = new ArrayList<String>();
        textColors = new ArrayList<String>();
        arretsLabel = new ArrayList<String>();
        arretsCode = new ArrayList<String>();
        lastVisited = new ArrayList<String>();
        getAllData();

        loadList(0);

        CheckBtnBackGroud(0);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                String arret = adapterView.getAdapter().getItem(position).toString();
                Intent intent = new Intent(HistoryActivity.this,HoraireArretActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("shortName",shortNames.get(position));
                bundle1.putString("color",bgColors.get(position));
                bundle1.putString("txtColor",textColors.get(position));
                bundle1.putString("arretLabel",arretsLabel.get(position));
                bundle1.putString("arret",arretsCode.get(position));
                bundle1.putString("code","SEM:"+shortNames.get(position));
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });
        Btnfooter();

    }

    private void Btnfooter()
    {
        int val = shortNames.size()%NUM_ITEMS_PAGE;
        val = val==0?0:1;
        noOfBtns=shortNames.size()/NUM_ITEMS_PAGE+val;

        LinearLayout ll = (LinearLayout)findViewById(R.id.btnLay);

        btns    =new Button[noOfBtns];

        for(int i=0;i<noOfBtns;i++)
        {
            btns[i] =   new Button(this);
            btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
            btns[i].setText(""+(i+1));

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.addView(btns[i], lp);

            final int j = i;
            btns[j].setOnClickListener(new View.OnClickListener() {

                public void onClick(View v)
                {
                    loadList(j);
                    CheckBtnBackGroud(j);
                }
            });
        }

    }

        private void CheckBtnBackGroud(int index)
        {
            for(int i=0;i<noOfBtns;i++)
            {
                if(i==index)
                {
                    btns[index].setBackgroundDrawable(getResources().getDrawable(R.drawable.button_rounded));
                    btns[i].setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                }
                else
                {
                    btns[i].setBackgroundColor(getResources().getColor(android.R.color.white));
                    btns[i].setTextColor(getResources().getColor(android.R.color.black));
                }
            }

        }


    public void getAllData() {
        String selectQuery = "SELECT  * FROM LAST_VISITED ORDER BY lastVisitedTime DESC";

        dataBaseTAG dbTag = new dataBaseTAG(HistoryActivity.this);
        SQLiteDatabase db = dbTag.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                shortNames.add(cursor.getString(cursor.getColumnIndex("shortName")));
                arretsLabel.add(cursor.getString(cursor.getColumnIndex("label")));
                bgColors.add(cursor.getString(cursor.getColumnIndex("color")));
                textColors.add(cursor.getString(cursor.getColumnIndex("textColor")));
                arretsCode.add(cursor.getString(cursor.getColumnIndex("code")));
                lastVisited.add(cursor.getString(cursor.getColumnIndex("lastVisitedTime")));
            } while (cursor.moveToNext());
        }
        db.close();
    }

    private void loadList(int number)
    {
        ArrayList<String> sortName = new ArrayList<String>();
        ArrayList<String> sortLabel = new ArrayList<>();
        ArrayList<String> sortColor = new ArrayList<>();
        ArrayList<String> sortTextColor = new ArrayList<>();

        int start = number * NUM_ITEMS_PAGE;
        for(int i=start;i<(start)+NUM_ITEMS_PAGE;i++)
        {
            if(i<shortNames.size())
            {
                sortName.add(shortNames.get(i));
                sortLabel.add(arretsLabel.get(i));
                sortColor.add(bgColors.get(i));
                sortTextColor.add(textColors.get(i));
            }
            else
            {
                break;
            }
        }
        sd = new ListViewAdapterH(HistoryActivity.this,sortName,sortLabel,sortTextColor,sortColor);
        listView.setAdapter(sd);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
