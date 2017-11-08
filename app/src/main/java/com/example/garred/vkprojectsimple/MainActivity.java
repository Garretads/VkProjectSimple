package com.example.garred.vkprojectsimple;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by garred on 14.03.17.
 */

public class MainActivity extends AppCompatActivity {
    private static final int VK_AUTH = 27;
    private static final int VK_FRIENDS = 52;
    private Button pressButton;
    Intent loginIntent,getFriendsIntent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pressButton = (Button) findViewById(R.id.button);
        pressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginIntent = new Intent(MainActivity.this,LoginActivity.class);
                startActivityForResult(loginIntent,VK_AUTH);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle newData;
        String token,uid;
        switch (requestCode) {
            case VK_AUTH:
                if (resultCode == Activity.RESULT_OK) {
                    newData = data.getExtras();
                    token = newData.getString("token");
                    uid = newData.getString("uid");
                    getFriendsIntent = new Intent(MainActivity.this,FriendListActivity.class);
                    getFriendsIntent.putExtra("params",newData);
                    startActivityForResult(getFriendsIntent,VK_FRIENDS);
                }
                break;
        }
    }
}
