package com.base.wujinli.baseutil.baseadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * author: WuJinLi
 * time  : 17/7/3
 * desc  : 公共的adapter（适用于listiew，gridview）
 */

public abstract class CommonAdapter<T> extends BaseAdapter {

    private List<T> list;
    private Context context;
    private int layoutId;

    public CommonAdapter(List<T> list, Context context, int layoutId) {
        this.list = list;
        this.context = context;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /**
         * 初始化viewholder
         * 这里已经整合了convertView的初始化和初始化viewholder初始化
         */
        ViewHolder viewHolder = getViewHolder(convertView, parent, position);
        convert(viewHolder, getItem(position));
        return viewHolder.getConvertView();
    }

    public abstract void convert(ViewHolder viewHolder, Object item);

    private ViewHolder getViewHolder(View convertView, ViewGroup parent, int position) {
        return ViewHolder.getViewHolder(context, convertView, parent, layoutId, position);
    }


    /**
     * 刷新数据
     *
     * @param _list   新数据集合
     * @param isClear 是否清除原来数据的标记，为true时，将list数据清空在添加数据，为false时则在原来的基础上再次添加
     */
    public void reFreshData(List<T> _list, Boolean isClear) {
        if (isClear) {
            list.clear();
        }

        list.addAll(_list);
        notifyDataSetChanged();
    }


    /**
     * 添加单条数据
     *
     * @param t
     */
    private void addData(T t) {
        list.add(t);
        notifyDataSetChanged();
    }

    /**
     * 清除所有数据
     */
    private void clearAllData() {
        if (list != null) {
            list.clear();
        }
        notifyDataSetChanged();
    }

    /**
     * 移除指定数据
     *
     * @param position
     */
    private void removeDetailData(int position) {
        if (list != null) {
            list.remove(position);
        }
        notifyDataSetChanged();
    }
}
