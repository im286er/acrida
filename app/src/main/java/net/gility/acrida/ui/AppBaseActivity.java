package net.gility.acrida.ui;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import net.gility.acrida.R;
import net.gility.acrida.android.ApplicationLoader;
import net.gility.acrida.manager.AppManager;

import static net.gility.acrida.config.AppConfig.KEY_NIGHT_MODE_SWITCH;

/**
 * @author Alimy
 *         Created by Michael Li on 7/18/16.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public abstract class AppBaseActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init() {
        initAppTheme();
        AppManager.obtain().addActivity(this);
    }

    private void initAppTheme() {
        if (ApplicationLoader.getPreferences().getBoolean(KEY_NIGHT_MODE_SWITCH, false)) {
            setTheme(R.style.AppBaseTheme_Night);
        } else {
            setTheme(R.style.AppBaseTheme_Light);
        }
    }
}
