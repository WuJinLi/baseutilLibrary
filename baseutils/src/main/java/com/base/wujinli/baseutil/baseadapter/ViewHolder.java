package com.base.wujinli.baseutil.baseadapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * author: WuJinLi
 * time  : 17/7/3
 * desc  : 封装ViewHolder（结合CommonAdapter来使用）
 * 封装减少每次代码的重复编写，减少勿用代码，
 */

public class ViewHolder {
    private SparseArray<View> mViews;
    private int position;
    private View mConvertView;

    private LayoutInflater inflater;
    private Context context;

    public ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.mViews = new SparseArray<>();
        this.context = context;
        this.position = position;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //初始化inflater对象
        mConvertView = inflater.inflate(layoutId, parent, false);//添加布局
        mConvertView.setTag(this);
    }


    /**
     * 获取到一个viewolder对象
     *
     * @param context
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static ViewHolder getViewHolder(Context context, View convertView, ViewGroup parent,
                                           int layoutId, int position) {

        if (convertView == null) {
            return new ViewHolder(context, parent, layoutId, position);
        }
        return (ViewHolder) convertView.getTag();
    }


    /**
     * 获取convertView
     *
     * @return
     */
    public View getConvertView() {
        return mConvertView;
    }

    public int getPosition() {
        return position;
    }


    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {

        View view = mViews.get(viewId);

        //如果该view为空，则初始化并且添加mview集合中
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }


    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId, String text) {
        TextView textView = getView(viewId);
        if (textView != null) {
            textView.setText(text);
        }
        return this;
    }


    /**
     * 为ImageView设置图片(ImageResource)
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public ViewHolder setImageResource(int viewId, int drawableId) {
        ImageView imageView = getView(viewId);
        if (imageView != null) {
            imageView.setImageResource(drawableId);
        }
        return this;
    }


    /**
     * 为ImageView设置图片(Bitmap)
     *
     * @param viewId
     * @param bm
     * @return
     */
    public ViewHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView imageView = getView(viewId);
        if (imageView != null) {
            imageView.setImageBitmap(bm);
        }
        return this;
    }


    /**
     * 为ImageView设置图片(imgUrl)
     *
     * @param viewId
     * @param imgUrl
     * @return
     */
    public ViewHolder setImageFromNet(int viewId, String imgUrl) {
        ImageView imageView = getView(viewId);
        if (imageView != null) {
            Glide.with(context).load(imgUrl).into(imageView);
        }
        return this;
    }
}
