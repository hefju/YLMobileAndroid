package ylescort.ylmobileandroid;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;


public class HomYLBoxScan extends ActionBarActivity implements View.OnClickListener {

    private TextView homylboxscan_tv_title;
    private TextView homylboxscan_tv_boxname;
    private TextView homylboxscan_tv_boxcount;
    private TextView homylboxscan_tv_boxaction;
    private TextView homylboxscan_tv_boxtype;
    private TextView homylboxscan_tv_boxstaut;
    private TextView homylboxscan_tv_tasktype;

    private Spinner homylboxscan_sp_tasktype;

    private RadioButton homylboxscan_rbtn_get;
    private RadioButton homylboxscan_rbtn_give;
    private RadioButton homylboxscan_rbtn_full;
    private RadioButton homylboxscan_rbtn_empty;
    private RadioButton homylboxscan_rbtn_moneyboxs;
    private RadioButton homylboxscan_rbtn_cardbox;
    private RadioButton homylboxscan_rbtn_Voucher;
    private RadioButton homylboxscan_rbtn_Voucherbag;

    private Button homylboxscan_btn_date;
    private Button homylboxscan_btn_scan;
    private Button homylboxscan_btn_nonelable;
    private Button homylboxscan_btn_ent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hom_ylbox_scan);
        InitView();
    }

    private void InitView() {

        homylboxscan_tv_title = (TextView) findViewById(R.id.homylboxscan_tv_title);
        homylboxscan_tv_boxname = (TextView) findViewById(R.id.homylboxscan_tv_boxname);
        homylboxscan_tv_boxcount = (TextView) findViewById(R.id.homylboxscan_tv_boxcount);
        homylboxscan_tv_boxaction = (TextView) findViewById(R.id.homylboxscan_tv_boxaction);
        homylboxscan_tv_boxtype = (TextView) findViewById(R.id.homylboxscan_tv_boxtype);
        homylboxscan_tv_boxstaut = (TextView) findViewById(R.id.homylboxscan_tv_boxstaut);
        homylboxscan_tv_tasktype = (TextView) findViewById(R.id.homylboxscan_tv_tasktype);

        homylboxscan_sp_tasktype = (Spinner) findViewById(R.id.homylboxscan_sp_tasktype);

        homylboxscan_rbtn_get = (RadioButton) findViewById(R.id.homylboxscan_rbtn_get);
        homylboxscan_rbtn_give = (RadioButton) findViewById(R.id.homylboxscan_rbtn_give);
        homylboxscan_rbtn_full = (RadioButton) findViewById(R.id.homylboxscan_rbtn_full);
        homylboxscan_rbtn_empty = (RadioButton) findViewById(R.id.homylboxscan_rbtn_empty);
        homylboxscan_rbtn_moneyboxs = (RadioButton) findViewById(R.id.homylboxscan_rbtn_moneyboxs);
        homylboxscan_rbtn_cardbox = (RadioButton) findViewById(R.id.homylboxscan_rbtn_cardbox);
        homylboxscan_rbtn_Voucher = (RadioButton) findViewById(R.id.homylboxscan_rbtn_Voucher);
        homylboxscan_rbtn_Voucherbag = (RadioButton) findViewById(R.id.homylboxscan_rbtn_Voucherbag);

        homylboxscan_btn_date = (Button) findViewById(R.id.homylboxscan_btn_date);
        homylboxscan_btn_scan = (Button) findViewById(R.id.homylboxscan_btn_scan);
        homylboxscan_btn_nonelable = (Button) findViewById(R.id.homylboxscan_btn_nonelable);
        homylboxscan_btn_ent = (Button) findViewById(R.id.homylboxscan_btn_ent);

        homylboxscan_rbtn_get.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hom_ylbox_scan, menu);
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
            case R.id.homylboxscan_btn_date:
                break;
            case R.id.homylboxscan_btn_scan:
                break;
            case R.id.homylboxscan_btn_nonelable:
                break;
            case R.id.homylboxscan_btn_ent:
                break;
            case R.id.homylboxscan_rbtn_Voucher:
                break;
            case R.id.homylboxscan_rbtn_Voucherbag:
                break;
        }
    }
}
