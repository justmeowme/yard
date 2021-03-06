package com.example.yard.application_services;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yard.R;
import com.example.yard.adapters.PollsAdapter;
import com.example.yard.data.DataObject;
import com.example.yard.data.Poll;
import com.example.yard.utils.JSONInteractor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class MyPollsService extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        JSONInteractor jsonInteractor = new JSONInteractor(this, "data.json");
        Activity activity = this;
        try {
            DataObject data = jsonInteractor.readJSON(DataObject.class);
            setContentView(data.getMypolls().size() > 0 ? R.layout.activity_maps_service_filled : R.layout.activity_maps_service);

            if (data.getMypolls().size() == 0) {
                Button mButton = findViewById(R.id.nextButton);
                mButton.setOnClickListener(v -> {

                    //CREATE ALERT FOR CREATING POLLS
                    AlertDialog.Builder builder = new AlertDialog.Builder(MyPollsService.this);
                    View view = getLayoutInflater().inflate(R.layout.alert_create_statement, null);
                    builder.setView(view);

                    EditText formTitle = view.findViewById(R.id.title);
                    EditText formText = view.findViewById(R.id.message);
                    EditText formAddress = view.findViewById(R.id.address);

                    builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        if (formTitle.getText().toString().length() == 0 || formText.getText().toString().length() == 0 || formAddress.getText().toString().length() == 0) {
                            Toast.makeText(this, "Нельзя оставлять поля заявки пустыми", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                ArrayList<Poll> polls = data.getPolls();
                                int newId = polls.get(polls.size() - 1).getId() + 1;
                                polls.add(new Poll(newId, 1, 0, formTitle.getText().toString(), formText.getText().toString(), formAddress.getText().toString(), "https://newtambov.ru/storage/taisia/2018/05/DSC02076.jpg"));
                                data.setPolls(polls);
                                ArrayList<Integer> my_polls = data.getMypolls();
                                my_polls.add(newId);
                                data.setMypolls(my_polls);
                                jsonInteractor.writeJSON(data);

                                activity.recreate();
                            } catch (IOException e) {
                                Log.e("Maps Service", "Error occurred while reading/writing data", e);
                            }
                        }
                    });

                    builder.setNegativeButton(android.R.string.no, null).show();
                });
            } else {
                PollsAdapter pollsAdapter = new PollsAdapter(this);
                pollsAdapter.setLockedPolls(data.getMypolls());
                pollsAdapter.updateData(data.getMyPollsObject());

                RecyclerView pollsView = findViewById(R.id.polls_view);
                pollsView.setAdapter(pollsAdapter);
                pollsView.setItemAnimator(null);
                pollsView.setLayoutManager(new LinearLayoutManager(this));

                Button mButton = findViewById(R.id.nextButton);
                mButton.setOnClickListener(v -> {
                    //CREATE ALERT
                    AlertDialog.Builder builder = new AlertDialog.Builder(MyPollsService.this);
                    View view = getLayoutInflater().inflate(R.layout.alert_create_statement, null);
                    builder.setView(view);

                    EditText formTitle = view.findViewById(R.id.title);
                    EditText formText = view.findViewById(R.id.message);
                    EditText formAddress = view.findViewById(R.id.address);

                    builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        if (formTitle.getText().toString().length() == 0 || formText.getText().toString().length() == 0 || formAddress.getText().toString().length() == 0) {
                            Toast.makeText(this, "Нельзя оставлять поля заявки пустыми", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                ArrayList<Poll> polls = data.getPolls();
                                int newId = polls.get(polls.size() - 1).getId() + 1;
                                polls.add(new Poll(newId, 1, 0, formTitle.getText().toString(), formText.getText().toString(), formAddress.getText().toString(), "https://newtambov.ru/storage/taisia/2018/05/DSC02076.jpg"));
                                data.setPolls(polls);
                                ArrayList<Integer> my_polls = data.getMypolls();
                                my_polls.add(newId);
                                data.setMypolls(my_polls);
                                jsonInteractor.writeJSON(data);
                                pollsAdapter.updateData(data.getMyPollsObject());
                            } catch (IOException e) {
                                Log.e("Maps Service", "Error occurred while reading/writing data", e);
                            }
                        }
                    });
                    builder.setNegativeButton(android.R.string.no, null).show();
                });
            }

        } catch (FileNotFoundException e) {
            Log.e("Maps Service", "Error occurred while reading/writing data", e);
        }
    }
}