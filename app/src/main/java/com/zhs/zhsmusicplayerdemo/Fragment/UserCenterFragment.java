package com.zhs.zhsmusicplayerdemo.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhs.zhsmusicplayerdemo.R;

public class UserCenterFragment extends Fragment {
    private TextView account;
    private LinearLayout changePassword;
    private LinearLayout about;
    private LinearLayout useHelp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        View content = inflater.inflate(R.layout.fragment_usercenter, container,false);
        account = (TextView) content.findViewById(R.id.account);
        changePassword = (LinearLayout) content.findViewById(R.id.changepassword);
        about = (LinearLayout) content.findViewById(R.id.about);
        useHelp = (LinearLayout) content.findViewById(R.id.usehelp);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        useHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return content;
    }
}
