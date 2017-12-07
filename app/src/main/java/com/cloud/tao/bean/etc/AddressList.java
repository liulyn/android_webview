package com.cloud.tao.bean.etc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gezi-pc on 2016/10/11.
 */

public class AddressList implements Serializable{


    private static final long serialVersionUID = 981933476699609467L;
    public List<Address> address_list = new ArrayList<Address>();

    public class Address implements Serializable{


        private static final long serialVersionUID = 6543585019291313819L;
        public String dev_id;
        public String u_client_id;
        public String province;
        public String city;
        public String area;
        public String street;
        public String zip;
        public String consignee;
        public String phone;
        public String mobilephone;
        public int is_default;

        @Override
        public String toString() {
            return "Address{" +
                    "dev_id='" + dev_id + '\'' +
                    ", u_client_id='" + u_client_id + '\'' +
                    ", province='" + province + '\'' +
                    ", city='" + city + '\'' +
                    ", area='" + area + '\'' +
                    ", street='" + street + '\'' +
                    ", zip='" + zip + '\'' +
                    ", consignee='" + consignee + '\'' +
                    ", phone='" + phone + '\'' +
                    ", mobilephone='" + mobilephone + '\'' +
                    ", is_default=" + is_default +
                    '}';
        }
    }
}
