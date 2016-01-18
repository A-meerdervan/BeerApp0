package nl.mprog.project.bieraanbiedingnotificatie;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


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
    private static final String tag = "*C_NotifyFrag";
    private ArrayList<String> favoritesList = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

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

        View view = inflater.inflate(R.layout.fragment_notify, container, false);

        // Set up the favorite beers listview
        fillFavoListAndListen(view);

        // Set the settings to what the user had previous
        SharedPreferences prefs = getActivity().getSharedPreferences("NotifySettings", Context.MODE_PRIVATE);
        Boolean previousSettingsDetected = prefs.getBoolean("previousSettingsDetected", false);
        Log.d(tag, previousSettingsDetected + " Dit is bij pre.set.detc. in oncreate fragment");
        // Only set if the user has saved his settings before
        if (previousSettingsDetected){
            String zipNumbers = prefs.getString("zipNumbers", "1657");
            String zipLetters = prefs.getString("zipLettes", "LH");
            String radius = prefs.getString("radius", "1000");
            String maxPrice = prefs.getString("maxPrice", "12.10");
            // TODO: add the beers

            // Set the views to the saved settings
            EditText zipCodeNumbersET = (EditText)view.findViewById(R.id.zipCodeNumbersET);
            EditText zipCodeLettersET = (EditText)view.findViewById(R.id.zipCodeLettersET);
            EditText radiusET = (EditText)view.findViewById(R.id.radiusET);
            EditText maxPriceET = (EditText)view.findViewById(R.id.maxPriceNotifyET);

            zipCodeLettersET.setText(zipLetters);
            zipCodeNumbersET.setText(zipNumbers);
            radiusET.setText(radius);
            maxPriceET.setText(maxPrice);
        }


        Button button = (Button) view.findViewById(R.id.saveNotifySettingsBtn);
        button.setOnClickListener(this);

        return view;
    }

    private void fillFavoListAndListen(View view) {
        // create and set adapter to the list
        favoritesList.add("yolo");
        favoritesList.add("heinikjotum");
        adapter = new ArrayAdapter<>(getActivity(), R.layout.favo_beer_listview, favoritesList);
        ListView list = (ListView)view.findViewById(R.id.favoBeerList);
        list.setAdapter(adapter);

        // An item is removed by a long click
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int index, long id) {
                favoritesList.remove(index);
                // TODO: aanpassen in shared prefs dat de lijst veranderd is
                adapter.notifyDataSetChanged();
                return true;
            }
        });

    }

    // When the settings are saved transfer the information to the Activity so that
    // it can update
    @Override
    public void onClick(View v) {
        if (mListener != null) {
            SharedPreferences prefs = getActivity().getSharedPreferences("NotifySettings", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            EditText zipCodenumbersET = (EditText)getView().findViewById(R.id.zipCodeNumbersET);
            EditText zipCodeLettersET = (EditText)getView().findViewById(R.id.zipCodeLettersET);
            EditText radiusET = (EditText)getView().findViewById(R.id.radiusET);
            EditText maxPriceET = (EditText)getView().findViewById(R.id.maxPriceNotifyET);

            // Check whether all fields are filled
            if (zipCodeLettersET.getText().toString().equals("") ||
                    zipCodenumbersET.getText().toString().equals("") ||
                    radiusET.getText().toString().equals("") ||
                    maxPriceET.getText().toString().equals("")) {
                Toast.makeText(this.getActivity(), "Niet alle velden zijn ingevuld", Toast.LENGTH_SHORT).show();
                return;
            }
            // when this function is called and all fields are filled, that means new settings are
            // saved. So the bool that keeps track of this is set to true
            editor.putBoolean("previousSettingsDetected", true);

            // TODO: fixen dat ie de favo beers opslaat

            String zipCodeNumbers = zipCodenumbersET.getText().toString();
            String zipCodeLetters = zipCodeLettersET.getText().toString();
            String zipCode = zipCodeNumbers + zipCodeLetters;
            int radius = Integer.parseInt(radiusET.getText().toString());
            Double maxPrice = Double.valueOf(maxPriceET.getText().toString());
            // Save the settings to the sharedpreferences
            editor.putString("zipNumbers", zipCodenumbersET.getText().toString());
            editor.putString("zipLetters", zipCodeLettersET.getText().toString());
            editor.putString("maxPrice", maxPriceET.getText().toString());
            editor.putString("radius", radiusET.getText().toString());
            // TODO: de bieren opslaan
            // editor.putString()
            editor.commit();
            // Use the fragment activity communication interface object to give the relevant
            // settings to the activity
            mListener.onFragmentInteraction(zipCode, radius, maxPrice);
            // Let the user now the settings are saved
            Toast.makeText(this.getActivity(), "Opgeslagen, aanbiediengen ophalen...", Toast.LENGTH_LONG).show();
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
        public void onFragmentInteraction(String zipCode, int radius, Double maxPrice);

        //TODO: meer methods toevoegen
    }

}
