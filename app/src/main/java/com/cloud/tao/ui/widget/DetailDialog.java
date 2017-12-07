package com.cloud.tao.ui.widget;

/**
 * Created by gezi-pc on 2016/10/29.
 */

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.cloud.tao.R;
import com.cloud.tao.bean.etc.RegMoneyList;






public class DetailDialog extends Dialog {

        private Context context;
        private String title;
        private RegMoneyList.RegMoney regMoney;



        public DetailDialog(Context context, String title,RegMoneyList.RegMoney item) {
            super(context, R.style.dialogWindowAnim);
            this.context = context;
            this.title = title;
            this.regMoney = item;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            init();
        }

        public void init() {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.reg_money_history_dialog, null);

            setContentView(view);

            TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
            TextView tv_reg_money = (TextView) view.findViewById(R.id.tv_reg_money);
            TextView tv_counter_free = (TextView) view.findViewById(R.id.tv_counter_free);
            TextView tv_state = (TextView) view.findViewById(R.id.tv_state);
            ImageView iv_close = (ImageView)view.findViewById(R.id.iv_close);

            tvTitle.setText(title);
            tv_reg_money.setText("购买金额：￥"+regMoney.money);
            tv_counter_free.setText("￥"+regMoney.poundage);
            tv_state.setText(""+regMoney.score);
            iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DetailDialog.this.dismiss();
                }
            });

            Window dialogWindow = getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
            lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
            lp.height = (int)(d.heightPixels*0.3);
            lp.dimAmount=0.5f;
            dialogWindow.setAttributes(lp);
            dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        }

    }

