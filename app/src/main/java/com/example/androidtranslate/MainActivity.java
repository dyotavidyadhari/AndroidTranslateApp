package com.example.androidtranslate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.icu.util.ULocale;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    //DataBaseHelper myDB;
    private static final int REUEST_CODE_SPEECH_INPUT = 1;
    private static final String TAG = "Response";
    Spinner spinAwal, spinTujuan;
    ListView daftarHis;
    TextInputEditText masukanKata;
    MaterialButton buttonTrans, btnSwap, buttonVoice;
    TextView tampilkan;

    DB_Controller controller;

    private ArrayAdapter<CharSequence> aAdapter, bAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        daftarHis = (ListView) findViewById(R.id.daftarHistory);

        controller = new DB_Controller(this,"",null,1);

        buttonTrans = (MaterialButton) findViewById(R.id.btnTranslate);
        buttonVoice = (MaterialButton) findViewById(R.id.btnVoice);
        tampilkan = (TextView) findViewById(R.id.txtHistory);
        spinAwal = (Spinner) findViewById(R.id.spnAwal);
        spinTujuan = (Spinner) findViewById(R.id.spnTujuan);
        btnSwap = (MaterialButton) findViewById(R.id.buttonSwap);

        final Intent buttonBack = getIntent();

        masukanKata = (TextInputEditText) findViewById(R.id.sourceText);

        aAdapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_bahasa, android.R.layout.simple_spinner_item);
        aAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinAwal.setAdapter(aAdapter);

        bAdapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_bahasa, android.R.layout.simple_spinner_item);
        bAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTujuan.setAdapter(bAdapter);

        buttonVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });

        btnSwap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int positionSpinner1 = spinAwal.getSelectedItemPosition() ;
                int positionSpinner2 = spinTujuan.getSelectedItemPosition() ;
                if (spinAwal.getAdapter().equals(aAdapter)) {
                    spinAwal.setAdapter(bAdapter);
                    spinTujuan.setAdapter(aAdapter);
                } else {
                    spinAwal.setAdapter(aAdapter);
                    spinTujuan.setAdapter(bAdapter);
                }
                spinAwal.setSelection(positionSpinner2);
                spinTujuan.setSelection(positionSpinner1);
            }
        });

        buttonTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    if (masukanKata.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(),
                                "Your text is empty", Toast.LENGTH_SHORT).show();
                    }else {
                        postRequest();
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }

            }

            public void postRequest() throws IOException{
                final String userInputSpinnerSource = spinAwal.getSelectedItem().toString();
                final String userInputSpinnerTarget = spinTujuan.getSelectedItem().toString();
                final String inputWord = masukanKata.getText().toString();

                String url1 = "https://translated-mymemory---translation-memory.p.rapidapi.com/api/get?langpair="+userInputSpinnerSource+"%7C"+userInputSpinnerTarget+"&q="+inputWord+"&mt=1&onlyprivate=0&de=a%40b.c";

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url1)
                        .get()
                        .addHeader("x-rapidapi-key", "5ceca1ee17msh20fb0aafedb7c19p14bb6cjsn848a75028a7a")
                        .addHeader("x-rapidapi-host", "translated-mymemory---translation-memory.p.rapidapi.com")
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        String mmsg = e.getMessage().toString();
                        Log.w("onFailure:",mmsg );
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String mmsg = response.body().string();
                        Log.e(TAG, mmsg );

                        if(mmsg != null){
                            try{
                                JSONObject ambilDataBahasa = new JSONObject(mmsg);

                                    String bhs = ambilDataBahasa.getJSONObject("responseData").getString("translatedText");

                                    Intent translateto = new Intent(MainActivity.this,ShowTranslation.class);
                                    translateto.putExtra("source",userInputSpinnerSource);
                                    translateto.putExtra("target",userInputSpinnerTarget);
                                    translateto.putExtra("input",inputWord);
                                    translateto.putExtra("output",bhs);

                                    startActivity(translateto);

                                controller.insert_student(userInputSpinnerSource,
                                        userInputSpinnerTarget,
                                        bhs,
                                        inputWord
                                );


                            }catch (final JSONException e){
                                e.printStackTrace();
                            }
                        }

                    }
                });
            }
        });


        daftarHis.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, controller.list_all_students()));

    }

    private void speak() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Hi! Speak something..");

        try{
            startActivityForResult(intent, REUEST_CODE_SPEECH_INPUT);
        }catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REUEST_CODE_SPEECH_INPUT: {
                if(resultCode == RESULT_OK && null != data){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    masukanKata.setText(result.get(0));
                }
                break;
            }
        }

    }

}

