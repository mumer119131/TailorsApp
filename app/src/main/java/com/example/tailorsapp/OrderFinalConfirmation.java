package com.example.tailorsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tailorsapp.Database.DatabaseHelper;
import com.example.tailorsapp.Database.OrderDataBaseHelper;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class OrderFinalConfirmation extends AppCompatActivity {
    private String id,str_deliveryDate;
    private String currentDate;
    Date deliveryDate;
    private TextInputEditText price,type,furtherDetails;
    private TextView deliveryDateTV;
    private Button datePickerBtn,confirmOrder;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_final_confirmation);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            id=bundle.getString("clientID","");
        }

        //Getting all Elements

        price = findViewById(R.id.priceET);
        type = findViewById(R.id.typeET);
        furtherDetails = findViewById(R.id.futherDetailsTV);
        deliveryDateTV = findViewById(R.id.deliveryDateTV);
        datePickerBtn = findViewById(R.id.btnTimePicker);
        confirmOrder = findViewById(R.id.btnConfirmOrder);

        //Adding Date Picker Material Design Functionality

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("SELECT DELIVERY DATE");
        final MaterialDatePicker<Long> materialDatePicker = builder.build();
        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(),"MATERIAL_DATE_PICKER");
            }
        });
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                deliveryDateTV.setText(materialDatePicker.getHeaderText());
                TimeZone timeZoneUTC = TimeZone.getDefault();
                int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
                SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                Date date = new Date(selection + offsetFromUTC);
                deliveryDateTV.setText(simpleFormat.format(date));
                deliveryDate = date;
                str_deliveryDate = simpleFormat.format(date);
            }
        });

        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidationAndPushData();
            }
        });



    }

    private void ValidationAndPushData() {
        String name="";
        String str_price = price.getText().toString().trim();
        String str_type = type.getText().toString().trim();
        String str_furtherDetails= furtherDetails.getText().toString().trim();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        currentDate = sdf.format(new Date());

        if(TextUtils.isEmpty(str_price)){
            price.setError("Enter the Price");
            price.requestFocus();
            return;
        }else if(TextUtils.isEmpty(str_deliveryDate)){
            datePickerBtn.setError("Select the Date");
            datePickerBtn.requestFocus();
            return;
        }else{
            //GETTING CUSTOMER NAME
            databaseHelper = new DatabaseHelper(this);
            Cursor cursor = databaseHelper.getDatabyID(Integer.parseInt(id));
            if(cursor.moveToFirst()){
                name = cursor.getString(1);
                cursor.close();
            }

            //INSERTING DATA TO SQL DATABASE
            OrderDataBaseHelper helper = new OrderDataBaseHelper(this);
            helper.InsertDataIntoTable(Integer.parseInt(id),name,str_price,str_type,currentDate,str_deliveryDate,"Pending",str_furtherDetails);
            Toast.makeText(this, "Order Added", Toast.LENGTH_SHORT).show();
            finish();
        }





    }

}