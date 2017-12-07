package com.cloud.tao.bean.etc;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by gezi-pc on 2016/10/24.
 */

public class ShoppingCar  implements Serializable {

    private static final long serialVersionUID = -3413534617406320467L;

    public ArrayList<ShoppingCarGoods> goods = new ArrayList<ShoppingCarGoods>();

}
