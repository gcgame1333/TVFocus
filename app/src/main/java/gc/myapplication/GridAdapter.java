package gc.myapplication;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by gaochao on 2018/1/20.
 */

public class GridAdapter extends Adapter<GridAdapter.Item> {

    private List<String> datas;

    public GridAdapter(List<String> datas) {
        this.datas = datas;
    }

    @Override
    public Item onCreateViewHolder(ViewGroup parent, int viewType) {
        Item item = new Item(LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, null));
        return item;
    }

    @Override
    public void onBindViewHolder(Item holder, int position) {
        holder.itemView.setTag(position);
        holder.tag.setText(datas.get(position));
    }

    @Override
    public int getItemCount() {
        return datas != null ? datas.size() : 0;
    }

    static class Item extends RecyclerView.ViewHolder {
        public ImageView icon;
        public TextView tag;

        public Item(final View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            tag = itemView.findViewById(R.id.tag);
            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        itemView.animate()
                                .scaleX(1.15f)
                                .scaleY(1.15f)
                                .setDuration(250)
                                .setInterpolator(new DecelerateInterpolator())
                                .start();
                    } else {
                        itemView.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(250)
                                .setInterpolator(new DecelerateInterpolator())
                                .start();
                    }
                }
            });
        }
    }
}
