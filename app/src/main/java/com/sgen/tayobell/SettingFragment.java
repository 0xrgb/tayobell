package com.sgen.tayobell;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SettingFragment extends Fragment  {

    ListView settinglistView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.tabsetting_view, container, false);

        settinglistView= (ListView)v.findViewById(R.id.settingListView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);

        // 아이템을 추가
        adapter.add("알림설정");
        adapter.add("지역설정");

        settinglistView.setAdapter(adapter);

        return v;
    }
}