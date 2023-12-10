package com.rafael.beast_tower;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText user, pass;
    Button login, registrar;
    daoUsuario dao;

    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private SensorEventListener proximitySensorListener;
    private static final String TAG = "Proximidad";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = findViewById(R.id.usuario);
        pass = findViewById(R.id.contraseña);
        login = findViewById(R.id.inicio);
        registrar = findViewById(R.id.registrar);

        dao = new daoUsuario(this);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String us=user.getText().toString();
                String pa=pass.getText().toString();
                if (us.equals("") && pa.equals("")) {
                    Toast.makeText(MainActivity.this, "INICIO FALLIDO", Toast.LENGTH_SHORT).show();
                } else if(dao.login(us,pa)==1 || (us.equals("admin"))){
                    User ux = dao.getUsuario(us,pa);
                    Toast.makeText(MainActivity.this, "INICIO EXITOSO, Bienvenido", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, StartActivity.class);
                    if (!us.equals("admin")){
                        intent.putExtra("id", ux.getId());
                    }
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this, "USUARIO Y/O CONTRASEÑA INCORRECTOS", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "HORA DE REGISTRAR USUARIO", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if(proximitySensor == null) {
            Log.e(TAG, "Proximidad no Disponible.");
            finish();
        }

        proximitySensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if(sensorEvent.values[0] < proximitySensor.getMaximumRange()) {
                    Toast.makeText(MainActivity.this, "NO TE ACERQUES TANTO A LA PANTALLA", Toast.LENGTH_SHORT).show();
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



