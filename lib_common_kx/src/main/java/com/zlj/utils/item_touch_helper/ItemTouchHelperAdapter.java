package com.zlj.utils.item_touch_helper;

/**
 * Created by zlj on 2018/7/27 0027.
 * @email：282384507@qq.com
 * @Word：Thought is the foundation of understanding
 *
 *ItemHelper数据交换的接口
 * @Since 1.0
 */
public interface ItemTouchHelperAdapter {
    //数据交换
    void onItemMove(int fromPosition, int toPosition);
    //数据删除
    void onItemDissmiss(int position);
}
