package com.cloud.tao.ui.widget.bean;

import com.bigkoo.pickerview.model.IPickerViewData;

/**
 * Created by gezi-pc on 2016/10/15.
 */

    public class PickerViewData implements IPickerViewData {
        private String content;

        public PickerViewData(String content) {
            this.content = content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public String getPickerViewText() {
            return content;
        }
    }
