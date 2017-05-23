package ylescort.ylmobileandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.reflect.TypeToken;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import TaskClass.Box;
import TaskClass.YLTask;
import YLAdapter.YLVaultcheckboxAdapter;
import YLDataService.YLBoxScanCheck;
import YLSystemDate.YLMediaPlayer;
import YLSystemDate.YLSysTime;
import YLSystemDate.YLSystem;


public class vault_check_ylbox extends YLBaseScanActivity implements View.OnClickListener {

    private ListView vault_check_lv;
    private Button vault_check_btn_scan;
    private Button vault_check_btn_conFirm;
    private Button vault_check_btn_basedep;
    private Button vault_check_btn_complete;
    private Button vault_check_btn_boxinfo;
    private TextView vault_check_tv_statistics;
    private TextView vault_check_tv_scanman;
    private TextView vault_check_tv_baseName;
    private TextView vault_check_tv_boxinfo;
    private RelativeLayout vault_check_rl_title;
    private RelativeLayout vault_check_rl_boxinfo;

    private YLVaultcheckboxAdapter ylVaultcheckboxAdapter;
    private YLMediaPlayer ylMediaPlayer;
    private List<Box> Allboxlist;
    private String[] strings;
    private int oragecolor;
    private int bulecolor;
    private boolean clickfalg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault_check_ylbox);
        try {
            InitLayout();
            InitData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void HandSetHotKey(int keyCode) {
        switch (keyCode){
            case 131:ScanBox();
                break;
            case 132:ScanBox();
                break;
        }
    }

    @Override
    protected void InitLayout() {
        vault_check_lv = (ListView)findViewById(R.id.vault_check_lv);
        vault_check_btn_scan = (Button)findViewById(R.id.vault_check_btn_scan);
        vault_check_btn_conFirm = (Button)findViewById(R.id.vault_check_btn_conFirm);
        vault_check_btn_basedep = (Button)findViewById(R.id.vault_check_btn_basedep);
        vault_check_btn_complete = (Button)findViewById(R.id.vault_check_btn_complete);
        vault_check_btn_boxinfo = (Button)findViewById(R.id.vault_check_btn_boxinfo);
        vault_check_tv_statistics = (TextView)findViewById(R.id.vault_check_tv_statistics);
        vault_check_rl_title = (RelativeLayout)findViewById(R.id.vault_check_rl_title);
        vault_check_tv_scanman =  (TextView)findViewById(R.id.vault_check_tv_scanman);
        vault_check_tv_baseName = (TextView)findViewById(R.id.vault_check_tv_baseName);
        vault_check_tv_boxinfo = (TextView)findViewById(R.id.vault_check_tv_boxinfo);
        vault_check_rl_boxinfo = (RelativeLayout)findViewById(R.id.vault_check_rl_boxinfo);

        vault_check_btn_scan.setOnClickListener(this);
        vault_check_btn_conFirm.setOnClickListener(this);
        vault_check_btn_basedep.setOnClickListener(this);
        vault_check_btn_complete.setOnClickListener(this);
        vault_check_btn_boxinfo.setOnClickListener(this);

        vault_check_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Allboxlist.size() > 0){
                    DeleteBox(position);
                }
            }
        });
    }

    private void DeleteBox(final  int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(vault_check_ylbox.this);
        builder.setMessage("是否删除该款箱？");
        builder.setTitle("提示");
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Allboxlist.remove(position);
                ylVaultcheckboxAdapter.notifyDataSetChanged();
                String str = "总计："+Allboxlist.size()+"个";
                vault_check_tv_statistics.setText(str);
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
    protected void InitData() throws Exception {
        Allboxlist = new ArrayList<>();
        strings = new String[]{};
        JSONObject p = new JSONObject();
        p.put("DeviceID",YLSystem.getHandsetIMEI());
        p.put("ISWIFI",YLSystem.getNetWorkState());
        p.put("UserID",YLSystem.getUser().getEmpID());
        String url = YLSystem.GetBaseUrl(getApplicationContext())+"GetUserAuth";

        YLWebDataAsyTaskForeground y = new YLWebDataAsyTaskForeground(p,url,2) {
            @Override
            protected void onPostExecute(String s) {
                YLProgressDialog.dismiss();
                List<String> basestrlst = new ArrayList<>();
                basestrlst = gson.fromJson(s, new TypeToken<List<String>>() {}.getType());
                strings = basestrlst.toArray(new String[basestrlst.size()]);

                if (strings[0].equals("0")){
                    YLMessagebox("未分配基地权限");
                    vault_check_btn_basedep.setText("基地");
                    vault_check_tv_baseName.setText("所属基地");
                    vault_check_btn_scan.setEnabled(false);
                    vault_check_btn_conFirm.setEnabled(false);
                    vault_check_btn_basedep.setEnabled(false);
                    vault_check_btn_complete.setEnabled(false);
                    return;
                }

                if (strings.length == 1){
                    vault_check_btn_basedep.setText(strings[0]);
                    vault_check_tv_baseName.setText(strings[0]);
                    vault_check_btn_scan.setEnabled(true);
                    vault_check_btn_conFirm.setEnabled(true);
                    vault_check_btn_basedep.setEnabled(false);
                }
            }
        };
        y.execute();
        y.doInBackground();

        ylMediaPlayer = new YLMediaPlayer(getApplicationContext());
        bulecolor =  getResources().getColor(R.color.androidbluel);
        oragecolor =  getResources().getColor(R.color.orange);
        clickfalg = true;
        vault_check_tv_statistics.setText("总计: 0 个");
        vault_check_tv_scanman.setText("盘库人-"+YLSystem.getUser().getName());

        DisplayYLBox(Allboxlist);
    }

    @Override
    public void YLPutdatatoList(String recivedata) {
        for (int i = Allboxlist.size() - 1; i >= 0; i--) {
            if (Allboxlist.get(i).getBoxID().equals(recivedata)){
                ylMediaPlayer.SuccessOrFail(false);
                vault_check_rl_title.setBackgroundColor(oragecolor);
                return;
            }
        }

        Box box= YLBoxScanCheck.CheckBoxbyUHF(recivedata, getApplicationContext());
        if (box.getBoxName().equals("无数据"))return;
        box.setActionTime(YLSysTime.GetStrCurrentTime());
        Allboxlist.add(box);
        String str = "总计："+Allboxlist.size()+"个";
        vault_check_tv_boxinfo.setText(box.getBoxName());
        vault_check_tv_statistics.setText(str);
        vault_check_rl_title.setBackgroundColor(bulecolor);
        ylMediaPlayer.SuccessOrFail(true);
        ylVaultcheckboxAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.vault_check_btn_scan:
                ScanBox();
                break;
            case R.id.vault_check_btn_conFirm:
                if (clickfalg) {
                    UpData(vault_check_tv_baseName.getText().toString());
                }
                break;
            case R.id.vault_check_btn_basedep:
                GetBaseDepartment();
                break;
            case R.id.vault_check_btn_complete:
                vault_check_tv_baseName.setText("补打标签");
                vault_check_btn_scan.setEnabled(true);
                vault_check_btn_conFirm.setEnabled(true);
                vault_check_btn_basedep.setEnabled(true);
                vault_check_btn_complete.setEnabled(false);
                break;
            case R.id.vault_check_btn_boxinfo:
                ShowBoxInfo();
        }
    }

    private void ShowBoxInfo() {
        if (vault_check_rl_boxinfo.getVisibility() == View.GONE){
            vault_check_rl_boxinfo.setVisibility(View.VISIBLE);
            vault_check_btn_boxinfo.setText("隐藏");
        }else{
            vault_check_rl_boxinfo.setVisibility(View.GONE);
            vault_check_btn_boxinfo.setText("显示");
        }
    }

    private void UpData(String s) {
        YLTask ylTask = new YLTask();
        ylTask.setTaskATMEndTime(Allboxlist.size() + "");//盘库数量
        ylTask.setLstBox(Allboxlist);
        if (s.equals("补打标签")) {
            ylTask.setTaskATMBeginTime(YLSystem.getBaseName());//盘库基地
            UpLoadRemark(ylTask);
        }else {
            ylTask.setTaskATMBeginTime(s);//盘库基地
            UpLoadChecBox(ylTask);
        }
    }

    private void UpLoadChecBox(final YLTask ylTask) {

        AlertDialog.Builder builder = new AlertDialog.Builder(vault_check_ylbox.this);
        builder.setMessage("请选择上传类型？");
        builder.setTitle("提示");
        builder.setPositiveButton("初始上传", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    JSONObject j = new JSONObject();
                    j.put("STask",gson.toJson(ylTask));
                    j.put("empid",YLSystem.getUser().getEmpID());
                    j.put("deviceID", YLSystem.getHandsetIMEI());
                    j.put("init","1");
                    String url = YLSystem.GetBaseUrl(getApplicationContext())
                            +"StoreUploadCountBoxRecord";
                    YLWebDataAsyTaskForeground y = new YLWebDataAsyTaskForeground(j,url,2) {
                        @Override
                        protected void onPostExecute(String s) {
                            YLProgressDialog.dismiss();
                            MyLog(s);
                        }
                    };
                    y.execute();
                    y.doInBackground();

                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("普通上传", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                    try {
                        JSONObject j = new JSONObject();
                        j.put("STask",gson.toJson(ylTask));
                        j.put("empid",YLSystem.getUser().getEmpID());
                        j.put("deviceID", YLSystem.getHandsetIMEI());
                        j.put("init","0");
                        String url = YLSystem.GetBaseUrl(getApplicationContext())
                                +"StoreUploadCountBoxRecord";
                        YLWebDataAsyTaskForeground y = new YLWebDataAsyTaskForeground(j,url,2) {
                            @Override
                            protected void onPostExecute(String s) {
                                YLProgressDialog.dismiss();
                                clickfalg = true;
                                MyLog(s);
                            }
                        };
                        y.execute();
                        y.doInBackground();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
            }
        });
        builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    private void UpLoadRemark(final YLTask ylTask) {

        AlertDialog.Builder builder = new AlertDialog.Builder(vault_check_ylbox.this);
        builder.setMessage("确认上传补打标签？");
        builder.setTitle("提示");
        builder.setPositiveButton("上传", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {

                    JSONObject j = new JSONObject();
                    j.put("STask",gson.toJson(ylTask));
                    j.put("empid",YLSystem.getUser().getEmpID());
                    j.put("deviceID",YLSystem.getHandsetIMEI());
                    String url = YLSystem.GetBaseUrl(getApplicationContext()) + "StoreUploadCountBadBoxRecord";
                    YLWebDataAsyTaskForeground y = new YLWebDataAsyTaskForeground(j,url,2) {
                        @Override
                        protected void onPostExecute(String s) {
                            MyLog(s);
                            clickfalg = true;
                            YLProgressDialog.dismiss();
                        }
                    };
                    y.execute();
                    y.doInBackground();

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
        builder.create().show();


    }

    private void GetBaseDepartment() {
        new AlertDialog.Builder(this).setTitle("请选择基地")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(strings, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String choicebase = strings[which];
                        vault_check_btn_basedep.setText(choicebase);
                        vault_check_tv_baseName.setText(choicebase);

                        vault_check_btn_scan.setEnabled(true);
                        vault_check_btn_complete.setEnabled(true);
                        vault_check_btn_conFirm.setEnabled(true);
                        vault_check_btn_basedep.setEnabled(false);
                        dialog.dismiss();
                    }
                }).show();
    }

    private void ScanBox() {
        if (vault_check_btn_scan.getText().equals("扫描")){
            vault_check_btn_scan.setText("暂停");
            Scan1DCmd(2);
        }else {
            vault_check_btn_scan.setText("扫描");
            Scan1DCmd(0);
        }

    }

    private void DisplayYLBox(List<Box> boxList) {
        ylVaultcheckboxAdapter =
                new YLVaultcheckboxAdapter(this, boxList, R.layout.vault_check_ylboxitem);
        vault_check_lv.setAdapter(ylVaultcheckboxAdapter);
    }


}
