package com.yazilimmuhendisligi.yemeksiparis.ui_firma.FirmaBilgileriGuncelle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yazilimmuhendisligi.yemeksiparis.R;
import com.yazilimmuhendisligi.yemeksiparis.ui_musteri.MusteriBilgileriGuncelle.MusteriBilgileriGuncelle;

import java.util.HashMap;
import java.util.Map;

public class FirmaBilgileriGuncelle extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String sehir;
    EditText semt, sokak, acikAdres, yolTarifi,firmaAdi,firmaSahibi,firmaTelefon;
    FirebaseFirestore db;
    FirebaseAuth auth;
    TextView guncelAdres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firma_bilgileri_guncelle);
        setTitle("Firma Bilgisi Güncelle");
        Spinner sehirlerSpinner = findViewById(R.id.firma_spinner);
        ArrayAdapter<CharSequence> adapter  = ArrayAdapter.createFromResource(getApplicationContext(), R.array.sehirler, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sehirlerSpinner.setAdapter(adapter);
        sehirlerSpinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        firmaAdi=findViewById(R.id.firma_adi);
        firmaSahibi=findViewById(R.id.firma_sahip);
        firmaTelefon=findViewById(R.id.firma_telefon_numarası);
        semt = findViewById(R.id.firma_semt);
        sokak = findViewById(R.id.firma_sokak);
        acikAdres = findViewById(R.id.firma_acık_adres);
        yolTarifi = findViewById(R.id.firma_yol_tarifi);
        //guncel adres
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

        public void firma_dbAdresEkle(View view)
        {
            Map<String, String> adresBilgisiMap = new HashMap<>();
            adresBilgisiMap.put("firma_adi",firmaAdi.getText().toString());
            adresBilgisiMap.put("firma_sahip",firmaSahibi.getText().toString());
            adresBilgisiMap.put("firma_telefon",firmaTelefon.getText().toString());
            adresBilgisiMap.put("sehir",sehir);
            adresBilgisiMap.put("semt",semt.getText().toString());
            adresBilgisiMap.put("sokak",sokak.getText().toString());
            adresBilgisiMap.put("acik_adres",acikAdres.getText().toString());
            adresBilgisiMap.put("yol_tarifi",yolTarifi.getText().toString());

            DocumentReference adresRef = db.document("kullanici_bilgileri/"+auth.getUid()+"/firmaBilgi/firmaAdres");
            if(adresRef.get().isSuccessful())
            {
                adresRef.set(adresBilgisiMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(FirmaBilgileriGuncelle.this, "Firma başarıyla güncellendi.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(FirmaBilgileriGuncelle.this, "Error: " +e.getLocalizedMessage() , Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else  adresRef.set(adresBilgisiMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(FirmaBilgileriGuncelle.this, "Firma bilgisi başarıyla eklendi.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(FirmaBilgileriGuncelle.this, "Error: " +e.getLocalizedMessage() , Toast.LENGTH_SHORT).show();
                }
            });

        }





    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        sehir = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
