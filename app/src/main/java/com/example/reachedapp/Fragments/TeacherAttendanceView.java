package com.example.reachedapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.reachedapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeacherAttendanceView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeacherAttendanceView extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TeacherAttendanceView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TeacherAttendanceView.
     */
    // TODO: Rename and change types and number of parameters
    public static TeacherAttendanceView newInstance(String param1, String param2) {
        TeacherAttendanceView fragment = new TeacherAttendanceView();


        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);



        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        String[] fruits = {"Apple", "Banana", "Cherry", "Date", "Grape", "Kiwi", "Mango", "Pear"};

        View view = inflater.inflate(R.layout.fragment_teacher_attendance_view, container, false);
        //Creating the instance of ArrayAdapter containing list of fruit names
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.drpdown_item, fruits);
        //Getting the instance of AutoCompleteTextView
        AutoCompleteTextView grades = (AutoCompleteTextView) view.findViewById(R.id.gradesAutoComplete);
        grades.setThreshold(1);//will start working from first character
        grades.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

        RecyclerView mRecyclerView = view.findViewById(R.id.daySelector);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));





        return view;
    }
}