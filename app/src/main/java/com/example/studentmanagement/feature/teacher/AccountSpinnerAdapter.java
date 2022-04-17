package com.example.studentmanagement.feature.teacher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.studentmanagement.R;
import com.example.studentmanagement.database.entity.Account;
import com.itextpdf.text.pdf.parser.XObjectDoHandler;

import java.util.List;

public class AccountSpinnerAdapter extends BaseAdapter {
    Context context;
    List<Account> accountList;

    public AccountSpinnerAdapter(Context context, List<Account> accountList) {
        this.context = context;
        this.accountList = accountList;
    }

    @Override
    public int getCount() {
        return accountList.size();
    }


    @Override
    public Object getItem(int position) {
        return accountList.get(position);
    }


    class ViewHolder {
        TextView txtAccountId, txtAccountEmail, txtAccountPhone;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.account_item, parent, false);
        TextView txtAccountId = rootView.findViewById(R.id.txt_account_id_item);
        TextView txtAccountName = rootView.findViewById(R.id.txt_account_name_item);
        Account account = accountList.get(position);
        txtAccountId.setText(account.getId()+"");
        txtAccountName.setText(account.getEmail());
        return rootView;
    }
}
