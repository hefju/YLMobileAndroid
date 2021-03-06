package ylescort.ylmobileandroid;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import TaskClass.Box;
import TaskClass.YLTask;
import YLAdapter.YLValutboxitemAdapter;
import YLDataService.WebService;
import YLDataService.YLBoxScanCheck;
import YLSystemDate.YLEditData;
import YLSystemDate.YLMediaPlayer;
import YLSystemDate.YLSysTime;
import YLSystemDate.YLSystem;


public class vault_in_detail extends YLBaseScanActivity implements View.OnClickListener {

    private TextView vault_in_detail_tv_taskname;
    private ListView vault_in_detail_listview;
    private TextView vault_in_detail_tv_tolly;
    private TextView vault_in_detail_tv_check;
    private TextView vault_in_detail_tv_boxname;
    private Button vault_in_detail_btn_scan1d;
    private Button vault_in_detail_btn_scanuhf;
    private Button vault_in_detail_btn_cleanmore;
    private Button vault_in_detail_btn_enter;
    private Button vault_out_detail_btn_order;

    private RadioButton vault_in_detail_rbtn_allbox;
    private RadioButton vault_in_detail_rbtn_lackbox;
    private RadioButton vault_in_detail_rbtn_morebox;
    private RadioButton vault_in_detail_rbtn_order;

    private YLTask ylTask;

    private YLMediaPlayer ylMediaPlayer;

    private List<Box> displayboxlist;
    private List<Box> Allboxlist;
    private List<Box> OrderBoxlist;
    private int boxorder;

    private YLValutboxitemAdapter ylValutboxitemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault_in_detail);
        try {
            InitLayout();
            InitData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void InitLayout() {
        vault_in_detail_tv_taskname = (TextView) findViewById(R.id.vault_in_detail_tv_taskname);
        vault_in_detail_listview = (ListView) findViewById(R.id.vault_in_detail_listview);
        vault_in_detail_tv_tolly = (TextView) findViewById(R.id.vault_in_detail_tv_tolly);
        vault_in_detail_tv_check = (TextView) findViewById(R.id.vault_in_detail_tv_check);
        vault_in_detail_tv_boxname = (TextView) findViewById(R.id.vault_in_detail_tv_boxname);
        vault_in_detail_btn_scan1d = (Button) findViewById(R.id.vault_in_detail_btn_scan1d);
        vault_in_detail_btn_scanuhf = (Button) findViewById(R.id.vault_in_detail_btn_scanuhf);
        vault_in_detail_btn_cleanmore = (Button)findViewById(R.id.vault_in_detail_btn_cleanmore);
        vault_in_detail_btn_enter = (Button) findViewById(R.id.vault_in_detail_btn_enter);
        vault_out_detail_btn_order = (Button) findViewById(R.id.vault_out_detail_btn_order);

        vault_in_detail_rbtn_allbox = (RadioButton)findViewById(R.id.vault_in_detail_rbtn_allbox);
        vault_in_detail_rbtn_lackbox = (RadioButton)findViewById(R.id.vault_in_detail_rbtn_lackbox);
        vault_in_detail_rbtn_morebox = (RadioButton)findViewById(R.id.vault_in_detail_rbtn_morebox);
        vault_in_detail_rbtn_order = (RadioButton)findViewById(R.id.vault_in_detail_rbtn_order);

        vault_in_detail_btn_scan1d.setOnClickListener(this);
        vault_in_detail_btn_scanuhf.setOnClickListener(this);
        vault_in_detail_btn_enter.setOnClickListener(this);
        vault_in_detail_rbtn_allbox.setOnClickListener(this);
        vault_in_detail_rbtn_lackbox.setOnClickListener(this);
        vault_in_detail_rbtn_morebox.setOnClickListener(this);
        vault_in_detail_rbtn_order.setOnClickListener(this);
        vault_in_detail_btn_cleanmore.setOnClickListener(this);
        vault_out_detail_btn_order.setOnClickListener(this);
        vault_in_detail_btn_scan1d.setBackgroundColor(-13388315);
        vault_in_detail_btn_scanuhf.setBackgroundColor(-13388315);

        vault_in_detail_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (displayboxlist.size() < 1) return;
                Box box = displayboxlist.get(position);
                if (box.getValutcheck() == null) return;
                if (box.getValutcheck().equals("多") || box.getValutcheck().equals("核")) {
                    ShowMultChoice(position);
                }
            }
        });
    }

    @Override
    protected void InitData() throws Exception {
        displayboxlist = new ArrayList<Box>();
        Allboxlist = new ArrayList<Box>();
        OrderBoxlist = new ArrayList<Box>();
        ylMediaPlayer = new YLMediaPlayer();
        boxorder = 1;

        ylTask = YLEditData.getYlTask();
        String title ="任务:"+ ylTask.getLine()+"\r\n执行人:"+ ylTask.getTaskManager();
        vault_in_detail_tv_taskname.setText(title);
        WebService webService = new WebService();
        displayboxlist = webService.GetVaultInBoxList(ylTask.getTaskID(),YLSystem.getHandsetIMEI(),
                YLSystem.getUser().getEmpID(),getApplicationContext());
        if (displayboxlist.get(0).getServerReturn().equals("1")){
            for (int i = 0;i<displayboxlist.size();i++){
                Box box = new Box();
                box = displayboxlist.get(i);
                Allboxlist.add(box);
            }
        }
        DisPlayBoxlistAdapter();
//        ylValutboxitemAdapter.notifyDataSetChanged();
        StatisticalBoxList(displayboxlist);
    }

    @Override
    public void YLPutdatatoList(String recivedata) {
        ScanBoxInListView(recivedata, "1D");
    }


    private void ShowMultChoice(final int position) {
        new AlertDialog.Builder(this).setTitle("请选择类型").setIcon
                (android.R.drawable.ic_dialog_info).setSingleChoiceItems(R.array.ylboxfullorempty, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Box box = displayboxlist.get(position);
                        switch (which) {
                            case 0:
                                box.setBoxStatus("实");
                                box.setValutcheck("核");
                                displayboxlist.set(position, box);
                                FilterBoxdisplay();
                                break;
                            case 1:
                                box.setBoxStatus("空");
                                box.setValutcheck("核");
                                displayboxlist.set(position, box);
                                FilterBoxdisplay();
                                break;
                            case 2:
                                Box removebox = displayboxlist.get(position);
                                for (int i = 0; i < Allboxlist.size(); i++) {
                                    Box alllistboxbox = new Box();
                                    alllistboxbox = Allboxlist.get(i);
                                    if (removebox.getBoxID().equals(alllistboxbox.getBoxID())){
                                        Allboxlist.remove(i);
                                        break;
                                    }
                                }
                                displayboxlist.remove(position);
                                break;
                        }
//                        if (displayboxlist.size() == position + 1) {
//                            displayboxlist.set(position, box);
////                            DisPlayBoxlistAdapter(displayboxlist);
//                            ylValutboxitemAdapter.notifyDataSetChanged();
////                            vault_in_detail_listview.setSelection(position + 1);
//                        } else {
////                            DisPlayBoxlistAdapter(displayboxlist);
                        ylValutboxitemAdapter.notifyDataSetChanged();
////                            vault_in_detail_listview.setSelection(position);
//                        }
                        dialog.dismiss();
                    }
                }).show();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.vault_in_detail_btn_scan1d:
                if (vault_in_detail_btn_scan1d.getText().equals("扫描")){
                    vault_in_detail_btn_scan1d.setBackgroundColor(-30720);
                    Scan1DCmd(2);
                    vault_in_detail_btn_scan1d.setText("停止");
                }else {
                    vault_in_detail_btn_scan1d.setBackgroundColor(-13388315);
                    vault_in_detail_btn_scan1d.setText("扫描");
                    Scan1DCmd(0);
                }

                break;
            case R.id.vault_in_detail_btn_scanuhf:
                if (vault_in_detail_btn_scanuhf.getText().equals("UHF")){
                    vault_in_detail_btn_scanuhf.setBackgroundColor(-30720);
                    vault_in_detail_btn_scanuhf.setText("停止");
                }else {
                    vault_in_detail_btn_scanuhf.setBackgroundColor(-13388315);
                    vault_in_detail_btn_scanuhf.setText("UHF");
                }
                break;
            case R.id.vault_in_detail_btn_enter:ConfirmData();
                break;
            case R.id.vault_in_detail_rbtn_allbox:FilterBoxdisplay();
                break;
            case R.id.vault_in_detail_rbtn_lackbox:FilterBoxdisplay();
                break;
            case R.id.vault_in_detail_rbtn_morebox:FilterBoxdisplay();
                break;
            case R.id.vault_in_detail_rbtn_order:FilterBoxdisplay();
                break;
            case R.id.vault_in_detail_btn_cleanmore:CleanMoreBox();
                break;
            case R.id.vault_out_detail_btn_order:AddBoxOrder();
                break;
        }
    }

    private void AddBoxOrder() {
        boxorder++;
        vault_out_detail_btn_order.setText(boxorder + "");
    }

    private void CleanMoreBox() {
        if (Allboxlist.size() <1)return;
        for (int i = 0; i< Allboxlist.size();i++){
            if (Allboxlist.get(i).getValutcheck().equals("多")){
                Allboxlist.remove(i);
                --i;
            }
        }
        FilterBoxdisplay();
    }


    private void ScanBoxInListView(String recivedata,String Form) {
        if (recivedata.length() !=10)return;
        boolean boxrecheck = true;
        for (int i = 0;i<Allboxlist.size();i++){
            Box listbox = Allboxlist.get(i);
            if (listbox.getBoxID().equals(recivedata)){
                if (listbox.getValutcheck().equals("")){
                    listbox.setValutcheck("对");
                    listbox.setTradeAction("入");
                    listbox.setActionTime(YLSysTime.GetStrCurrentTime());
                    listbox.setBoxOrder(boxorder + "");
                    vault_in_detail_tv_boxname.setText(listbox.getBoxName());
                    boxrecheck = false;
                    Allboxlist.set(i,listbox);
                    Log.e(YLSystem.getKimTag(), listbox.toString() + "修改");
                    ylMediaPlayer.SuccessOrFailMidia("success", getApplicationContext());
                    break;
                }else {
                    if (Form.equals("1D")){
                        ylMediaPlayer.SuccessOrFailMidia("success", getApplicationContext());}
                    return;
                }
            }
        }
        if (boxrecheck){
            Box box= YLBoxScanCheck.CheckBoxbyUHF(recivedata, getApplicationContext());
            if (box.getBoxName().equals("无数据"))return;
            box.setValutcheck("多");
            box.setBoxCount("1");
            box.setBoxStatus("无");
            box.setBoxType("无");
            box.setTradeAction("入");
            box.setActionTime(YLSysTime.GetStrCurrentTime());
            box.setBoxTaskType(ylTask.getTaskType());
            box.setBoxOrder(boxorder + "");
            Allboxlist.add(box);
            vault_in_detail_tv_boxname.setText(box.getBoxName());
//            Log.e(YLSystem.getKimTag(), box.toString());
            Log.e(YLSystem.getKimTag(), Allboxlist.size() + "");
            ylMediaPlayer.SuccessOrFailMidia("success", getApplicationContext());
        }
        FilterBoxdisplay();
    }

    private void GetBoxToListView(Box box) {
        try {
            if (box.getBoxName().equals("无数据")) {
                return;
            }
            boolean boxcheck = true;
            int position = 0;
            Log.e(YLSystem.getKimTag(),Allboxlist.size()+"");
            for (int i = 0; i < Allboxlist.size(); i++) {
                Box hombox = Allboxlist.get(i);
                if (hombox.getBoxID().equals(box.getBoxID())) {
                    if (hombox.getValutcheck().equals("")) {
                        hombox.setValutcheck("对");
                        hombox.setTradeAction("入");
                        hombox.setActionTime(YLSysTime.GetStrCurrentTime());
                        Allboxlist.set(i, hombox);
                        boxcheck = false;
                        position = i;
                        break;
                    } else if (hombox.getValutcheck().equals("多")
                            || hombox.getValutcheck().equals("对")) {
                        ylMediaPlayer.SuccessOrFailMidia("success", getApplicationContext());
                        return;
                    }
                }
            }
            if (boxcheck) {
                box.setValutcheck("多");
                box.setBoxCount("1");
                box.setBoxStatus("无");
                box.setBoxType("无");
                box.setTradeAction("入");
                box.setActionTime(YLSysTime.GetStrCurrentTime());
                box.setBoxTaskType(ylTask.getTaskType());
                Allboxlist.add(box);
            }
            FilterBoxdisplay();
//            ylValutboxitemAdapter.notifyDataSetInvalidated();
//            DisPlayBoxlistAdapter(displayboxlist);
            if (position == 0) {
                position = displayboxlist.size();
            }
            vault_in_detail_listview.setSelection(position);
            ylMediaPlayer.SuccessOrFailMidia("success", getApplicationContext());
            StatisticalBoxList(Allboxlist);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void StatisticalBoxList(List<Box> boxList){
        int homstr = 0,vaulter= 0,correct = 0 ,morebox = 0;
        if (boxList == null || boxList.size() < 1)return;
        for (Box box :boxList){
//            if (box.getValutcheck().equals("")){
//                homstr +=1;
//            }else if (box.getValutcheck().equals("对")){
//                vaulter +=1;
//                correct +=1;
//                homstr +=1;
//            }else if (box.getValutcheck().equals("核")){
//                vaulter +=1;
//            }

            switch (box.getValutcheck()){
                case "":
                    homstr +=1;
                    break;
                case "对":
                    vaulter +=1;
                    correct +=1;
                    homstr +=1;
                    break;
                case "核":
                    vaulter +=1;
                    break;
                case "多":
                    vaulter +=1;
                    morebox+=1;
                    break;
            }
        }

        int lackbox = homstr - correct;
        String hom = "业务员上传:"+homstr+"个"+" 符合:"+correct+"个";
        String vault = "库管员:"+vaulter+"个"+" 少:"+lackbox+"个 多："+morebox+"个";
        vault_in_detail_tv_tolly.setText(hom);
        vault_in_detail_tv_check.setText(vault);
    }

    private void DisPlayBoxlistAdapter(){
        ylValutboxitemAdapter = new YLValutboxitemAdapter(getApplicationContext()
                , displayboxlist, R.layout.vault_in_detail_boxitem);
        vault_in_detail_listview.setAdapter(ylValutboxitemAdapter);

    }

    private void FilterBoxdisplay(){
        displayboxlist.clear();
        //全部
        if (vault_in_detail_rbtn_allbox.isChecked()){
            for (int i = 0; i<Allboxlist.size();i++){
                Box box = Allboxlist.get(i);
                displayboxlist.add(box);
            }
        }

        //缺少
        if (vault_in_detail_rbtn_lackbox.isChecked()){
            for (int i = 0 ;i < Allboxlist.size();i++){
                Box box = Allboxlist.get(i);
                if (box.getValutcheck().equals("") ||box.getValutcheck() == null){
                    displayboxlist.add(box);
                }
            }
        }

        //多箱
        if (vault_in_detail_rbtn_morebox.isChecked()){
            for (int i = 0; i<Allboxlist.size();i++){
                Box box = Allboxlist.get(i);
                if (box.getValutcheck().equals("多")){
                    displayboxlist.add(box);
                }
            }
        }

        //顺序
        if (vault_in_detail_rbtn_order.isChecked()){
            if (Allboxlist.size()<1)return;

            List<String> orderlist = new ArrayList<>();
            for (Box box : Allboxlist) {
                if (box.getBoxOrder() != null){
                    orderlist.add(box.getBoxOrder());
                }
            }
            orderlist = removeDeuplicate(orderlist);

            for (String order : orderlist){
                int count = 0;
                for (Box box : Allboxlist) {
                    if (box.getBoxOrder() == null)continue;
                    if ( box.getBoxOrder().equals(order)){
                        count++;
                    }
                }
                Box box = new Box();
                box.setBoxName("第 " + order + " 次扫描");
                box.setBoxStatus("数量: ");
                box.setBoxType("             " + count + "  个");
                displayboxlist.add(box);
            }
        }

        ylValutboxitemAdapter.notifyDataSetChanged();
        StatisticalBoxList(Allboxlist);
    }

    private List removeDeuplicate(List arlList)
    {
        HashSet h=new HashSet(arlList);
        arlList.clear();
        arlList.addAll(h);
        List list=new ArrayList();
        list=arlList;
        return list;
    }

    @Override
    public void HandSetHotKey(int keyCode) {
        switch (keyCode){
            case 131:
                if (vault_in_detail_btn_scan1d.getText().equals("扫描")){
                    vault_in_detail_btn_scan1d.setBackgroundColor(-30720);
                    Scan1DCmd(2);
                    vault_in_detail_btn_scan1d.setText("停止");
                }else {
                    Scan1DCmd(0);
                    vault_in_detail_btn_scan1d.setBackgroundColor(-13388315);
                    vault_in_detail_btn_scan1d.setText("扫描");
                }
                break;
            case 132:
                if (vault_in_detail_btn_scanuhf.getText().equals("UHF")){
                    vault_in_detail_btn_scanuhf.setBackgroundColor(-30720);
                    vault_in_detail_btn_scanuhf.setText("停止");
                }else {
                    vault_in_detail_btn_scanuhf.setBackgroundColor(-13388315);
                    vault_in_detail_btn_scanuhf.setText("UHF");
                }
                break;
            case 4:
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
        }
        super.HandSetHotKey(keyCode);
    }

    private void ConfirmData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(vault_in_detail.this);
        builder.setMessage("是否确认上传数据");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Allboxlist.size() < 1) return;
                boolean boxcheck = false;

                //未设置款箱状态也能上传0623kim
                for (Box box : Allboxlist) {
                    if (box.getValutcheck() != null) {
                        if (box.getBoxStatus().equals("无")) {
                            boxcheck = true;
                            break;
                        }
                    }
                }
                if (boxcheck) {
                    new AlertDialog.Builder(vault_in_detail.this).setTitle("提示")
                            .setMessage("有款箱未状态设置\r\n请完成设置")
                            .setPositiveButton("确定", null).show();
                } else {
                    try {
                        ylTask.setLstBox(Allboxlist);
                        YLEditData.setYlTask(ylTask);
                        Log.e(YLSystem.getKimTag(), Allboxlist.size()+"上传数量");
                        WebService webService = new WebService();
                        String returstr = webService.PostVaultInBoxList(YLSystem.getUser(), getApplicationContext());
                        Log.e(YLSystem.getKimTag(), returstr+"上传回传");
                        if (returstr.contains("0")) {
                            Log.e(YLSystem.getKimTag(), "0");
                        } else if (returstr.contains("1")) {
                            new AlertDialog.Builder(vault_in_detail.this).setTitle("提示")
                                    .setMessage("已上传完成。")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ylTask.setTaskState("已上传");
                                            Scan1DCmd(0);
//                                            vault_in_detail.this.finish();
                                            dialog.dismiss();
                                            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                                        }
                                    }).show();
                        }else if (returstr.equals("2")){
                            Toast.makeText(getApplicationContext(),"该线路已上传，如需覆盖请联系信息技术部",Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vault_in_detail, menu);
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

            YLEditData.setYleditcarbox(Allboxlist);
            Intent intent = new Intent();
            intent.setClass(vault_in_detail.this, vault_in_detail_statistics.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
