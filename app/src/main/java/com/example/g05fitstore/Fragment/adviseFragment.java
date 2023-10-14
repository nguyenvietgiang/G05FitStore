package com.example.g05fitstore.Fragment;
import android.content.Context;
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
import com.example.g05fitstore.R;

public class adviseFragment extends Fragment implements SensorEventListener {
    private TextView textView;
    private SensorManager sensorManager;
    private Sensor lightSensor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_advise, container, false);
        textView = view.findViewById(R.id.lightSensorTextView);

        // Lấy ra SensorManager và cảm biến ánh sáng
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            textView.setText("Thiết bị không hỗ trợ cảm biến ánh sáng.");
        }

        return view;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float lightValue = event.values[0];
        textView.setText("Giá trị cảm biến ánh sáng: " + lightValue);
        if (lightValue > 100) {
            textView.setText("Ánh sáng quá mạnh, nó sẽ ảnh hướng xấu trong việc bảo quản nông sản.");
        } else if (lightValue < 50) {
            textView.setText("Ánh sáng quá yếu, nó sẽ ảnh hướng xấu trong việc bảo quản nông sản.");
        } else {
            textView.setText("Ánh sáng ở mức độ ổn định, phù hợp cho việc bảo quản nông sản.");
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

