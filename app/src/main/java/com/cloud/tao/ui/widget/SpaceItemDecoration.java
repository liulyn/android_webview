package com.cloud.tao.ui.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * author:janecer on 2016/8/26 22:13
 */


public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    public static final int LEFT = 0,TOP = 1, RIGHT =2 ,BOTTOM =3 ;
    private int type = LEFT ; // 0 left ,1 top ,2 right ,3 bottom


    private int space;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    /**
     *
     * @param type  0 left ,1 top ,2 right ,3 bottom
     * @param space
     */
    public SpaceItemDecoration(int type,int space) {
        this(space) ;
        this.type = type ;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if(parent.getChildAdapterPosition(view) != 0){
            switch (type) {
                case LEFT :
                    outRect.left = space;
                    break ;
                case TOP :
                    outRect.top = space;
                    break ;
                case RIGHT :
                    outRect.right = space;
                    break ;
                case BOTTOM :
                    outRect.bottom = space;
                    break ;
            }

        }
    }

}
