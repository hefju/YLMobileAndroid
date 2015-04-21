package ylescort.ylmobileandroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class VaultInOrOut extends ActionBarActivity implements View.OnClickListener {

    Button vaultinorout_btn_in;
    Button vaultinorout_btn_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault_in_or_out);
        IninLayout();
    }

    private void IninLayout() {
        vaultinorout_btn_in = (Button) findViewById(R.id.vaultinorout_btn_in);
        vaultinorout_btn_out = (Button)findViewById(R.id.vaultinorout_btn_out);
        vaultinorout_btn_in.setOnClickListener(this);
        vaultinorout_btn_out.setOnClickListener(this);
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
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.vaultinorout_btn_in:
                intent.setClass(VaultInOrOut.this,vault_in_operate.class);
                startActivity(intent);
                break;
            case R.id.vaultinorout_btn_out:
                intent.setClass(VaultInOrOut.this,vault_out_operate.class);
                startActivity(intent);
                break;
        }
    }
}
