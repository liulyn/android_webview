package com.cloud.tao.bean.etc;

import java.util.List;

/**
 * Created by gezi-pc on 2016/10/14.
 */

public class Region {

    public List<Province> list;

    public class Province{
        public String name;
        public List<City> city;

        public class City{
            public String name;
            public String[] area;

        }
    }

}
