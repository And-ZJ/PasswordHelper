package com.andzj.passwordhelper.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TableRow;
import android.widget.TextView;

import com.andzj.passwordhelper.R;
import com.andzj.passwordhelper.bean.AccountPasswordInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zj on 2017/1/18.
 */

public class AccountPasswordItemAdapter extends ArrayAdapter<AccountPasswordInfo> {
    public static String TAG = "AccountPasswordItemAdapter";

    private static int resourceId = R.layout.item_account_password_info;
    private Context mContext;
    private List<AccountPasswordInfo> objects;

    public AccountPasswordItemAdapter(Context context, List<AccountPasswordInfo> objects)
    {
        super(context,resourceId,objects);
        this.mContext = context;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AccountPasswordInfo info = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null)
        {
            view = LayoutInflater.from(mContext).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.categoryView = (TextView) view.findViewById(R.id.category_view);
            viewHolder.accountView = (TextView) view.findViewById(R.id.account_view);
            viewHolder.passwordView = (TextView) view.findViewById(R.id.password_view);
            viewHolder.noteView = (TextView) view.findViewById(R.id.note_view);
            viewHolder.chooseBox = (CheckBox) view.findViewById(R.id.choose_box);
            view.setTag(viewHolder);
        }
        else
        {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.categoryView.setText(info.getAccountCategory());
        viewHolder.accountView.setText(info.getAccountDescribe());
        viewHolder.passwordView.setText(info.getPasswordDescribe());
        viewHolder.noteView.setText(info.getNoteDescribe());


        return view;
    }

    private class ViewHolder{
        TableRow categoryRow;
        TextView categoryView;
        TableRow accountRow;
        TextView accountView;
        TableRow passwordRow;
        TextView passwordView;
        TableRow noteRow;
        TextView noteView;
        CheckBox chooseBox;
    }
}

