package com.example.pruebaappredsocial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private Context context;
    private List<Video> videoList;

    public VideoAdapter(Context context, List<Video> videoList) {
        this.context = context;
        this.videoList = videoList;
    }


    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_item, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Video video = videoList.get(position);
        holder.tvNombreUsuario.setText(video.getNombreUsuario());
        holder.tvDescripcionVideo.setText(video.getDescripcion());
        holder.tvNumLikes.setText(String.valueOf(video.getNumLikes()));
        holder.tvNumComentarios.setText(String.valueOf(video.getNumComentarios()));
        holder.tvNumCompartidos.setText(String.valueOf(video.getNumCompartidos()));

        // Carga la miniatura del video usando Picasso
        Picasso.get().load(video.getUrlMiniatura()).into(holder.ivMiniaturaVideo);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombreUsuario, tvDescripcionVideo, tvNumLikes, tvNumComentarios, tvNumCompartidos;
        ImageView ivMiniaturaVideo;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreUsuario = itemView.findViewById(R.id.tvNombreUsuario);
            tvDescripcionVideo = itemView.findViewById(R.id.tvDescripcionVideo);
            ivMiniaturaVideo = itemView.findViewById(R.id.ivMiniaturaVideo);
            tvNumLikes = itemView.findViewById(R.id.tvNumLikes);
            tvNumComentarios = itemView.findViewById(R.id.tvNumComentarios);
            tvNumCompartidos = itemView.findViewById(R.id.tvNumCompartidos);
        }
    }
}
