package org.jast.simpleandroidlauncher;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    /**
     * package name which it need to show.
     */
    private String[] stayApps = {

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        Log.v("+=====+", "onCreate");

        loadApps();
    }

    @Override
    protected void onResume() {
        super.onResume();
        disableStatusbarExpand();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private PackageManager manager;
    private List<AppDetail> apps;

    private void loadApps() {
        manager = getPackageManager();
        apps = new ArrayList<>();

        // add the setting icon
        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        AppDetail app = new AppDetail();
        app.label = "Setting";
        app.name = SettingActivity.class.getName();
        app.icon = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_settings_applications_black_50dp);
        app.appType = AppDetail.TYPE_SETTING_APP;
        apps.add(app);

        List<ResolveInfo> availableActivities = manager.queryIntentActivities(i, 0);
        for (ResolveInfo ri : availableActivities) {
            app = new AppDetail();
            for (String stayApp : stayApps) {
                if (stayApp.equals(ri.activityInfo.packageName)) {
                    app.label = ri.loadLabel(manager);
                    app.name = ri.activityInfo.packageName;
                    Log.v("+=====+", "app.name:" + app.name);
                    app.icon = ri.activityInfo.loadIcon(manager);
                    app.appType = AppDetail.TYPE_OTHER_APP;
                    apps.add(app);
                    break;
                }

            }
        }

        AppListAdapter adapter = new AppListAdapter(this, apps);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    private void disableStatusbarExpand() {
        Log.v("+=====+", "disable Status bar Expand");
        try {
            /*
            * you need system signatures first.
            * */
            Object service = getSystemService("statusbar");
            Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");

            Method expand = statusBarManager.getMethod("disable", int.class);

            /*
            * Reference by View.STATUS_BAR_DISABLE_EXPAND,
            * because this constant is hide in Android, you can see it, but you can not use it.
            * So,we need to use the Method invoke way to implement way disable status bar.
            * */
            expand.invoke(service, 0x00010000);

            // The is only expand/collapse the status bar not disable, which mean user can still control status bar
//            Method expand;
//            if (Build.VERSION.SDK_INT >= 17) {
//                expand = statusBarManager.getMethod("expandNotificationsPanel");
//            } else {
//                expand = statusBarManager.getMethod("expand");
//            }
//            expand.invoke(service);

//            Method collapse = null;
//            if (Build.VERSION.SDK_INT >= 16) {
//                collapse = statusBarManager.getMethod("collapsePanels");
//            } else {
//                collapse = statusBarManager.getMethod("collapse");
//            }
//            collapse.setAccessible(true);
//            collapse.invoke(service);


            /*
            * Please reference :
            * View.STATUS_BAR_DISABLE_BACK
            * View.STATUS_BAR_DISABLE_EXPAND
            * View.STATUS_BAR_DISABLE_NOTIFICATION_ICONS
            * View.STATUS_BAR_DISABLE_NOTIFICATION_ALERTS
            * View.STATUS_BAR_DISABLE_NOTIFICATION_TICKER
            * View.STATUS_BAR_DISABLE_SYSTEM_INFO
            *
            * Than you maybe get what you need, I did not try those constants if I invoke it, so I don't know what will happen.
            * */
//            statusbarManager.getMethod("disable", int.class).invoke(service, 0x00000001);
//            expand.invoke(service, 0x00000001);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
