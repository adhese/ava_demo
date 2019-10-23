package com.endare.adhese.sdksample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class EventViewerDialogFragment extends DialogFragment {

    private static final String EVENTS_KEY = "com.endare.adhese.sdksample.EventViewerDialogFragment.EVENTS";

    private ArrayList<String> events;

    public static EventViewerDialogFragment newInstance(ArrayList<String> events) {
        EventViewerDialogFragment fragment = new EventViewerDialogFragment();

        Bundle args = new Bundle();
        args.putStringArrayList(EVENTS_KEY, events);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        events = getArguments().getStringArrayList(EVENTS_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_event_viewer, container, false);

        ListView listView = view.findViewById(R.id.eventList);

        ArrayAdapter adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, events);
        listView.setAdapter(adapter);

        return view;
    }
}
