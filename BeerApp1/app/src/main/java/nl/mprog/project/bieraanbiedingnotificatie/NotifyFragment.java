package nl.mprog.project.bieraanbiedingnotificatie;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotifyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotifyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

//public class NotifyFragment extends android.support.v4.app.Fragment {
// TODO: deze regel gebruiken en kijken of het ook chill werkt:
public class NotifyFragment extends Fragment implements View.OnClickListener {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotifyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotifyFragment newInstance(String param1, String param2) {
        NotifyFragment fragment = new NotifyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public NotifyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // Code inspired by: http://weimenglee.blogspot.nl/2013/08/android-tip-handling-events-in-fragment.html
    // This function puts an onClickListener on the Save Settings button.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate( R.layout.fragment_notify, container, false);

        Button button = (Button) view.findViewById(R.id.saveNotifySettingsBtn);
        button.setOnClickListener(this);

        return view;
    }

    // When the settings are saved transfer the information to the Activity so that
    // it can update
    @Override
    public void onClick(View v) {
        if (mListener != null) {

            EditText latitude = (EditText)getView().findViewById(R.id.latitudeET);
            EditText longitude = (EditText)getView().findViewById(R.id.longitudeET);
            EditText radiusET = (EditText)getView().findViewById(R.id.radiusET);
            EditText maxPriceET = (EditText)getView().findViewById(R.id.maxPriceNotifyET);

            // Check whether all fields are filled
            if (latitude.getText().toString().equals("") ||
                    longitude.getText().toString().equals("") ||
                    radiusET.getText().toString().equals("") ||
                    maxPriceET.getText().toString().equals("")) {
                Toast.makeText(this.getActivity(), "Alle velden zijn niet ingevuld", Toast.LENGTH_SHORT).show();
                return;
            }

            Double lat = Double.valueOf(latitude.getText().toString());
            Double lng = Double.valueOf(longitude.getText().toString());
            int radius = Integer.parseInt(radiusET.getText().toString());
            Double maxPrice = Double.valueOf(maxPriceET.getText().toString());

            mListener.onFragmentInteraction(lat, lng, radius, maxPrice);

        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

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
        public void onFragmentInteraction(Double lat, Double lng, int radius, Double maxPrice);

        //TODO: meer methods toevoegen
    }

}
