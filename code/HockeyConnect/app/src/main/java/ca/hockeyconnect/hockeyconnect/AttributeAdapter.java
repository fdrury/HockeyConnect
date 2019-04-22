package ca.hockeyconnect.hockeyconnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

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
        if(convertView == null) {
            // Get the data item for this position
            final Attribute attribute = attributes.get(position);

            // inflate the view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_attribute_slider, parent, false);

            // Lookup view for data population
            TextView attributeName = (TextView) convertView.findViewById(R.id.textViewAttributeName);

            // Populate the data into the template view using the data object
            attributeName.setText(attribute.getAttributeName());

            final SeekBar seekBar = convertView.findViewById(R.id.seekBar);
            ImageButton helpButton = convertView.findViewById(R.id.helpButton);
            final TextView attributeValueView = convertView.findViewById(R.id.textViewAttributeValue);
            final int index = position;

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    attributes.get(index).setValue(progress);
                    if (progress == 0)
                        attributeValueView.setText(R.string.not_rated_name);
                    else
                        attributeValueView.setText(Integer.toString(progress));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            helpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), attribute.getAttributeDescription(), Toast.LENGTH_LONG).show();
                }
            });

            seekBar.setProgress(attribute.getValue());
        }

        // Return the completed view to render on screen
        return convertView;
    }

    /*public void addAndSet(Attribute attribute, int tryoutID, int playerID) {
        final Attribute localAttribute = attribute;
        final Context context = getContext();

        final RequestQueue mRequestQueue1 = Volley.newRequestQueue(getContext());
        String url1 = String.format("%s/getGameEval/%s/%d", getContext().getResources().getString(R.string.server_url), tryoutID, playerID);
        final JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest (Request.Method.GET, url1, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // TODO: populate sliders
                        try {
                            localAttribute.setValue(response.getInt("Value"));
                            context.super.add(localAttribute);
                        } catch(Exception e) {
                            // TODO: catch exception
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: does anything need to be done here?
                    }
                }
        );
        mRequestQueue1.add(jsonObjectRequest1);
    }*/

}
