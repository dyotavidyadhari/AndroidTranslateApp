package com.example.androidtranslate;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.accessibilityservice.GestureDescription;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.Transliterator;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class Notification extends AppCompatActivity {
    ListView listNotifikasi;

    DB_Controller_Firebase controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Notification");

        controller = new DB_Controller_Firebase(this,"",null,1);


        try{
        Bundle bundle = getIntent().getExtras();
        String judul = bundle.getString("title");
        String isi = bundle.getString("message");
            if (bundle.getString("title")==null){
                Log.w("null?","yes");
            }
        controller.insert_notif(judul,isi);} catch (Exception e) {
            e.printStackTrace();
        }


        listNotifikasi = (ListView) findViewById(R.id.listNotif);
        listNotifikasi.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, controller.list_all_students()));
        listNotifikasi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String chosen = listNotifikasi.getItemAtPosition(position).toString();
                String[] list_chosen = chosen.split("\n",2);
                showDialog(list_chosen[0], list_chosen[1]);
            }
        });
    }
    //back
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        return true;
    }
    //



    private void showDialogAll(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("Are you sure you want to delete all record?");

        // set pesan dari dialog
        alertDialogBuilder

                .setIcon(R.drawable.remove)
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        controller.delete_all();
                        Notification.this.finish();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menudel, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setMode(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    private void setMode(int selectedMode) {
        switch (selectedMode){
            case R.id.delete:
                showDialogAll();


        }
    }


}