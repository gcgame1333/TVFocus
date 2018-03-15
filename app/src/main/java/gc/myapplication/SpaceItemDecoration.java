package gc.myapplication;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zhaoli on 2016/11/25.
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int widthSpace;
    private int heightSpace;
    private int lineCount;

    public SpaceItemDecoration(int widthSpace, int heightSpace) {
        this(widthSpace, heightSpace, 6);
    }

    public SpaceItemDecoration(int widthSpace, int heightSpace, int lineCount) {
        this.widthSpace = widthSpace;
        this.heightSpace = heightSpace;
        this.lineCount = lineCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (parent.getChildAdapterPosition(view) / lineCount != 0) {
            outRect.top = heightSpace;
        }
        if (parent.getChildAdapterPosition(view) % lineCount != (lineCount - 1)) {
            outRect.right = widthSpace;
        }
    }
}