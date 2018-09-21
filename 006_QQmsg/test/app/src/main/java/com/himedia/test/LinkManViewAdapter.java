package com.himedia.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Neo on 2018/9/21.
 */

public class LinkManViewAdapter extends BaseAdapter {

    private static final String TAG = "LinkManViewAdapter";
    private List<LinkManEntity> data;
    private Context mContext;
    private LayoutInflater mInflater;

    private int mSelect = -1;   //选中项
    private boolean isFocus = false;   //是否聚焦
    private LinkManEntity linkManEntity = new LinkManEntity();


    public LinkManViewAdapter(Context context, List<LinkManEntity> data) {
        this.mContext = context;
        this.data = data;
        mInflater = LayoutInflater.from(context);
        Log.d(TAG,"LinkManViewAdapter getCount = "+data.size());
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinkManEntity linkman = data.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.linkman_item, null);
            viewHolder = new ViewHolder();
            viewHolder.head = convertView.findViewById(R.id.iv_linkman_head);
            viewHolder.remark = convertView.findViewById(R.id.tv_linkman_remark);
            viewHolder.msg_num = (TextView) convertView.findViewById(R.id.iv_linkman_status);
            viewHolder.item = (LinearLayout)convertView.findViewById(R.id.item_linkman);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(linkman.getLinkId() > 0){
            Bitmap bitmap = ImageUtils.getBitmap(""+linkman.getLinkId());
            viewHolder.head.setImageBitmap(ImageUtils.toRoundBitmap(bitmap));
        }

        viewHolder.remark.setText(linkman.getRemark());

        if(linkman.isHasRead()){
            viewHolder.msg_num.setVisibility(View.GONE);
        }else{
            viewHolder.msg_num.setVisibility(View.VISIBLE);
            int num = linkman.getMsgNum();
            String msg_num;
            if(num > 99){
                msg_num = "99+";
            }else{
                msg_num = ""+num;
            }
            viewHolder.msg_num.setText(msg_num);
        }

        //选中效果
        if(mSelect == position){
            viewHolder.remark.setTextSize(TypedValue.COMPLEX_UNIT_PX,mContext.getResources().getDimension(R.dimen.linkman_select));
            if(isFocus){
                viewHolder.item.setBackgroundResource(R.drawable.item_linkman_focus);
                viewHolder.remark.setTextColor(mContext.getResources().getColor(R.color.msg_white));
            }else {
                viewHolder.item.setBackgroundResource(R.drawable.item_linkman_selected);
                viewHolder.remark.setTextColor(mContext.getResources().getColor(R.color.msg_black));
            }

        }else{
            viewHolder.item.setBackgroundResource(R.drawable.item_linkman_default);
            viewHolder.remark.setTextColor(mContext.getResources().getColor(R.color.msg_white_50));
            viewHolder.remark.setTextSize(TypedValue.COMPLEX_UNIT_PX,mContext.getResources().getDimension(R.dimen.linkman_defalut));
        }
        return convertView;
    }


    //通过ViewHolder显示项的内容
    static class ViewHolder {
        public ImageView head;
        public TextView remark;
        public TextView msg_num ;
        public LinearLayout item;
    }


    public void changeSelected(int positon){ //选中效果刷新
        if(positon != mSelect){
            mSelect = positon;
            notifyDataSetChanged();
        }
    }

    public void setFocusStatus(boolean status){ //聚焦效果刷新
        Log.d(TAG,"setFocusStatus = "+status);
        isFocus = status;
        notifyDataSetChanged();

    }
}
