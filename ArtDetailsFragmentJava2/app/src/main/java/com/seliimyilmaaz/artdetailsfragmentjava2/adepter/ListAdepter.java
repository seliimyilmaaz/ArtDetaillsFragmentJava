package com.seliimyilmaaz.artdetailsfragmentjava2.adepter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.PrimaryKey;

import com.seliimyilmaaz.artdetailsfragmentjava2.databinding.HolderArtBinding;
import com.seliimyilmaaz.artdetailsfragmentjava2.model.Art;
import com.seliimyilmaaz.artdetailsfragmentjava2.view.ListOfArtsFragmentDirections;

import java.util.List;

public class ListAdepter extends RecyclerView.Adapter<ListAdepter.ListHolder> {

    private List<Art> arts;

    public ListAdepter(List<Art> arts){
        this.arts = arts;
    }

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        HolderArtBinding binding = HolderArtBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ListHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ListHolder holder, int position) {
        holder.binding.recycleViewTextHolder.setText(arts.get(position).artName);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListOfArtsFragmentDirections.ActionListOfArtsFragmentToDetailsOfArts actions
                        = ListOfArtsFragmentDirections.actionListOfArtsFragmentToDetailsOfArts("old");
                actions.setInfo("old");
                actions.setArtId(arts.get(holder.getAdapterPosition()).id);
                Navigation.findNavController(v).navigate(actions);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arts.size();
    }

    public class ListHolder extends RecyclerView.ViewHolder{

        private HolderArtBinding binding;

        public ListHolder(HolderArtBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
