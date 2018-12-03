package com.example.nacerfarid.finaltest;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.nacerfarid.finaltest.Adapter.ListViewAdapter;
import com.example.nacerfarid.finaltest.Service.notificationService;
import com.example.nacerfarid.finaltest.Utils.DataBase.dataBaseTAG;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavorisActivity extends AppCompatActivity {
    ArrayList<String> shortNames,bgColors, textColors,arretsLabel,arretsCode;
    @BindView(R.id.listeFavoris) ListView listView;
    ListViewAdapter listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoris);
        ButterKnife.bind(this);


        shortNames = new ArrayList<String>();
        bgColors = new ArrayList<String>();
        textColors = new ArrayList<String>();
        arretsLabel = new ArrayList<String>();
        arretsCode = new ArrayList<String>();
        getAllData();

        listViewAdapter = new ListViewAdapter(FavorisActivity.this,shortNames,arretsLabel,textColors,bgColors);
        listView.setAdapter(listViewAdapter);

        listViewAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                String arret = adapterView.getAdapter().getItem(position).toString();
                Intent intent = new Intent(FavorisActivity.this,HoraireArretActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("shortName",shortNames.get(position));
                bundle1.putString("color",bgColors.get(position));
                bundle1.putString("txtColor",textColors.get(position).replace("''","'"));
                bundle1.putString("arretLabel",arretsLabel.get(position));
                bundle1.putString("arret",arretsCode.get(position));
                bundle1.putString("code","SEM:"+shortNames.get(position));
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1 = new Intent(getApplicationContext(), notificationService.class);
                intent1.putExtra("codeArret",arretsCode.get(position));
                intent1.putExtra("numTransport",shortNames.get(position));
                intent1.putExtra("nomStation",arretsLabel.get(position));
                intent1.putExtra("typeTransport","TRAM");
                getApplicationContext().startService(intent1);
                return true;
            }
        });

    }

    public void getAllData() {
        String selectQuery = "SELECT  * FROM ARRETS ";

        dataBaseTAG dbTag = new dataBaseTAG(FavorisActivity.this);
        SQLiteDatabase db = dbTag.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                shortNames.add(cursor.getString(cursor.getColumnIndex("shortName")));
                arretsLabel.add(cursor.getString(cursor.getColumnIndex("label")));
                bgColors.add(cursor.getString(cursor.getColumnIndex("color")));
                textColors.add(cursor.getString(cursor.getColumnIndex("textColor")));
                arretsCode.add(cursor.getString(cursor.getColumnIndex("code")));
            } while (cursor.moveToNext());
        }
        db.close();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
