package de.android.testaufgabe;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment implements View.OnClickListener{
    private Button button;
    private ProgressBar progressBar;
    private TextView textView;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> arrayList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        button = (Button)view.findViewById(R.id.button);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        textView = (TextView)view.findViewById(R.id.textView);
        listView = (ListView)view.findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                arrayList);

        button.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        progressBar.setVisibility(View.VISIBLE);
    }
}
