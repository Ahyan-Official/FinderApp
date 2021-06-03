package com.visualsearch.finder.notification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.visualsearch.finder.Adapter.NotificationAdapter;
import com.visualsearch.finder.Model.Notification;
import com.visualsearch.finder.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NotificationActivity extends AppCompatActivity
{
    ImageView back;

    List<Notification> notificationList;
    NotificationAdapter notificationAdapter;
    RecyclerView recyclerView;
    ImageView settings;
    String alerttext="Select your sleeping hours\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        recyclerView = findViewById(R.id.notification_recycler);
        settings = findViewById(R.id.settings);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setReverseLayout(true);
//        recyclerView.setStackFromEnd(true);

        notificationList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(this, notificationList);
        recyclerView.setAdapter(notificationAdapter);

        back = findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(NotificationActivity.this).create();
                alertDialog.setTitle("Notification Settings");
                alertDialog.setMessage(alerttext);

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "1st Interval", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar mcurrentTime = Calendar.getInstance();
                        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        int minute = mcurrentTime.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(NotificationActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                //eReminderTime.setText( selectedHour + ":" + selectedMinute);
                                alerttext= alerttext.concat("1st Interval "+selectedHour + ":" + selectedMinute+"\n");

                                alertDialog.setMessage(alerttext);

                                alertDialog.show();

                            }
                        }, hour, minute, true);//Yes 24 hour time
                        mTimePicker.setTitle("Select Time");
                        mTimePicker.show();                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "2nd Interval", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Calendar mcurrentTime = Calendar.getInstance();
                        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        int minute = mcurrentTime.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(NotificationActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                //eReminderTime.setText( selectedHour + ":" + selectedMinute);
                                alerttext= alerttext.concat("2nd Interval "+selectedHour + ":" + selectedMinute+"\n");
                                alertDialog.setMessage(alerttext);

                                alertDialog.show();

                            }
                        }, hour, minute, true);//Yes 24 hour time
                        mTimePicker.setTitle("Select Time");
                        mTimePicker.show();
                    }
                });
                alertDialog.show();




            }
        });

        getNotification();
    }

    private void getNotification()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Notifications");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notificationList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Notification notification = dataSnapshot.getValue(Notification.class);
                    notificationList.add(notification);
                }
                notificationAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
