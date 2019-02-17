package com.f0x1d.flexlauncher.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.f0x1d.flexlauncher.App;
import com.f0x1d.flexlauncher.R;
import com.f0x1d.flexlauncher.adapter.AppsAdapter;
import com.f0x1d.flexlauncher.model.AppModel;
import com.f0x1d.flexlauncher.utils.Utils;
import com.f0x1d.simpledb.SimpleDB;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FavoriteFragment extends Fragment {

    public static FavoriteFragment newInstance() {
        Bundle args = new Bundle();
        FavoriteFragment fragment = new FavoriteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private List<AppModel> apps = new ArrayList<>();
    
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    
    private AppsAdapter adapter;
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.favorite_fragment_layout, container, false);
            toolbar = v.findViewById(R.id.toolbar);
            toolbar.setTitle("Favorite Apps");
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });
            
            adapter = new AppsAdapter(apps, getActivity(), true);
                new myThread().execute();
            
            recyclerView = v.findViewById(R.id.recyclerView);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
            llm.setOrientation(RecyclerView.VERTICAL);
            
            recyclerView.setLayoutManager(llm);
            recyclerView.setAdapter(adapter);
        return v;
    }

    public class myThread extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... Params) {
            apps.clear();

            final SimpleDB simpleDB = new SimpleDB(new File(App.getContext().getCacheDir(), "favorite.txt"));

            PackageManager pm = getContext().getPackageManager();

            Intent i = new Intent(Intent.ACTION_MAIN, null);
            i.addCategory(Intent.CATEGORY_LAUNCHER);

            List<ResolveInfo> allApps = pm.queryIntentActivities(i, 0);

            try {
                List<String> packages = simpleDB.readLines();
                
                for (ResolveInfo resolveInfo : allApps) {
                    for (String apackage : packages){
                        if (resolveInfo.activityInfo.packageName.equals(apackage)){
                            apps.add(new AppModel(resolveInfo.loadLabel(getContext().getPackageManager()), resolveInfo.activityInfo.loadIcon(getContext().getPackageManager()),
                                    resolveInfo.activityInfo.packageName));
                        }
                    }
                }
            } catch (Exception e){}
            
            return "Success";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("azino", false))
                Collections.sort(apps, new MainFragment.CustomComparator());

            try {
                adapter.notifyDataSetChanged();
            } catch (Exception e){}
        }

    }
}
