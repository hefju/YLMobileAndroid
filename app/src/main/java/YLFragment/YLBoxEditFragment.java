package YLFragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import TaskClass.Box;
import YLAdapter.YLBoxEdiAdapter;
import YLSystemDate.YLSystem;
import ylescort.ylmobileandroid.R;

/**
 * Created by Administrator on 2015/5/18.
 */
public class YLBoxEditFragment extends Fragment {

    private List<Box> boxList ;
    private ListView ylboxeidtlistviewfm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        boxList = YLSystem.getEdiboxList();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.yllistviewfragment,null);
        FindView(view);
        return view;
    }

    private void FindView(View view) {
        ylboxeidtlistviewfm = (ListView) view.findViewById(R.id.ylboxeidtlistviewfm);
        final YLBoxEdiAdapter ylBoxEdiAdapter = new YLBoxEdiAdapter(getActivity(),boxList,R.id.ylboxeidtlistviewfm);
        ylboxeidtlistviewfm.setAdapter(ylBoxEdiAdapter);
        ylboxeidtlistviewfm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ylBoxEdiAdapter.setSelectItem(position);
                ylBoxEdiAdapter.notifyDataSetInvalidated();
            }
        });
    }
}
