package com.cloud.tao.ui.activity.upgrade;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import com.cloud.tao.R;
import com.cloud.tao.bean.etc.upgrade.UpgradeBean;
import com.cloud.tao.callback.NoDoubleClickListener;
import com.cloud.tao.util.ToastUtils;

/**
 * Created by created
 * des: 版本更新内容提示
 */
public class UpgradeActivity extends Activity {

    private TextView tv_upgrade_content,tv_upgrade_confirmg,tv_upgrade_from_browser;
    UpgradeBean mUpgradeBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_upgrade_content);
        tv_upgrade_content = (TextView) findViewById(R.id.tv_upgrade_content);
//        tv_upgrade_confirmg = (TextView) findViewById(R.id.tv_upgrade_confirmg);
        tv_upgrade_from_browser= (TextView) findViewById(R.id.tv_upgrade_from_browser);
        initData();
        initListener();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }


    private void initData() {
        Bundle bundle = getIntent().getExtras();
        mUpgradeBean = bundle.getParcelable("upgradeBean");
        tv_upgrade_content.setText(mUpgradeBean.update_info);
    }

    private void initListener() {
        tv_upgrade_from_browser.setOnClickListener(mOnDoubleClickListener);
//        tv_upgrade_confirmg.setOnClickListener(mOnDoubleClickListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private NoDoubleClickListener mOnDoubleClickListener = new NoDoubleClickListener() {
        @Override
        public void noDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.tv_upgrade_from_browser:
                    String downloadUrlBrowser = mUpgradeBean.android_download_url;
                    if (mUpgradeBean==null&&TextUtils.isEmpty(downloadUrlBrowser)) {
                        ToastUtils.showToastShort(getApplicationContext(), "获取下载地址失败");
                    } else {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(downloadUrlBrowser));
                        startActivity(intent);
                    }
                    break;
//                case R.id.tv_upgrade_confirmg:
//                    String downloadUrl = mUpgradeBean.android_download_url;
//                    if (mUpgradeBean==null&&TextUtils.isEmpty(downloadUrl)) {
//                        ToastUtils.showToastShort(getApplicationContext(), "获取下载地址失败");
//                    } else {
//                        Intent intent = new Intent(UpgradeActivity.this, UpgradeDownloadActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("downloadUrl", mUpgradeBean.android_download_url);
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                        UpgradeActivity.this.finish();
//                    }
//                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            ToastUtils.showToastDouble(this,"请您先更新App新版本");
            return true;
        }else{
            return super.onKeyDown(keyCode, event);
        }
    }

}
