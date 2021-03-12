package com.example.androidtranslate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class ShowTranslation extends AppCompatActivity {
    TextView src,trgt, alternativeSugg;
    TextInputEditText viewinput, viewoutput;
    AppCompatImageButton buttonBack;
    //DataBaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_translation);

        src = (TextView) findViewById(R.id.textViewSource);
        trgt = (TextView) findViewById(R.id.textViewTarget);
        viewinput = (TextInputEditText) findViewById(R.id.txtInput);
        viewoutput = (TextInputEditText) findViewById(R.id.txtOutput);
        buttonBack = (AppCompatImageButton) findViewById(R.id.btnBack);
        String hasil1, hasil2, taw;


        Intent intent = getIntent();
        hasil1=intent.getStringExtra("input");
        hasil2=intent.getStringExtra("output");

        src.setText(intent.getStringExtra("source"));
        trgt.setText(intent.getStringExtra("target"));
        viewinput.setText(hasil1);
        viewoutput.setText(hasil2);

        //ytaw=hasil1;

        //AddData(hasil1);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent btnBack = new Intent(ShowTranslation.this,MainActivity.class);
                startActivity(btnBack);
            }
        });

    }
    /*
    private void AddData(String taw) {
        boolean insertData = myDB.addData(taw);
        if (insertData == true) {
            Toast.makeText(ShowTranslation.this, "Successfully entered data!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(ShowTranslation.this, "Something wrong", Toast.LENGTH_LONG).show();
        }

    }
    */
}