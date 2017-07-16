package com.example.martin.singleselect;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import adapter.MyAdapter;

public class MainActivity extends AppCompatActivity {

    public Button bt_show_select;
    public EditText et_answer;
    ArrayList<String> selectList = new ArrayList<String>();   //存放选项内容数据
    HashMap<Integer, String> textChange = new HashMap<Integer, String>();    //存放修改的选项
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        bt_show_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectView();
            }
        });

    }
    public void init(){
        bt_show_select = (Button) findViewById(R.id.bt_show_select);
        et_answer = (EditText)findViewById(R.id.et_answer);
        //----------选项赋值--------
        for(int i=0;i<5;i++){
            selectList.add("选项"+i);
        }
    }

    public void showSelectView(){
        AlertDialog.Builder builder;
        final AlertDialog alertDialog;//新建弹出窗
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);//获取到LayoutInflater的实例
        View layout = inflater.inflate(R.layout.activity_select_view,null);//加载布局
        final ListView myListView = (ListView) layout.findViewById(R.id.mylistview);
        final Button bt_edit = (Button) layout.findViewById(R.id.bt_edit);
        builder = new AlertDialog.Builder(this);
        builder.setView(layout);
        alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        final MyAdapter adapter = new MyAdapter(this, selectList, new MyAdapter.OnRadioButtonSelect() {
            @Override
            public void onSelect(String s) {
                et_answer.setText(s);
                alertDialog.cancel();
            }
        });
        myListView.setAdapter(adapter);


        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bt_edit.getText().toString().equals("编辑")){
                    bt_edit.setText("完成");
                    adapter.Change(false);
                }else if(bt_edit.getText().toString().equals("完成")) {
                    bt_edit.setText("编辑");
                    adapter.Change(true);
                    //--------关闭软键盘---------
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    textChange = adapter.getTextChange();
//                    System.out.print("...textChange:" + textChange);
                    Set<Integer> key = textChange.keySet();
                    Iterator<Integer> it = key.iterator();
                    while (it.hasNext()) {
                        int str = it.next();
                        selectList.set(str,textChange.get(str));
                    }
                    adapter.refresh(selectList);
//                    System.out.print("...advice:" + adviceList);
                }
            }
        });

    }

}
