package ch.domenik.calendar.tile;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import ch.domenik.calendar.ListItem;
import ch.domenik.calendar.R;

public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.RecyclerViewHolder> {

    private ArrayList<ListItem> dataSource = new ArrayList<ListItem>();
    public interface AdapterCallback{
        void onItemClicked(Integer menuPosition);
    }
    private AdapterCallback callback;

    private String drawableIcon;
    private Context context;


    public MainMenuAdapter(Context context, ArrayList<ListItem> dataArgs, AdapterCallback callback){
        this.context = context;
        this.dataSource = dataArgs;
        this.callback = callback;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);

        return new RecyclerViewHolder(view);
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        RelativeLayout listContainer;
        TextView listItem;
        TextView listItemDate;

        public RecyclerViewHolder(View view) {
            super(view);
            listContainer = view.findViewById(R.id.list_container);
            listItem = view.findViewById(R.id.list_item);
            listItemDate = view.findViewById(R.id.list_item_date);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        ListItem data_provider = dataSource.get(position);
        Drawable background;
        String textColor;

        if(Objects.equals(data_provider.getType(), "light")){
            background = ContextCompat.getDrawable(context, R.drawable.rounded_corner_light);
            textColor = "#4CAF50";
        } else {
            background = ContextCompat.getDrawable(context, R.drawable.rounded_corner_dark);
            textColor = "#009688";
        }

        holder.listItemDate.setText(data_provider.getDate());
        holder.listItemDate.setTextColor(Color.parseColor(textColor));
        holder.listItem.setText(data_provider.getText());
        holder.listItem.setBackground(background);
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }
}

