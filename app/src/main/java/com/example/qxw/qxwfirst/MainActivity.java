package com.example.qxw.qxwfirst;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qxw.app.Myservice;
import com.example.qxw.bean.Next_SixDay_Weather;
import com.example.qxw.bean.TodayWeather;
import com.example.qxw.bean.ViewpageAdapter;
import com.example.qxw.util.NetUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qxw on 2016/9/21.
 */
public class MainActivity extends Activity implements View.OnClickListener,ViewPager.OnPageChangeListener {
    private static final int UPDATE_TODAY_WEATHER = 1;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPDATE_TODAY_WEATHER:
                    updateTodayWeather((TodayWeather) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };
    private ImageView mCitySelcet;
    private ImageView mUpdateBtn;
    private TextView cityTv, timeTv, humidityTv, weekTv, pmDataTv,
            pmQualityTv, temperatureTv, climateTv, windTv, city_name_Tv;

    private ImageView weatherImg, pmImg;
    private ImageView weatherImg1,weatherImg2,weatherImg3,weatherImg4,weatherImg5,weatherImg6;
    private ViewpageAdapter vpAdapter;
    private ViewPager vp;
    private List<View> views;
    private ImageView[] dots;
    private int[] ids ={R.id.iv1,R.id.iv2};
    private Next_SixDay_Weather[] Sixday_weather;//保存六日天气的数组
    private TextView Day1,Day2,Day3,Day4,Day5,Day6;
    private TextView temperature1,temperature2,temperature3,temperature4,temperature5,temperature6;
    private TextView fengli1,fengli2,fengli3,fengli4,fengli5,fengli6;
    private TextView weather1,weather2,weather3,weather4,weather5,weather6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences.Editor editor =getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putInt("tag",1);
        editor.commit();
        setContentView(R.layout.weather_info);
        mUpdateBtn = (ImageView) findViewById(R.id.title_update_btn);
        mUpdateBtn.setOnClickListener(this);

        if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
            Log.d("myWeather", "网络OK");
            Toast.makeText(MainActivity.this, "网络OK！", Toast.LENGTH_LONG).show();
        } else {
            Log.d("myWeather", "网络挂了");
            Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();

        }
        mCitySelcet = (ImageView)findViewById(R.id.title_city_manager);
        mCitySelcet.setOnClickListener(this);
        initView();
        initNext_SixDay_Weather();//初始化六日天气
        initDots();
        //Intent intent = new Intent(this, Myservice.class);
        //startService(intent);

    }
/*
* 初始化6日天气的Viewpage*/
   private void initNext_SixDay_Weather(){
        LayoutInflater inflater = LayoutInflater.from(this);
        views = new ArrayList<View>();
        views.add(inflater.inflate(R.layout.next_sixday_weather_page1,null));
        views.add(inflater.inflate(R.layout.next_sixday_weather_page2,null));
        vpAdapter = new ViewpageAdapter(views,this);
        vp = (ViewPager) findViewById(R.id.sixday_weather_pageview);
        vp.setAdapter(vpAdapter);
        vp.setOnPageChangeListener(this);

       weatherImg1 = (ImageView)views.get(0).findViewById(R.id.day1_weatherImg);
       weatherImg2 = (ImageView)views.get(0).findViewById(R.id.day2_weatherImg);
       weatherImg3 = (ImageView)views.get(0).findViewById(R.id.day3_weatherImg);
       weatherImg4 = (ImageView)views.get(1).findViewById(R.id.day4_weatherImg);
       weatherImg5 = (ImageView)views.get(1).findViewById(R.id.day5_weatherImg);
       weatherImg6 = (ImageView)views.get(1).findViewById(R.id.day6_weatherImg);

       Day1 = (TextView)views.get(0).findViewById(R.id.day1);
       Day2 = (TextView)views.get(0).findViewById(R.id.day2);
       Day3 = (TextView)views.get(0).findViewById(R.id.day3);
       Day4 = (TextView)views.get(1).findViewById(R.id.day4);
       Day5 = (TextView)views.get(1).findViewById(R.id.day5);
       Day6 = (TextView)views.get(1).findViewById(R.id.day6);

       temperature1 = (TextView)views.get(0).findViewById(R.id.day1_wendu);
       temperature2 = (TextView)views.get(0).findViewById(R.id.day2_wendu);
       temperature3 = (TextView)views.get(0).findViewById(R.id.day3_wendu);
       temperature4 = (TextView)views.get(1).findViewById(R.id.day4_wendu);
       temperature5 = (TextView)views.get(1).findViewById(R.id.day5_wendu);
       temperature6 = (TextView)views.get(1).findViewById(R.id.day6_wendu);

       weather1 = (TextView)views.get(0).findViewById(R.id.day1_weather);
       weather2 = (TextView)views.get(0).findViewById(R.id.day2_weather);
       weather3 = (TextView)views.get(0).findViewById(R.id.day3_weather);
       weather4 = (TextView)views.get(1).findViewById(R.id.day4_weather);
       weather5 = (TextView)views.get(1).findViewById(R.id.day5_weather);
       weather6 = (TextView)views.get(1).findViewById(R.id.day6_weather);

       fengli1 = (TextView)views.get(0).findViewById(R.id.day1_fengli);
       fengli2 = (TextView)views.get(0).findViewById(R.id.day2_fengli);
       fengli3 = (TextView)views.get(0).findViewById(R.id.day3_fengli);
       fengli4 = (TextView)views.get(1).findViewById(R.id.day4_fengli);
       fengli5 = (TextView)views.get(1).findViewById(R.id.day5_fengli);
       fengli6 = (TextView)views.get(1).findViewById(R.id.day6_fengli);

       Day1.setText("N/A");
       Day2.setText("N/A");
       Day3.setText("N/A");
       Day4.setText("N/A");
       Day5.setText("N/A");
       Day6.setText("N/A");

       temperature1.setText("N/A");
       temperature2.setText("N/A");
       temperature3.setText("N/A");
       temperature4.setText("N/A");
       temperature5.setText("N/A");
       temperature6.setText("N/A");

       weather1.setText("N/A");
       weather2.setText("N/A");
       weather3.setText("N/A");
       weather4.setText("N/A");
       weather5.setText("N/A");
       weather6.setText("N/A");

       fengli1.setText("N/A");
       fengli2.setText("N/A");
       fengli3.setText("N/A");
       fengli4.setText("N/A");
       fengli5.setText("N/A");
       fengli6.setText("N/A");


    }
private void initDots(){
    dots = new ImageView[views.size()];
    for (int i=0;i<views.size();i++){
        dots[i]=(ImageView) findViewById(ids[i]);

    }

}
    @Override
    public void onClick(View view) {
        String cityCode =null;
        if(view.getId() == R.id.title_city_manager){
            Intent i = new Intent(this,selcet_city.class);
            //startActivity(i);
            startActivityForResult(i,1);
        }
        if (view.getId() == R.id.title_update_btn) {
            SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
            cityCode = sharedPreferences.getString("main_ city_code", "101010100");
            Log.d("myWeather", cityCode);
            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                Log.d("myWeather", "网络OK");
                queryWeatherCode(cityCode);
            } else {
                Log.d("myWeather", "网络挂了");
                Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String newCityCode= data.getStringExtra("cityCode");
            Log.d("myWeather", "选择的城市代码为"+newCityCode);
            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                Log.d("myWeather", "网络OK");
                queryWeatherCode(newCityCode);
            } else {
                Log.d("myWeather", "网络挂了");
                Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
            }
        }
    }
    /**
     * @param cityCode
     */
    private void queryWeatherCode(String cityCode) {
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        Log.d("myWeather", address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
                TodayWeather todayWeather = null;
                try {
                    URL url = new URL(address);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while ((str = reader.readLine()) != null) {
                        response.append(str);
                        Log.d("myWeather", str);
                    }
                    String responseStr = response.toString();
                    Log.d("myWeather", responseStr);//新添加
                    todayWeather = parseXML(responseStr);
                    if (todayWeather != null) {
                        Log.d("myWeather", todayWeather.toString());
                        Message msg = new Message();
                        msg.what = UPDATE_TODAY_WEATHER;
                        msg.obj = todayWeather;
                        mHandler.sendMessage(msg);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                }
            }
        }).start();
    }


    //更新天气在updateTodayWwather中改
    void updateTodayWeather(TodayWeather todayWeather) {
        String pm25str = todayWeather.getPm25();
        String weatherim = todayWeather.getType();
        String weatherim1 = Sixday_weather[0].getWeather();
        String weatherim2 = Sixday_weather[1].getWeather();
        String weatherim3 = Sixday_weather[2].getWeather();
        String weatherim4 = Sixday_weather[3].getWeather();

        city_name_Tv.setText(todayWeather.getCity() + "天气");
        cityTv.setText(todayWeather.getCity());
        timeTv.setText(todayWeather.getUpdatetime() + "发布");
        humidityTv.setText("湿度：" + todayWeather.getShidu());
        weekTv.setText(todayWeather.getDate());
        temperatureTv.setText(todayWeather.getHigh() + "~" + todayWeather.getLow());
        climateTv.setText(todayWeather.getType());
        windTv.setText("风力:" + todayWeather.getFengli());

        Day1.setText(Sixday_weather[0].getWeekday());
        Day2.setText(Sixday_weather[1].getWeekday());
        Day3.setText(Sixday_weather[2].getWeekday());
        Day4.setText(Sixday_weather[3].getWeekday());

        temperature1.setText(Sixday_weather[0].getHigh() + "~" +Sixday_weather[0].getLow());
        temperature2.setText(Sixday_weather[1].getHigh() + "~" +Sixday_weather[1].getLow());
        temperature3.setText(Sixday_weather[2].getHigh() + "~" +Sixday_weather[2].getLow());
        temperature4.setText(Sixday_weather[3].getHigh() + "~" +Sixday_weather[3].getLow());

        weather1.setText(Sixday_weather[0].getWeather());
        weather2.setText(Sixday_weather[1].getWeather());
        weather3.setText(Sixday_weather[2].getWeather());
        weather4.setText(Sixday_weather[3].getWeather());

        fengli1.setText(Sixday_weather[0].getFengli());
        fengli2.setText(Sixday_weather[1].getFengli());
        fengli3.setText(Sixday_weather[2].getFengli());
        fengli4.setText(Sixday_weather[3].getFengli());



        if ( pm25str==null){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_0_50);
            pmDataTv.setText("0");
            pmQualityTv.setText("无");

        }else {
            pmDataTv.setText(todayWeather.getPm25());
            pmQualityTv.setText(todayWeather.getQuality());
            int pm25 =Integer.valueOf(pm25str).intValue();
            if (pm25 >= 0 && pm25 <= 50) {
                pmImg.setImageResource(R.drawable.biz_plugin_weather_0_50);
            } else if (pm25 >= 51 && pm25 <= 100) {
                pmImg.setImageResource(R.drawable.biz_plugin_weather_51_100);
            } else if (pm25 >= 101 && pm25 <= 150) {
                pmImg.setImageResource(R.drawable.biz_plugin_weather_101_150);
            } else if (pm25 >= 151 && pm25 <= 200) {
                pmImg.setImageResource(R.drawable.biz_plugin_weather_151_200);
            } else if (pm25 >= 201 && pm25 <= 300) {
                pmImg.setImageResource(R.drawable.biz_plugin_weather_201_300);
            } else if (pm25 > 300) {
                pmImg.setImageResource(R.drawable.biz_plugin_weather_greater_300);
            }
        }
            String[] wheathertype = {"晴", "沙尘暴", "大雪", "暴雪", "小雪", "中雪",
                    "阵雪", "暴雨", "大雨", "中雨", "小雨", "阵雨", "雨夹雪", "阴",
                    "雾", "特大暴雨", "雷阵雨", "雷阵雨冰雹", "多云", "大暴雨"};

            if (weatherim.equals(wheathertype[0])) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_qing);
            } else if (weatherim.equals(wheathertype[1])) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
            } else if (weatherim.equals(wheathertype[2])) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_daxue);
            } else if (weatherim.equals(wheathertype[3])) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoxue);
            } else if (weatherim.equals(wheathertype[4])) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
            } else if (weatherim.equals(wheathertype[5])) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
            } else if (weatherim.equals(wheathertype[6])) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
            } else if (weatherim.equals(wheathertype[7])) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoyu);
            } else if (weatherim.equals(wheathertype[8])) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_dayu);
            } else if (weatherim.equals(wheathertype[9])) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
            } else if (weatherim.equals(wheathertype[10])) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
            } else if (weatherim.equals(wheathertype[11])) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
            } else if (weatherim.equals(wheathertype[12])) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
            } else if (weatherim.equals(wheathertype[13])) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_yin);
            } else if (weatherim.equals(wheathertype[14])) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_wu);
            } else if (weatherim.equals(wheathertype[15])) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
            } else if (weatherim.equals(wheathertype[16])) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
            } else if (weatherim.equals(wheathertype[17])) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
            } else if (weatherim.equals(wheathertype[18])) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_duoyun);
            } else if (weatherim.equals(wheathertype[19])) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
            }

/*
* 第一天*/
        if ( weatherim1.equals(wheathertype[0])){
            weatherImg1.setImageResource(R.drawable.biz_plugin_weather_qing);
        }else if (weatherim1.equals(wheathertype[1])){
            weatherImg1.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
        }else if (weatherim1.equals(wheathertype[2])){
            weatherImg1.setImageResource(R.drawable.biz_plugin_weather_daxue);
        }else if (weatherim1.equals(wheathertype[3])){
            weatherImg1.setImageResource(R.drawable.biz_plugin_weather_baoxue);
        }else if (weatherim1.equals(wheathertype[4])){
            weatherImg1.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
        }else if (weatherim1.equals(wheathertype[5])){
            weatherImg1.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
        }else if (weatherim1.equals(wheathertype[6])){
            weatherImg1.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
        }else if (weatherim1.equals(wheathertype[7])){
            weatherImg1.setImageResource(R.drawable.biz_plugin_weather_baoyu);
        }else if (weatherim1.equals(wheathertype[8])){
            weatherImg1.setImageResource(R.drawable.biz_plugin_weather_dayu);
        }else if (weatherim1.equals(wheathertype[9])){
            weatherImg1.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
        }else if (weatherim1.equals(wheathertype[10])){
            weatherImg1.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
        }else if (weatherim1.equals(wheathertype[11])){
            weatherImg1.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
        }else if (weatherim1.equals(wheathertype[12])){
            weatherImg1.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
        }else if (weatherim1.equals(wheathertype[13])){
            weatherImg1.setImageResource(R.drawable.biz_plugin_weather_yin);
        }else if (weatherim1.equals(wheathertype[14])){
            weatherImg1.setImageResource(R.drawable.biz_plugin_weather_wu);
        }else if (weatherim1.equals(wheathertype[15])){
            weatherImg1.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
        }else if (weatherim1.equals(wheathertype[16])){
            weatherImg1.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
        }else if (weatherim1.equals(wheathertype[17])){
            weatherImg1.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
        }else if (weatherim1.equals(wheathertype[18])){
            weatherImg1.setImageResource(R.drawable.biz_plugin_weather_duoyun);
        }else if (weatherim1.equals(wheathertype[19])){
            weatherImg1.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
        }
        /*
        * 第二天*/
        if ( weatherim2.equals(wheathertype[0])){
            weatherImg2.setImageResource(R.drawable.biz_plugin_weather_qing);
        }else if (weatherim2.equals(wheathertype[1])){
            weatherImg2.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
        }else if (weatherim2.equals(wheathertype[2])){
            weatherImg2.setImageResource(R.drawable.biz_plugin_weather_daxue);
        }else if (weatherim2.equals(wheathertype[3])){
            weatherImg2.setImageResource(R.drawable.biz_plugin_weather_baoxue);
        }else if (weatherim2.equals(wheathertype[4])){
            weatherImg2.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
        }else if (weatherim2.equals(wheathertype[5])){
            weatherImg2.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
        }else if (weatherim2.equals(wheathertype[6])){
            weatherImg2.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
        }else if (weatherim2.equals(wheathertype[7])){
            weatherImg2.setImageResource(R.drawable.biz_plugin_weather_baoyu);
        }else if (weatherim2.equals(wheathertype[8])){
            weatherImg2.setImageResource(R.drawable.biz_plugin_weather_dayu);
        }else if (weatherim2.equals(wheathertype[9])){
            weatherImg2.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
        }else if (weatherim2.equals(wheathertype[10])){
            weatherImg2.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
        }else if (weatherim2.equals(wheathertype[11])){
            weatherImg2.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
        }else if (weatherim2.equals(wheathertype[12])){
            weatherImg2.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
        }else if (weatherim2.equals(wheathertype[13])){
            weatherImg2.setImageResource(R.drawable.biz_plugin_weather_yin);
        }else if (weatherim2.equals(wheathertype[14])){
            weatherImg2.setImageResource(R.drawable.biz_plugin_weather_wu);
        }else if (weatherim2.equals(wheathertype[15])){
            weatherImg2.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
        }else if (weatherim2.equals(wheathertype[16])){
            weatherImg2.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
        }else if (weatherim2.equals(wheathertype[17])){
            weatherImg2.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
        }else if (weatherim2.equals(wheathertype[18])){
            weatherImg2.setImageResource(R.drawable.biz_plugin_weather_duoyun);
        }else if (weatherim2.equals(wheathertype[19])){
            weatherImg2.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
        }
         /*
        * 第三天*/
        if ( weatherim3.equals(wheathertype[0])){
            weatherImg3.setImageResource(R.drawable.biz_plugin_weather_qing);
        }else if (weatherim3.equals(wheathertype[1])){
            weatherImg3.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
        }else if (weatherim3.equals(wheathertype[2])){
            weatherImg3.setImageResource(R.drawable.biz_plugin_weather_daxue);
        }else if (weatherim3.equals(wheathertype[3])){
            weatherImg3.setImageResource(R.drawable.biz_plugin_weather_baoxue);
        }else if (weatherim3.equals(wheathertype[4])){
            weatherImg3.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
        }else if (weatherim3.equals(wheathertype[5])){
            weatherImg3.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
        }else if (weatherim3.equals(wheathertype[6])){
            weatherImg3.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
        }else if (weatherim3.equals(wheathertype[7])){
            weatherImg3.setImageResource(R.drawable.biz_plugin_weather_baoyu);
        }else if (weatherim3.equals(wheathertype[8])){
            weatherImg3.setImageResource(R.drawable.biz_plugin_weather_dayu);
        }else if (weatherim3.equals(wheathertype[9])){
            weatherImg3.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
        }else if (weatherim3.equals(wheathertype[10])){
            weatherImg3.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
        }else if (weatherim3.equals(wheathertype[11])){
            weatherImg3.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
        }else if (weatherim3.equals(wheathertype[12])){
            weatherImg3.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
        }else if (weatherim3.equals(wheathertype[13])){
            weatherImg3.setImageResource(R.drawable.biz_plugin_weather_yin);
        }else if (weatherim3.equals(wheathertype[14])){
            weatherImg3.setImageResource(R.drawable.biz_plugin_weather_wu);
        }else if (weatherim3.equals(wheathertype[15])){
            weatherImg3.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
        }else if (weatherim3.equals(wheathertype[16])){
            weatherImg3.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
        }else if (weatherim3.equals(wheathertype[17])){
            weatherImg3.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
        }else if (weatherim3.equals(wheathertype[18])){
            weatherImg3.setImageResource(R.drawable.biz_plugin_weather_duoyun);
        }else if (weatherim3.equals(wheathertype[19])){
            weatherImg3.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
        }
        if ( weatherim4.equals(wheathertype[0])){
            weatherImg4.setImageResource(R.drawable.biz_plugin_weather_qing);
        }else if (weatherim4.equals(wheathertype[1])){
            weatherImg4.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
        }else if (weatherim4.equals(wheathertype[2])){
            weatherImg4.setImageResource(R.drawable.biz_plugin_weather_daxue);
        }else if (weatherim4.equals(wheathertype[3])){
            weatherImg4.setImageResource(R.drawable.biz_plugin_weather_baoxue);
        }else if (weatherim4.equals(wheathertype[4])){
            weatherImg4.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
        }else if (weatherim4.equals(wheathertype[5])){
            weatherImg4.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
        }else if (weatherim4.equals(wheathertype[6])){
            weatherImg4.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
        }else if (weatherim4.equals(wheathertype[7])){
            weatherImg4.setImageResource(R.drawable.biz_plugin_weather_baoyu);
        }else if (weatherim4.equals(wheathertype[8])){
            weatherImg4.setImageResource(R.drawable.biz_plugin_weather_dayu);
        }else if (weatherim4.equals(wheathertype[9])){
            weatherImg4.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
        }else if (weatherim4.equals(wheathertype[10])){
            weatherImg4.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
        }else if (weatherim4.equals(wheathertype[11])){
            weatherImg4.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
        }else if (weatherim4.equals(wheathertype[12])){
            weatherImg4.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
        }else if (weatherim4.equals(wheathertype[13])){
            weatherImg4.setImageResource(R.drawable.biz_plugin_weather_yin);
        }else if (weatherim4.equals(wheathertype[14])){
            weatherImg4.setImageResource(R.drawable.biz_plugin_weather_wu);
        }else if (weatherim4.equals(wheathertype[15])){
            weatherImg4.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
        }else if (weatherim4.equals(wheathertype[16])){
            weatherImg4.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
        }else if (weatherim4.equals(wheathertype[17])){
            weatherImg4.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
        }else if (weatherim4.equals(wheathertype[18])){
            weatherImg4.setImageResource(R.drawable.biz_plugin_weather_duoyun);
        }else if (weatherim4.equals(wheathertype[19])){
            weatherImg4.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
        }



        Toast.makeText(MainActivity.this, "更新成功！", Toast.LENGTH_SHORT).show();
    }

    private TodayWeather parseXML(String xmldata) {
        TodayWeather todayWeather = null;
        int fengxiangCount = 0;
        int fengliCount = 0;
        int dateCount = 0;
        int highCount = 0;
        int lowCount = 0;
        int typeCount = 0;
        int i = 0;

        try {
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int eventType = xmlPullParser.getEventType();
            Log.d("myWeather", "parseXML");
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
// 判断当前事件是否为文档开始事件
                    case XmlPullParser.START_DOCUMENT:
                        break;
// 判断当前事件是否为标签元素开始事件
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("resp")) {
                            todayWeather = new TodayWeather();
                            Sixday_weather = new  Next_SixDay_Weather[]
                                    { new Next_SixDay_Weather(),new Next_SixDay_Weather(),
                                            new Next_SixDay_Weather(), new Next_SixDay_Weather(),
                                            new Next_SixDay_Weather(), new Next_SixDay_Weather()};
                        }
                        if (todayWeather != null) {
                            if (xmlPullParser.getName().equals("city")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setCity(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("updatetime")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setUpdatetime(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("shidu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setShidu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("wendu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWendu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("pm25")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setPm25(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("quality")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setQuality(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengxiang(xmlPullParser.getText());
                                fengxiangCount++;
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli(xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setDate(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh(xmlPullParser.getText().substring(2).trim());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType(xmlPullParser.getText());
                                typeCount++;
                            } else if(xmlPullParser.getName().equals("date")) {
                                eventType = xmlPullParser.next();
                                String tmp = xmlPullParser.getText();
                                int index = tmp.indexOf("星");
                                Log.d("index", "index:"+index + " current i:  " + i);
                                Sixday_weather[i].setWeekday("星期" + tmp.charAt(index + 2));
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("high") ) {
                                eventType = xmlPullParser.next();
                                Sixday_weather[i].setHigh(xmlPullParser.getText().substring(2).trim());
                                highCount++;

                            } else if (xmlPullParser.getName().equals("low") ) {
                                eventType = xmlPullParser.next();
                                Sixday_weather[i].setLow(xmlPullParser.getText().substring(2).trim());
                                lowCount++;

                            } else if (xmlPullParser.getName().equals("type")&&(typeCount==2||typeCount==4||typeCount==6||typeCount==8)) {//只有type和风力需要这样做，因为只有这两个标签是分day和night的
                                eventType = xmlPullParser.next();
                                Sixday_weather[i].setWeather(xmlPullParser.getText());
                                typeCount++;

                            } else if (xmlPullParser.getName().equals("fengli") && (fengliCount==3||fengliCount==5||fengliCount==7||fengliCount==9)) {
                                eventType = xmlPullParser.next();
                                Sixday_weather[i].setFengli(xmlPullParser.getText());
                                fengliCount++;
                                i++;
                            }else if (xmlPullParser.getName().equals("fengli") ) {
                                eventType = xmlPullParser.next();               //当fenglicount=2时的数据不需要存下来，这里仅让fenglicount自增但不把获取的xml数据存储下来
                                fengliCount++;
                            }else if (xmlPullParser.getName().equals("type") ) {
                                eventType = xmlPullParser.next();
                                typeCount++;
                            }
                        }
                        break;
                    // 判断当前事件是否为标签元素结束事件
                    case XmlPullParser.END_TAG:
                        break;
                } // 进入下一个元素并触发相应事件
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return todayWeather;
    }

    void initView() {
        city_name_Tv = (TextView) findViewById(R.id.title_city_name);
        cityTv = (TextView) findViewById(R.id.city);
        timeTv = (TextView) findViewById(R.id.time);
        humidityTv = (TextView) findViewById(R.id.humidity);
        weekTv = (TextView) findViewById(R.id.week_today);
        pmDataTv = (TextView) findViewById(R.id.pm_data);
        pmQualityTv = (TextView) findViewById(R.id.pm2_5_quality);
        pmImg = (ImageView) findViewById(R.id.pm2_5_img);
        temperatureTv = (TextView) findViewById(R.id.temperature);
        climateTv = (TextView) findViewById(R.id.climate);
        windTv = (TextView) findViewById(R.id.wind);
        weatherImg = (ImageView) findViewById(R.id.weather_img);
        //此处可以设置天气图片更新变化
        city_name_Tv.setText("N/A");
        cityTv.setText("N/A");
        timeTv.setText("N/A");
        humidityTv.setText("N/A");
        pmDataTv.setText("N/A");
        pmQualityTv.setText("N/A");
        weekTv.setText("N/A");
        temperatureTv.setText("N/A");
        climateTv.setText("N/A");
        windTv.setText("N/A");

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int a=0; a<ids.length;a++){
            if (a==position){
                dots[a].setImageResource(R.drawable.page_indicator_focused);

            }else {
                dots[a].setImageResource(R.drawable.page_indicator_unfocused);
            }
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
