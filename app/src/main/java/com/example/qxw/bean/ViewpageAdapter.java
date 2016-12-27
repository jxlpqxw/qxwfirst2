package com.example.qxw.bean;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2016/12/3.
 */
public class ViewpageAdapter extends PagerAdapter {
    private List<View> views;
    private Context context;


   public ViewpageAdapter(List<View> views,Context context){
       this.context = context;
       this.views = views;
   }


    @Override
    public int getCount() {
        return views.size();
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position){//用于创建position位置所在的视图
        container.addView(views.get(position));//添加position位置的视图，并返回这个视图对象。
        return views.get(position);
    }
    @Override
    public void destroyItem(ViewGroup container,int postion, Object object){//用于删除position位置的视图
        container.removeView(views.get(postion));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }
}
