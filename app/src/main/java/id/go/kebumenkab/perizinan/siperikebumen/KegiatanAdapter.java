package id.go.kebumenkab.perizinan.siperikebumen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Faldy on 25/03/2017.
 */

public class KegiatanAdapter extends ArrayAdapter<Kegiatan> implements View.OnClickListener {
    private ArrayList<Kegiatan> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView title;
        TextView description;
        ImageView icon;
    }

    public KegiatanAdapter(ArrayList<Kegiatan> data, Context context) {
        super(context, R.layout.list_kegiatan, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(mContext, "KeKlik di " + v.getTag().toString(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Kegiatan kegiatan = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_kegiatan, parent, false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.description = (TextView) convertView.findViewById(R.id.description);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.title.setText(kegiatan.getTitle());
        viewHolder.description.setText(kegiatan.getDescription());
        viewHolder.icon.setImageResource(kegiatan.getIcon());
        // Return the completed view to render on screen
        return result;
    }
}
