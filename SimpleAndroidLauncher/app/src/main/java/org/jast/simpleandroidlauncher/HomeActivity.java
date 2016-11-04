package org.jast.simpleandroidlauncher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String STATUS_BAR_SERVICE = "statusbar";
    private static final int DISABLE_NONE = 0x00000000;
    private static final int STATUS_BAR_DISABLE_EXPAND = 0x00010000;

    RecyclerView mRecyclerView;

    /**
     * 要顯示的app
     */
    String[] stayApps = {

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

//        mStatusBarManager = (StatusBarManager) mContext.getSystemService(Context.STATUS_BAR_SERVICE);
//        mStatusBarManager.disable(StatusBarManager.DISABLE_EXPAND);



        loadApps();
    }

    @Override
    protected void onResume() {
        super.onResume();
        disableStatusbarExpand(STATUS_BAR_DISABLE_EXPAND);
    }

    private PackageManager manager;
    private List<AppDetail> apps;

    private void loadApps() {
        manager = getPackageManager();
        apps = new ArrayList<>();

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> availableActivities = manager.queryIntentActivities(i, 0);
        for (ResolveInfo ri : availableActivities) {
            AppDetail app = new AppDetail();
            for (String stayApp : stayApps) {
                if (stayApp.equals(ri.activityInfo.packageName)) {
                    app.label = ri.loadLabel(manager);
                    app.name = ri.activityInfo.packageName;
                    Log.v("+=====+", "app.name:" + app.name);
                    app.icon = ri.activityInfo.loadIcon(manager);
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

    private void disableStatusbarExpand(int what) {
        try {
            Object service = getSystemService("statusbar");
            Class<?> statusbarManager = null;
            statusbarManager = Class.forName("android.app.StatusBarManager");
            Method collapse = statusbarManager.getMethod("collapse");
            collapse.setAccessible(true);
            collapse.invoke(service);
        } catch (Exception e) {
            Log.v("+===+", "e:" + e.getMessage());

        }
    }
}
