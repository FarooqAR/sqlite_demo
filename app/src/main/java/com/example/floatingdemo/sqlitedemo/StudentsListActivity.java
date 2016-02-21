package com.example.floatingdemo.sqlitedemo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class StudentsListActivity extends AppCompatActivity {
    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);
        mListView  = (ListView) findViewById(R.id.list);
        SqliteHelper helper = SqliteHelper.getHelper(this);
        helper.getAllStudents(new SqliteHelper.CompletionListener() {
            @Override
            public void onInserted(Long result) {

            }

            @Override
            public void onRetrieved(ArrayList<Student> students) {
                CustomListAdapter adapter = new CustomListAdapter(getApplicationContext(),R.layout.student_list_item,students);
                mListView.setAdapter(adapter);
            }
        });
    }
    public class CustomListAdapter extends ArrayAdapter<Student>{
        ArrayList<Student> students;
        public CustomListAdapter(Context context, int resource, List<Student> objects) {
            super(context, resource, objects);
            students = (ArrayList<Student>) objects;
        }

        @Override
        public int getCount() {
            return students.size();
        }

        @Override
        public Student getItem(int position) {
            return students.get(position);
        }

        @Override
        public int getPosition(Student item) {
            return students.indexOf(item);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.student_list_item,null,false);
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.uni = (TextView) convertView.findViewById(R.id.uni);
                holder.age = (TextView) convertView.findViewById(R.id.age);
                convertView.setTag(holder);
            }
            Student student = students.get(position);
            holder.name.setText(student.getName());
            holder.uni.setText(student.getUni());
            holder.age.setText(student.getAge());
            return convertView;
        }
        public class ViewHolder{
            public TextView name;
            public TextView uni;
            public TextView age;
        }
    }

}
