package com.example.kalkulatorbmiapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText editTextWeight, editTextHeight;
    Button buttonCalculate;
    TextView textViewResult, textViewIdealWeight, textViewCategory;
    Spinner spinnerGender;
    String selectedGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextWeight = findViewById(R.id.editTextWeight);
        editTextHeight = findViewById(R.id.editTextHeight);
        buttonCalculate = findViewById(R.id.buttonCalculate);
        textViewResult = findViewById(R.id.textViewResult);
        textViewIdealWeight = findViewById(R.id.textViewIdealWeight);
        textViewCategory = findViewById(R.id.textViewCategory);
        spinnerGender = findViewById(R.id.spinnerGender);

        // Populate the gender spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);

        // Set the selected gender when spinner item is selected
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGender = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBMI();
            }
        });
    }

    private void calculateBMI() {
        String weightStr = editTextWeight.getText().toString();
        String heightStr = editTextHeight.getText().toString();

        if (weightStr.isEmpty() || heightStr.isEmpty()) {
            Toast.makeText(MainActivity.this, "Please enter both weight and height!", Toast.LENGTH_SHORT).show();
            return;
        }

        float weight = Float.parseFloat(weightStr);
        float height = Float.parseFloat(heightStr) / 100; // Convert height from cm to m

        float bmi = weight / (height * height);

        textViewResult.setText("Your BMI: " + bmi);

        // Calculate ideal weight for different age groups and genders
        float idealWeightSD = calculateIdealWeight(bmi, 2);
        float idealWeightSMP = calculateIdealWeight(bmi, 1);
        float idealWeightSMA = calculateIdealWeight(bmi, 0.5f);

        textViewIdealWeight.setText("Ideal weight for:");
        textViewIdealWeight.append("\n- Anak SD: " + idealWeightSD + " kg");
        textViewIdealWeight.append("\n- Anak SMP: " + idealWeightSMP + " kg");
        textViewIdealWeight.append("\n- Anak SMA: " + idealWeightSMA + " kg");

        // Determine BMI category
        String category = getCategory(bmi);
        textViewCategory.setText("BMI Category: " + category);
    }

    private float calculateIdealWeight(float bmi, float adjustment) {
        // Calculate ideal weight based on gender
        if (selectedGender.equals("Male")) {
            return bmi - adjustment;
        } else if (selectedGender.equals("Female")) {
            return bmi - (adjustment + 1); // Female's ideal weight is 1 kg lower
        }
        return 0;
    }

    private String getCategory(float bmi) {
        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi >= 18.5 && bmi < 25) {
            return "Normal Weight";
        } else {
            return "Overweight";
        }
    }
}
