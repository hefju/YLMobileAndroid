package ylescort.ylmobileandroid;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import TaskClass.Box;
import YLAdapter.YLBoxEdiAdapter;
import YLDataService.AnalysisBoxList;
import YLSystemDate.YLEditData;

public class vault_in_detail_statistics extends ActionBarActivity implements View.OnClickListener {

    private TextView vault_in_detail_statistics_tv_fullbox;
    private TextView vault_in_detail_statistics_tv_emptybox;
    private Button vault_in_detail_statistics_btn_hom;
    private Button vault_in_detail_statistics_btn_vault;
    private Button vault_in_detail_statistics_btn_surplus;

    private ListView vault_in_detail_statistics_listview;
    private AnalysisBoxList analysisBoxList;
    private YLBoxEdiAdapter ylBoxEdiAdapter;
    private List<Box> allboxList;
    private List<Box> showboxList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault_in_detail_statistics);
        InitView();
        InitData();

    }

    private void InitData() {
        analysisBoxList = new AnalysisBoxList();
        allboxList = new ArrayList<>();
        showboxList = new ArrayList<>();
        for (Box box : YLEditData.getYleditcarbox()) {
            allboxList.add(box);
            showboxList.add(box);
        }
        LoadBoxData(showboxList);
        StatisticsYLBox(allboxList);
    }

    private void StatisticsYLBox(List<Box> boxList) {

        if (boxList.size()==0)return;

        List<String> stringList = analysisBoxList.AnsysisBoxListForKeeper(boxList);
        String fullbox = "款箱："+stringList.get(0)+"  卡箱："+stringList.get(2)+"\r\n"+"凭证箱："+stringList.get(4)+
                " 凭证袋："+stringList.get(6);
        String emptybox =  "款箱："+stringList.get(1)+"  卡箱："+stringList.get(3)+"\r\n"+"凭证箱："+stringList.get(5)+
                " 凭证袋："+stringList.get(7);
        vault_in_detail_statistics_tv_fullbox.setText(fullbox);
        vault_in_detail_statistics_tv_emptybox.setText(emptybox);
//        this.setTitle("库管员:"+boxList.size()+" 业务员："+boxList.size());
    }


    private void LoadBoxData(List<Box> boxList){
        if (boxList ==null)return;
        ylBoxEdiAdapter = new YLBoxEdiAdapter(this, boxList,R.layout.activity_boxedititem);
        ylBoxEdiAdapter.setSelectItem(0);
        vault_in_detail_statistics_listview.setAdapter(ylBoxEdiAdapter);
    }

    private void InitView() {
        vault_in_detail_statistics_tv_fullbox = (TextView) findViewById(R.id.vault_in_detail_statistics_tv_fullbox);
        vault_in_detail_statistics_tv_emptybox = (TextView) findViewById(R.id.vault_in_detail_statistics_tv_emptybox);
        vault_in_detail_statistics_btn_hom = (Button) findViewById(R.id.vault_in_detail_statistics_btn_hom);
        vault_in_detail_statistics_btn_vault =(Button) findViewById(R.id.vault_in_detail_statistics_btn_vault);
        vault_in_detail_statistics_btn_surplus=(Button) findViewById(R.id.vault_in_detail_statistics_btn_surplus);

        vault_in_detail_statistics_listview = (ListView) findViewById(R.id.vault_in_detail_statistics_listview);

        vault_in_detail_statistics_btn_hom.setOnClickListener(this);
        vault_in_detail_statistics_btn_vault.setOnClickListener(this);
        vault_in_detail_statistics_btn_surplus.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.vault_in_detail_statistics_btn_hom:
                StatisticsYLBox(allboxList);
                ylBoxEdiAdapter.notifyDataSetChanged();
                this.setTitle("总计：" + allboxList.size());
            break;
            case R.id.vault_in_detail_statistics_btn_vault:

                showboxList.clear();

                for (Box box : allboxList) {
                    if (box.getValutcheck() != null){
                        if (box.getValutcheck().equals("对")){
                            showboxList.add(box);
                        }
                    }
                }

                StatisticsYLBox(showboxList);
                this.setTitle("总计：" + showboxList.size());
                ylBoxEdiAdapter.notifyDataSetChanged();

                break;
            case R.id.vault_in_detail_statistics_btn_surplus:

                showboxList.clear();

                for (Box box : allboxList) {
                    if (box.getValutcheck() != null){
                        if (!box.getValutcheck().equals("对")){
                            showboxList.add(box);
                        }
                    }
                }
                this.setTitle("总计：" + showboxList.size());
                StatisticsYLBox(showboxList);
                ylBoxEdiAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4){
            finish();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
        return super.onKeyDown(keyCode, event);
    }
}
