package com.f0x1d.flexlauncher.adapter;

import android.app.Activity;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.f0x1d.flexlauncher.R;
import com.f0x1d.flexlauncher.model.AppModel;
import com.f0x1d.flexlauncher.utils.Utils;

import java.util.List;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.AppViewHolder> {

    private List<AppModel> apps;
    private Activity activity;

    public AppsAdapter(List<AppModel> apps, Activity activity){
        this.apps = apps;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AppViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, final int position) {
        holder.icon.setImageDrawable(apps.get(position).icon);
        holder.name.setText(apps.get(position).name);
        if (PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("predominant", true))
            holder.name.setTextColor(Utils.getDominantColor(Utils.drawableToBitmap(apps.get(position).icon)));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(apps.get(position).launch);
                Toast.makeText(activity, "Flex started!", Toast.LENGTH_SHORT).show();
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
