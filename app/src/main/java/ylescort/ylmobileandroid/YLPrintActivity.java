package ylescort.ylmobileandroid;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hdhe.nfc.NFCcmdManager;
import com.example.nfc.util.Tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import TaskClass.ArriveTime;
import TaskClass.Box;
import TaskClass.BoxCombyOrder;
import TaskClass.BoxCombyTime;
import TaskClass.GatherPrint;
import TaskClass.Site;
import TaskClass.TasksManager;
import TaskClass.YLTask;
import YLAdapter.YLBoxEdiAdapter;
import YLDataService.AnalysisBoxList;
import YLDataService.YLBoxScanCheck;
import YLDataService.YLSiteInfo;
import YLPrinter.YLPrint;
import YLSystemDate.YLEditData;
import YLSystemDate.YLMediaPlayer;
import YLSystemDate.YLSysTime;
import YLSystemDate.YLSystem;

/**
 * Created by Administrator on 2016-03-15.
 */
public class YLPrintActivity extends YLBaseActivity implements View.OnClickListener {

    private Spinner ylprinter_sp;
    private ListView ylprinter_lv;
    private Button ylprinter_btn_gather;
    private Button ylprinter_btn_detail;
    private Button ylprinter_btn_cancel;
    private Button ylprinter_btn_readhf;
    private TextView ylprinter_tv_give;
    private TextView ylprinter_tv_get;

    private List<Box> list ;
    private List<Box> displaylistbox;
    private GatherPrint gatherPrint;
    private YLPrint ylPrint;
    private TasksManager tasksManager = null;
    private YLTask ylTask;
    private Site site;
    private String TradeTime;

    private YLBoxEdiAdapter ylBoxEdiAdapter;
    private AnalysisBoxList analysisBoxList;
    private YLSiteInfo ylSiteInfo;

    private NFCcmdManager manager ;
    private YLMediaPlayer ylMediaPlayer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ylprinter);
        InitLayout();
        InitData();
        InitHFreader();
    }

    @Override
    protected void InitLayout() {
        ylprinter_sp = (Spinner) findViewById(R.id.ylprinter_sp);
        ylprinter_lv = (ListView) findViewById(R.id.ylprinter_lv);
        ylprinter_btn_gather = (Button) findViewById(R.id.ylprinter_btn_gather);
        ylprinter_btn_detail = (Button) findViewById(R.id.ylprinter_btn_detail);
        ylprinter_btn_cancel = (Button) findViewById(R.id.ylprinter_btn_cancel);
        ylprinter_btn_readhf = (Button) findViewById(R.id.ylprinter_btn_readhf);
        ylprinter_tv_give = (TextView) findViewById(R.id.ylprinter_tv_give);
        ylprinter_tv_get = (TextView) findViewById(R.id.ylprinter_tv_get);

        ylprinter_btn_gather.setOnClickListener(this);
        ylprinter_btn_detail.setOnClickListener(this);
        ylprinter_btn_cancel.setOnClickListener(this);
        ylprinter_btn_readhf.setOnClickListener(this);
        ylprinter_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String TimeID = adapterView.getItemAtPosition(i).toString();
                displaylistbox.clear();
                for (Box box : list) {
                    if (box.getTimeID().equals(TimeID)) {
                        displaylistbox.add(box);
                    }
                }

                TradeTime = site.getLstArriveTime().get(i).ATime;

                ylBoxEdiAdapter.notifyDataSetChanged();
                if (displaylistbox.size() > 0) {
                    BoxCombyOrder ylBoxComparator = new BoxCombyOrder();
                    Collections.sort(displaylistbox, ylBoxComparator);
                    TallyBox(displaylistbox);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    protected void InitData() {

        try {
            tasksManager = YLSystem.getTasksManager();
            ylTask = tasksManager.CurrentTask;
            site = YLEditData.getPrintSite();
            ylPrint = new YLPrint();
            list = new ArrayList<>();
            displaylistbox = new ArrayList<>();
            analysisBoxList = new AnalysisBoxList();
            ylSiteInfo = new YLSiteInfo(getApplicationContext());
            ylMediaPlayer = new YLMediaPlayer(getApplicationContext());

            String siteID = site.getSiteID();

            for (Box box : ylTask.lstBox) {
                if (box.getSiteID().equals(siteID)){
                    list.add(box);
                }
            }

            GetTimeID();

            DisplayListBox(displaylistbox);

            ylPrint.InitBluetooth();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void InitHFreader() {
        try{
            manager = NFCcmdManager.getNFCcmdManager(YLSystem.getHFport(), 115200, 0);
            manager.readerPowerOn();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "HF初始化失败", Toast.LENGTH_SHORT).show();
        }
    }


    private void DisplayListBox(List<Box> displaylistbox) {
        if (displaylistbox ==null)return;
        ylBoxEdiAdapter = new YLBoxEdiAdapter(this, displaylistbox,R.layout.activity_boxedititem);
        ylBoxEdiAdapter.setSelectItem(0);
        ylprinter_lv.setAdapter(ylBoxEdiAdapter);
    }

    private void TallyBox(List<Box> boxlist){
        if (boxlist == null)return;

        List<Integer> boxlistansy =  analysisBoxList.AnsysisBoxListForPrintGather(boxlist);


        String getboxstr = "收箱:"+boxlistansy.get(0)+" 其中:收实箱 "+boxlistansy.get(1)+" 收空箱 "+boxlistansy.get(2);
        String giveboxstr ="送箱: "+boxlistansy.get(3)+" 其中:送实箱 "+boxlistansy.get(4)+" 送空箱 "+boxlistansy.get(5);

        ylprinter_tv_get.setText(getboxstr);
        ylprinter_tv_give.setText(giveboxstr);
    }

    private void GetTimeID() {
        ArrayList<String> TimeIDlist = new ArrayList<>();

        String TimeID ="";

        for (Box box : list) {
            if (box.getTimeID().equals(TimeID)){
                TimeID = box.getTimeID();
            }else {
                TimeID = box.getTimeID();
                TimeIDlist.add(TimeID);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_spinner_item,TimeIDlist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ylprinter_sp.setAdapter(adapter);
        ylprinter_sp.setPrompt("到达顺序");

        String FristTimeID =  ylprinter_sp.getItemAtPosition(0).toString();

        for (Box box : list) {
            if (box.getTimeID().equals(FristTimeID)){
                displaylistbox.add(box);
            }
        }

    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.ylprinter_btn_gather:

                    PrintGather();

                    break;
                case R.id.ylprinter_btn_detail:

                    PrintDetail();

                    break;
                case R.id.ylprinter_btn_readhf:

                    CheckSietHF();

                    break;

                case R.id.ylprinter_btn_cancel:
                    finish();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case 131:
                CheckSietHF();
                break;
            case 132:
                CheckSietHF();
                break;
        }

        return super.onKeyDown(keyCode, event);
    }

    private Boolean CheckSietHF(){
        manager.init_14443A();
        byte[] uid = manager.inventory_14443A();
        if (uid!= null){
            String readhf = Tools.Bytes2HexString(uid, uid.length);
            if (readhf.equals(site.getServerReturn())){
                ylMediaPlayer.SuccessOrFail(true);
                return true;
            }else {
                ylMediaPlayer.SuccessOrFail(false);
            }
        }else {
            ylMediaPlayer.SuccessOrFail(false);
        }
        return  false;
    }

    private void PrintDetail() throws  Exception{

        List<Box> detaillist = new ArrayList<>();
        for (Box box : displaylistbox) {
//            if (!box.getBoxTaskType().equals("早送晚收")){
                detaillist.add(box);
//            }
        }
        if (detaillist.size()>0) {
            String TaskTimeID = detaillist.get(0).getTaskTimeID()+"";

//        String TaskTimeID2 = String.format("%02d",TaskTimeID);
            if (TaskTimeID.length()== 1){
                TaskTimeID = "0"+TaskTimeID;
            }
            String Number = ylTask.getTaskID()+TaskTimeID;

            AnalysisBoxList analysisBoxList = new AnalysisBoxList();
            gatherPrint = analysisBoxList.AnsysisBoxListForPrint(displaylistbox);
            String Client = ylSiteInfo.GetClientbySiteID(site.getSiteID());
            gatherPrint.setSiteName(site.getSiteName());
            gatherPrint.setClintName(Client);
            gatherPrint.setTradeTime(YLSysTime.GetStrCurrentTime());
            gatherPrint.setCarNumber(ylTask.getTaskCar());
            gatherPrint.setTaskNumber("NO." + ylTask.getTaskID() + TaskTimeID);
            gatherPrint.setHomName(ylTask.getTaskManagerNo() +"-"+ ylTask.getTaskManager());

            ylPrint.PrintDetail(detaillist,1,gatherPrint,YLSystem.getUser().getEmpNO()+"-"+YLSystem.getUser().getName());
        }
    }

    private void PrintGather()throws Exception{

        String TaskTimeID = displaylistbox.get(0).getTaskTimeID()+"";

//        String TaskTimeID2 = String.format("%02d",TaskTimeID);
        if (TaskTimeID.length()== 1){
            TaskTimeID = "0"+TaskTimeID;
        }

        AnalysisBoxList analysisBoxList = new AnalysisBoxList();
        gatherPrint = analysisBoxList.AnsysisBoxListForPrint(displaylistbox);
        String Client = ylSiteInfo.GetClientbySiteID(site.getSiteID());
        gatherPrint.setSiteName(site.getSiteName());
        gatherPrint.setClintName(Client);
        gatherPrint.setTradeTime(YLSysTime.GetStrCurrentTime());
        gatherPrint.setCarNumber(ylTask.getTaskCar());
        gatherPrint.setTaskNumber("NO." + ylTask.getTaskID() + TaskTimeID);
        gatherPrint.setHomName(ylTask.getTaskManagerNo() +"-"+ ylTask.getTaskManager());

        ylPrint.PrintGather(gatherPrint,1);

    }


    @Override
    protected void onStop() {
        manager.readerPowerOff();
        super.onStop();
    }

    @Override
    protected void onPostResume() {
        manager = NFCcmdManager.getNFCcmdManager(YLSystem.getHFport(), 115200, 0);
        manager.readerPowerOn();
        super.onPostResume();
    }



}
