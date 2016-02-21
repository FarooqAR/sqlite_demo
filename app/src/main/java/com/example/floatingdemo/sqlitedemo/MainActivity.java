package com.example.floatingdemo.sqlitedemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText mName;
    EditText mUni;
    EditText mAge;
    Button mSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mName = (EditText) findViewById(R.id.name);
        mUni= (EditText) findViewById(R.id.university);
        mAge= (EditText) findViewById(R.id.age);
        mSubmit = (Button) findViewById(R.id.submit);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mName.getText().toString();
                String uni = mUni.getText().toString();
                String age = mAge.getText().toString();
                if(!name.equals("") && !uni.equals("") && !age.equals("")){
                    SqliteHelper helper = SqliteHelper.getHelper(MainActivity.this);
                    helper.createStudent(name, uni, age, new SqliteHelper.CompletionListener() {
                        @Override
                        public void onInserted(Long result) {
                            if(result != -1){
                                Toast.makeText(MainActivity.this,"Student Inserted",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onRetrieved(ArrayList<Student> students) {
                            //leave it empty
                        }
                    });
                }
            }
        });
        ((Button) findViewById(R.id.show)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,StudentsListActivity.class);
                startActivity(intent);
            }
        });
    }
}
