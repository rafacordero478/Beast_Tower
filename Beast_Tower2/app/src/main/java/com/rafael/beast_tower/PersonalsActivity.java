package com.rafael.beast_tower;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.w3c.dom.Text;



import androidx.appcompat.app.AppCompatActivity;

public class PersonalsActivity extends AppCompatActivity {
    TextView txInfo;
    CheckBox ar1, ar2, ar3, ar4, ob1, ob2, ob3, ob4, ob5, met1, met2, met3;
    RadioButton veg, med, nin, az1, az2, az3, vaso2_6, vaso7_10, vasom10, casa, gym, box, artm, run, noequip, bas, com, min30, min45, min60;
    Button obpersonal;

    private String clienteId="";
    //mqtt://cole478:L0BFmLLjEaGTbALy@cole478.cloud.shiftr.io
    private static String mqttHost = "tcp://cole478.cloud.shiftr.io";
    private static String mqttUser = "cole478";
    private static String mqttPass = "L0BFmLLjEaGTbALy";
    private MqttAndroidClient cliente;
    private MqttConnectOptions opciones;
    static String topic = "CUESTIONARIO";
    static String topicMsgOn = "COMPLETADO";
    static String topicMsgOff = "INCOMPLETO";
    private boolean permisoPublicar;


    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private SensorEventListener proximitySensorListener;
    private static final String TAG = "Proximidad";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_layout);

        ar1 = findViewById(R.id.area1);
        ar2 = findViewById(R.id.area2);
        ar3 = findViewById(R.id.area3);
        ar4 = findViewById(R.id.area4);

        ob1 = findViewById(R.id.obj1);
        ob2 = findViewById(R.id.obj2);
        ob3 = findViewById(R.id.obj3);
        ob4 = findViewById(R.id.obj4);
        ob5 = findViewById(R.id.obj5);

        veg = findViewById(R.id.vegetariana);
        med = findViewById(R.id.mediterranea);
        nin = findViewById(R.id.ninguno);

        az1 = findViewById(R.id.no_tanto);
        az2 = findViewById(R.id.veces_semana);
        az3 = findViewById(R.id.todos_dias);

        vaso2_6 = findViewById(R.id.vasos26);
        vaso7_10 = findViewById(R.id.vasos710);
        vasom10 = findViewById(R.id.mas_vasos10);

        casa = findViewById(R.id.casa);
        gym = findViewById(R.id.gimnasio);

        box = findViewById(R.id.boxeo);
        artm = findViewById(R.id.art_marcial);
        run = findViewById(R.id.correr);

        noequip = findViewById(R.id.no_equip);
        bas = findViewById(R.id.basico);
        com = findViewById(R.id.completo);

        min30 = findViewById(R.id.minutos_30);
        min45 = findViewById(R.id.minutos_45);
        min60 = findViewById(R.id.hora);

        met1 = findViewById(R.id.perder_peso);
        met2 = findViewById(R.id.ganar_musculo);
        met3 = findViewById(R.id.definir_cuerpo);


        obpersonal = findViewById(R.id.obtener1);

        obpersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((ar1.isChecked() || ar2.isChecked() || ar3.isChecked() || ar4.isChecked()) && (ob1.isChecked() || ob2.isChecked() || ob3.isChecked() || ob4.isChecked() || ob5.isChecked()) && (veg.isChecked() || med.isChecked() || nin.isChecked()) && (az1.isChecked() || az2.isChecked() || az3.isChecked()) && (vaso2_6.isChecked() || vaso7_10.isChecked() || vasom10.isChecked()) && (casa.isChecked() || gym.isChecked()) && (box.isChecked() || artm.isChecked() || run.isChecked()) && (noequip.isChecked() || bas.isChecked() || com.isChecked()) && (min30.isChecked() || min45.isChecked() || min60.isChecked()) && (met1.isChecked() || met2.isChecked() || met3.isChecked())){
                    enviarMensaje(topic, topicMsgOn);
                    Intent intent = new Intent(PersonalsActivity.this, PdfWait.class);
                    startActivity(intent);
                }
                else {
                    enviarMensaje(topic, topicMsgOff);
                }
            }
        });
        connectBroker();


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
                    Toast.makeText(PersonalsActivity.this, "NO TE ACERQUES TANTO A LA PANTALLA", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
    }
    private void checkConnection(){
        if(this.cliente.isConnected()){
            this.permisoPublicar = true;
        }
        else{
            this.permisoPublicar = false;
            connectBroker();
        }
    }

    private void enviarMensaje(String topic, String msg){
        checkConnection();
        try{
            int qos = 0;
            this.cliente.publish(topic, msg.getBytes(), qos, false);
            Toast.makeText(getBaseContext(), topic + ":" + msg, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void connectBroker(){
        this.cliente = new MqttAndroidClient(this.getApplicationContext(), mqttHost, this.clienteId);
        this.opciones = new MqttConnectOptions();
        this.opciones.setUserName(mqttUser);
        this.opciones.setPassword(mqttPass.toCharArray());
        try{
            IMqttToken token = this.cliente.connect(opciones);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(PersonalsActivity.this, "Conectado", Toast.LENGTH_SHORT).show();
                    suscribirseTopic();
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(PersonalsActivity.this, "Falló conexión", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (MqttException e){
            e.printStackTrace();
        }
    }

    private void suscribirseTopic(){
        try {
            this.cliente.subscribe(topic, 0);
        }
        catch (MqttSecurityException e){
            e.printStackTrace();
        }
        catch (MqttException e){
            e.printStackTrace();
        }
        this.cliente.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Toast.makeText(getBaseContext(), "Se desconectó el servidor", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                txInfo = findViewById(R.id.txtInfo);
                if(topic.matches(topic)){
                    String msg = new String(message.getPayload());
                    if(msg.matches(topicMsgOn)){
                        txInfo.setText(msg);
                        txInfo.setBackgroundColor(GREEN);
                    }
                    else if(msg.matches(topicMsgOff)){
                        txInfo.setText(msg);
                        txInfo.setBackgroundColor(RED);
                    }
                }
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
            }
        });
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
