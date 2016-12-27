package com.example.qxw.qxwfirst;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.qxw.bean.ViewpageAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/27.
 */
public class guid extends Activity implements ViewPager.OnPageChangeListener  {
    private ViewpageAdapter viewPageAdapter;
    private ViewPager vp;
    private List<View> views;
    private ImageView[] dots;
    private int [] ids ={R.id.iv1,R.id.iv2};
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        if (pref.getInt("tag",0)==1) {
            Intent i = new Intent(guid.this, MainActivity.class);
            startActivity(i);
        }else {
            setContentView(R.layout.guid);
            initViews();//初始化视图
            initDots();//初始化小圆点
            btn = (Button) views.get(1).findViewById(R.id.butn1);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(guid.this, MainActivity.class);
                    startActivity(i);
                    finish();

                }
            });


        }
    }

    void initDots(){
        dots = new ImageView[views.size()];
        for (int i=0; i<views.size();i++ ){
            dots[i]=(ImageView) findViewById(ids[i]);

        }


    }
    private void initViews(){
        LayoutInflater inflater = LayoutInflater.from(this);
        views = new ArrayList<View>();
        views.add(inflater.inflate(R.layout.page1,null));
        views.add(inflater.inflate(R.layout.page2,null));
        viewPageAdapter = new ViewpageAdapter(views,this);
        vp =(ViewPager)findViewById(R.id.guid1);
        vp.setAdapter(viewPageAdapter);
        vp.setOnPageChangeListener(this);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int a = 0;a<ids.length;a++){
            if (a == position){
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
