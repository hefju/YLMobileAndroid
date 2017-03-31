package ylescort.ylmobileandroid;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.List;


import YLDataService.BaseBoxDBSer;
import YLDataService.BaseClientDBSer;
import YLDataService.BaseEmpDBSer;
import YLDataService.BaseSiteDBSer;
import YLDataService.WebService;
import YLSystemDate.YLSystem;
import YLWebService.UpdateManager;

public class SettingsActivity extends PreferenceActivity {

    private static final boolean ALWAYS_SIMPLE_PREFS = false;
    ProgressDialog Cachedialog;//缓存数据进度窗口
    int CacheCount=0;//缓存数据当前进度
    int CacheMaxcount=4;//缓存数据最大进度

    Handler mHandler;
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
                    Toast.makeText(getApplicationContext(), "You are so boring.", Toast.LENGTH_SHORT).show();
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
        final Preference pCacheLastUpdate=findPreference("CacheLastUpdate");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String content =  prefs.getString("CacheLastUpdate", "ALL");
        pCacheLastUpdate.setSummary(content);
        pCacheLastUpdate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                clickcount++;
                if(clickcount>2) {
                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor edit = settings.edit();
                    edit.putString("CacheLastUpdate", "ALL");
                    edit.apply();
                    clickcount=0;
                    pCacheLastUpdate.setSummary("ALL");

                    Toast.makeText(SettingsActivity.this,"缓存时间初始化成功.",Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });

        //清空缓存数据
        Preference pCacheCleanData= findPreference("CacheCleanData");
        pCacheCleanData.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                 builder.setMessage("确认清空基础数据吗？");
                 builder.setTitle("提示");
                 builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                          dialog.dismiss();
                         (new BaseBoxDBSer(SettingsActivity.this)).DeleteAll();
                         (new BaseClientDBSer(SettingsActivity.this)).DeleteAll();
                         (new BaseEmpDBSer(SettingsActivity.this)).DeleteAll();
                         (new BaseSiteDBSer(SettingsActivity.this)).DeleteAll();

                          Toast.makeText(SettingsActivity.this,"删除完毕.",Toast.LENGTH_SHORT).show();
                         }
                      });
                  builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           dialog.dismiss();
                          }
                     });
                 builder.create().show();

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
                CacheCount=0;
                Cachedialog.setProgress(CacheCount);
                Cachedialog.setMax(CacheMaxcount);
                Cachedialog.setIndeterminate(false);
                Cachedialog.setCancelable(true);
                Cachedialog.show();

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
                String timeLastUpdate = prefs.getString("CacheLastUpdate", "ALL");
                final String datetime=timeLastUpdate;

                new Thread()
                {
                    public void run()
                    {
                        try
                        {
                            Cachedialog.setProgress(CacheCount++);
                            WebService.GetBaseEmp(getApplicationContext(),mHandler,datetime);

//                            Thread.sleep(20);
                            Cachedialog.setProgress(CacheCount++);
                            WebService.GetBaseClient(getApplicationContext(), mHandler,datetime);
//
//                            Thread.sleep(20);
                            Cachedialog.setProgress(CacheCount++);
                            WebService.GetBaseSite(getApplicationContext(), mHandler,datetime);
//
//                            Thread.sleep(20);
                            Cachedialog.setProgress(CacheCount++);
                            WebService.GetBaseBox(getApplicationContext(), mHandler,datetime);
//
                            Thread.sleep(20);

                            Cachedialog.cancel();

                            Message msg = mHandler.obtainMessage(100);
                            mHandler.sendMessage(msg);
//                            Looper.prepare();
//                            Toast.makeText(SettingsActivity.this,"操作成功.",Toast.LENGTH_SHORT).show();
//                            Looper.loop();
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

        //用于回传数据更新UI
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {

               //String content = (String) msg.obj;
                switch (msg.what) {
                    case 1:
                        String content = (String) msg.obj;
                        pCacheLastUpdate.setSummary("hello world " + content);
                        break;
                    case 20:
                        content = (String) msg.obj;
                        Cachedialog.setMessage(content);
                        break;
                    case 21:
                        content = (String) msg.obj;
                        Cachedialog.setMessage(content);
                        break;
                    case 100:
                        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        String date = sDateFormat.format(new java.util.Date());

                        //测试不开
//                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                        SharedPreferences.Editor edit = settings.edit();
//                        edit.putString("CacheLastUpdate", date);//YLSystem.getUser().getTime()
//                        edit.apply();
//                        pCacheLastUpdate.setSummary(date);

                        Toast.makeText(SettingsActivity.this,"操作成功.",Toast.LENGTH_LONG).show();
                        break;
                }


                super.handleMessage(msg);
            }
        };

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
