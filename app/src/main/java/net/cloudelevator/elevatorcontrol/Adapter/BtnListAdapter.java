package net.cloudelevator.elevatorcontrol.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.cloudelevator.elevatorcontrol.R;

import java.util.ArrayList;

public class BtnListAdapter extends RecyclerView.Adapter<BtnListAdapter.ViewHolder> {
    private ArrayList<String> mData;
    private RecyclerOnClickListener listener;

    public void setListener(RecyclerOnClickListener listener) {
        this.listener = listener;
    }

    public interface RecyclerOnClickListener{
        void onClickBtn(View v,int position);
    }

    public BtnListAdapter(ArrayList<String> data){
       this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_btn,parent,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.mButton.setText(mData.get(position));
        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickBtn(v,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        Button mButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mButton = itemView.findViewById(R.id.item_btn);
        }
    }
}
