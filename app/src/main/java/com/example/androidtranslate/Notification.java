package com.example.androidtranslate;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Notification extends AppCompatActivity {
    ListView listNotifikasi;

    DB_Controller_Firebase controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        controller = new DB_Controller_Firebase(this,"",null,1);

        TextView txttitle = findViewById(R.id.txttitle);
        TextView txtmsg = findViewById(R.id.txtmsg);
        Bundle bundle = getIntent().getExtras();
        String judul = bundle.getString("title");
        String isi = bundle.getString("message");
        controller.insert_notif(judul,isi);
        txttitle.setText(judul);
        txtmsg.setText(isi);
        if (bundle.getString("title")==null){
            Log.w("heleeeee","heeeee");
        }



        listNotifikasi = (ListView) findViewById(R.id.listNotif);
        listNotifikasi.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, controller.list_all_students()));



        listNotifikasi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                showDialog();
            }
        });
    }
    private void showDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("Are you sure you want to delete this record?");

        // set pesan dari dialog
        alertDialogBuilder

                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

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

}