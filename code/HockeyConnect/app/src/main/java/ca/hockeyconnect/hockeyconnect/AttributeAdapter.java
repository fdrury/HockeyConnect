package ca.hockeyconnect.hockeyconnect;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class AttributeAdapter extends ArrayAdapter<Attribute> {
    public AttributeAdapter(Context context, ArrayList<Attribute> attributes) {
        super(context, 0, attributes);
    }
}
