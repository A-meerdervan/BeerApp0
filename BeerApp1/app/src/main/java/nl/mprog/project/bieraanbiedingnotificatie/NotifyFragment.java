package nl.mprog.project.bieraanbiedingnotificatie;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
public class NotifyFragment extends Fragment implements View.OnClickListener,OnItemSelectedListener {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String tag = "*C_NotifyFrag";
    private ArrayList<String> favoritesList = new ArrayList<>();
    private List<String> beerOptionsList = new ArrayList<>();
    private boolean notInOnCreate = false;

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

        // Fill the dropdown list spinner about the beer selection
        // First fill the list of options that is possible:
        fillBeerOptionsList();
        Spinner beersDropDown = (Spinner) view.findViewById(R.id.favoBeerSpinner);
        // Spinner click listener
        beersDropDown.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a spinner layout
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getActivity().getApplicationContext(), R.layout.beer_options_drop_down, beerOptionsList);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.beer_options_drop_down); //(android.R.layout.simple_dropdown_item_1line);
        beersDropDown.setAdapter(adapter);

        // Set the settings to what the user had previous
        SharedPreferences prefs = getActivity().getSharedPreferences("NotifySettings", Context.MODE_PRIVATE);
        Boolean previousSettingsDetected = prefs.getBoolean("previousSettingsDetected", false);
        Log.d(tag, previousSettingsDetected + " Dit is bij pre.set.detc. in oncreate fragment");
        // Only set if the user has saved his settings before
        if (previousSettingsDetected){
            String zipNumbers = prefs.getString("zipNumbers", "default");
            String zipLetters = prefs.getString("zipLetters", "default");
            String radius = prefs.getString("radius", "-1");
            String maxPrice = prefs.getString("maxPrice", "-1");
            favoritesList = new ArrayList<>(prefs.getStringSet("favoBeersList", new HashSet<String>()));
            if (favoritesList.size() == 0) {
                // TODO: Iets zeggen miss?
            }
            // Set up the favorite beers listing
            fillFavoListAndListen(view);
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


    // When a beer title is selected it should be added to the users favorites
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // When the fragment is created this method is called for the first item in the beer options
        // list. To prevent this, this if statement is used.
//        if (notInOnCreate) {
            // On selecting a spinner item add it to the favorites list and display it.
        // But not the first item, since this is "choose a brand"
        if (position != 0) {
            String item = parent.getItemAtPosition(position).toString();
            // Only add if the item is not already chosen by the user
            if (!favoritesList.contains(item)) {
                favoritesList.add(item);
                addFavoriteToLayout(item, getView());
            } else { // show the user this is not possible
                Toast.makeText(parent.getContext(), item + " heeft u al gekozen", Toast.LENGTH_SHORT).show();
            }
//        }
//        else {
//            notInOnCreate = true;
//        }
        }
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private void addFavoriteToLayout(String beerTitle, View view){

//        favoritesList.add(beerTitle);
        // Create new LinearLayout view with one TextView
        LinearLayout newLayout = (LinearLayout)getActivity().getLayoutInflater().inflate(R.layout.favoritebeer_template, null);
//        LinearLayout newLayout = new LinearLayout(getActivity().getApplicationContext());
//        newLayout.setOrientation(LinearLayout.HORIZONTAL);
//        newLayout.setClickable(true);

//        newLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.Gold));

        TextView favoBeer = new TextView(getActivity().getApplicationContext());
        newLayout.addView(favoBeer);

        // Remove a beer after a long click
        newLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                v.setVisibility(View.GONE);
                Log.d(tag, "voor verwijderen " + favoritesList.size());
                favoritesList.remove( ( (TextView)( ((LinearLayout) v).getChildAt(0)) ).getText().toString());
                // TODO: ook verwijderen uit shared prefs en uit de array ?
                Log.d(tag, "favorites list lengte is nu" + favoritesList.size());
                return true;
            }
        });
        // TODO: Dit stond hier om te hebben dat het item highlight zolang ie ingedrukt is, maar nu maakt ie m gewoon permanent grijs
//            newLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    v.setBackgroundColor(getActivity().getResources().getColor(R.color.MainBackgroundColor));
//                }
//            });

        // fill the text of the new Textview and add it to the layout in the activity
        favoBeer.setText(beerTitle);
        favoBeer.setTextColor(getActivity().getResources().getColor(R.color.Black));
        LinearLayout parentLayout = (LinearLayout) view.findViewById(R.id.favoBeerLinLay);
        parentLayout.addView(newLayout);
    }

    private void fillFavoListAndListen(View view) {
        // create and set adapter to the list
        for ( String favorite : favoritesList) {
            addFavoriteToLayout(favorite, view);
        }
    }

    // When the Save Settings button is clicked, settings are saved and transfered to the Activity
    // so that it can update
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
            editor.putStringSet("favoBeersList", new HashSet(favoritesList));

            // Save the favorite beers to a list and pass it to the activity
            List<String> favoriteBeers = new ArrayList<>();
            View favoBeerLinLayView = getView().findViewById(R.id.favoBeerLinLay);
            LinearLayout favoBeerLinLay = (LinearLayout)favoBeerLinLayView;
            for (int index = 0; index < favoBeerLinLay.getChildCount(); index++) {
                // If the view is visible this means it was not deleted by the user
                if (favoBeerLinLay.getChildAt(index).getVisibility() == View.VISIBLE) {
                    // The linlay contains linlay's that contain one textview, get the textview
                    // text and add it to the list of favorite beers
                    favoriteBeers.add( ((TextView) ( ( (LinearLayout)(favoBeerLinLay.getChildAt(index)) ).getChildAt(0) ) ).getText().toString() );
                }
            }

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
            mListener.onFragmentInteraction(zipCode, radius, maxPrice, favoriteBeers);
            // Let the user now the settings are saved
            Toast.makeText(this.getActivity(), "Opgeslagen, aanbiedingen ophalen...", Toast.LENGTH_LONG).show();
        }
    }

    // This function fills the spinner drop down option list from the supported brands class
    private void fillBeerOptionsList() {
        SupportedBrandsMap supportedBrandsMap = new SupportedBrandsMap();
        beerOptionsList.add("Kies een merk");
        for (Object key : supportedBrandsMap.keySet()) {
                beerOptionsList.add((String)key);
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
        public void onFragmentInteraction(String zipCode, int radius, Double maxPrice, List<String> favoriteBeers);

        //TODO: meer methods toevoegen
    }

}
