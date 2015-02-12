package ylescort.ylmobileandroid;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import java.util.List;

import TaskClass.BaseEmp;
import YLDataService.WebService;
import YLSystem.YLSystem;
import YLWebService.UpdateManager;

public class SettingsActivity extends PreferenceActivity {

    private static final boolean ALWAYS_SIMPLE_PREFS = false;
    ProgressDialog Cachedialog;//缓存数据进度窗口
    int CacheCount=0;//缓存数据当前进度
    int CacheMaxcount=4;//缓存数据最大进度


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        setupSimplePreferencesScreen();
    }

    private void setupSimplePreferencesScreen() {
        if (!isSimplePreferences(this)) {
            return;
        }

        // Add 'general' preferences.
        addPreferencesFromResource(R.xml.pref_general);

        // Bind the summaries of EditText/List/Dialog/Ringtone preferences to
        // their values. When their values change, their summaries are updated
        // to reflect the new value, per the Android Design guidelines.
        bindPreferenceSummaryToValue(findPreference("HandsetName"));
        bindPreferenceSummaryToValue(findPreference("version"));
        bindPreferenceSummaryToValue(findPreference("CacheLastUpdate"));

        Preference pVersion= findPreference("version");
        try {
            pVersion.setSummary(getVersionName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        clickcount=0;
        pVersion.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                clickcount++;
                if (clickcount > 5) {
                    Toast.makeText(getApplicationContext(), "不要这么无聊好吗.", Toast.LENGTH_SHORT).show();
                    clickcount = 0;
                }
                return false;
            }
        });

        //版本更新
        Preference pUpdate= findPreference("GetNewVersion");
        pUpdate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Log.d("jutest","GetNewVersion click");
                UpdateManager um=new UpdateManager(SettingsActivity.this);
                um.check();

                return false;
            }
        });

        //上次更新时间
        Preference pCacheLastUpdate=findPreference("CacheLastUpdate");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String content =  prefs.getString("CacheLastUpdate", "2000-1-1");
        pCacheLastUpdate.setSummary(content);
        pCacheLastUpdate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                List<BaseEmp> lst =  WebService.GetBaseEmp(getApplicationContext());
                BaseEmp x=lst.get(0);
                Toast.makeText(getApplicationContext(),x.EmpName,Toast.LENGTH_SHORT).show();
                return false;
            }
        });


        //缓存数据
        Preference pCache= findPreference("CacheData");
        pCache.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Log.d("jutest","CacheData click");
                Cachedialog = new ProgressDialog(SettingsActivity.this);
                Cachedialog.setMessage("正 在 下 载...");
                Cachedialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                Cachedialog.setProgress(CacheCount);
                Cachedialog.setMax(CacheMaxcount);
                Cachedialog.setIndeterminate(false);
                Cachedialog.setCancelable(true);
                Cachedialog.show();

                new Thread()
                {
                    public void run()
                    {
                        try
                        {
                            while (CacheCount <= CacheMaxcount)
                            {

                                Cachedialog.setProgress(CacheCount++);
                                Thread.sleep(2000);
                            }
                            Cachedialog.cancel();
                            Looper.prepare();
                            Toast.makeText(SettingsActivity.this,"操作成功.",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                        catch (InterruptedException e)
                        {
                            Cachedialog.cancel();
                        }
                    }
                }.start();

                return false;
            }
        });

    }
    static  int clickcount=0;

    private String getVersionName() throws Exception
    {
      return   YLSystem .getVerName(getApplicationContext());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this) && !isSimplePreferences(this);
    }

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Determines whether the simplified settings UI should be shown. This is
     * true if this is forced via {@link #ALWAYS_SIMPLE_PREFS}, or the device
     * doesn't have newer APIs like {@link PreferenceFragment}, or the device
     * doesn't have an extra-large screen. In these cases, a single-pane
     * "simplified" settings UI should be shown.
     */
    private static boolean isSimplePreferences(Context context) {
        return ALWAYS_SIMPLE_PREFS || Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
                || !isXLargeTablet(context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        if (!isSimplePreferences(this)) {
            loadHeadersFromResource(R.xml.pref_headers, target);
        }
    }

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }





}
