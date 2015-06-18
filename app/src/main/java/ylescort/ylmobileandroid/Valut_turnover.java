package ylescort.ylmobileandroid;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;

import TaskClass.YLTask;
import YLSystemDate.YLEditData;
import YLSystemDate.YLSysTime;
import ylescort.ylmobileandroid.R;

public class Valut_turnover extends ActionBarActivity {

    private ListView vault_turnover_listview;
    private Button vault_turnover_btn_vaultin;
    private Button vault_turnover_btn_vaultout;
    private Button vault_turnover_btn_scan;
    private Button vault_turnover_btn_upload;

    private YLTask vaultinylTask;
    private YLTask vaultoutylTask;

    private String PickDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valut_turnover);
        try {
            InitView();
            InitData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitData() throws Exception {
        vaultinylTask = new YLTask();
        vaultoutylTask = new YLTask();
        PickDate = YLSysTime.DateToStr(YLEditData.getDatePick());

    }

    private void InitView() {
        vault_turnover_listview = (ListView) findViewById(R.id.vault_turnover_listview);
        vault_turnover_btn_vaultin = (Button) findViewById(R.id.vault_turnover_btn_vaultin);
        vault_turnover_btn_vaultout = (Button) findViewById(R.id.vault_turnover_btn_vaultout);
        vault_turnover_btn_scan = (Button) findViewById(R.id.vault_turnover_btn_scan);
        vault_turnover_btn_upload = (Button) findViewById(R.id.vault_turnover_btn_upload);
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
}
