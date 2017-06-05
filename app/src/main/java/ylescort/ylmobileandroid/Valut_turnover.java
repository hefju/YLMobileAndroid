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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import TaskClass.Box;
import TaskClass.BoxCombyOrder;
import TaskClass.User;
import TaskClass.YLTask;
import YLAdapter.YLValutboxitemAdapter;
import YLDataService.AnalysisBoxList;
import YLDataService.WebServerValutturnover;
import YLDataService.YLBoxScanCheck;
import YLSystemDate.YLBaseDBSer;
import YLSystemDate.YLEditData;
import YLSystemDate.YLMediaPlayer;
import YLSystemDate.YLRecord;
import YLSystemDate.YLSysTime;
import YLSystemDate.YLSystem;

public class Valut_turnover extends YLBaseScanActivity implements View.OnClickListener {

    private ListView vault_turnover_listview;
    private TextView vault_turnover_tv_title;
    private TextView vault_turnover_tv_title2;

    private Button vault_turnover_btn_vaultin;
    private Button vault_turnover_btn_vaultout;
    private Button vault_turnover_btn_scan;
    private Button vault_turnover_btn_uhf;
    private Button vault_turnover_btn_upload;
    private Button vault_turnover_btn_count;
    private Button valut_turnover_btn_clearmore;
    private RadioButton vault_turnover_rbtn_all;
    private RadioButton vault_turnover_rbtn_count;
    private RadioButton vault_turnover_rbtn_lack;
    private RadioButton vault_turnover_rbtn_more;

    private YLTask vaultinylTask;
    private YLTask vaultoutylTask;

    private String PickDate;
    private List<Box> valutoutboxList;
    private List<Box> valutinboxList;
    private List<Box> AllboxList;
    private List<Box> Displayboxlist;

    private WebServerValutturnover webServerValutturnover;

    private YLValutboxitemAdapter ylBoxEdiAdapter;

    private String InBaseName;
    private String OutBaseName;
    private String BoxOper;

    private User user;

    private YLMediaPlayer ylMediaPlayer;

    private int boxorder;
    private AnalysisBoxList analysisBoxList;

    private int androidblue;
    private int androidorange;
    private boolean uploadflag;
    private boolean scanflag;

    private YLBaseDBSer ylBaseDBSer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valut_turnover);
        try {
            InitLayout();
            InitData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void InitLayout() {
        vault_turnover_listview = (ListView) findViewById(R.id.vault_turnover_listview);
        vault_turnover_btn_vaultin = (Button) findViewById(R.id.vault_turnover_btn_vaultin);
        vault_turnover_btn_vaultout = (Button) findViewById(R.id.vault_turnover_btn_vaultout);
        vault_turnover_btn_scan = (Button) findViewById(R.id.vault_turnover_btn_scan);
        vault_turnover_btn_upload = (Button) findViewById(R.id.vault_turnover_btn_upload);
        vault_turnover_btn_uhf = (Button)findViewById(R.id.vault_turnover_btn_uhf);
        vault_turnover_btn_count = (Button)findViewById(R.id.vault_turnover_btn_count);
        valut_turnover_btn_clearmore = (Button)findViewById(R.id.valut_turnover_btn_clearmore);
        vault_turnover_rbtn_all = (RadioButton)findViewById(R.id.vault_turnover_rbtn_all);
        vault_turnover_rbtn_count = (RadioButton)findViewById(R.id.vault_turnover_rbtn_count);
        vault_turnover_rbtn_lack = (RadioButton)findViewById(R.id.vault_turnover_rbtn_lack);
        vault_turnover_rbtn_more = (RadioButton)findViewById(R.id.vault_turnover_rbtn_more);

        vault_turnover_tv_title = (TextView)findViewById(R.id.vault_turnover_tv_title);
        vault_turnover_tv_title2= (TextView)findViewById(R.id.vault_turnover_tv_title2);

        vault_turnover_btn_vaultin.setOnClickListener(this);
        vault_turnover_btn_vaultout.setOnClickListener(this);
        vault_turnover_btn_scan.setOnClickListener(this);
        vault_turnover_btn_upload.setOnClickListener(this);
        vault_turnover_btn_count.setOnClickListener(this);
        vault_turnover_rbtn_all.setOnClickListener(this);
        vault_turnover_rbtn_count.setOnClickListener(this);
        vault_turnover_rbtn_lack.setOnClickListener(this);
        vault_turnover_rbtn_more.setOnClickListener(this);
        valut_turnover_btn_clearmore.setOnClickListener(this);

        Valut_turnover.this.setTitle("未设置出入库操作");
//        vault_turnover_tv_title.setText("未设置出入库操作");

        BoxOper = "0";
        androidblue = getResources().getColor(R.color.androidbluel);
        androidorange = getResources().getColor(R.color.androidyellowl);
        vault_turnover_btn_scan.setBackgroundColor(androidblue);
//        vault_turnover_btn_uhf.setBackgroundColor(-13388315);

        vault_turnover_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ListView listView = (ListView) parent;
                if (vault_turnover_rbtn_count.isChecked()){
                    SetCount(position);
                }else {
                    EditBoxstaut(position);
                }
            }
        });
    }

    @Override
    protected void InitData() throws Exception {
        boxorder = 1;
        ylMediaPlayer = new YLMediaPlayer(getApplicationContext());
        ylBaseDBSer = new YLBaseDBSer(getApplication());
        vaultinylTask = new YLTask();
        vaultoutylTask = new YLTask();
        AllboxList = new ArrayList<>();
        Displayboxlist = new ArrayList<>();
        OutBaseName = YLSystem.getBaseName();
        PickDate = YLSysTime.DateToStr(YLEditData.getDatePick());
        webServerValutturnover = new WebServerValutturnover();
        InBaseName = "";
        analysisBoxList = new AnalysisBoxList();
        uploadflag = true;
        scanflag = false;

        user = new User();
        user = YLSystem.getUser();
        user.setServerReturn(OutBaseName);
        user.setTaskDate(PickDate);
        DisplayBoxListAdapter(Displayboxlist);
        YLRecord.WriteRecord("夜间周转","出库基地:"+OutBaseName+"日期"+PickDate);
    }

    @Override
    public void YLPutdatatoList(String recivedata) {
        try {
            ScanBoxInListView(recivedata, "1D");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()){
                case R.id.vault_turnover_btn_vaultin:YLRecord.WriteRecord("夜间周转","选择入库");VaultIn();
                    break;
                case R.id.vault_turnover_btn_vaultout:YLRecord.WriteRecord("夜间周转","选择出库");VaultOut();
                    break;
                case R.id.vault_turnover_btn_scan:YLRecord.WriteRecord("夜间周转","连续扫描");ValutScanCmd();
                    break;
                case R.id.vault_turnover_btn_upload:YLRecord.WriteRecord("夜间周转","上传数据");UpLoadDialog();
                    break;
//                case R.id.vault_check_btn_uhf:
//                ScanUHF("scan");
//                    break;
                case R.id.vault_turnover_btn_count:YLRecord.WriteRecord("夜间周转","点击添加批次");Addcount();
                    break;
                case R.id.vault_turnover_rbtn_all:YLRecord.WriteRecord("夜间周转","点击筛选全部"); FilterBoxdisplay();
                    break;
                case R.id.vault_turnover_rbtn_count: YLRecord.WriteRecord("夜间周转","点击筛选批次");FilterBoxdisplay();
                    break;
                case R.id.vault_turnover_rbtn_more:YLRecord.WriteRecord("夜间周转","点击筛选多箱");FilterBoxdisplay();
                    break;
                case R.id.vault_turnover_rbtn_lack:YLRecord.WriteRecord("夜间周转","点击筛选缺箱");FilterBoxdisplay();
                    break;
                case R.id.valut_turnover_btn_clearmore:YLRecord.WriteRecord("夜间周转","点击清除多箱");ClearMoreBox();
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void ClearMoreBox()throws Exception{
        for (int i = 0; i < AllboxList.size(); i++) {
            Box box = AllboxList.get(i);
            if (box.getValutcheck().equals("多")){
                AllboxList.remove(i);
                i--;
            }
        }
        FilterBoxdisplay();
    }

    private void Addcount() {
        boxorder +=1;
        vault_turnover_btn_count.setText(boxorder + "");
    }

    private void FilterBoxdisplay() throws Exception{
        Displayboxlist.clear();
        if (vault_turnover_rbtn_all.isChecked()){
            Displayboxlist.addAll(AllboxList);
//            Log.e(YLSystem.getKimTag(),Displayboxlist.size()+"过滤列表");
        }
        if (vault_turnover_rbtn_count.isChecked()) {
            if (AllboxList.size() > 0) {
                for (int i = 0; i < AllboxList.size(); i++) {
                    if (AllboxList.get(i).getBoxOrder() == null) {
                        AllboxList.get(i).setBoxOrder("0");
                    }
                }
                if (!vault_turnover_btn_vaultout.isEnabled()){
                    BoxCombyOrder boxCombyOrder = new BoxCombyOrder();

                    Collections.sort(AllboxList, boxCombyOrder);
                    String boxorder = AllboxList.get(0).getBoxOrder();
                    int count = 0;
                    for (int i = 0; i < AllboxList.size(); i++) {
                        if (AllboxList.get(i).getBoxOrder().equals(boxorder)) {
                            count++;
                        } else {
                            Box box = new Box();
                            box.setBoxName("第" + boxorder + "次");
                            box.setValutcheck("数量：" + count + "");
                            count = 1;
                            boxorder = AllboxList.get(i).getBoxOrder();
                            Displayboxlist.add(box);
                        }
                        if (i == AllboxList.size() - 1) {
                            Box box = new Box();
                            box.setBoxName("第" + boxorder + "次");
                            box.setValutcheck("数量：" + count + "");
                            boxorder = AllboxList.get(i).getBoxOrder();
                            Displayboxlist.add(box);
                        }
                    }
                }else if (!vault_turnover_btn_vaultin.isEnabled()){
                    List<String> orderlist = new ArrayList<>();
                    BoxCombyOrder boxCombyOrder = new BoxCombyOrder();

                    Collections.sort(AllboxList, boxCombyOrder);

//                    for (Box box : AllboxList) {
//                        if (!box.getBoxOrder().equals("0")){
//                            orderlist.add(box.getBoxOrder());
//                        }
//                    }
//                    orderlist = removeDeuplicate(orderlist);
//                    for (String order : orderlist){
//                        int count = 0;
//                        for (Box box : AllboxList) {
//                            if (box.getBoxOrder().equals(order)){
//                                count++;
//                            }
//                        }
//                        Box box = new Box();
//                        box.setBoxName("第" + order + "次");
//                        box.setValutcheck("数量：" + count + "");
//                        Displayboxlist.add(box);
//                    }

                    String orderstr = "0";
                    for (Box box : AllboxList) {
                        if (box.getBoxOrder().equals(orderstr)) continue;
                        orderstr = box.getBoxOrder();
                        orderlist.add(orderstr);

                    }

                    for (String order : orderlist){
                        int count = 0;
                        for (Box box : AllboxList) {
                            if (!box.getBoxOrder().equals("0")) {
                                if (box.getBoxOrder().equals(order)) {
                                    count++;
                                }
                            }
                        }
                        Box box = new Box();
                        box.setBoxName("第" + order + "次");
                        box.setValutcheck("数量：" + count + "");
                        Displayboxlist.add(box);
                    }
                }
            }
        }

        if (vault_turnover_rbtn_lack.isChecked()){
            if (AllboxList.size() > 0) {
                for (Box box : AllboxList) {
                    if (box.getValutcheck().equals("")){
                        Displayboxlist.add(box);
                    }
                }
            }
        }
        if (vault_turnover_rbtn_more.isChecked()){
            if (AllboxList.size()>0){
                for (Box box : AllboxList) {
                    if (box.getValutcheck() != null){
                        if (box.getValutcheck().equals("多")){
                            Displayboxlist.add(box);
                        }
                    }
                }
            }
        }
        ylBoxEdiAdapter.notifyDataSetChanged();
    }

    private List removeDeuplicate(List arlList) {
        HashSet h=new HashSet(arlList);
        arlList.clear();
        arlList.addAll(h);
        List list=new ArrayList();
        list=arlList;
        return list;
    }

    private void UpLoadDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Valut_turnover.this);
        builder.setMessage("确认上传吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UploadData();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void UploadData() {
        try {
            ylBaseDBSer.CacheData();
            if (AllboxList.size() > 0) {

                for (Box box : AllboxList) {
                    if (box.getValutcheck().equals("多")) {
                        new AlertDialog.Builder(Valut_turnover.this).setTitle("提示")
                                .setMessage("列表有多箱未设定空实状态，请设定后再上传。")
                                .setPositiveButton("确定", null).show();
                        uploadflag= false;
                        return;
                    }
                }

                vaultoutylTask.setTaskDate(PickDate);
                vaultoutylTask.setLstBox(AllboxList);
                YLEditData.setYlTask(vaultoutylTask);
                uploadflag= true;
                String uploadstate =
                        webServerValutturnover.Valutturnoverupload(YLSystem.getUser(), getApplicationContext());
                if (uploadstate.equals("1")) {
                    AllboxList.clear();
                    Displayboxlist.clear();
                    ylBoxEdiAdapter.notifyDataSetChanged();

                    vault_turnover_btn_vaultout.setEnabled(true);
                    vault_turnover_btn_vaultin.setEnabled(true);
                    Valut_turnover.this.setTitle("未设置出入库操作");
//            vault_turnover_tv_title.setText("未设置出入库操作");
                    boxorder = 1;
                    vault_turnover_btn_count.setText("1");
                    BoxOper = "0";
                }else {
                    uploadflag= false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void VaultOut() {
        if (!uploadflag)return;
        new AlertDialog.Builder(this).setTitle("请选择基地").setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(R.array.basedepartment, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String choicbase = "";
                        switch (which) {
                            case 0:
                                choicbase = "南海基地";
                                break;
                            case 1:
                                choicbase = "大良基地";
                                break;
                            case 2:
                                choicbase = "乐从基地";
                                break;
                            case 3:
                                choicbase = "三水基地";
                                break;
                        }
                        if (OutBaseName.equals(choicbase)) {
                            Toast.makeText(getApplicationContext(), "不能选择本基地"
                                    , Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            return;
                        }
//                        if (InBaseName.equals("")) {
//                            InBaseName = choicbase;
//                        } else  {
//                            Toast.makeText(getApplicationContext(), "请上传后再更换基地"
//                                    , Toast.LENGTH_SHORT).show();
//                            dialog.dismiss();
//                            return;
//                        }
                        InBaseName = choicbase;
                        Valut_turnover.this.setTitle( InBaseName+"出库：");
                        vault_turnover_btn_vaultout.setEnabled(false);
                        vault_turnover_btn_vaultin.setEnabled(true);
                        try {
                            AllboxList.clear();
                            valutoutboxList = webServerValutturnover.VaultTrunoverOutBoxList(OutBaseName, InBaseName,
                                    YLSystem.getUser().getDeviceID(), YLSystem.getUser().getEmpID()
                                    , PickDate, getApplicationContext());
                            if (!valutoutboxList.get(0).getServerReturn().contains("没有")) {
                                AllboxList.addAll(valutoutboxList);
//                                vault_turnover_tv_title.setText("出库总数: " + valutoutboxList.size());
                                Valut_turnover.this.setTitle(InBaseName + "出库：" + valutoutboxList.size());
                            } else {
//                                vault_turnover_tv_title.setText("出库总数:0");
                                Valut_turnover.this.setTitle(InBaseName + "出库：0");
                            }
                            FilterBoxdisplay();
                            BoxOper = "out";
                            Log.e(YLSystem.getKimTag(), AllboxList.size()+"");
                            ShowBoxList(AllboxList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                }).show();

        ylBoxEdiAdapter.notifyDataSetChanged();
    }

    private void VaultIn() {
        try {
            if(uploadflag) {
                Valut_turnover.this.setTitle("当前操作：入库");
                BoxOper = "in";
                AllboxList.clear();
                valutinboxList = webServerValutturnover.ValutInBoxList(user, getApplicationContext());
                if (valutinboxList.get(0).getBoxID() != null) {
                    AllboxList.addAll(valutinboxList);
                }
                FilterBoxdisplay();
                Log.e(YLSystem.getKimTag(), AllboxList.size() + "入库列表");
                AnalyBoxes(AllboxList, "in");
                ylBoxEdiAdapter.notifyDataSetChanged();
                vault_turnover_btn_vaultout.setEnabled(true);
                vault_turnover_btn_vaultin.setEnabled(false);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void AnalyBoxes(List<Box> displayboxList, String inorout) {
        if (inorout.equals("in")){
//            String intext ="应入库数："+ displayboxList.size()+"  ";
            int scanbox = 0;
            int inallbox = 0;

            for (Box box : displayboxList) {
                if (!box.getValutcheck().equals("多") &!box.getValutcheck().equals("核")){
                    inallbox++;
                }
                if (!box.getValutcheck().equals(""))scanbox++;
            }
            
            String inallboxstr = "应入库数："+inallbox+"  ";
            String correctstr = "库管扫描:"+scanbox+"  ";
//            vault_turnover_tv_title.setText(intext+"\r\n"+correctstr);
            Valut_turnover.this.setTitle(inallboxstr + correctstr);
        }else if (inorout.equals("out")){
            String outstr = "出库总数："+displayboxList.size();
//            vault_turnover_tv_title.setText(outstr);
            Valut_turnover.this.setTitle(outstr);
        }

        ShowBoxList(displayboxList);

    }

    private void ShowBoxList(List<Box> displayboxList) {
        if (displayboxList.size()==0)return;

        List<String> stringList = analysisBoxList.AnsysisBoxListForKeeper(displayboxList);
        String boxtype = "实：款箱："+stringList.get(0)+"  卡箱："+stringList.get(2)+"凭箱："+stringList.get(4)+
                " 凭袋："+stringList.get(6);
        String boxstaut =  "空：款箱："+stringList.get(1)+"  卡箱："+stringList.get(3)+"凭箱："+stringList.get(5)+
                " 凭袋："+stringList.get(7);
        vault_turnover_tv_title.setText(boxtype);
        vault_turnover_tv_title2.setText(boxstaut);

    }

    private void ScanBoxInListView(String recivedata, String form) throws  Exception{
        if (recivedata.length() !=10)return;
        boolean checkbox = true;
        if (BoxOper.equals("out")){
            for (int i = AllboxList.size() -1;i>=0;i--){
                if (AllboxList.get(i).getBoxID().equals(recivedata)){
                    checkbox = false;
                    if (form.equals("1D")){
                        ylMediaPlayer.SuccessOrFail(true);}
                    break;
                }
            }
            if (checkbox){
                Box box= YLBoxScanCheck.CheckBoxbyUHF(recivedata, getApplicationContext());
                if (box.getBoxName().equals("无数据"))return;
                box.setValutcheck(InBaseName);
                box.setBaseValutOut(OutBaseName);
                box.setBaseValutIn(InBaseName);
                box.setTradeAction("出");
                box.setTimeID("1");
                box.setBoxOrder(boxorder + "");
                box.setActionTime(YLSysTime.GetStrCurrentTime());
                AllboxList.add(box);
                ylMediaPlayer.SuccessOrFail(true);
                AnalyBoxes(AllboxList, "out");
                Log.e(YLSystem.getKimTag(),box.toString()+"出库");
                vault_turnover_listview.setSelection(AllboxList.size() - 1);
            }
        }else {
            boolean addmore = true;
            for (int i = AllboxList.size() -1;i >=0;i--){
                Box box = AllboxList.get(i);

                if (box.getBoxID().equals(recivedata)) {

                    String boxcheck = box.getValutcheck();
//                    if (box.getValutcheck().equals("对")||box.getValutcheck().equals("多")){
                    if (boxcheck.equals("对")||boxcheck.equals("多")|| boxcheck.equals("核")){
                        addmore = false;
                        ylMediaPlayer.SuccessOrFail(true);
                        continue;
                    }

                    box.setValutcheck("对");
                    box.setTradeAction("入");
                    box.setBaseValutOut(box.getBaseValutOut());
                    box.setTimeID("2");
                    box.setBoxOrder(boxorder + "");
                    box.setActionTime(YLSysTime.GetStrCurrentTime());
                    AllboxList.set(i, box);
                    addmore = false;
                    ylMediaPlayer.SuccessOrFail(true);
                    vault_turnover_listview.setSelection(i);
                    AnalyBoxes(AllboxList, "in");
                }
            }
            if (addmore){
                ylMediaPlayer.SuccessOrFail(false);
                Scan1DCmd(0);
                ValutScanCmd();
                ChoiceBoxBaseAndStauts(recivedata);
                scanflag = true;
            }
        }
        FilterBoxdisplay();
//        ylBoxEdiAdapter.notifyDataSetChanged();
    }

    private void PutBoxToBoxList(Box box) {
        try {
            if (box.getBoxName().equals("illegalbox") || box.getBoxName().equals("无数据")) return;
            boolean checkbox = true;
            if (BoxOper.equals("out")) {
                for (int i = 0; i < AllboxList.size(); i++) {
                    if (box.getBoxID().equals(AllboxList.get(i).getBoxID())) {
                        checkbox = false;
                        break;
                    }
                }
                if (checkbox) {
                    if (BoxOper.equals("out")) {
                        box.setValutcheck(InBaseName);
                        box.setBaseValutOut(OutBaseName);
                        box.setBaseValutIn(InBaseName);
                        box.setTradeAction("出");
                        box.setTimeID("1");
                        box.setActionTime(YLSysTime.GetStrCurrentTime());
                        AllboxList.add(box);
                        ylMediaPlayer.SuccessOrFail(true);
                        AnalyBoxes(AllboxList, "out");
                        vault_turnover_listview.setSelection(AllboxList.size() - 1);
                    }
                }
            } else {
                for (int i = 0; i < AllboxList.size(); i++) {
                    Box boxin = new Box();
                    boxin = AllboxList.get(i);
                    if (boxin.getBoxID().equals(box.getBoxID())) {
                        boxin.setValutcheck("对");
                        boxin.setTradeAction("入");
                        boxin.setBaseValutOut(boxin.getBaseValutOut());
                        boxin.setTimeID("2");
                        boxin.setActionTime(YLSysTime.GetStrCurrentTime());
                        AllboxList.set(i, boxin);
                        ylMediaPlayer.SuccessOrFail(true);
                        vault_turnover_listview.setSelection(i);
                        AnalyBoxes(AllboxList, "in");
                    }
                }
            }
            ylBoxEdiAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ChoiceBoxBaseAndStauts(final  String moreboxid){
        if (scanflag)return;
        final Box morebox = YLBoxScanCheck.CheckBoxbyUHF(moreboxid, getApplicationContext());
        if (morebox.getBoxID().equals("0")){
            ylMediaPlayer.SuccessOrFail(false);
            scanflag = false;
            return;
        }

        AlertDialog.Builder builder = new  AlertDialog.Builder(Valut_turnover.this);
        builder.setIcon(android.R.drawable.ic_input_add);
        builder.setTitle("请选择周转入库多箱信息");
        builder.setCancelable(false);
        View view = LayoutInflater.from(Valut_turnover.this).inflate(R.layout.vault_trunover_choice,null);
        builder.setView(view);
        final RadioButton boxempty = (RadioButton) view.findViewById(R.id.vault_turnover_rbtn_boxempty);
        final RadioButton boxfull = (RadioButton) view.findViewById(R.id.vault_turnover_rbtn_boxfull);
        final RadioButton rbtn_nh = (RadioButton) view.findViewById(R.id.vault_turnover_rbtn_nh);
        final RadioButton rbtn_dl = (RadioButton) view.findViewById(R.id.vault_turnover_rbtn_dl);
        final RadioButton rbtn_lc = (RadioButton) view.findViewById(R.id.vault_turnover_rbtn_lc);
        final RadioButton rbtn_ss = (RadioButton) view.findViewById(R.id.vault_turnover_rbtn_ss);

        switch (OutBaseName){
            case "南海基地":rbtn_nh.setEnabled(false);rbtn_dl.setChecked(true);rbtn_nh.setVisibility(View.GONE);
                break;
            case "大良基地":rbtn_dl.setEnabled(false);rbtn_lc.setChecked(true);rbtn_dl.setVisibility(View.GONE);
                break;
            case "乐从基地":rbtn_lc.setEnabled(false);rbtn_ss.setChecked(true);rbtn_lc.setVisibility(View.GONE);
                break;
            case "三水基地":rbtn_ss.setEnabled(false);rbtn_nh.setChecked(true);rbtn_ss.setVisibility(View.GONE);
                break;
        }

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String stauts = "";
                String base = "";
                if (boxempty.isChecked()){
                    stauts = "空";
                }else {
                    stauts = "实";
                }
                if (rbtn_nh.isChecked()){
                    base = "南海基地";
                }
                if (rbtn_dl.isChecked()){
                    base = "大良基地";
                }
                if (rbtn_lc.isChecked()){
                    base = "乐从基地";
                }
                if (rbtn_ss.isChecked()){
                    base = "三水基地";
                }

                morebox.setBoxStatus(stauts);
                morebox.setValutcheck("核");
                morebox.setTradeAction("入");
                morebox.setBaseValutIn(OutBaseName);
                morebox.setBaseValutOut(base);
                morebox.setTimeID("2");
                morebox.setBoxOrder(boxorder + "");
                morebox.setActionTime(YLSysTime.GetStrCurrentTime());
                AllboxList.add(morebox);
                ylMediaPlayer.SuccessOrFail(true);
                vault_turnover_listview.setSelection(AllboxList.size()-1);
                AnalyBoxes(AllboxList, "in");
                try {
                    FilterBoxdisplay();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                scanflag = false;
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scanflag = false;
                ylMediaPlayer.SuccessOrFail(false);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }



    private void DisplayBoxListAdapter(List<Box> boxList){
        if (boxList ==null)return;
        ylBoxEdiAdapter = new YLValutboxitemAdapter(this, boxList,R.layout.vault_in_detail_boxitem);
        vault_turnover_listview.setAdapter(ylBoxEdiAdapter);
    }


    private void SetCount(int position) {
        try {
            String counttime = Displayboxlist.get(position).getBoxName();
            counttime = counttime.substring(1, counttime.length() - 1);
            final int Count = Integer.parseInt(counttime);
            AlertDialog.Builder builder = new AlertDialog.Builder(Valut_turnover.this);
            builder.setMessage("确认将批次改变为：第 " + Count + " 次");
            builder.setTitle("提示");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    boxorder = Count;
                    vault_turnover_btn_count.setText(boxorder + "");
                    YLRecord.WriteRecord("夜间周转","点击添加批次");
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void EditBoxstaut(final int postion){
        if (BoxOper.equals("in")) {
            String  Boxcheck = Displayboxlist.get(postion).getValutcheck();
            if (Boxcheck.equals("对"))return;
            new AlertDialog.Builder(this).setTitle("请选择空实").setIcon(android.R.drawable.ic_dialog_info)
                    .setSingleChoiceItems(R.array.ylboxfullorempty, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String choicbase = "";
                            switch (which) {
                                case 0:
                                    choicbase = "实";
                                    break;
                                case 1:
                                    choicbase = "空";
                                    break;
                                case 2:choicbase = "删除";
                                    break;
                            }
                            if (choicbase.equals("删除")) {
                                Box Dpbox = Displayboxlist.get(postion);
                                for (int i = 0; i < AllboxList.size(); i++) {
                                    Box Abbox = AllboxList.get(i);
                                    if (Dpbox.getBoxID().equals(Abbox.getBoxID())){
                                        AllboxList.remove(i);
                                        ylBoxEdiAdapter.notifyDataSetChanged();
                                        try {
                                            FilterBoxdisplay();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                AnalyBoxes(AllboxList, "in");
                            }else {
                                Box Dpbox = Displayboxlist.get(postion);
                                for (int i = 0; i < AllboxList.size(); i++) {
                                    Box Albox = AllboxList.get(i);
                                    if (Dpbox.getBoxID().equals(Albox.getBoxID())) {
                                        Albox.setBoxStatus(choicbase);
                                        Albox.setValutcheck("核");
                                        ylBoxEdiAdapter.notifyDataSetChanged();
                                    }
                                }
                                AnalyBoxes(AllboxList, "in");
                            }
                            dialog.dismiss();
                        }
                    }).show();

        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(Valut_turnover.this);
            builder.setMessage("确认删除吗?");
            builder.setTitle("提示");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        AllboxList.remove(postion);
                        FilterBoxdisplay();
                        ylBoxEdiAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }

    @Override
    public void HandSetHotKey(int keyCode) {
        switch (keyCode){
            case 131:ValutScanCmd();
                break;
            case 132:
//                ScanUHF("scan");
                break;
            case 4:
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
        }
        super.HandSetHotKey(keyCode);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_valut_turnover, menu);
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

    private void ValutScanCmd() {

        if (BoxOper.equals("0"))return;
        if (scanflag)return;
        String sacnbtntext = vault_turnover_btn_scan.getText().toString();
        if (sacnbtntext.equals("扫描/F1")) {
            Scan1DCmd(2);
            vault_turnover_btn_scan.setBackgroundColor(androidorange);
            vault_turnover_btn_scan.setText("停止/F1");
        } else {
            Scan1DCmd(0);
            vault_turnover_btn_scan.setText("扫描/F1");
            vault_turnover_btn_scan.setBackgroundColor(androidblue);
        }

    }

}
