package id.go.kebumenkab.perizinan.siperikebumen;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MasukFragment extends Fragment {


    public MasukFragment() {
        // Required empty public constructor
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MasukFragment newInstance() {
        MasukFragment fragment = new MasukFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_masuk, container, false);

        // ListView requirements
        ArrayList masuk = new ArrayList<>();

        masuk.add(new Kegiatan(R.mipmap.ic_launcher_round, "John Doe", "Kegiatan Badan Usaha"));
        masuk.add(new Kegiatan(R.mipmap.ic_launcher_round, "John Doe", "Kegiatan Badan Usaha"));
        masuk.add(new Kegiatan(R.mipmap.ic_launcher_round, "John Doe", "Kegiatan Badan Usaha"));
        masuk.add(new Kegiatan(R.mipmap.ic_launcher_round, "John Doe", "Kegiatan Badan Usaha"));
        masuk.add(new Kegiatan(R.mipmap.ic_launcher_round, "John Doe", "Kegiatan Badan Usaha"));
        masuk.add(new Kegiatan(R.mipmap.ic_launcher_round, "John Doe", "Kegiatan Badan Usaha"));
        masuk.add(new Kegiatan(R.mipmap.ic_launcher_round, "John Doe", "Kegiatan Badan Usaha"));
        masuk.add(new Kegiatan(R.mipmap.ic_launcher_round, "John Doe", "Kegiatan Badan Usaha"));
        masuk.add(new Kegiatan(R.mipmap.ic_launcher_round, "John Doe", "Kegiatan Badan Usaha"));
        masuk.add(new Kegiatan(R.mipmap.ic_launcher_round, "John Doe", "Kegiatan Badan Usaha"));
        masuk.add(new Kegiatan(R.mipmap.ic_launcher_round, "John Doe", "Kegiatan Badan Usaha"));
        masuk.add(new Kegiatan(R.mipmap.ic_launcher_round, "John Doe", "Kegiatan Badan Usaha"));

        KegiatanAdapter adapter = new KegiatanAdapter(masuk, getContext().getApplicationContext());

        ListView listView = (ListView) rootView.findViewById(R.id.list_view_masuk);
        listView.setAdapter(adapter);
        return rootView;
    }

}
