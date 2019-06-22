package com.example.project1.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.project1.R;

import com.example.project1.fragment_chat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AdapterMsn extends RecyclerView.Adapter<HolderMsn> {

    private List<MsnReceive> listMensaje = new ArrayList<>();
    private Context c;

    public AdapterMsn(Context c) {
        this.c = c;
    }

    public void addMensaje(MsnReceive m){
        listMensaje.add(m);
        notifyItemInserted(listMensaje.size());
    }

    @Override
    public HolderMsn onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.card_show_chat,parent,false);
        return new HolderMsn(v);
    }

    @Override
    public void onBindViewHolder(HolderMsn holder, int position) {
        holder.getName().setText(listMensaje.get(position).getName());
        holder.getMsn().setText(listMensaje.get(position).getMsn());
        if(listMensaje.get(position).getType_msn().equals("2")){
            holder.getPhotoMsn().setVisibility(View.VISIBLE);
            holder.getMsn().setVisibility(View.VISIBLE);
            Glide.with(c).load(listMensaje.get(position).getUrlPhoto()).into(holder.getPhotoMsn());
        }else if(listMensaje.get(position).getType_msn().equals("1")){
            holder.getPhotoMsn().setVisibility(View.GONE);
            holder.getMsn().setVisibility(View.VISIBLE);
        }
        if(listMensaje.get(position).getPhotoPerfil().isEmpty()){
            holder.getPhotoMsnPerfil().setImageResource(R.mipmap.ic_launcher_round);
        }else{
            Glide.with(c).load(listMensaje.get(position).getPhotoPerfil()).into(holder.getPhotoMsnPerfil());
        }
        Long codigoHora = listMensaje.get(position).getHour();
        Date d = new Date(codigoHora);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");//a pm o am
        holder.getHour().setText(sdf.format(d));
    }

    @Override
    public int getItemCount() {
        return listMensaje.size();
    }
}
