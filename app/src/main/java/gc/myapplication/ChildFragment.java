package gc.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaochao on 2018/1/20.
 */

public class ChildFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    public ChildFragment() {

    }

    public String tag;

    public ChildFragment(String tag) {
        this.tag = tag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.child_fragment, container, false);
            recyclerView = view.findViewById(R.id.recyclerView);
//            FocusWindow.findFocusGroup(recyclerView).setOnFocusGroupIntercept(new OnFocusDirectionIntercept() {
//                @Override
//                public boolean interceptDirection(int direction) {
//                    if (direction == View.FOCUS_UP) {
//                        return ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition() != 0;
//                    }
//                    return false;
//                }
//            });
            init();
        }
        return view;
    }

    private List<String> datas = new ArrayList<>();

    private void init() {
        recyclerView.addItemDecoration(new SpaceItemDecoration(Tools.dip2px(getContext(), 10), Tools.dip2px(getContext(), 10), 3));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        for (int i = 0; i < 60; i++) {
            datas.add(tag + "-item:" + i);
        }
        adapter = new GridAdapter(datas);
        recyclerView.setAdapter(adapter);
    }
}
