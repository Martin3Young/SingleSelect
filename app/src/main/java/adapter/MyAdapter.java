package adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.martin.singleselect.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Martin on 2016/12/7.
 */

public class MyAdapter extends BaseAdapter{
    Context context;
    ArrayList<String> list;
    private LayoutInflater inflater;
    HashMap<String,Boolean> states=new HashMap<String,Boolean>();//用于记录每个RadioButton的状态，并保证只可选一个
    HashMap<Integer, String> textChange = new HashMap<Integer, String>(); //储存已改变的选项数据

    OnRadioButtonSelect mOnRadioButtonSelect;
    boolean isVisiable = true;

    public MyAdapter(Context context,ArrayList list,OnRadioButtonSelect mOnRadioButtonSelect){
        this.context = context;
        this.list = list;
        this.mOnRadioButtonSelect=mOnRadioButtonSelect;
        inflater = LayoutInflater.from(context);
    }

    public void refresh(ArrayList<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }




    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
//        if(convertView==null){
//            holder = new Holder();
//            convertView = inflater.inflate(R.layout.item_listview, null);
//            holder.advice = (EditText) convertView.findViewById(R.id.tv_advice);
//            holder.raButton = (RadioButton) convertView.findViewById(R.id.radioButton);
//            convertView.setTag(holder);
//        }else{
//            holder = (Holder) convertView.getTag();
//        }
        holder = new Holder();
        convertView = inflater.inflate(R.layout.item_listview, null);
        holder.advice = (EditText) convertView.findViewById(R.id.tv_advice);
        holder.raButton = (RadioButton) convertView.findViewById(R.id.radioButton);

        convertView.setTag(holder);
        holder.advice.setText(list.get(position));
        if (isVisiable) {
            holder.advice.setFocusable(false);
            holder.raButton.setVisibility(View.VISIBLE);
//            holder.raButton.setEnabled(true);
            System.out.println("...setFocusable:" + isVisiable);
        } else {
            holder.advice.setFocusable(true);
            holder.raButton.setVisibility(View.INVISIBLE);
//            holder.raButton.setEnabled(false);
            System.out.println("...setFocusable:" + isVisiable);
        }


        final RadioButton raButton = (RadioButton) convertView.findViewById(R.id.radioButton);
        holder.raButton = raButton;

        holder.advice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                textChange.put(position, editable.toString());
            }
        });


        //当RadioButton被选中时，将其状态记录进States中，并更新其他RadioButton的状态使它们不被选中
        holder.raButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //Toast.makeText(context, list.get(position), Toast.LENGTH_LONG).show();
                //重置，确保最多只有一项被选中
                for(String key:states.keySet()){
                    states.put(key, false);
                }
                //states.put(String.valueOf(position), raButton.isChecked());
                states.put(list.get(position), raButton.isChecked());
                MyAdapter.this.notifyDataSetChanged();
                if(mOnRadioButtonSelect!=null){
                    mOnRadioButtonSelect.onSelect(list.get(position).toString());
                }
            }
        });

        boolean res=false;
        if(states.get(list.get(position)) == null || states.get(list.get(position))== false){
            res=false;
            states.put(list.get(position), false);
        } else
            res = true;

        holder.raButton.setChecked(res);


        return convertView;
    }




    //----------获取选中的radio的值-----------
    public String getValue() {
        String value = "";
        Iterator iter = states.entrySet().iterator();
        while (iter.hasNext()){
            Map.Entry entry = (Map.Entry)iter.next();//找到所有key-value对集合
            if(entry.getValue().equals(true)) {//通过判断是否有该value值
                value = (String) entry.getKey();//取得key值
            }
        }

        return value;
    }




    protected class Holder{
        EditText advice;
        RadioButton raButton;
    }

    //通过传入的boolean控制控件的状态
    public void Change(boolean click){
        isVisiable = click;
        notifyDataSetChanged();
        System.out.println("...isVisiable:" + isVisiable);

    }

    //返回已改变的选项数据
    public HashMap<Integer, String> getTextChange(){
        return textChange;
    }

    //获取字符串借口
    public interface OnRadioButtonSelect{
        void onSelect(String s);
    }
}
