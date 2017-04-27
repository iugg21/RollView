package com.ctrun.rollviewdemo;

import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ctrun.rollview.RollView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RollView rollView;

    private ArrayList<String> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this);
        rollView = (RollView) findViewById(R.id.rollView);
        rollView.setOnItemClickListener(onClickMessage);

        refreshData();
        rollView.updateRollData(data);
    }

    RollView.OnItemClickListener onClickMessage = new RollView.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            String text = data.get(position);
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onRefresh() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1000);
                refreshData();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rollView.updateRollData(data);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });

            }
        }).start();

    }

    private int flag = 1;
    private void refreshData() {
        data.clear();

        for (int i=0; i<3; i++) {
            data.add("消息通知" + flag);
            flag++;
        }

    }
}
