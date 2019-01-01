package ca.hockeyconnect.hockeyconnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AttributeAdapter extends ArrayAdapter<Attribute> {
    public AttributeAdapter(Context context, ArrayList<Attribute> attributes) {
        super(context, 0, attributes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Attribute attribute = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_attribute_slider, parent, false);
        }
        // Lookup view for data population
        TextView attributeName = (TextView) convertView.findViewById(R.id.textViewAttributeName);
        // Populate the data into the template view using the data object
        attributeName.setText(attribute.getAttributeName());
        // Return the completed view to render on screen
        return convertView;
    }

}
