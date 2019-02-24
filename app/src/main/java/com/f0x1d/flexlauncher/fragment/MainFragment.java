package com.f0x1d.flexlauncher.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.f0x1d.flexlauncher.App;
import com.f0x1d.flexlauncher.R;
import com.f0x1d.flexlauncher.activity.SettingsActivity;
import com.f0x1d.flexlauncher.adapter.AppsAdapter;
import com.f0x1d.flexlauncher.model.AppModel;
import com.f0x1d.simpledb.SimpleDB;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainFragment extends Fragment {

    private RecyclerView recyclerView;
    private Toolbar toolbar;

    private ArrayList<AppModel> apps = new ArrayList<>();
    private AppsAdapter adapter;

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_INSTALL);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        intentFilter.addDataScheme("package");
        getContext().registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        getContext().unregisterReceiver(receiver);
        super.onDestroy();
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setup();
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_layout, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        toolbar = view.findViewById(R.id.toolbar);
            toolbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), SettingsActivity.class));
                    getActivity().finish();
                }
            });
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setTitle("Flex Launcher");
            toolbar.inflateMenu(R.menu.favorite);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
            llm.setOrientation(RecyclerView.VERTICAL);

        recyclerView.setLayoutManager(llm);
        adapter = new AppsAdapter(apps, getActivity(), false);
        recyclerView.setAdapter(adapter);

        setup();
        return view;
    }

    private void setup(){
        apps.clear();

        PackageManager pm = getContext().getPackageManager();

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> allApps = pm.queryIntentActivities(i, 0);

        try {
            for (ResolveInfo resolveInfo : allApps) {
                apps.add(new AppModel(resolveInfo.loadLabel(getContext().getPackageManager()), resolveInfo.activityInfo.loadIcon(getContext().getPackageManager()),
                        resolveInfo.activityInfo.packageName));
            }

        } catch (Exception e){}

        if (!PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("azino", false))
            Collections.sort(apps, new CustomComparator());

        try {
            adapter.notifyDataSetChanged();
        } catch (Exception e){}
    }

    public class myThread extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... Params) {
            apps.clear();

            PackageManager pm = getContext().getPackageManager();

            Intent i = new Intent(Intent.ACTION_MAIN, null);
            i.addCategory(Intent.CATEGORY_LAUNCHER);

            List<ResolveInfo> allApps = pm.queryIntentActivities(i, 0);

            try {
                for (ResolveInfo resolveInfo : allApps) {
                    apps.add(new AppModel(resolveInfo.loadLabel(getContext().getPackageManager()), resolveInfo.activityInfo.loadIcon(getContext().getPackageManager()),
                            resolveInfo.activityInfo.packageName));
                }

            } catch (Exception e){}
            return "Success";

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("azino", false))
                Collections.sort(apps, new CustomComparator());

            try {
                adapter.notifyDataSetChanged();
            } catch (Exception e){}
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.favorite:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(android.R.id.content, FavoriteFragment.newInstance(), "fav")
                        .addToBackStack(null)
                        .commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.favorite, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public static class CustomComparator implements Comparator<AppModel> {
        @Override
        public int compare(AppModel o1, AppModel o2) {
            return o1.name.compareTo(o2.name);
        }
    }
}
