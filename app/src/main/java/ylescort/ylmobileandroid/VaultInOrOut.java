package ylescort.ylmobileandroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import YLSystemDate.YLEditData;
import YLSystemDate.YLSystem;


public class VaultInOrOut extends ActionBarActivity implements View.OnClickListener {

    Button vaultinorout_btn_in;
    Button vaultinorout_btn_out;
    Button vaultinorout_btn_CheckVault;
    Button vaultinorout_btn_turnover;
    DatePicker vaultinorout_datepicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault_in_or_out);
        VaultInOrOut.this.setTitle("出入库操作员: "+ YLSystem.getUser().getName());
        IninLayout();
    }

    private void IninLayout() {
        vaultinorout_btn_in = (Button) findViewById(R.id.vaultinorout_btn_in);
        vaultinorout_btn_out = (Button)findViewById(R.id.vaultinorout_btn_out);
        vaultinorout_btn_CheckVault = (Button)findViewById(R.id.vaultinorout_btn_CheckVault);
        vaultinorout_btn_turnover = (Button)findViewById(R.id.vaultinorout_btn_turnover);
        vaultinorout_datepicker = (DatePicker)findViewById(R.id.vaultinorout_datepicker);

        vaultinorout_btn_in.setOnClickListener(this);
        vaultinorout_btn_out.setOnClickListener(this);
        vaultinorout_btn_CheckVault.setOnClickListener(this);
        vaultinorout_btn_turnover.setOnClickListener(this);
    }

    private void GetDatePickerDate(){
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        int year = vaultinorout_datepicker.getYear();
        int month = vaultinorout_datepicker.getMonth();
        int day = vaultinorout_datepicker.getDayOfMonth();
        calendar.set(year,month,day);
        YLEditData.setDatePick(calendar.getTime());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = new Intent();
        switch (keyCode){
            case 131:
                intent.setClass(VaultInOrOut.this, vault_in_operate.class);
                GetDatePickerDate();
                startActivity(intent);
                break;
            case 132:
                intent.setClass(VaultInOrOut.this, vault_out_operate.class);
                GetDatePickerDate();
                startActivity(intent);
                break;
            case 134:
                intent.setClass(VaultInOrOut.this, vault_check_ylbox.class);
                GetDatePickerDate();
                startActivity(intent);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vault_in_or_out, menu);
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
        GetDatePickerDate();
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.vaultinorout_btn_in:
                intent.setClass(VaultInOrOut.this,vault_in_operate.class);
                startActivity(intent);
                break;
            case R.id.vaultinorout_btn_out:
                intent.setClass(VaultInOrOut.this, vault_out_operate.class);
                startActivity(intent);
                break;
            case R.id.vaultinorout_btn_CheckVault:
                intent.setClass(VaultInOrOut.this,vault_check_ylbox.class);
                startActivity(intent);
                break;
            case R.id.vaultinorout_btn_turnover:
                intent.setClass(VaultInOrOut.this,Valut_turnover.class);
                startActivity(intent);
                break;
        }
    }
}
