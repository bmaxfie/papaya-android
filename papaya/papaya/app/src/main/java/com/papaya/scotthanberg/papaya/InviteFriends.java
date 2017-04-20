package com.papaya.scotthanberg.papaya;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.HashMap;

public class InviteFriends extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);

        Menu menu = new Menu(InviteFriends.this);

        if (savedInstanceState != null) {
            AccountData.data.clear();
            AccountData.data.putAll((HashMap<AccountData.AccountDataType, Object>) savedInstanceState.getSerializable(AccountData.ACCOUNT_DATA));
            //AccountData.data = (HashMap<AccountData.AccountDataType, Object>) savedInstanceState.getSerializable(AccountData.ACCOUNT_DATA);
        }
        else if (getIntent().hasExtra(AccountData.ACCOUNT_DATA)) {
            AccountData.data.clear();
            AccountData.data.putAll((HashMap<AccountData.AccountDataType, Object>) getIntent().getSerializableExtra(AccountData.ACCOUNT_DATA));
            //AccountData.data = (HashMap<AccountData.AccountDataType, Object>) getIntent().getSerializableExtra(AccountData.ACCOUNT_DATA);
        }

    }
}
