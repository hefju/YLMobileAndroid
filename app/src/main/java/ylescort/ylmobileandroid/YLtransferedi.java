package ylescort.ylmobileandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import TaskClass.ArriveTime;
import TaskClass.BaseBox;
import TaskClass.Box;
import TaskClass.Site;
import YLAdapter.YLBoxEdiAdapter;
import YLDataOperate.YLCarBoxOperate;
import YLDataOperate.YLtransferDataOperate;
import YLDataService.AnalysisBoxList;
import YLSystemDate.YLEditData;
import YLSystemDate.YLRecord;

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
    private List<Box> carboxlist;

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
        try {
            InitLayout();
            InitData();
        } catch (Exception e) {
            MyLog(e.toString());
            e.printStackTrace();
        }
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
                if (YLtransferDataOperate.getSiteTaskTimeID() == 0) {
                    displayboxList = yfdo.EditerBoxDisplay(ChooseSite, SitetimeID);
                    DisplayBoxData(displayboxList);
                }
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

                String tasktype = displayboxList.get(indext).getBoxTaskType();
                yltransferedi_sp_tasktype.setSelection(GetTaskTypeindext(tasktype));

                ylBoxEdiAdapter.notifyDataSetChanged();
            }
        });
    }

    private int GetTaskTypeindext(String tasktype){
        int i = 0;
        switch (tasktype){
            case "早送晚收":i = 0;
                break;
            case "上下介":i = 1;
                break;
            case "同行调拨":i = 2;
                break;
            case "跨行调拨":i = 3;
                break;
            case "企业收送款":i = 4;
                break;
            case "寄库箱":i = 5;
                break;
        }
        return i;
    }

    @Override
    protected void InitData() throws Exception{
        displayboxList = new ArrayList<>();
        carboxlist = new ArrayList<>();
        //获取初始车内款箱数量
        for (Box box : YLCarBoxOperate.getYLEditeCarBoxList()) {
            Box carbox = new Box(box);
            carboxlist.add(carbox);
        }
        arriveTime = new ArriveTime();
        indext = 0;
        analysisBoxList = new AnalysisBoxList();
        yfdo = new YLtransferDataOperate();
        ycbo = new YLCarBoxOperate();
        ChooseSite = YLtransferDataOperate.getChooseSite();
        SitetimeID = YLtransferDataOperate.getSitetimeID();
        yltransferedi_tv_title.setText(ChooseSite.getSiteName());

        //根据网点交接状态获取数据
        MyLog("交接状态:"+YLtransferDataOperate.getSiteTaskTimeID());
        if (YLtransferDataOperate.getSiteTaskTimeID() == 0){
            //不在交接状态下
            Recheckboxlist(ChooseSite);
            yltransferedi_btn_del.setEnabled(false);
        }else {
            //交接状态下
            CheckScanboxlist();
            yltransferedi_btn_del.setEnabled(true);
        }

        GetArriveTime();
    }

    private void GetArriveTime() {

    }

    private void CheckScanboxlist() {
        GetTimeIDtoSP(false);

        for (Box box : YLtransferDataOperate.getTransferingboxes()) {
            Box dbox = new Box(box);
            displayboxList.add(dbox);
        }
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
        for (Box box : yfdo.EditerBoxDisplay(site, SitetimeID)) {
            Box dbox = new Box(box);
            displayboxList.add(dbox);
        }
        DisplayBoxData(displayboxList);
    }


    private void DisplayBoxData(List<Box> boxList){
        if (boxList ==null)return;
        boxList = YLEditData.ListtoSettolist(boxList);
        if (boxList.size()>0) {
            for (int i = 0; i < boxList.size(); i++) {
                boxList.get(i).setBoxOrder(i + 1 + "");
            }
        }
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case 4:NoSaveData();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void NoSaveData() {
        YLCarBoxOperate.setYLCurrectCarBoxList(carboxlist);
        finish();
    }

    private void SaveData() {

        AlertDialog.Builder builder = new AlertDialog.Builder(YLtransferedi.this);
        builder.setMessage("确认保存返回?");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                YLtransferDataOperate.setTransferingboxes(displayboxList);
                finish();
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();

    }

    private void DeleteBox(int delindex) {
        if (displayboxList.size() == 0) return;
        Box box = displayboxList.get(delindex);
        if (box.getTradeAction().equals("收")) {
            ycbo.RemoveCarBox(box.getBoxID());
        } else {
            if (box.getId() == 0){
                ycbo.AddBoxOnEditeCarBox(box);
            }
        }
        YLRecord.WriteRecord("网点编辑","款箱删除:"+box.getBoxName());
        displayboxList.remove(delindex);
        indext = 0;
        ylBoxEdiAdapter.setSelectItem(indext);
        ylBoxEdiAdapter.notifyDataSetChanged();
        TallyBox();
    }
}
