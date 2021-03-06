package ylescort.ylmobileandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import TaskClass.Box;
import TaskClass.GatherPrint;
import TaskClass.TasksManager;
import TaskClass.User;
import TaskClass.YLTask;
import YLAdapter.YLBoxEdiAdapter;
import YLDataService.AnalysisBoxList;
import YLDataService.WebService;
import YLPrinter.YLPrint;
import YLSystemDate.YLRecord;
import YLSystemDate.YLSystem;
import ylescort.ylmobileandroid.R;


public class HandovermanCheck extends ActionBarActivity implements View.OnClickListener {

    private TextView handoverman_tv_Title;
    private ListView handoverman_listview;
    private TextView handoverman_tv_monenybox;
    private TextView handoverman_tv_cardbox;
    private TextView handoverman_tv_voucher;
    private TextView handoverman_tv_voucherbag;
    private TextView handoverman_tv_getbox;
    private TextView handoverman_tv_givebox;
    private TextView handoverman_tv_fullbox;
    private TextView handoverman_tv_emptybox;
    private Button handoverman_btn_cancel;
    private Button handoverman_btn_enter;
    private Button handoverman_btn_carbox;
    private Button handoverman_btn_invalut;

    private TasksManager tasksManager = null;//任务管理类
    private YLTask ylTask;//当前选中的任务

    private List<Box> carlist;
    private List<Box> alllist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handoverman_check);
        try {
            InitView();
            InitData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitView() {

        Bundle bundle = this.getIntent().getExtras();
        String taskid = bundle.getString("taskid");
        String taskName = bundle.getString("taskName");

        handoverman_tv_Title = (TextView) findViewById(R.id.handoverman_tv_Title);
        handoverman_tv_monenybox = (TextView) findViewById(R.id.handoverman_tv_monenybox);
        handoverman_tv_cardbox = (TextView) findViewById(R.id.handoverman_tv_cardbox);
        handoverman_tv_voucher = (TextView) findViewById(R.id.handoverman_tv_voucher);
        handoverman_tv_voucherbag = (TextView) findViewById(R.id.handoverman_tv_voucherbag);
        handoverman_tv_getbox = (TextView) findViewById(R.id.handoverman_tv_getbox);
        handoverman_tv_givebox = (TextView) findViewById(R.id.handoverman_tv_givebox);
        handoverman_tv_fullbox = (TextView) findViewById(R.id.handoverman_tv_fullbox);
        handoverman_tv_emptybox = (TextView) findViewById(R.id.handoverman_tv_emptybox);
        handoverman_listview = (ListView) findViewById(R.id.handoverman_listview);
        handoverman_btn_cancel = (Button) findViewById(R.id.handoverman_btn_cancel);
        handoverman_btn_enter = (Button) findViewById(R.id.handoverman_btn_enter);
        handoverman_btn_carbox = (Button) findViewById(R.id.handoverman_btn_carbox);
        handoverman_btn_invalut = (Button)findViewById(R.id.handoverman_btn_invalut);

        handoverman_tv_Title.setText(taskName);handoverman_tv_Title.setTag(taskid);

        handoverman_btn_enter.setOnClickListener(this);
        handoverman_btn_cancel.setOnClickListener(this);
        handoverman_btn_carbox.setOnClickListener(this);
        handoverman_btn_invalut.setOnClickListener(this);
    }


    private void InitData() throws Exception{

        tasksManager= YLSystem.getTasksManager();//获取任务管理类
        ylTask=tasksManager.CurrentTask;//当前选中的任务
        carlist = new ArrayList<>();
        if (ylTask.getLstCarBox() != null){
            carlist = ylTask.getLstCarBox();
            for (int i = 0; i < carlist.size(); i++) {
                carlist.get(i).setBoxOrder((i+1)+"");
            }
        }
        alllist = new ArrayList<>();
    }

    private void Analysis(List<Box> boxList) {

        AnalysisBoxList analysisBoxList = new AnalysisBoxList();
        List<Integer> stringList =  analysisBoxList.AnsysisBoxList(boxList);
        handoverman_tv_monenybox.setText("款箱:" + stringList.get(0));
        handoverman_tv_cardbox.setText("卡箱:"+stringList.get(1));
        handoverman_tv_voucher.setText("凭证箱:"+stringList.get(2));
        handoverman_tv_voucherbag.setText("凭证袋:"+stringList.get(3));
//        handoverman_tv_getbox.setText("收箱:"+stringList.get(4));
//        handoverman_tv_givebox.setText("送箱:" + stringList.get(5));
        String getboxtext =handoverman_tv_getbox.getText().toString() + (stringList.get(4)+stringList.get(5));
        handoverman_tv_getbox.setText(getboxtext);
        handoverman_tv_fullbox.setText("实箱:" + stringList.get(6));
        handoverman_tv_emptybox.setText("空箱:" + stringList.get(7));
    }

    private void BoxCheckAdapter(List<Box> boxList){
        if (boxList == null)return;
        YLBoxEdiAdapter ylBoxEdiAdapter = new YLBoxEdiAdapter(getApplicationContext(),
                boxList,R.layout.activity_boxedititem);
        handoverman_listview.setAdapter(ylBoxEdiAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_handoverman_check, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.handoverman_btn_enter:
                try {
                    ComfirmStoreIn();
                    YLRecord.WriteRecord("申请","确认入库");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.handoverman_btn_cancel:
                finish();
                YLRecord.WriteRecord("申请","返回");
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
            case R.id.handoverman_btn_carbox:
                handoverman_tv_getbox.setText("车内箱数：");
                BoxCheckAdapter(carlist);
                if (carlist != null){
                    Analysis(carlist);
                }
                YLRecord.WriteRecord("申请","查看车内款箱");
                break;
            case R.id.handoverman_btn_invalut:
//                try {
//                    WebService webService = new WebService();
//                    alllist =  webService.GetVaultInBoxList(handoverman_tv_Title.getTag().toString(),
//                            YLSystem.getUser().getDeviceID(),YLSystem.getUser().getEmpID(),getApplicationContext());
//                    if (alllist.size()==1& alllist.get(0).getServerReturn().equals("没有入库箱。")){
//                        alllist.clear();
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                handoverman_tv_getbox.setText("应入库箱总数：");
//                BoxCheckAdapter(alllist);
//                if (alllist.size()==0)return;
//                Analysis(alllist);
                try {
                    PrintVaultsheet();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void PrintVaultsheet() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HandovermanCheck.this);
        builder.setTitle("金库入库打印");
        final String[] multiChoiceItems = {"入库汇总数","入库清单表"};
        final boolean[] defaultSelectedStatus = {true,false};
        builder.setMultiChoiceItems(multiChoiceItems, defaultSelectedStatus, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                defaultSelectedStatus[i] = b;
            }
        });
        builder.setPositiveButton("打印", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (defaultSelectedStatus[0]){
                    PrintGather();
                    YLRecord.WriteRecord("申请","打印入库汇总");
                }
                if (defaultSelectedStatus[1]){
                    PrintDetail();
                    YLRecord.WriteRecord("申请","打印入库明细");
                }
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

    private void PrintDetail() {
        try {
            YLPrint ylPrint = new YLPrint();
            ylPrint.InitBluetooth();
            List<Box> boxes = new ArrayList<>();
            if (ylTask.lstCarBox != null){
                boxes = ylTask.lstCarBox;
            }

            boxes = orderboxlist(boxes);

            AnalysisBoxList analysisBoxList = new AnalysisBoxList();
            GatherPrint gatherPrint = analysisBoxList.AnsysisBoxListForPrint(boxes);
            gatherPrint.setSiteName("");
            gatherPrint.setClintName("所属基地:"+ YLSystem.getBaseName());
            gatherPrint.setTradeTime("任务日期:"+ylTask.getTaskDate());
            gatherPrint.setCarNumber("");
            gatherPrint.setTaskNumber("NO."+ylTask.getTaskID());
            gatherPrint.setHomName(YLSystem.getUser().getEmpNO()+"-"+YLSystem.getUser().getName());
            gatherPrint.setTaskLine("任务线路:"+ylTask.getLine());

            ylPrint.PrintDetail2(boxes,2,gatherPrint);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Box> OrderByBoxList(List<Box> boxes) {
        List<Box> boxorder = new ArrayList<>();

        for (int i = 0; i < boxes.size(); i++) {
            Box box = boxes.get(i);

            if (box.getBoxTaskType().equals("寄库箱")
                    & box.getBoxStatus().equals("实")){
                boxorder.add(box);
                boxes.remove(i);
                continue;
            }
            if (box.getBoxTaskType().equals("早送晚收")
                    & box.getBoxStatus().equals("实")){
                boxorder.add(box);
                boxes.remove(i);
                continue;
            }
            if (box.getBoxTaskType().equals("企业收送款")
                    & box.getBoxStatus().equals("实")){
                boxorder.add(box);
                boxes.remove(i);
                continue;
            }
            if (box.getBoxTaskType().equals("同行调拨")
                    & box.getBoxStatus().equals("实")){
                boxorder.add(box);
                boxes.remove(i);
                continue;
            }
            if (box.getBoxTaskType().equals("跨行调拨")
                    & box.getBoxStatus().equals("实")){
                boxorder.add(box);
                boxes.remove(i);
                continue;
            }
            if (box.getBoxTaskType().equals("上下介")
                    & box.getBoxStatus().equals("空")){
                boxorder.add(box);
                boxes.remove(i);
                continue;
            }
            if (box.getBoxTaskType().equals("寄库箱")
                    & box.getBoxStatus().equals("空")){
                boxorder.add(box);
                boxes.remove(i);
                continue;
            }
            if (box.getBoxTaskType().equals("早送晚收")
                    & box.getBoxStatus().equals("空")){
                boxorder.add(box);
                boxes.remove(i);
                continue;
            }
            if (box.getBoxTaskType().equals("企业收送款")
                    & box.getBoxStatus().equals("空")){
                boxorder.add(box);
                boxes.remove(i);
                continue;
            }
            if (box.getBoxTaskType().equals("同行调拨")
                    & box.getBoxStatus().equals("空")){
                boxorder.add(box);
                boxes.remove(i);
                continue;
            }
            if (box.getBoxTaskType().equals("跨行调拨")
                    & box.getBoxStatus().equals("空")){
                boxorder.add(box);
                boxes.remove(i);
            }
        }

        return  boxorder;
    }

    private List<Box> orderboxlist(List<Box> boxes){
        List<Box> boxList = new ArrayList<>();
        for (Box box : boxes) {
            Box box1 = new Box(box);
            boxList.add(box1);
        }

        List<Box> newboxes = new ArrayList<>();
        for (int i = 0; i < boxList.size(); i++) {
            Box box = boxList.get(i);
            if (box.getBoxTaskType().equals("上下介")
                    &  box.getBoxStatus().equals("实")){
                newboxes.add(box);
                boxList.remove(i);
                i--;
            }
        }
        for (int i = 0; i < boxList.size(); i++) {
            Box box = boxList.get(i);
            if (box.getBoxTaskType().equals("寄库箱")
                    &  box.getBoxStatus().equals("实")){
                newboxes.add(box);
                boxList.remove(i);
                i--;
            }
        }
        for (int i = 0; i < boxList.size(); i++) {
            Box box = boxList.get(i);
            if (box.getBoxTaskType().equals("早送晚收")
                    &  box.getBoxStatus().equals("实")){
                newboxes.add(box);
                boxList.remove(i);
                i--;
            }
        }
        for (int i = 0; i < boxList.size(); i++) {
            Box box = boxList.get(i);
            if (box.getBoxTaskType().equals("企业收送款")
                    &  box.getBoxStatus().equals("实")){
                newboxes.add(box);
                boxList.remove(i);
                i--;
            }
        }
        for (int i = 0; i < boxList.size(); i++) {
            Box box = boxList.get(i);
            if (box.getBoxTaskType().equals("同行调拨")
                    &  box.getBoxStatus().equals("实")){
                newboxes.add(box);
                boxList.remove(i);
                i--;
            }
        }
        for (int i = 0; i < boxList.size(); i++) {
            Box box = boxList.get(i);
            if (box.getBoxTaskType().equals("跨行调拨")
                    &  box.getBoxStatus().equals("实")){
                newboxes.add(box);
                boxList.remove(i);
                i--;
            }
        }
        for (int i = 0; i < boxList.size(); i++) {
            Box box = boxList.get(i);
            if (box.getBoxTaskType().equals("上下介")
                    &  box.getBoxStatus().equals("空")){
                newboxes.add(box);
                boxList.remove(i);
                i--;
            }
        }
        for (int i = 0; i < boxList.size(); i++) {
            Box box = boxList.get(i);
            if (box.getBoxTaskType().equals("寄库箱")
                    &  box.getBoxStatus().equals("空")){
                newboxes.add(box);
                boxList.remove(i);
                i--;
            }
        }
        for (int i = 0; i < boxList.size(); i++) {
            Box box = boxList.get(i);
            if (box.getBoxTaskType().equals("早送晚收")
                    &  box.getBoxStatus().equals("空")){
                newboxes.add(box);
                boxList.remove(i);
                i--;
            }
        }
        for (int i = 0; i < boxList.size(); i++) {
            Box box = boxList.get(i);
            if (box.getBoxTaskType().equals("企业收送款")
                    &  box.getBoxStatus().equals("空")){
                newboxes.add(box);
                boxList.remove(i);
                i--;
            }
        }
        for (int i = 0; i < boxList.size(); i++) {
            Box box = boxList.get(i);
            if (box.getBoxTaskType().equals("同行调拨")
                    &  box.getBoxStatus().equals("空")){
                newboxes.add(box);
                boxList.remove(i);
                i--;
            }
        }
        for (int i = 0; i < boxList.size(); i++) {
            Box box = boxList.get(i);
            if (box.getBoxTaskType().equals("跨行调拨")
                    &  box.getBoxStatus().equals("空")){
                newboxes.add(box);
                boxList.remove(i);
                i--;
            }
        }

        Log.e(YLSystem.getKimTag(),"款箱数量"+ newboxes.size());
        return newboxes;
    }


    private void PrintGather() {
        try {
            YLPrint ylPrint = new YLPrint();
            ylPrint.InitBluetooth();
            List<Box> boxes = new ArrayList<>();
            if (ylTask.lstCarBox != null){
                boxes = ylTask.lstCarBox;
            }
            AnalysisBoxList analysisBoxList = new AnalysisBoxList();
            GatherPrint gatherPrint = analysisBoxList.AnsysisBoxListForPrint(boxes);
            gatherPrint.setSiteName("");
            gatherPrint.setClintName("所属基地:"+ YLSystem.getBaseName());
            gatherPrint.setTradeTime("任务日期:"+ylTask.getTaskDate());
            gatherPrint.setCarNumber("");
            gatherPrint.setTaskNumber("NO."+ylTask.getTaskID());
            gatherPrint.setHomName(YLSystem.getUser().getEmpNO()+"-"+YLSystem.getUser().getName());
            gatherPrint.setTaskLine(ylTask.getLine());
            ylPrint.PrintGather(gatherPrint,2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ComfirmStoreIn()throws Exception{
        WebService webService = new WebService();
        User user = new User();
        user = YLSystem.getUser();
        user.setTaskDate(handoverman_tv_Title.getTag().toString());
        String SerRetrun =  webService.ComfirmStoreIn(user, getApplicationContext());
        Log.e(YLSystem.getKimTag(),SerRetrun+"");
        if (SerRetrun.equals("1")){
            Toast.makeText(getApplicationContext(),"已申请成功！",Toast.LENGTH_SHORT).show();
            finish();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }else {
            Toast.makeText(getApplicationContext(),"未能申请成功，请连接有效网络再申请",Toast.LENGTH_SHORT).show();
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
