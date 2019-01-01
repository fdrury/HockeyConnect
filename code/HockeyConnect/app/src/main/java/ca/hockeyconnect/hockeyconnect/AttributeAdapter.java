package ca.hockeyconnect.hockeyconnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AttributeAdapter extends ArrayAdapter<Attribute> {
    private final Context context;
    private final ArrayList<Attribute> attributes;

    public AttributeAdapter(Context context, ArrayList<Attribute> attributes) {
        super(context, 0, attributes);
        this.context = context;
        this.attributes = attributes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Attribute attribute = attributes.get(position);

        // inflate the view
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item_attribute_slider, parent, false);

        // Lookup view for data population
        TextView attributeName = (TextView) rowView.findViewById(R.id.textViewAttributeName);

        // Populate the data into the template view using the data object
        attributeName.setText(attribute.getAttributeName());

        // Return the completed view to render on screen
        return rowView;
    }

}
