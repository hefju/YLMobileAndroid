package ylescort.ylmobileandroid;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginFpActivity extends ActionBarActivity implements View.OnClickListener {

    TextView txtTitle,txtUser1,txtUser2,txtMessage;
    Button btnFpOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_fp);
        InitUi();
    }

    private void InitUi() {
        txtTitle= (TextView) findViewById(R.id.txtTitle);
        txtUser1= (TextView) findViewById(R.id.txtUser1);
        txtUser2= (TextView) findViewById(R.id.txtUser2);
        txtMessage= (TextView) findViewById(R.id.txtMessage);
        btnFpOpen= (Button) findViewById(R.id.btnFpOpen);

        SetNormolMessage("请点击开启指纹");
    }

    private void SetNormolMessage(String msg){
        txtMessage.setText(msg);
        txtMessage.setTextColor(Color.BLACK);
    }
    private void SetErrorMessage(String msg){
        txtMessage.setText(msg);
        txtMessage.setTextColor(Color.RED);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFpOpen:
                //开启指纹

                break;
            default:
                break;
        }
    }

}
