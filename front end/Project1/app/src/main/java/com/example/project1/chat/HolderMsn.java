package com.example.project1.chat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project1.R;

import de.hdodenhof.circleimageview.CircleImageView;



public class HolderMsn extends RecyclerView.ViewHolder {

    private TextView name;
    private TextView msn;
    private TextView hour;
    private CircleImageView photoMsnPerfil;
    private ImageView photoMsn;

    public HolderMsn(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.nombreMensaje);
        msn = (TextView) itemView.findViewById(R.id.mensajeMensaje);
        hour = (TextView) itemView.findViewById(R.id.horaMensaje);
        photoMsnPerfil = (CircleImageView) itemView.findViewById(R.id.fotoPerfilMensaje);
        photoMsn = (ImageView) itemView.findViewById(R.id.mensajeFoto);
    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public TextView getMsn() {
        return msn;
    }

    public void setMsn(TextView msn) {
        this.msn = msn;
    }

    public TextView getHour() {
        return hour;
    }

    public void setHour(TextView hour) {
        this.hour = hour;
    }

    public CircleImageView getPhotoMsnPerfil() {
        return photoMsnPerfil;
    }

    public void setPhotoMsnPerfil(CircleImageView photoMsnPerfil) {
        this.photoMsnPerfil = photoMsnPerfil;
    }

    public ImageView getPhotoMsn() {
        return photoMsn;
    }

    public void setPhotoMsn(ImageView photoMsn) {
        this.photoMsn = photoMsn;
    }
}
