package com.cloud.tao.bean.etc;

import java.util.ArrayList;

/**
 * Created by gezi-pc on 2016/10/29.
 */

public class RecordList {
    public ArrayList<Record> record_list;

    public class Record{
        public String activity_name;
        public String state_info;
        public Long play_time;
        public String prize_type;
        public String prize_name;
        public String rand_no;

    }

}
