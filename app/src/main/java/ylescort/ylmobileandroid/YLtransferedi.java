package ylescort.ylmobileandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import TaskClass.ArriveTime;
import TaskClass.Box;
import TaskClass.Site;
import YLAdapter.YLBoxEdiAdapter;
import YLDataOperate.YLCarBoxOperate;
import YLDataOperate.YLtransferDataOperate;
import YLDataService.AnalysisBoxList;

public class YLtransferedi extends YLBaseActivity implements View.OnClickListener {

    private TextView yltransferedi_tv_title;
    private ListView yltransferedi_listview;
    private Spinner yltransferedi_sp_tasktype;
    private Spinner yltransferedi_sp_TimeID;

    private TextView yltransferedi_tv_gog;
    private TextView yltransferedi_tv_eof;
    private TextView yltransferedi_tv_mac;
    private TextView yltransferedi_tv_vab;

    private Button yltransferedi_btn_enter;
    private Button yltransferedi_btn_back;
    private Button yltransferedi_btn_del;

    private List<Box> displayboxList;
    private List<ArriveTime> arriveTimeList;
    private ArriveTime arriveTime;
    private YLBoxEdiAdapter ylBoxEdiAdapter;
    private int indext;
    private AnalysisBoxList analysisBoxList;
    private YLtransferDataOperate yfdo;
    private YLCarBoxOperate ycbo;
    private Site ChooseSite;
    private String SitetimeID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yltransferedi);
        InitLayout();
        InitData();
    }

    @Override
    protected void InitLayout() {

        yltransferedi_tv_title = (TextView)findViewById(R.id.yltransferedi_tv_title);
        yltransferedi_listview = (ListView)findViewById(R.id.yltransferedi_listview);

        yltransferedi_sp_tasktype = (Spinner)findViewById(R.id.yltransferedi_sp_tasktype);
        yltransferedi_sp_TimeID = (Spinner)findViewById(R.id.yltransferedi_sp_TimeID);

        yltransferedi_tv_gog = (TextView)findViewById(R.id.yltransferedi_tv_gog);
        yltransferedi_tv_eof = (TextView)findViewById(R.id.yltransferedi_tv_eof);
        yltransferedi_tv_mac = (TextView)findViewById(R.id.yltransferedi_tv_mac);
        yltransferedi_tv_vab = (TextView)findViewById(R.id.yltransferedi_tv_vab);

        yltransferedi_btn_enter = (Button)findViewById(R.id.yltransferedi_btn_enter);
        yltransferedi_btn_back = (Button)findViewById(R.id.yltransferedi_btn_back);
        yltransferedi_btn_del = (Button)findViewById(R.id.yltransferedi_btn_del);

        yltransferedi_btn_enter.setOnClickListener(this);
        yltransferedi_btn_back.setOnClickListener(this);
        yltransferedi_btn_del.setOnClickListener(this);

        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this, R.array.TaskType
                , android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yltransferedi_sp_tasktype.setAdapter(arrayAdapter);
        yltransferedi_sp_tasktype.setPrompt("交接类型");

        yltransferedi_sp_TimeID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SitetimeID = adapterView.getItemAtPosition(i).toString();
                displayboxList = yfdo.EditerBoxDisplay(ChooseSite,SitetimeID);
                DisplayBoxData(displayboxList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        yltransferedi_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                indext = i;
                ylBoxEdiAdapter.setSelectItem(indext);
                ylBoxEdiAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void InitData() {
        displayboxList = new ArrayList<>();
        arriveTime = new ArriveTime();
        indext = 0;
        analysisBoxList = new AnalysisBoxList();
        yfdo = new YLtransferDataOperate();
        ycbo = new YLCarBoxOperate();

        ChooseSite = YLtransferDataOperate.getChooseSite();
        SitetimeID = YLtransferDataOperate.getSitetimeID();
        yltransferedi_tv_title.setText(ChooseSite.getSiteName());


        //根据网点交接状态获取数据
        if (YLtransferDataOperate.getSiteTaskTimeID() == 0){
            //不在交接状态下
            Recheckboxlist(ChooseSite);
            yltransferedi_btn_del.setEnabled(false);
        }else {
            //交接状态下
            CheckScanboxlist();
        }

        GetArriveTime();
    }

    private void GetArriveTime() {

    }

    private void CheckScanboxlist() {
        GetTimeIDtoSP(false);
        displayboxList = YLtransferDataOperate.getTransferingboxes();
        DisplayBoxData(displayboxList);
    }

    private void GetTimeIDtoSP(boolean b) {
        List<String> TimeidList = new ArrayList<>();
        if (b){
            TimeidList = yfdo.SpTimeID(ChooseSite.getSiteID());
            SitetimeID = TimeidList.get(0);
        }else {
            TimeidList.add(SitetimeID);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_spinner_item,TimeidList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yltransferedi_sp_TimeID.setAdapter(adapter);
        yltransferedi_sp_TimeID.setPrompt("到达顺序");
    }

    private void Recheckboxlist(Site site) {
        GetTimeIDtoSP(true);
        displayboxList = yfdo.EditerBoxDisplay(site,SitetimeID);
        DisplayBoxData(displayboxList);
    }


    private void DisplayBoxData(List<Box> boxList){
        if (boxList ==null)return;
        ylBoxEdiAdapter = new YLBoxEdiAdapter(this, boxList,R.layout.activity_boxedititem);
        ylBoxEdiAdapter.setSelectItem(indext);
        yltransferedi_listview.setAdapter(ylBoxEdiAdapter);
        TallyBox();
    }

    private void TallyBox() {
        List<Integer> list = analysisBoxList.AnsysisBoxList(displayboxList);
        String getandgive = "收箱："+list.get(4)+"    送箱："+list.get(5);
        String emptyandfull = "实箱："+list.get(6)+"    空箱: "+list.get(7);
        String moneyandcard = "款箱："+list.get(0)+"    卡箱："+list.get(1);
        String vouchandvouchbag = "凭箱："+list.get(2)+"    凭袋："+list.get(3);

        yltransferedi_tv_gog.setText(getandgive);
        yltransferedi_tv_eof.setText(emptyandfull);
        yltransferedi_tv_mac.setText(moneyandcard);
        yltransferedi_tv_vab.setText(vouchandvouchbag);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.yltransferedi_btn_enter://保存确定
                SaveData();
                break;
            case R.id.yltransferedi_btn_back://不保存数据返回
                NoSaveData();
                break;
            case R.id.yltransferedi_btn_del://删除款箱
                DeleteBox(indext);
                break;
        }
    }

    private void NoSaveData() {

    }

    private void SaveData() {
//        List<Box> all = YLtransferDataOperate.AllTransferboxes;
//        for (int i = 0; i < all.size(); i++) {
//            if (all.get(i).TaskTimeID == TaskTimeID){
//                all.remove(i);
//                i--;
//            }
//        }
//        all.addAll(displayboxList);
    }

    private void DeleteBox(int indext) {
        if (displayboxList.size() ==0)return;
        Box box = displayboxList.get(indext);
        if (box.getTradeAction().equals("收")){
            ycbo.RemoveCarBox(box.getBoxID());
        }else {
            ycbo.AddBoxOnEditeCarBox(box);
        }
        displayboxList.remove(indext);
        ylBoxEdiAdapter.setSelectItem(0);
        ylBoxEdiAdapter.notifyDataSetChanged();
    }
}