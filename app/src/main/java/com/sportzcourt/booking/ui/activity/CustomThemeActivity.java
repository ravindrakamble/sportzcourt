package com.sportzcourt.booking.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.sportzcourt.booking.R;
import com.sportzcourt.booking.util.CustomThemeChangeUtils;

public class CustomThemeActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        CustomThemeChangeUtils.setThemeToActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_theme);

        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        finish();
        super.onBackPressed();
      /*  startActivity(new Intent(getBaseContext(), SettingsTheme.class));
        CustomThemeActivity.this.finish();*/
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                CustomThemeChangeUtils.THEME = "DEFAULT";
                CustomThemeChangeUtils.changeToTheme(this);
                break;
            case R.id.button2:
                CustomThemeChangeUtils.THEME = "Gray";
                CustomThemeChangeUtils.changeToTheme(this);
                //startActivity(new Intent(SettingsTheme.this, CustomThemeActivity.class));
                break;
            case R.id.button3:
                CustomThemeChangeUtils.THEME = "Radial";
                CustomThemeChangeUtils.changeToTheme(this);
                //  startActivity(new Intent(SettingsTheme.this, CustomThemeActivity.class));
                break;
            default:
                break;

        }
    }
}
