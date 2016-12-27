package com.example.qxw.qxwfirst;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.qxw.app.MyApplication;
import com.example.qxw.bean.City;

import java.security.PrivateKey;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by qxw on 2016/10/18.
 */
public class selcet_city extends Activity implements View.OnClickListener {
    private ImageView mBackBtn;
    private MyApplication LVapp;//11.27
    private List<City> myCityList;//11.27
    private static final String TAG = "selectcity";
    private String s1;//用来存点击listview后的城市名
    private String s2;//城市的编码
    private EditText search_city_name;//城市搜索框12.22
    private List<City> after_search; //12.22 搜索框输入结果之后匹配的list
    private String[] data1;
    private String[] data2;
    private ArrayAdapter<String> adapter1;
    @Override
    protected void onCreate(Bundle savedIstanceState) {
        super.onCreate(savedIstanceState);
        setContentView(R.layout.select_city);
        LVapp = (MyApplication) getApplication(); //11.27
        myCityList = LVapp.getCityList();//11.27
        data1 = new String[myCityList.size()];//11.27
        data2 = new String[myCityList.size()];//11.27
        for (int i = 0; i < myCityList.size(); i++) {
            data1[i] = myCityList.get(i).getCity();
            data2[i] = myCityList.get(i).getNumber();
        }//11.27 data1数组存储着城市名称,data2数组用来存储城市的CityCode*/
        adapter1 = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, data1);//11.27
        ListView listView = (ListView) findViewById(R.id.select_city_name_bar);//11.27
        listView.setAdapter(adapter1);//11.27
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                s1 = data1[i];
                s2 = data2[i];
                Toast.makeText(selcet_city.this, "您选择了:" + s1 + "天气", Toast.LENGTH_SHORT).show();
            }
        });
        search_city_name = (EditText) findViewById(R.id.search_city_bar);//12 22
        /*
        * 添加文本变化的监听器*/
        search_city_name.addTextChangedListener(new TextWatcher() {

            private String Str1;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Str1=editable.toString();
                after_search = search(Str1, myCityList);
                for (int i = 0; i < after_search.size(); i++) {
                    data1[i] = after_search.get(i).getCity();
                    data2[i] = after_search.get(i).getNumber();
                }

                adapter1.notifyDataSetChanged();


            }

        });


        // Log.d("selectcity",data3[2]);
        // Str =  search_city_name.getText().toString();
        // Log.d("selectcity",Str);
        //Log.d("selectcity",data3[2]);

        /*
        *选择使用哪个适配器 */







          /* ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                    this, android.R.layout.simple_list_item_1, data3);
            listView.setAdapter(adapter2);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    s1 = data3[i];
                    Toast.makeText(selcet_city.this, "您选择了:" + s1 + "天气", Toast.LENGTH_SHORT).show();
                }
            });*/

        mBackBtn = (ImageView) findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.title_back:
                SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
                editor.putString("main_ city_code",s2);
                editor.commit();
                Intent i =new Intent();
                //String s3 = s2;
                //Toast.makeText(selcet_city.this, "s3 ="+ s2, Toast.LENGTH_SHORT).show();
                i.putExtra("cityCode",s2);
                setResult(RESULT_OK,i);
                finish();
                break;
            default:
                break;

        }
    }

    /*搜索显示listview功能*/
    public List search(String name,List list){
        List results = new ArrayList();
        Pattern pattern = Pattern.compile(name);
        for(int i=0; i < list.size(); i++){
            Matcher matcher = pattern.matcher(((City)list.get(i)).getCity());
            if(matcher.find()){
                results.add(((City) list.get(i)));
            }
        }
        return results;
    }

}
