package ylescort.ylmobileandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import TaskClass.Box;
import TaskClass.OverWeightBox;
import YLAdapter.YLOverweightAdapter;
import YLDataService.YLBoxScanCheck;
import YLSystemDate.YLMediaPlayer;
import YLSystemDate.YLSysTime;
import YLSystemDate.YLSystem;

public class Overweight extends YLBaseScanActivity implements View.OnClickListener {

    private ListView overweight_lv;
    private Button overweight_btn_scan;
    private Button overweight_btn_upload;
    private List<OverWeightBox> overweights;
    private YLOverweightAdapter ylva;
    private YLMediaPlayer player;
    private int listposition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overweight);
        try {
            InitLayout();
            InitData();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void InitLayout() {
        overweight_lv  = (ListView) findViewById(R.id.overweight_lv);
        overweight_btn_scan = (Button) findViewById(R.id.overweight_btn_scan);
        overweight_btn_upload = (Button) findViewById(R.id.overweight_btn_upload);

        overweight_btn_scan.setOnClickListener(this);
        overweight_btn_upload.setOnClickListener(this);
        overweight_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listposition = position;
                OverWeightBox o = overweights.get(position);
                ShowTextDailog(o,false);
            }
        });
    }

    @Override
    protected void InitData() throws Exception {
        overweights = new ArrayList<>();
        ylva = new YLOverweightAdapter(getApplicationContext(),overweights,R.layout.overweightbox_listitem);
        overweight_lv.setAdapter(ylva);
        player = new YLMediaPlayer(getApplicationContext());
        this.setTitle("超重箱扫描:"+YLSystem.getBaseName());
    }

    @Override
    public void YLPutdatatoList(String recivedata) {
        AddBoxlist(recivedata);
    }

    private void AddBoxlist(String recivedata) {
        Box b = YLBoxScanCheck.CheckBoxbyUHF(recivedata,getApplicationContext());
        if (b.getBoxID().equals("0"))return;
        for (OverWeightBox o : overweights) {
            if (o.getBoxID().equals(recivedata)){
                player.SuccessOrFail(false);
                return;
            }
        }

        OverWeightBox weightBox = new OverWeightBox();
        weightBox.setServerReturn(overweights.size()+1+"");
        weightBox.setBoxID(b.getBoxID());
        weightBox.setBoxName(b.getBoxName());
        weightBox.setBoxType(b.getBoxType());
        weightBox.setBoxWeight("0");
        weightBox.setActionTime(YLSysTime.GetStrCurrentTime());
        ShowTextDailog(weightBox,true);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.overweight_btn_scan:
                ScanOverWeightScan();
                break;
            case R.id.overweight_btn_upload:
                UpLoadData();
                break;
        }
    }

    private void UpLoadData() {
        try {
            JSONObject j = new JSONObject();
            j.put("SOWBox",gson.toJson(overweights));
            j.put("empid",YLSystem.getUser().getEmpID());
            j.put("deviceID",YLSystem.getHandsetIMEI());
            String url = YLSystem.GetBaseUrl(getApplicationContext())+"StoreUploadOverWeightBox";

            YLWebDataAsyTaskForeground y = new YLWebDataAsyTaskForeground(j,url,2) {
                @Override
                protected void onPostExecute(String s) {
                    YLProgressDialog.dismiss();

                    String str = gson.fromJson(s,String.class);
                    if (str == null){
                        YLMessagebox("网络出错，请重新上传");
                    }else if (str.equals("1")){
                        YLMessagebox("上传成功!");
                        overweights.clear();
                        ylva.notifyDataSetChanged();
                    }else if (str.equals("0")){
                        YLMessagebox("上传失败!");
                    }
                }
            };
            y.execute();
            y.doInBackground();


        } catch (JSONException e) {
            YLProgressDialog.dismiss();
            e.printStackTrace();
        }
    }

    private void ScanOverWeightScan() {
        Scan1DCmd(1);
    }

    public void ShowTextDailog(final OverWeightBox weightBox,
                               final boolean add){
        String deloredi = add ? "取消":"删除";
        String enter = add ? "确认":"修改";

        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        new AlertDialog.Builder(this).setTitle(weightBox.getBoxName())
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(et)
                .setPositiveButton(enter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String input = et.getText().toString();
                        if (input.length() > 0){
                            weightBox.setBoxWeight(input);
                            if (add) {
                                overweights.add(weightBox);
                            }else{
                                overweights.set(listposition,weightBox);
                            }
                            ylva.notifyDataSetChanged();
                            player.SuccessOrFail(true);
                        }
                    }
                }).setNegativeButton(deloredi, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!add){
                    overweights.remove(listposition);
                    ylva.notifyDataSetChanged();
                }
                dialog.dismiss();
            }
        }).show();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case 131:ScanOverWeightScan();
                break;
            case 132:ScanOverWeightScan();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
