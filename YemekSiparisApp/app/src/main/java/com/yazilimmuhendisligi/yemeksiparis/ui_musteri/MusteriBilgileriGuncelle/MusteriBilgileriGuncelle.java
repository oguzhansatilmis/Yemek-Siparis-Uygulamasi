package com.yazilimmuhendisligi.yemeksiparis.ui_musteri.MusteriBilgileriGuncelle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yazilimmuhendisligi.yemeksiparis.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MusteriBilgileriGuncelle extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
//Değişkenlerin ön tanımlar
String sehir;
EditText semt, sokak, acikAdres, yolTarifi;
FirebaseFirestore db;
FirebaseAuth auth;
TextView guncelAdres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musteri_bilgileri_guncelle);
        setTitle("Adres Bilgisini Güncelle");
        Spinner sehirlerSpinner = findViewById(R.id.sehirler_spinner);
        ArrayAdapter <CharSequence> adapter  = ArrayAdapter.createFromResource(getApplicationContext(), R.array.sehirler, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sehirlerSpinner.setAdapter(adapter);
        sehirlerSpinner.setOnItemSelectedListener(this);
        semt = findViewById(R.id.semt_musteri_text);
        sokak = findViewById(R.id.sokak_musteri_text);
        acikAdres = findViewById(R.id.acik_adres_musteri_text);
        yolTarifi = findViewById(R.id.yol_tarifi_musteri_text);
        guncelAdres = findViewById(R.id.guncel_adres_musteri_text);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        guncelAdresiAl();
    }

    public void dbAdresEkle(View view)
    {
        Map <String, String> adresBilgisiMap = new HashMap<>();
        adresBilgisiMap.put("sehir",sehir);
        adresBilgisiMap.put("semt",semt.getText().toString());
        adresBilgisiMap.put("sokak",sokak.getText().toString());
        adresBilgisiMap.put("acik_adres",acikAdres.getText().toString());
        adresBilgisiMap.put("yol_tarifi",yolTarifi.getText().toString());

        DocumentReference adresRef = db.document("kullanici_bilgileri/"+auth.getUid()+"/adres/birincil_adres");
        if(adresRef.get().isSuccessful())
        {
            adresRef.set(adresBilgisiMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(MusteriBilgileriGuncelle.this, "Adres başarıyla güncellendi.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MusteriBilgileriGuncelle.this, "Error: " +e.getLocalizedMessage() , Toast.LENGTH_SHORT).show();
                }
            });
        }
        else  adresRef.set(adresBilgisiMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MusteriBilgileriGuncelle.this, "Adres başarıyla eklendi.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MusteriBilgileriGuncelle.this, "Error: " +e.getLocalizedMessage() , Toast.LENGTH_SHORT).show();
            }
        });
    guncelAdresiAl();
    }

    public void guncelAdresiAl()
    {
        final DocumentReference adresRef = db.document("kullanici_bilgileri/"+auth.getUid()+"/adres/birincil_adres");
        adresRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                String acikAdresS = documentSnapshot.get("acik_adres").toString();
                String sehirS = documentSnapshot.get("sehir").toString();
                String semtS = documentSnapshot.get("semt").toString();
                String sokakS = documentSnapshot.get("sokak").toString();
                String yolTarifiS = documentSnapshot.get("yol_tarifi").toString();
                guncelAdres.setText(String.format("%s, %s\n%s, %s\nYol Tarifi: %s", acikAdresS, sokakS, semtS, sehirS,yolTarifiS));
                }
                else guncelAdres.setText("KAYITLI BİR ADRES BULUNMAMAKTADIR!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MusteriBilgileriGuncelle.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override //Spinner'dan veri almak için
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         sehir = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
