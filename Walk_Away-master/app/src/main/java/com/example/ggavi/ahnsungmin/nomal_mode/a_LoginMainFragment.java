package com.example.ggavi.ahnsungmin.nomal_mode;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.ggavi.ahnsungmin.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link a_LoginMainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link a_LoginMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class a_LoginMainFragment extends Fragment
{
    // '로그인MainActivity'에서 받아오기 위해 선언
    private String userID;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public a_LoginMainFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static a_LoginMainFragment newInstance(String param1, String param2) {
        a_LoginMainFragment fragment = new a_LoginMainFragment();
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
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);

        // 이미지 버튼 추가
        ImageButton imageBtn = (ImageButton) getView().findViewById(R.id.walk_imageButton);
        imageBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // MainActivity에서 userID를 받아온다.
                userID = getActivity().getIntent().getExtras().getString("userID").toString();

                Intent intent = new Intent(getActivity(), LoggedInWalk.class);
                intent.putExtra("userID", userID);
                startActivity(intent);

                getActivity().finish();
            }
        });
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.login_fragment_main, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


/*

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(getActivity() != null && getActivity() instanceof 로그인Activity) {
            userID = ((로그인Activity) getActivity()).getData();
        }
    }
*/


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
