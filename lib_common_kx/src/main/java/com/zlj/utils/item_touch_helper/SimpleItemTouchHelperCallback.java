package com.zlj.utils.item_touch_helper;

import android.util.Log;

import java.util.List;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by zlj on 2018/7/27 0027.
 * @email：282384507@qq.com
 * @Word：Thought is the foundation of understanding
 *
 * @Since 1.0
 */
public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback{

    private ItemTouchHelperAdapter mAdapter;
    private List images;

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter,List images){
        mAdapter = adapter;
        this.images=images;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //设置允许拖动的范围
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN| ItemTouchHelper.RIGHT| ItemTouchHelper.LEFT;
//        int swipeFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN| ItemTouchHelper.RIGHT| ItemTouchHelper.LEFT;
        return makeMovementFlags(dragFlags,0);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();//得到item原来的position
        int toPosition = target.getAdapterPosition();//得到目标position
        Log.e("Jun","fromPosition=="+fromPosition);
        Log.e("Jun","toPosition=="+toPosition);
        Log.e("Jun","images.size()=="+images.size());
        if (toPosition == images.size()||fromPosition==images.size()) {
            return true;
        }
        mAdapter.onItemMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //mAdapter.onItemDissmiss(viewHolder.getAdapterPosition());
    }

    /**
     * 长按选中的时候
     * @param viewHolder
     * @param actionState
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if(viewHolder!=null){
            if(viewHolder.itemView!=null){
                viewHolder.itemView.animate().scaleX(1.2f).scaleY(1.2f).setDuration(150).start();
            }
        }
    }

    /**
     * 当用户与item的交互结束并且item也完成了动画时调用
     * @param recyclerView
     * @param viewHolder
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if(viewHolder!=null){
            if(viewHolder.itemView!=null){
                viewHolder.itemView.animate().scaleX(1f).scaleY(1f).setDuration(150).start();
            }
        }
    }
}
