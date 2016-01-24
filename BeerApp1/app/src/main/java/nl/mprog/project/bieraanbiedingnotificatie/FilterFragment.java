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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FilterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilterFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String tag = "*C_FilterFrag";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private SupportedSupermarketsMap supportedSupermarketsMap = new SupportedSupermarketsMap();
    private SupportedBrandsMap supportedBrandsMap = new SupportedBrandsMap();
    private String sortOptionSelected;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FilterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FilterFragment newInstance(String param1, String param2) {
        FilterFragment fragment = new FilterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FilterFragment() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter, container, false);

        // Add the checkboxes
        addCheckboxesToLayout(view);

        // Fill the dropdown list spinner about the beer selection
        Spinner sortOptionsSP = (Spinner) view.findViewById(R.id.sortSpinner);
        List<String> sortOptions = new ArrayList<>();
        sortOptions.add("Prijs");
        sortOptions.add("Supermarkt keten");
        sortOptions.add("Bier merk");
        // Spinner click listener
        sortOptionsSP.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a spinner layout
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getActivity().getApplicationContext(), R.layout.beer_options_drop_down, sortOptions);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.beer_options_drop_down); //(android.R.layout.simple_dropdown_item_1line);
        sortOptionsSP.setAdapter(adapter);

        // Set onclick listener for filter and sort button
        Button button = (Button) view.findViewById(R.id.filterAndSortBtn);
        button.setOnClickListener(this);
        return view;
    }

    // When a Sort option in selected it should be available saved as the current one
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        sortOptionSelected = parent.getItemAtPosition(position).toString();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
    }

    private int getViewId(String id) {
        // TODO: dit weer incommenten
        int resId = getActivity().getApplicationContext().getResources().getIdentifier(id, "id", "nl.mprog.project.bieraanbiedingnotificatie");
        if (resId != 0) {
            return resId;
        }
        // If view is not found:
        Log.d(tag, "The view id of " + id + " was not found");
        return 0;
    }

    // When the filter and sort button is clicked, settings are saved and transfered to the Activity
    // so that it can update
    @Override
    public void onClick(View v) {
        if (mListener != null) {

            // Get the maxPrice set by the user (if it was filled in)
            EditText maxPriceET = (EditText)getView().findViewById(R.id.maxPriceFilterET);
            Double maxPrice;
            if (maxPriceET.getText().toString().equals("")){
                maxPrice = null;
            }
            else{ maxPrice = Double.valueOf(maxPriceET.getText().toString()); }

            // Create a list of checked beer options by looping all checkboxes
            List<String> checkedBeerOptions = new ArrayList<>();
            for (String beerOption : supportedBrandsMap.getBrands()){
                CheckBox checkBox = (CheckBox)getView().findViewById(getViewId(beerOption));
                if (checkBox.isChecked()){
                    checkedBeerOptions.add(beerOption);
                }
            }
            for (String item : checkedBeerOptions){
                Log.d(tag, item);
            }

            // Create a list of checked supermarket options by looping all checkboxes
            List<String> checkedSuperMarkets = new ArrayList<>();
            for (String superMarket : supportedSupermarketsMap.getChainNames()){
                CheckBox checkBox = (CheckBox)getView().findViewById(getViewId(superMarket));
                if (checkBox.isChecked()){
                    checkedSuperMarkets.add(superMarket);
                }
            }
            for (String item : checkedSuperMarkets){
                Log.d(tag, item);
            }

//                Toast.makeText(this.getActivity(), "Niet alle velden zijn ingevuld", Toast.LENGTH_SHORT).show();
//                return;
//            editor.putBoolean("previousSettingsDetected", true);
//            editor.putStringSet("favoBeersList", new HashSet(favoritesList));
//            editor.putString("zipNumbers", zipCodenumbersET.getText().toString());
//            editor.commit();

//            // Use the fragment activity communication interface object to give the relevant
//            // filter and sort options to the activity
            mListener.onFragmentInteraction(sortOptionSelected, maxPrice, checkedBeerOptions, checkedSuperMarkets);
//            // Let the user now the settings are saved
            Toast.makeText(this.getActivity(), "Aanbiedingen filteren...", Toast.LENGTH_LONG).show();
//        }
        }
    }

    private void addCheckboxesToLayout(View view) {
        SupportedSupermarketsMap supportedSupermarketsMap = new SupportedSupermarketsMap();
        List<String> chainNames = supportedSupermarketsMap.getChainNames();
        SupportedBrandsMap supportedBrandsMap = new SupportedBrandsMap();
        List<String> brands = supportedBrandsMap.getBrands();

        LinearLayout parentLayoutBeer = (LinearLayout) view.findViewById(R.id.beerCheckBoxLinLay);
        LinearLayout parentLayoutSuperMarkets = (LinearLayout) view.findViewById(R.id.superMarketCheckBoxLinLay);

        for (int i = 0; i < SupportedBrandsMap.brandsList.length; i++) {
            // add a checkbox to the layout
            addCheckBoxToLayout(parentLayoutBeer, SupportedBrandsMap.brandsList[i], brands.get(i), view);
        }
        for (int i = 0; i < SupportedSupermarketsMap.superMarketsList.length; i++){
            // add a checkbox to the layout
            addCheckBoxToLayout(parentLayoutSuperMarkets, SupportedSupermarketsMap.superMarketsList[i], chainNames.get(i), view);
        }
    }

    // This function adds a checkbox to an input linearlayout
    private void addCheckBoxToLayout(LinearLayout targetLayout, String name, String id, View view){
        LayoutInflater inflater = LayoutInflater.from(getActivity().getApplicationContext());
        CheckBox checkBox = (CheckBox) inflater.inflate(R.layout.checkbox, null, false);
//        CheckBox checkBox = new CheckBox(getActivity().getApplicationContext());
        checkBox.setText(name);
        checkBox.setId(getViewId(id));
        targetLayout.addView(checkBox);
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
        public void onFragmentInteraction(String sortOption, Double maxPrice, List<String> checkedBeerOptions, List<String> checkedSupermarkets);
    }

}
