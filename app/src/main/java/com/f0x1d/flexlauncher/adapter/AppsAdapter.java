package com.f0x1d.flexlauncher.adapter;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.f0x1d.flexlauncher.App;
import com.f0x1d.flexlauncher.R;
import com.f0x1d.flexlauncher.model.AppModel;
import com.f0x1d.flexlauncher.utils.Utils;
import com.f0x1d.simpledb.SimpleDB;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.AppViewHolder> {

    private List<AppModel> apps;
    private Activity activity;

    private boolean delete;

    public AppsAdapter(List<AppModel> apps, Activity activity, boolean delete){
        this.apps = apps;
        this.activity = activity;
        this.delete = delete;
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AppViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, final int position) {
        holder.icon.setImageDrawable(apps.get(position).icon);
        ViewGroup.LayoutParams layoutParams = holder.icon.getLayoutParams();
        layoutParams.height = PreferenceManager.getDefaultSharedPreferences(activity).getInt("iconSize", 128);
        layoutParams.width = PreferenceManager.getDefaultSharedPreferences(activity).getInt("iconSize", 128);
        holder.icon.setLayoutParams(layoutParams);

        holder.name.setText(apps.get(position).name);
        if (PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("predominant", true))
            holder.name.setTextColor(Utils.getDominantColor(Utils.drawableToBitmap(apps.get(position).icon)));
        holder.name.setTextSize(PreferenceManager.getDefaultSharedPreferences(activity).getInt("titleSize", 15));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(activity.getPackageManager().getLaunchIntentForPackage(apps.get(position).launch));
                Toast.makeText(activity, "Flex started!", Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final SimpleDB simpleDB = new SimpleDB(new File(App.getContext().getCacheDir(), "favorite.txt"));

                boolean favorite = false;
                String[] items;

                try {
                    if (Utils.isSystemPackage(activity.getPackageManager().getPackageInfo(apps.get(position).launch, 0))){
                        items = new String[]{"Add to favorites"};
                    } else {
                        items = new String[]{"Add to favorites", "Delete"};
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    items = new String[]{"Add to favorites"};
                }

                try {
                    if (Utils.isFavorite(apps.get(position).launch, simpleDB)){
                        favorite = true;

                        if (Utils.isSystemPackage(activity.getPackageManager().getPackageInfo(apps.get(position).launch, 0))){
                            items = new String[]{"Remove from favorites"};
                        } else {
                            items = new String[]{"Remove from favorites", "Delete"};
                        }
                    }
                } catch (Exception e) {}

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                final boolean finalFavorite = favorite;
                builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case 0:
                                    if (finalFavorite){
                                        try {
                                            simpleDB.removeLine(apps.get(position).launch);

                                            if (delete){
                                                apps.remove(position);
                                                notifyDataSetChanged();
                                            }

                                        } catch (IOException e) {}
                                    } else {
                                        try {
                                            simpleDB.addLine(apps.get(position).launch);
                                        } catch (IOException e) {}
                                    }
                                    break;
                                case 1:
                                    Intent intent = new Intent(Intent.ACTION_DELETE);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                        intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
                                    }

                                    intent.setData(Uri.parse("package:" + apps.get(position).launch));
                                    activity.startActivity(intent);
                                    break;
                            }
                        }
                    });
                    builder.show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    class AppViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView icon;

        public AppViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            icon = itemView.findViewById(R.id.icon);
        }
    }
}
