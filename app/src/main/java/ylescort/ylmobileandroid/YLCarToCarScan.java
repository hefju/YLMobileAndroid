package ylescort.ylmobileandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import TaskClass.Box;
import TaskClass.TasksManager;
import TaskClass.YLTask;
import YLAdapter.YLValutboxitemAdapter;
import YLDataService.YLBoxScanCheck;
import YLSystemDate.YLEditData;
import YLSystemDate.YLMediaPlayer;
import YLSystemDate.YLRecord;
import YLSystemDate.YLSysTime;
import YLSystemDate.YLSystem;

public class YLCarToCarScan extends YLBaseScanActivity implements View.OnClickListener {


    private TextView ylcartocarscan_tv_title;
    private TextView ylcartocarscan_analysis;
    private ListView ylcartocarscan_lv;
    private Button ylcartocarscan_btn_sacn;
    private Button ylcartocarscan_btn_upload;

    private List<Box> DisplayboxList;
    private List<Box> CarBoxlist;
    private String Scanstaut;
    private YLValutboxitemAdapter ylBoxAdapter;
    private YLMediaPlayer ylMediaPlayer;
    private String SelfTaskID;
    private String OtherTaskID;

    private TasksManager tasksManager = null;//任务管理类
    private YLTask ylTask;//当前选中的任务

    private int giveorget;//1代表送，2代表收

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ylcar_to_car_scan);
        InitLayout();
        InitData();
    }

    @Override
    protected void InitLayout() {
        ylcartocarscan_tv_title = (TextView) findViewById(R.id.ylcartocarscan_tv_title);
        ylcartocarscan_analysis = (TextView)findViewById(R.id.ylcartocarscan_analysis);
        ylcartocarscan_lv = (ListView) findViewById(R.id.ylcartocarscan_lv);
        ylcartocarscan_btn_sacn = (Button) findViewById(R.id.ylcartocarscan_btn_sacn);
        ylcartocarscan_btn_upload = (Button) findViewById(R.id.ylcartocarscan_btn_upload);

        ylcartocarscan_btn_sacn.setOnClickListener(this);
        ylcartocarscan_btn_upload.setOnClickListener(this);
        ylcartocarscan_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (giveorget == 1) {
                    DeleteBoxinList(i);
                } else {
                    EditBoxinList(i);
                }

            }
        });

    }

    private void EditBoxinList(final int position) {
        new AlertDialog.Builder(this).setTitle("请选择类型").setIcon
                (android.R.drawable.ic_dialog_info).setSingleChoiceItems(R.array.ylboxfullorempty, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Box box = DisplayboxList.get(position);
                        switch (which) {
                            case 0:
                                box.setBoxStatus("实");
                                box.setValutcheck("核");
                                DisplayboxList.set(position, box);
                                break;
                            case 1:
                                box.setBoxStatus("空");
                                box.setValutcheck("核");
                                DisplayboxList.set(position, box);
                                break;
                            case 2:
                                Box removebox = DisplayboxList.get(position);
                                if (removebox.getValutcheck().equals("多") ||
                                        removebox.getValutcheck().equals("核")) {
                                    DisplayboxList.remove(position);
                                }
                                break;
                        }
                        analysisBoxList();
                        ylBoxAdapter.notifyDataSetChanged();

                        dialog.dismiss();
                    }
                }).show();
    }

    private void DeleteBoxinList(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(YLCarToCarScan.this);
        builder.setMessage("确认删除?");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                YLRecord.WriteRecord("车组交接扫描","删除箱"+DisplayboxList.get(position));
                DisplayboxList.remove(position);
                Log.e(YLSystem.getKimTag(), DisplayboxList.size() + "数量");
                analysisBoxList();
                ylBoxAdapter.notifyDataSetChanged();
                dialog.dismiss();
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



    @Override
    protected void InitData() {

        ylMediaPlayer = new YLMediaPlayer();

        CarBoxlist = YLEditData.getYlcarbox();
        OtherTaskID = YLEditData.getCarToCarylTask().getTaskID();
        SelfTaskID = YLEditData.getYlTask().getTaskID();
//        MyLog("对方任务ID"+OtherTaskID+"自身任务ID"+SelfTaskID);

        tasksManager = YLSystem.getTasksManager();
        ylTask = tasksManager.CurrentTask;

        DisplayboxList = new ArrayList<>();

        giveorget = YLEditData.getCarToCargetorgive();
        String Title = "";
        if (giveorget == 1) {
            Title = "送箱扫描";

        } else {
            Title = "接箱扫描";
        }

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("TaskID", SelfTaskID);
            jsonObject.put("TimeID",YLEditData.getCarToCargetorgive());
            jsonObject.put("tTaskID", OtherTaskID);
            jsonObject.put("empid",YLSystem.getUser().getEmpID());
            jsonObject.put("deviceID",YLSystem.getHandsetIMEI());
            jsonObject.put("ISWIFI",YLSystem.getNetWorkState());
            String url = YLSystem.GetBaseUrl(getApplicationContext())+"GetBoxByTaskIDCarToCar";

            YLWebDataAsyTaskForeground Y = new YLWebDataAsyTaskForeground(jsonObject,url,2) {
                @Override
                protected void onPostExecute(String s) {
                    YLProgressDialog.dismiss();
                    DisplayboxList =  gson.fromJson(s, new TypeToken<List<Box>>() {
                    }.getType());
                    if (DisplayboxList == null){
                        DisplayboxList = new ArrayList<>();
                    }else {
                        if (DisplayboxList.get(0).getServerReturn().equals("0")){
                            DisplayboxList = new ArrayList<>();
                        }
                    }
                    LoadBoxData(DisplayboxList);
                }
            };
            Y.execute();
            Y.doInBackground();
        }catch (Exception e){
            e.printStackTrace();
        }
//        ylcartocarscan_tv_title.setText(Title);
        this.setTitle(Title);
        Scanstaut = "扫描";

    }

    @Override
    public void YLPutdatatoList(String recivedata) {

        Box dbox = YLBoxScanCheck.CheckBoxbyUHF(recivedata,getApplicationContext());
        if (dbox.getBoxName().equals("无数据")){
            ylMediaPlayer.SuccessOrFailMidia("fail",getApplicationContext());
            return;
        }

        if (YLEditData.getCarToCargetorgive() == 1){
            for (Box box : DisplayboxList) {
                if (box.getBoxID().equals(recivedata)){
                    ylMediaPlayer.SuccessOrFailMidia("fail",getApplicationContext());
                    return;
                }
            }
            GiveScan(dbox);
        }else {
            GetScan(dbox);
        }
    }

    private void GetScan(Box box) {
        boolean checkbox = true;
        for (int i = 0; i < DisplayboxList.size(); i++) {
             if (DisplayboxList.get(i).getBoxID().equals(box.getBoxID())){
                 Box getbox = DisplayboxList.get(i);
                 if (getbox.getValutcheck().equals("多")||getbox.getValutcheck().equals("核")){
                     checkbox = false;
                     ylMediaPlayer.SuccessOrFailMidia("fail", getApplicationContext());
                     break;
                 }
                 DisplayboxList.get(i).setValutcheck("对");
                 YLRecord.WriteRecord("车组交接扫描","收箱扫描对"+DisplayboxList.get(i).getBoxName());
                 ylMediaPlayer.SuccessOrFailMidia("success", getApplicationContext());
                 checkbox = false;
                 break;
             }
        }

        if (checkbox){
            box.setValutcheck("多");
            box.setTradeAction("收");
            box.setActionTime(YLSysTime.GetStrCurrentTime());
            box.setTimeID("2");
            box.setBoxCount("1");
            YLRecord.WriteRecord("车组交接扫描","收箱扫描多"+box.getBoxName());
            DisplayboxList.add(box);
            ylMediaPlayer.SuccessOrFailMidia("success", getApplicationContext());
        }
        analysisBoxList();
        ylBoxAdapter.notifyDataSetChanged();
    }

    private void GiveScan(Box box) {
        for (Box carbox : CarBoxlist) {
            if (carbox.getBoxID().equals(box.getBoxID())){
                Box newbox = carbox;
                newbox.setBaseValutIn(OtherTaskID);
                YLRecord.WriteRecord("车组交接扫描","送箱扫描"+newbox.getBoxName());
                DisplayboxList.add(newbox);
                ylMediaPlayer.SuccessOrFailMidia("success", getApplicationContext());
                break;
            }
        }
        analysisBoxList();
        ylBoxAdapter.notifyDataSetChanged();
    }

    private void analysisBoxList(){
        String str = "";
        if (giveorget == 1){
            str ="送箱数量:"+ DisplayboxList.size();
            ylcartocarscan_analysis.setText(str);
        }else {
            int count = 0;
            int total = 0;
            for (Box box : DisplayboxList) {
                String check = box.getValutcheck();
                if (check.equals("对")|| check.equals("多")|| check.equals("核")){
                    count ++;
                }
                if (check.equals("对")||check.equals("")){
                    total++;
                }
                str = "应收数量:"+ total+" 扫描数量："+count;
                ylcartocarscan_analysis.setText(str);
            }
        }
    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.ylcartocarscan_btn_sacn:
                    ScanCmd();
                    break;
                case R.id.ylcartocarscan_btn_upload:
                    YLRecord.WriteRecord("车组交接扫描","数据上传");
                    UpLoadData();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void UpLoadData()throws  Exception{

        MyLog(giveorget+"收送");
        if (giveorget== 2){
            for (Box box : DisplayboxList) {
                if (box.getValutcheck().equals("多")){
                    YLMessagebox("有款箱未设置空实状态，请设置后上传");
                    return;
                }
            }
        }

        final YLTask ylTask = new YLTask();
        ylTask.setLstBox(DisplayboxList);
        ylTask.setTaskID(SelfTaskID);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("STask",gson.toJson(ylTask));
        jsonObject.put("empid",YLSystem.getUser().getEmpID());
        jsonObject.put("deviceID",YLSystem.getHandsetIMEI());
        jsonObject.put("ISWIFI",YLSystem.getNetWorkState());

        String url = YLSystem.GetBaseUrl(getApplicationContext())+"UpLoadBoxCarToCar";
        YLWebDataAsyTaskForeground y = new YLWebDataAsyTaskForeground(jsonObject,url,2) {
            @Override
            protected void onPostExecute(String s) {
                YLProgressDialog.dismiss();
                String str = gson.fromJson(s,String.class);
                if (str != null){
                    if (str.equals("1")){
                        YLMessagebox("已上传成功");
                        if (giveorget == 1){
                            for (Box box : DisplayboxList) {
                                for (int i = 0; i < CarBoxlist.size(); i++) {
                                    if (CarBoxlist.get(i).getBoxID().equals(box.getBoxID())){
                                        CarBoxlist.remove(i);
                                        --i;
                                    }
                                }
                            }
                        }else {
                            for (Box box : DisplayboxList) {
                                CarBoxlist.add(box);
                            }
                        }
                        MyLog("剩余车内款箱：" + CarBoxlist.size());
//                        DisplayboxList.clear();
                        analysisBoxList();
                        ylBoxAdapter.notifyDataSetChanged();
                        ylTask.setLstCarBox(CarBoxlist);
                        tasksManager.SaveTask(getApplicationContext());

                    }else {
                        YLMessagebox("上传失败，请重新上传");
                    }
                }else {
                    YLMessagebox("上传失败，请重新上传");
                }
            }
        };
        y.execute();
        y.doInBackground();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 131:
                ScanCmd();
                break;
            case 132:
                ScanCmd();
                break;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void ScanCmd() {
        if (Scanstaut.equals("扫描")) {
            Scan1DCmd(2);
            Scanstaut = "停止";
            ylcartocarscan_btn_sacn.setText(Scanstaut);
        } else {
            Scan1DCmd(0);
            Scanstaut = "扫描";
            ylcartocarscan_btn_sacn.setText(Scanstaut);
        }
    }


    private void LoadBoxData(List<Box> boxList) {
        if (boxList== null)return;
        ylBoxAdapter = new YLValutboxitemAdapter(getApplicationContext()
                ,boxList,R.layout.vault_in_detail_boxitem);
        ylcartocarscan_lv.setAdapter(ylBoxAdapter);
        analysisBoxList();
    }

}



