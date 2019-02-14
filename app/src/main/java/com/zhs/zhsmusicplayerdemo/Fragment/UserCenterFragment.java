package com.zhs.zhsmusicplayerdemo.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhs.zhsmusicplayerdemo.Activities.ChangePasswordActivity;
import com.zhs.zhsmusicplayerdemo.Activities.DescribeActivity;
import com.zhs.zhsmusicplayerdemo.Activities.PlayMusicActivity;
import com.zhs.zhsmusicplayerdemo.R;

public class UserCenterFragment extends Fragment {
    private TextView account;
    private LinearLayout changePassword;
    private LinearLayout about;
    private LinearLayout useHelp;

    private String curAccount;
    private String curPassword;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        View content = inflater.inflate(R.layout.fragment_usercenter, container,false);
        account = (TextView) content.findViewById(R.id.account);
        changePassword = (LinearLayout) content.findViewById(R.id.changepassword);
        about = (LinearLayout) content.findViewById(R.id.about);
        useHelp = (LinearLayout) content.findViewById(R.id.usehelp);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),ChangePasswordActivity.class);
                intent.putExtra("account",curAccount);
                intent.putExtra("password",curPassword);
                startActivity(intent);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),DescribeActivity.class);
                intent.putExtra("type",1);
                startActivity(intent);
            }
        });

        useHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),DescribeActivity.class);
                intent.putExtra("type",2);
                startActivity(intent);
            }
        });

        return content;
    }

    public void setUser(String curaccount,String curpassword){
        account.setText(curaccount);
        this.curAccount = curaccount;
        this.curPassword = curpassword;
    }
}
