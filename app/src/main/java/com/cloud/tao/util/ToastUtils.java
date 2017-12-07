package com.cloud.tao.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cloud.tao.R;


/**
 * Created by space on 2015/11/18.
 */
public class ToastUtils {

    private static Toast mDoubleToast;

    private final static Handler handler = new Handler(Looper.getMainLooper());

    public static void showToastShort(final Context context,final String strMsg){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, strMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void showToastShort(Context context,int res){
        String strMsg = context.getResources().getString(res);
        showToastShort(context, strMsg);
    }

    public static void showToastLong(final Context context,final String strMsg){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, strMsg, Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void showToastLong(Context context,int res){
        String strMsg = context.getResources().getString(res);
        showToastLong(context,strMsg);
    }

    public static Dialog getLoadingDialog(Activity activity, String tvLoadingStr, boolean
            isCanceledOnTouchOutside){
        Dialog dlg = new Dialog(activity);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View viewContent =LayoutInflater.from(activity).inflate(R.layout.fragment_layout_loading_progress_dialog, null, false);;
        ProgressBar pb = (ProgressBar) viewContent.findViewById(R.id.iv_loading_progress_dialog_loadprogress);
        pb.setInterpolator(new AccelerateDecelerateInterpolator());
        pb.animate().setDuration(100);
        if(tvLoadingStr!=null && !TextUtils.isEmpty(tvLoadingStr)){
            TextView tvLoading = (TextView) viewContent.findViewById(R.id.more_data_msg);
            tvLoading.setText(""+tvLoadingStr);
        }
        dlg.setContentView(viewContent);
        dlg.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
        dlg.setCancelable(true);
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        Window window = dlg.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.dimAmount = 0.4f;
        lp.width = dm.widthPixels * 20 / 20;
        window.setAttributes(lp);
        return dlg;
    }

    public static void showToastDouble(Context context,String strMsg){
        if(mDoubleToast == null) {
            mDoubleToast = Toast.makeText(context, strMsg, Toast.LENGTH_SHORT);
        } else {
            mDoubleToast.setText(strMsg);
            mDoubleToast.setDuration(Toast.LENGTH_SHORT);
        }
        mDoubleToast.show();
    }

}
