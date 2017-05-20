package jp.gr.java_conf.ya.weartw; // Copyright (c) 2017 YA <ya.androidapp@gmail.com> All rights reserved. This software includes the work that is distributed in the Apache License 2.0

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.WearableRecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import twitter4j.ResponseList;
import twitter4j.Status;

import static jp.gr.java_conf.ya.weartw.ActionActivity.webIntentUrlKey;
import static jp.gr.java_conf.ya.weartw.TwitterUtil.webIntentUrlLike;

public class TlActivity extends WearableActivity {
    public static final SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(" MM/dd HH:mm");

    private Adapter adapter;
    private ResponseList<Status> tweets = null;
    private WearableRecyclerView recyclerView1;

    // region // UI
    private void initRecyclerView() {
        adapter = new Adapter(this) {
            @Override
            void onItemClick(int position) {
                Intent intent = new Intent(TlActivity.this, ActionActivity.class);
                intent.putExtra(webIntentUrlKey, webIntentUrlLike + Long.toString(tweets.get(position).getId()));
                startActivity(intent);
            }
        };
        recyclerView1 = (WearableRecyclerView) findViewById(R.id.recyclerView1);
        recyclerView1.setAdapter(adapter);
    }

    private void initTl(){
        tweets = TwitterUtil.getListStatuses(Twitter4jSetting.ownerScreenName, Twitter4jSetting.slug, 1);
        adapter.notifyDataSetChanged();
    }

    private void initUis() {
        setContentView(R.layout.activity_tl);
        setAmbientEnabled();

        initRecyclerView();
    }
    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUis();

        initTl();
    }

    // region // Wear only
    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
        if (isAmbient()) {
            recyclerView1.setBackgroundColor(getResources().getColor(android.R.color.black));
        } else {
            recyclerView1.setBackground(null);
        }
    }
    // endregion

    // region // to use the WearableRecyclerView
    public class Adapter extends WearableRecyclerView.Adapter<Adapter.ViewHolder> {
        private Context context;
        private LayoutInflater inflater;

        Adapter(Context context) {
            this.context = context;
            this.inflater = LayoutInflater.from(this.context);
        }

        void onItemClick(int position) {
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            final ViewHolder viewHolder = new ViewHolder(
                    inflater.inflate(R.layout.activity_tl_item, null));
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick(viewHolder.getAdapterPosition());
                }
            });
            return viewHolder;
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            final TextView tv = (TextView) viewHolder.itemView.findViewById(R.id.itemTextView1);

            final Status status = tweets.get(i);
            final StringBuilder sb = new StringBuilder(10);
            sb.append("@").append(status.getUser().getScreenName()).append(simpleDateFormat1)
                    .append(System.getProperty("line.separator")).append(status.getText());
            try {
                tv.setText(sb.toString());
            }catch(Exception e){
                tv.setText(e.getLocalizedMessage());
            }
        }

        @Override
        public int getItemCount() {
            return tweets.size();
        }

        public class ViewHolder extends WearableRecyclerView.ViewHolder {
            public TextView mTextView;

            public ViewHolder(View v) {
                super(v);
                mTextView = (TextView) v.findViewById(R.id.itemTextView1);
            }
        }
    }
    // endregion
}
