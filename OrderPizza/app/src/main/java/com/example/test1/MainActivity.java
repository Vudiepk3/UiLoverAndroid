package com.example.test1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import android.widget.Toast;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText editName,editPhone,editDate;
    Button btnDate,btnSubmit;
    RadioGroup rdgCheese,rdgShape;
    CheckBox chkP,chkM,chkV,chkA;
    ListView lv;
    RadioButton rdbCheese,rdbShape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWidget();
        app();
        rdbShape.setChecked(true);
        rdbCheese.setChecked(true);
    }

    private void app() {
        ArrayList<Pizza> arr = new ArrayList<Pizza>();
        MyAdapter adapter = new MyAdapter(MainActivity.this,R.layout.customlv,arr);
        lv.setAdapter(adapter);
        SquarePizza A = new SquarePizza("name","phone","date","selectCheese","getTopping()");
        arr.add(A);
        adapter.notifyDataSetChanged();
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder pizzaOrder = new AlertDialog.Builder(MainActivity.this);
                pizzaOrder.setTitle("Pizza Order");
                pizzaOrder.setMessage(arr.get(i).toString());
                pizzaOrder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        arr.remove(i);
                        adapter.notifyDataSetChanged();
                        dialogInterface.cancel();
                    }
                });
                pizzaOrder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                pizzaOrder.create().show();
                return false;
            }
        });

        View.OnClickListener myListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view==btnDate){
                    DatePickerDialog.OnDateSetListener cb = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            editDate.setText(i2+"/"+i1+"/"+i);
                        }
                    };
                    DatePickerDialog date = new DatePickerDialog(MainActivity.this,cb,2023,10,10);
                    date.show();
                }
                if (view == btnSubmit) {
                    String name=editName.getText().toString();
                    String phone=editPhone.getText().toString();
                    String date=editDate.getText().toString();
                    int cheese = rdgCheese.getCheckedRadioButtonId();
                    int shape = rdgShape.getCheckedRadioButtonId();
                    rdbCheese=findViewById(cheese);
                    rdbShape=findViewById(shape);

                    if(name.length()==0 || phone.length()==0 || date.length()==0){
                        Toast.makeText(MainActivity.this, "Yêu cầu điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    }
                    else if(!chkA.isChecked()&&!chkM.isChecked()&&!chkP.isChecked()&&!chkV.isChecked()){
                        Toast.makeText(MainActivity.this, "Yêu cầu chọn ít nhất 1 topping", Toast.LENGTH_SHORT).show();
                        }
                    else{
                        if(rdbShape.getText().toString().equals("Square")){
                            SquarePizza x = new SquarePizza(name,phone,date,rdbCheese.getText().toString(),getTopping());
                            arr.add(x);
                            adapter.notifyDataSetChanged();
                            setDefault();
                            Uri uri = Uri.parse("smsto:0932258264");
                            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                            intent.putExtra("sms_body", "Order: " + x.toString());
                            startActivity(intent);
                            return;
                        }
                        if(rdbShape.getText().toString().equals("Round Pizza")){
                            RoundPizza x = new RoundPizza(name,phone,date,rdbCheese.getText().toString(),getTopping());
                            arr.add(x);
                            adapter.notifyDataSetChanged();
                            setDefault();
                            Uri uri = Uri.parse("smsto:0932258264");
                            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                            intent.putExtra("sms_body", "Order: " + x.toString());
                            startActivity(intent);
                            return;
                        }

                    }
                }
            }
        };
        btnDate.setOnClickListener(myListener);
        btnSubmit.setOnClickListener(myListener);
    }

    public void setDefault(){
        editName.setText("");
        editPhone.setText("");
        editDate.setText("");
        rdbCheese=findViewById(R.id.cheese);
        rdbShape= findViewById(R.id.round);
        rdbShape.setChecked(true);
        rdbCheese.setChecked(true);
        chkA.setChecked(false);
        chkV.setChecked(false);
        chkP.setChecked(false);
        chkM.setChecked(false);
        editName.requestFocus();
    }

    public String getTopping(){
        ArrayList<CheckBox> listCB = new ArrayList<CheckBox>();
        listCB.add(chkA);
        listCB.add(chkM);
        listCB.add(chkP);
        listCB.add(chkV);
        String topping="";
        for(CheckBox checkbox : listCB){
            if(checkbox.isChecked()){
                topping+=checkbox.getText().toString();
                topping+=",";
            }
        }
        return topping;
    }

    private void getWidget() {
        editName=findViewById(R.id.editName);
        editPhone=findViewById(R.id.editPhone);
        editDate=findViewById(R.id.editDate);
        btnDate=findViewById(R.id.btnDate);
        btnSubmit=findViewById(R.id.btnSubmit);
        rdgCheese=findViewById(R.id.rdgCheese);
        rdgShape=findViewById(R.id.rdgShape);
        chkA=findViewById(R.id.chkA);
        chkP=findViewById(R.id.chkP);
        chkM=findViewById(R.id.chkM);
        chkV=findViewById(R.id.chkV);
        lv=findViewById(R.id.lv);
        rdbCheese=findViewById(R.id.cheese);
        rdbShape= findViewById(R.id.round);
    }
}