package stock.awesome.instock.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import stock.awesome.instock.R;
import stock.awesome.instock.misc_classes.Kit;
import stock.awesome.instock.misc_classes.Product;
import stock.awesome.instock.misc_classes.ProductInKit;


// populates kit items in a listview.
// the layout for each row that is inflated is android.R.layout.two_line_list_item
// (described in the getView method)
public class KitAdapter extends BaseAdapter {

    public LinkedHashMap<String, ProductInKit> mKitMap = new LinkedHashMap<>();
    public String[] mKeys;
    public static HashMap<String, Product> mProductMap;
    public HashMap<String, Integer> productPositions;
    // whether item in list is checked, ordered by position
    public ArrayList<Boolean> status = new ArrayList<>();
    public CheckBox checkBox;
    private Context mContext;
    private int layout = 0;
    private List<CheckBox> checkBoxState;

    public KitAdapter(Context context, Kit data, int layout){
        mContext = context;
        mKitMap  = data.getKitMap();
        mKeys = mKitMap.keySet().toArray(new String[mKitMap.size()]);
        this.layout = layout;
        checkBoxState = new ArrayList<>();

        productPositions = new HashMap<>();
        for (int i = 0; i < mKeys.length; i++) {
            status.add(false);
            // map each product to its position in the listview
            productPositions.put(mKeys[i], i);
        }
    }
    public void updateKit(Kit newKit, Product newProd, int qty) {
        mKitMap = newKit.getKitMap();
        mKeys = mKitMap.keySet().toArray(new String[mKitMap.size()]);
        for (int i = 0; i < mKeys.length; i++) {
            status.add(false);
            // map each product to its position in the listview
            productPositions.put(mKeys[i], i);
        }
        newProd.setQuantity(qty);
        mProductMap.put(newProd.getName(), newProd);
        notifyDataSetChanged();
    }
    public void reconstruct(Kit kitName) {
        mKitMap  = kitName.getKitMap();
        mKeys = mKitMap.keySet().toArray(new String[mKitMap.size()]);
        for (int i=0; i<mKeys.length; i++) {
            status.add(false);
            productPositions.put(mKeys[i], i);
        }
    }

    public static void getProductDetails(HashMap<String, Product> productMap) {
        mProductMap = productMap;
        Log.d("productMap received", productMap.toString());
    }

    @Override
    public int getCount() {
        return mKitMap.size();
    }

    @Override
    public Object getItem(int position) {
        return mKitMap.get(mKeys[position]);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }


    // LAGS UI THREAD BECAUSE IT IS ONLY CALLED AFTER THE PRODUCT DETAILS ARE RETURNED FROM DATABASE
    @Override
    public View getView(final int pos, View convertView, ViewGroup parent) {
        ProductInKit value = (ProductInKit) getItem(pos);
        String id = value.getId();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, parent, false);
        }

        TextView pinkId = (TextView) convertView.findViewById(R.id.product_in_kit_id);
        TextView pinkQty = (TextView) convertView.findViewById(R.id.product_in_kit_qty);
        TextView prodName = (TextView) convertView.findViewById(R.id.product_in_kit_name);
        TextView prodLocation = (TextView) convertView.findViewById(R.id.product_in_kit_location);

        pinkId.setText(id);
        pinkQty.setText(Integer.toString(value.getQuantity()));
        prodName.setText(mProductMap.get(id).getName());
        prodLocation.setText(mProductMap.get(id).getLocation());

        //to set status in kitAdapter, representing the status of the checkbox
        checkBox = (CheckBox) convertView.findViewById(R.id.product_in_kit_checkbox);
        checkBoxState.add(pos, checkBox);
        checkBox.setTag(pos);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    status.set(pos, true);
                } else {
                    status.set(pos, false);
                }
            }
        });
        checkBox.setChecked(status.get(pos));

        return convertView;
    }

    public void setChecked(int position) {
        checkBoxState.get(position).setChecked(true);

    }

}
