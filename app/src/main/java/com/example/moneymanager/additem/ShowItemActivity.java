package com.example.moneymanager.additem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymanager.R;
import com.example.moneymanager.main.MainActivity;
import com.example.moneymanager.models.HistoryChild;
import com.example.moneymanager.models.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ShowItemActivity extends AppCompatActivity implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener {
    private RecyclerView recyclerItems;
    private ShowItemsAdapter mAdapter;


    private ImageView btnBack;
    private NiceSpinner spinner;

    private LinearLayout numberKb;

    private LinearLayout keyboard, bgIcon;
    private TextView amount;
    private EditText edName;
    private ImageView icon;
    private Button num0, num1, num2, num3, num4, num5, num6, num7, num8, num9;
    private Button minus, plus, today, dot;
    private ImageButton backspace, result;
    private static String AMOUNT = "0";

    private Calendar now = Calendar.getInstance();;
    private DatabaseReference mDatabase;
    private static String uID = "0945455387test";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item);
        getSupportActionBar().hide();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnBack = findViewById(R.id.btnBack);
        recyclerItems = findViewById(R.id.recycleItems);
        spinner = findViewById(R.id.spinner);
        keyboard = findViewById(R.id.keyboard);
        bgIcon = findViewById(R.id.bgIcon);
        numberKb = findViewById(R.id.numberKb);

        icon = findViewById(R.id.icon);
        icon.setTag(R.string.key, "food");

        amount = findViewById(R.id.amount);
        amount.setOnClickListener(this);
        edName = findViewById(R.id.edName);
        edName.setOnClickListener(this);

        setUpKeyboard();

        showItems(recyclerItems, "expense");
        List<String> dataset = new LinkedList<>(Arrays.asList("Expense", "Income"));
        spinner.attachDataSource(dataset);
        spinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                if(position == 0){
                    showItems(recyclerItems, "expense");
                }else{
                    showItems(recyclerItems, "income");
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(keyboard.getVisibility() == View.VISIBLE){
            keyboard.setVisibility(View.INVISIBLE);
        }
        else {
            super.onBackPressed();
        }
    }

    private void showItems(final RecyclerView recyclerItems, final String category){
        final ArrayList<Item>mListItem = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 4);
        recyclerItems.setLayoutManager(layoutManager);
        recyclerItems.setItemAnimator(new DefaultItemAnimator());
        mDatabase.child("categories").child(uID).child(category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    mListItem.add(new Item((String) snapshot.getValue(), snapshot.getKey()));
                }
                if(category.equals("expense")) {
                    mListItem.add(new Item("add", "Add", true));
                }else{
                    mListItem.add(new Item("add", "Add", false));
                }
                mAdapter = new ShowItemsAdapter(ShowItemActivity.this, mListItem, icon, keyboard, bgIcon);
                mAdapter.notifyDataSetChanged();
                recyclerItems.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    /**
     * Keyboard
     */
    private void setUpKeyboard(){

        //Todo: Number press
        num0 = findViewById(R.id.num0);
        num0.setOnClickListener(this);
        num1 = findViewById(R.id.num1);
        num1.setOnClickListener(this);
        num2 = findViewById(R.id.num2);
        num2.setOnClickListener(this);
        num3 = findViewById(R.id.num3);
        num3.setOnClickListener(this);
        num4 = findViewById(R.id.num4);
        num4.setOnClickListener(this);
        num5 = findViewById(R.id.num5);
        num5.setOnClickListener(this);
        num6 = findViewById(R.id.num6);
        num6.setOnClickListener(this);
        num7 = findViewById(R.id.num7);
        num7.setOnClickListener(this);
        num8 = findViewById(R.id.num8);
        num8.setOnClickListener(this);
        num9 = findViewById(R.id.num9);
        num9.setOnClickListener(this);

        //Todo:
        today = findViewById(R.id.today);
        today.setText("Today\n"+now.get(Calendar.DAY_OF_MONTH)+"/"+now.get(Calendar.MONTH));
        today.setOnClickListener(this);
        minus = findViewById(R.id.minus);
        minus.setOnClickListener(this);
        plus  = findViewById(R.id.plus);
        plus.setOnClickListener(this);
        dot = findViewById(R.id.dot);
        dot.setOnClickListener(this);
        backspace = findViewById(R.id.backspace);
        backspace.setOnClickListener(this);
        result = findViewById(R.id.result);
        result.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edName:
                numberKb.setVisibility(View.GONE);
            case R.id.num0:
                AMOUNT = amount.getText().toString();
                if(AMOUNT.charAt(0) != '0'){
                    AMOUNT += "0";
                    amount.setText(AMOUNT);
                }
                break;
            case R.id.num1:
                AMOUNT = amount.getText().toString();
                if(AMOUNT.length() == 1 && AMOUNT.charAt(0) == '0'){
                    AMOUNT = "1";
                    amount.setText(AMOUNT);
                }
                else{
                    AMOUNT += "1";
                    amount.setText(AMOUNT);
                }
                break;
            case R.id.num2:
                AMOUNT = amount.getText().toString();
                if(AMOUNT.length() == 1 && AMOUNT.charAt(0) == '0'){
                    AMOUNT = "2";
                    amount.setText(AMOUNT);
                }
                else{
                    AMOUNT += "2";
                    amount.setText(AMOUNT);
                }
                break;
            case R.id.num3:
                AMOUNT = amount.getText().toString();
                if(AMOUNT.length() == 1 && AMOUNT.charAt(0) == '0'){
                    AMOUNT = "3";
                    amount.setText(AMOUNT);
                }
                else{
                    AMOUNT += "3";
                    amount.setText(AMOUNT);
                }
                break;
            case R.id.num4:
                AMOUNT = amount.getText().toString();
                if(amount.length() == 1 && amount.getText().toString().charAt(0) == '0'){
                    AMOUNT = "4";
                    amount.setText(AMOUNT);
                }
                else{
                    AMOUNT += "4";
                    amount.setText(AMOUNT);
                }
                break;
            case R.id.num5:
                AMOUNT = amount.getText().toString();
                if(amount.length() == 1 && amount.getText().toString().charAt(0) == '0'){
                    AMOUNT = "5";
                    amount.setText(AMOUNT);
                }
                else{
                    AMOUNT += "5";
                    amount.setText(AMOUNT);
                }
                break;
            case R.id.num6:
                AMOUNT = amount.getText().toString();
                if(amount.length() == 1 && amount.getText().toString().charAt(0) == '0'){
                    AMOUNT = "6";
                    amount.setText(AMOUNT);
                }
                else{
                    AMOUNT += "6";
                    amount.setText(AMOUNT);
                }
                break;
            case R.id.num7:
                AMOUNT = amount.getText().toString();
                if(amount.length() == 1 && amount.getText().toString().charAt(0) == '0'){
                    AMOUNT = "7";
                    amount.setText(AMOUNT);
                }
                else{
                    AMOUNT += "7";
                    amount.setText(AMOUNT);
                }
                break;
            case R.id.num8:
                AMOUNT = amount.getText().toString();
                if(amount.length() == 1 && amount.getText().toString().charAt(0) == '0'){
                    AMOUNT = "8";
                    amount.setText(AMOUNT);
                }
                else{
                    AMOUNT += "8";
                    amount.setText(AMOUNT);
                }
                break;
            case R.id.num9:
                AMOUNT = amount.getText().toString();
                if(amount.length() == 1 && amount.getText().toString().charAt(0) == '0'){
                    AMOUNT = "9";
                    amount.setText(AMOUNT);
                }
                else{
                    AMOUNT += "9";
                    amount.setText(AMOUNT);
                }
                break;

            case R.id.dot:
                AMOUNT = amount.getText().toString();
                AMOUNT += ".";
                amount.setText(AMOUNT);
                break;
            case R.id.plus:
                AMOUNT = amount.getText().toString();
                try {
                    if(!AMOUNT.contains("+") && !AMOUNT.contains("-")) {
                        AMOUNT += "+";
                        amount.setText(AMOUNT);
                    }
                    else if(AMOUNT.contains("+")){
                        String number1 = AMOUNT.substring(0, AMOUNT.indexOf("+"));
                        String number2 = AMOUNT.substring(AMOUNT.indexOf("+")+1);
                        Double num1 = Double.parseDouble(number1);
                        Double num2 = Double.parseDouble(number2);
                        amount.setText(String.valueOf(num1+num2)+"+");
                    }else if(AMOUNT.contains("-")){
                        String number1 = AMOUNT.substring(0, AMOUNT.indexOf("-"));
                        String number2 = AMOUNT.substring(AMOUNT.indexOf("-")+1);
                        Double num1 = Double.parseDouble(number1);
                        Double num2 = Double.parseDouble(number2);
                        amount.setText(String.valueOf(num1-num2)+"+");
                    }
                }catch (Exception e){
                    System.out.println("Number Format");
                }

                break;
            case R.id.minus:
                AMOUNT = amount.getText().toString();
                try {
                    if(!AMOUNT.contains("-") && !AMOUNT.contains("+")) {
                        AMOUNT += "-";
                        amount.setText(AMOUNT);
                    }
                    else if(AMOUNT.contains("-")){
                        String number1 = AMOUNT.substring(0, AMOUNT.indexOf("-"));
                        String number2 = AMOUNT.substring(AMOUNT.indexOf("-")+1);
                        Double num1 = Double.parseDouble(number1);
                        Double num2 = Double.parseDouble(number2);
                        amount.setText(String.valueOf(num1-num2)+"-");
                    } else if(AMOUNT.contains("+")){
                        String number1 = AMOUNT.substring(0, AMOUNT.indexOf("+"));
                        String number2 = AMOUNT.substring(AMOUNT.indexOf("+")+1);
                        Double num1 = Double.parseDouble(number1);
                        Double num2 = Double.parseDouble(number2);
                        amount.setText(String.valueOf(num1+num2)+"-");
                    }

                }catch (Exception e){
                    System.out.println("Number Format");
                }
                break;
            case R.id.today:
                AMOUNT = amount.getText().toString();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        ShowItemActivity.this,
                        now.get(Calendar.YEAR), // Initial year selection
                        now.get(Calendar.MONTH), // Initial month selection
                        now.get(Calendar.DAY_OF_MONTH) // Inital day selection
                );
                dpd.show(getSupportFragmentManager(), "DatePicker");
                break;
            case R.id.backspace:
                AMOUNT = amount.getText().toString();
                if(AMOUNT.length() == 1){
                amount.setText("0");
                }
                else if(AMOUNT.length()!=0) {
                    AMOUNT = AMOUNT.substring(0, AMOUNT.length() - 1);
                    amount.setText(AMOUNT);
                }
                break;
            case R.id.result:
//                try {
//                    if(AMOUNT.contains("+")){
//                        String number1 = AMOUNT.substring(0, AMOUNT.indexOf("+"));
//                        String number2 = AMOUNT.substring(AMOUNT.indexOf("+")+1);
//                        Double num1 = Double.parseDouble(number1);
//                        Double num2 = Double.parseDouble(number2);
//                        amount.setText(String.valueOf(num1+num2));
//                    }
//                    else if(AMOUNT.contains("-")){
//                        String number1 = AMOUNT.substring(0, AMOUNT.indexOf("-"));
//                        String number2 = AMOUNT.substring(AMOUNT.indexOf("-")+1);
//                        Double num1 = Double.parseDouble(number1);
//                        Double num2 = Double.parseDouble(number2);
//                        amount.setText(String.valueOf(num1-num2));
//                    }
//                }catch (NumberFormatException e){
//                    System.out.println("Number Format");
//                }

                if(Long.parseLong(amount.getText().toString().trim()) != 0) {
                    long timestamp = System.currentTimeMillis();
                    convertTimes(timestamp);
                    String month_year = convertTimes(timestamp);
                    String type = (String) icon.getTag(R.string.key);
                    String category = spinner.getText().toString().toLowerCase();
                    String name = edName.getText().toString().trim();
                    long _amount = Long.parseLong(amount.getText().toString().trim());
                    HistoryChild his = new HistoryChild(category, type, name, _amount);
                    mDatabase.child("histories").child(uID).child(month_year).child(String.valueOf(timestamp)).setValue(his);
                    Intent intent = new Intent(ShowItemActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    private String  convertTimes (long timestamp){
        Date date = new java.util.Date(timestamp);

        SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM-yyyy");
        String formattedDate = sdf.format(date);
        System.out.println(formattedDate);

        return formattedDate;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        today.setText("Today\n"+dayOfMonth+"/"+monthOfYear);
    }


}