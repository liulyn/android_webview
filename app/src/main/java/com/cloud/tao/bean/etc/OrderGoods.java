package com.cloud.tao.bean.etc;

import java.io.Serializable;

/**
 * Created by gezi-pc on 2016/10/24.
 */

public class OrderGoods implements Serializable{


    private static final long serialVersionUID = 3645273340782377447L;
    public int goods_id;
    public String goods_name;
    public Double goods_price;
    public int goods_count;
    public int goods_attr_id;
    public String attr_id_list;
    public String goods_attr_name;
}
