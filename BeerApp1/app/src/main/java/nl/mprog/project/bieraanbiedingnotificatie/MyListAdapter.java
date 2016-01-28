package nl.mprog.project.bieraanbiedingnotificatie;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Alex on 25-1-2016.
 * This class holds a custom ListView adapter that displays discount information, it is used in
 * 3 activities
 */

// This custom adapter class is made to fill the listview with discount information
    public class MyListAdapter extends ArrayAdapter<DiscountObject> {

        private List<DiscountObject> discountArray;
        private Activity activity;

        // constructor
        public MyListAdapter(List<DiscountObject> discountArray, Activity activity) {
            super(activity, R.layout.listview, discountArray);
            this.discountArray = discountArray;
            this.activity = activity;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = activity.getLayoutInflater().inflate(R.layout.listview, parent, false);
            }

            // Get the discount object
            DiscountObject discountObject = discountArray.get(position);

            // Fill the views

            // Fill the brand
            TextView brand = (TextView) itemView.findViewById(R.id.brand);
            brand.setText(discountObject.brandPrint);

            // Fill the format
            TextView format = (TextView) itemView.findViewById(R.id.format);
            format.setText(discountObject.format);

            // Fill the period
            TextView period = (TextView) itemView.findViewById(R.id.period);
            period.setText(discountObject.discountPeriod);

            // Fill the price
            TextView price = (TextView) itemView.findViewById(R.id.price);
            price.setText("€" + String.format("%.2f", discountObject.price));

            // Fill the literPrice
            TextView literPrice = (TextView) itemView.findViewById(R.id.literPrice);
            literPrice.setText("€" + discountObject.pricePerLiter + " p/lr");

            // Set the supermarkt image
            ImageView superMarkt = (ImageView) itemView.findViewById(R.id.imgSuper);
            superMarkt.setImageResource(getSuperImageResource(discountObject.superMarkt));


            // Set the item image
            ImageView itemImg = (ImageView) itemView.findViewById(R.id.img);
            itemImg.setImageResource(getBeerImageResource(discountObject.brand));
            return itemView;
        }

        // this returns the resource integer id of a supermaket image
        private int getSuperImageResource(String superMarket) {
            int resId = activity.getApplicationContext().getResources().getIdentifier(superMarket, "drawable", "nl.mprog.project.bieraanbiedingnotificatie");
            if (resId != 0) {
                return resId;
            }
            // If image is not found:
            return R.drawable.hertogjan;
        }

        // this returns the resource integer id of a brand image
        private int getBeerImageResource(String brand) {
            int resId = activity.getApplicationContext().getResources().getIdentifier(brand, "drawable", "nl.mprog.project.bieraanbiedingnotificatie");
            if (resId != 0) {
                return resId;
            }
            // If image is not found:
            return R.drawable.imagenotfound;
        }
    }