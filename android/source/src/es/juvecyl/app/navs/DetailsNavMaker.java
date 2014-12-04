
package es.juvecyl.app.navs;

import java.util.List;

import es.juvecyl.app.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DetailsNavMaker extends ArrayAdapter<DetailsNav> {
    int resource;
    String response;
    Context context;

    // Initialize adapter
    public DetailsNavMaker(Context context, int resource, List<DetailsNav> items)
    {
        super(context, resource, items);
        this.resource = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        LinearLayout alertView;
        // Get the current alert object
        DetailsNav al = getItem(position);
        // Inflate the view
        if (convertView == null) {
            alertView = new LinearLayout(getContext());
            alertView.setBackgroundColor(Color.parseColor(al.getBgColor()));
            alertView.setPadding(0, 15, 0, 15);
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi;
            vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, alertView, true);
        } else {
            alertView = (LinearLayout) convertView;
        }
        // Get the text boxes from the listitem.xml file
        TextView alertText = (TextView) alertView.findViewById(R.id.TitleList);
        // Assign the appropriate data from our alert object
        // above
        alertText.setText(al.getTitle());
        alertText.setPadding(20, 10, 0, 0);
        return alertView;
    }
}
