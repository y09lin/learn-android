package com.huim_lin.learn.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.huim_lin.learn.R;
import com.huim_lin.learn.util.ToastUtil;

public class TestActivity extends AppCompatActivity {
    private static final String TAG = "JustTest";
    TextView tv;
    WordActionCallback callback;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        tv = findViewById(R.id.text_test);
        tv.setTextIsSelectable(true);

        tv.setCustomSelectionActionModeCallback(new WordActionCallback());
        callback = (WordActionCallback) tv.getCustomSelectionActionModeCallback();

        tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        if (callback!=null && !TextUtils.isEmpty(getSelectedText())){
                            TestActivity.this.startActionMode(callback);
                        }
                        break;
                }
                return false;
            }
        });
    }

    private class WordActionCallback implements ActionMode.Callback{
        private Menu wordMenu;

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            Log.d(TAG, "onCreateActionMode: ");
            MenuInflater menuInflater = actionMode.getMenuInflater();
            menu.clear();
            menuInflater.inflate(R.menu.article_setting,menu);
            wordMenu = menu;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            Log.d(TAG, "onPrepareActionMode: ");
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            Log.d(TAG, "onActionItemClicked: ");
            switch (menuItem.getItemId()){
                case R.id.submit:
                    ToastUtil.showText(getSelectedText());
                    tv.setText(tv.getText());
                    wordMenu.close();
                    break;
            }
            actionMode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            Log.d(TAG, "onDestroyActionMode: ");
        }
    }

    private String getSelectedText(){
        int start = tv.getSelectionStart();
        int end = tv.getSelectionEnd();
        String text = tv.getText().toString();
        return text.substring(start,end);
    }
}
