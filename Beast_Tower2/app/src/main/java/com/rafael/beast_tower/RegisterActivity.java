package com.rafael.beast_tower;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    EditText user, pass;
    Button registrar, volver;
    daoUsuario dao;
    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private SensorEventListener proximitySensorListener;
    private static final String TAG = "Proximidad";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        user = findViewById(R.id.regisusuario);
        pass = findViewById(R.id.regiscontraseña);

        registrar = findViewById(R.id.regis_usu);
        volver = findViewById(R.id.volver);
        dao= new daoUsuario(this);

        registrar.setOnClickListener(view -> {
            User u= new User();
            u.setUsuario(user.getText().toString());
            u.setPass(pass.getText().toString());
            if(!u.isNull()){
                Toast.makeText(RegisterActivity.this, "ERROR: CAMPOS VACIOS", Toast.LENGTH_SHORT).show();
            } else if (dao.insertUsuario(u)) {
                Toast.makeText(RegisterActivity.this, "REGISTRO EXITOSO", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(RegisterActivity.this, "USUARIO YA REGISTRADO", Toast.LENGTH_SHORT).show();
            }
        });


        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RegisterActivity.this, "DE VUELTA A I.S.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if (proximitySensor == null) {
            Log.e(TAG, "Proximidad no Disponible.");
            finish();
        }

        proximitySensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.values[0] < proximitySensor.getMaximumRange()) {
                    Toast.makeText(RegisterActivity.this, "NO TE ACERQUES TANTO A LA PANTALLA", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(proximitySensorListener, proximitySensor, 2 * 1000 * 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(proximitySensorListener);
    }

}
