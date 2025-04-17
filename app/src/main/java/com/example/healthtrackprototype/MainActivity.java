package com.example.healthtrackprototype;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.*;

public class MainActivity extends AppCompatActivity {
    private int touchCounter = 0;
    List<DiseaseData> diseases = Arrays.asList(
            new DiseaseData("Influenza", Arrays.asList("Fever", "Cough", "Sore Throat", "Muscle Aches")),
            new DiseaseData("Common Cold", Arrays.asList("Runny Nose", "Congestion", "Sneezing", "Sore Throat")),
            new DiseaseData("COVID-19", Arrays.asList("Fever", "Cough", "Shortness of Breath", "Loss of Taste", "Fatigue")),
            new DiseaseData("Pneumonia", Arrays.asList("Chest Pain", "Cough", "Fever", "Shortness of Breath")),
            new DiseaseData("Asthma", Arrays.asList("Wheezing", "Shortness of Breath", "Chest Tightness")),
            new DiseaseData("Diabetes", Arrays.asList("Increased Thirst", "Frequent Urination", "Fatigue", "Blurred Vision")),
            new DiseaseData("Hypertension", Arrays.asList("Headache", "Dizziness", "Blurred Vision", "Fatigue")),
            new DiseaseData("Migraine", Arrays.asList("Headache", "Nausea", "Sensitivity to Light", "Visual Disturbances")),
            new DiseaseData("Arthritis", Arrays.asList("Joint Pain", "Swelling", "Stiffness", "Redness")),
            new DiseaseData("Appendicitis", Arrays.asList("Abdominal Pain", "Nausea", "Vomiting", "Loss of Appetite")),
            new DiseaseData("Hepatitis", Arrays.asList("Fatigue", "Jaundice", "Abdominal Pain", "Dark Urine")),
            new DiseaseData("Malaria", Arrays.asList("Fever", "Chills", "Sweating", "Muscle Pain")),
            new DiseaseData("Tuberculosis", Arrays.asList("Cough", "Weight Loss", "Fever", "Night Sweats")),
            new DiseaseData("Lyme Disease", Arrays.asList("Rash", "Fever", "Fatigue", "Muscle Pain")),
            new DiseaseData("Strep Throat", Arrays.asList("Sore Throat", "Fever", "Swollen Lymph Nodes", "Headache")),
            new DiseaseData("Chickenpox", Arrays.asList("Fever", "Rash", "Itching", "Fatigue")),
            new DiseaseData("Bronchitis", Arrays.asList("Cough", "Chest Discomfort", "Shortness of Breath", "Mucus Production")),
            new DiseaseData("Gastroenteritis", Arrays.asList("Nausea", "Vomiting", "Diarrhea", "Abdominal Pain")),
            new DiseaseData("Anemia", Arrays.asList("Fatigue", "Weakness", "Pale Skin", "Shortness of Breath")),
            new DiseaseData("Sinusitis", Arrays.asList("Nasal Congestion", "Headache", "Facial Pain", "Runny Nose"))
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Spinner symptoms = findViewById(R.id.symptoms);
        ListView selectedSymptomsList = findViewById(R.id.selectedSymptomsList);
        ArrayList<String> selectedSymptoms = new ArrayList<>();
        String[] options = {"Fever", "Cough", "Sore Throat", "Muscle Aches", "Runny Nose", "Congestion", "Sneezing",
                  "Shortness of Breath", "Loss of Taste", "Fatigue", "Chest Pain",
                "Wheezing", "Chest Tightness","Increased Thirst", "Frequent Urination", "Blurred Vision",
                "Headache", "Dizziness", "Nausea", "Sensitivity to Light", "Visual Disturbances",
                "Joint Pain", "Swelling", "Stiffness", "Redness", "Abdominal Pain", "Vomiting", "Loss of Appetite",
                 "Jaundice", "Dark Urine", "Chills", "Sweating", "Muscle Pain",
                 "Weight Loss", "Night Sweats", "Rash", "Swollen Lymph Nodes",
                  "Itching",  "Chest Discomfort", "Mucus Production", "Diarrhea",
                 "Weakness", "Pale Skin",
                "Nasal Congestion", "Facial Pain"
        };
        Arrays.sort(options);

        ArrayAdapter<String> selectAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, options);
        selectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        symptoms.setAdapter(selectAdapter);
        ArrayAdapter<String> selectedSymptomsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, selectedSymptoms);
        selectedSymptomsList.setAdapter(selectedSymptomsAdapter);
        symptoms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!selectedSymptoms.contains((String)symptoms.getSelectedItem()) && touchCounter > 0) {
                    String item = (String) symptoms.getSelectedItem();
                    selectedSymptoms.add(item);
                    selectedSymptomsAdapter.notifyDataSetChanged();
                }else{
                    touchCounter++;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        selectedSymptomsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(selectedSymptoms);
                selectedSymptoms.remove(position);
                selectedSymptomsAdapter.notifyDataSetChanged();
            }
        });
        Button submit = findViewById(R.id.submitSymptomsButton);
         ListView output = findViewById(R.id.diseaseOutput);
         ArrayList<String> predictions = new ArrayList<>();
        ArrayAdapter<String> outputAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, predictions);
        output.setAdapter(outputAdapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                predictions.clear();
               List<Prediction> results = predict(selectedSymptoms, diseases);
               for (Prediction p: results){
                   predictions.add(p.toString());

               }
               outputAdapter.notifyDataSetChanged();


            }
        });
    }
    static class DiseaseData {
        String name;
        List<String> symptoms;

        DiseaseData(String name, List<String> symptoms) {
            this.name = name;
            this.symptoms = symptoms;
        }
    }
    static class Prediction {
        String disease;
        double likelihood;

        Prediction(String disease, double likelihood) {
            this.disease = disease;
            this.likelihood = likelihood;
        }

        @Override
        public String toString() {
            return String.format("%s %.2f%%", disease, likelihood);
        }
    }
    public static List<Prediction> predict(List<String> inputSymptoms, List<DiseaseData> diseaseList) {
        List<Prediction> predictions = new ArrayList<>();

        for (DiseaseData disease : diseaseList) {
            int matchCount = 0;
            for (String symptom : inputSymptoms) {
                if (disease.symptoms.contains(symptom)) {
                    matchCount++;
                }
            }

            if (matchCount > 0) {
                double likelihood = (matchCount * 100.0) / disease.symptoms.size();
                predictions.add(new Prediction(disease.name, likelihood));
            }
        }

        // Sort by likelihood descending
        predictions.sort((a, b) -> Double.compare(b.likelihood, a.likelihood));
        return predictions;
    }

}