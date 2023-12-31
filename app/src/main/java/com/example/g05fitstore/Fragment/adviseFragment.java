package com.example.g05fitstore.Fragment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.g05fitstore.Client.SaveActivity;
import com.example.g05fitstore.R;

public class adviseFragment extends Fragment implements SensorEventListener {
    private TextView textView, lightNum,textViewtemp;
    private SensorManager sensorManager;
    private Sensor lightSensor, temperatureSensor;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_advise, container, false);
        textView = view.findViewById(R.id.lightSensorTextView);
        textViewtemp = view.findViewById(R.id.temperSensorTextView);
        lightNum = view.findViewById(R.id.lightSensorNum);


        // Lấy ra SensorManager và cảm biến ánh sáng và nhiệt độ
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
      temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        if (lightSensor != null ) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
       sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);

        } else {
            textView.setText("Thiết bị không hỗ trợ cảm biến ánh sáng và nhiệt độ");
        }

        return view;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            float temperatureInCelsius = event.values[0];


            if (temperatureInCelsius > 100) {
                textViewtemp.setText("Nhiệt độ quá cao!, không thích hợp bảo quản nông sản.");
            } else if (temperatureInCelsius < 35) {
                textViewtemp.setText("Nhiệt độ thấp, không thích hợp bảo quản nông sản ");
            } else {
               // textViewtemp.setText("nhiệt độ và ánh sáng ở mức độ ổn định, phù hợp cho việc bảo quản nông sản.");
            }
        } else {
            float lightValue = event.values[0];
            lightNum.setText("Giá trị cảm biến ánh sáng: " + lightValue);
            if (lightValue > 20) {
                textView.setText("Ánh sáng ở mức độ ổn định, phù hợp cho việc bảo quản nông sản.");
                startNewActivity(lightValue);
            }
        }
    }

    private void startNewActivity(float lightValue) {
        Activity activity = getActivity();

        if (activity != null) {
            Intent intent = new Intent(activity, SaveActivity.class);
            intent.putExtra("lightValue", lightValue+"");
            activity.startActivity(intent);
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}

