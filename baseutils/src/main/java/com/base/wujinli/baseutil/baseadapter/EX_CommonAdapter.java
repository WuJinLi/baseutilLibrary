package com.base.wujinli.baseutil.baseadapter;

/**
 * author: WuJinLi
 * time  : 17/7/3
 * desc  :CommonAdapter使用说明
 */

public class EX_CommonAdapter {
    /**
     *
     *CommonAdapter使用的方法：
     *
     * 类似一般adapter的使用，给listview（gridview）设置适配器，只是在初始化adapter时需要重写convert方法，
     * 用来实现convertview每一个控件的赋值和相关控件的点击事件
     *
     lv_list.setAdapter(adapter = new CommonAdapter<StudentBean>(list, this, R.layout
     .item_listview) {
    @Override public void convert(ViewHolder viewHolder, final Object item) {

    viewHolder.setText(R.id.tv_name, ((StudentBean) item).getName());
    viewHolder.setText(R.id.tv_class, ((StudentBean) item).getClassNo());
    viewHolder.setText(R.id.tv_address, ((StudentBean) item).getAddress());


    ((TextView) viewHolder.getView(R.id.tv_name)).setOnClickListener(new View
    .OnClickListener() {


    @Override public void onClick(View v) {
    showToastMsg(((StudentBean) item).getName());
    }
    });

    ((TextView) viewHolder.getView(R.id.tv_class)).setOnClickListener(new View
    .OnClickListener() {


    @Override public void onClick(View v) {
    showToastMsg(((StudentBean) item).getClassNo());
    }
    });


    ((TextView) viewHolder.getView(R.id.tv_address)).setOnClickListener(new View
    .OnClickListener() {


    @Override public void onClick(View v) {
    showToastMsg(((StudentBean) item).getAddress());
    }
    });
    }
    });
     */
}
