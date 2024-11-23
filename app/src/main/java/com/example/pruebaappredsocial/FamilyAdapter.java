package com.example.pruebaappredsocial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.FamilyViewHolder> {

    private List<FamilyMember> familyList;
    private Context context;

    public FamilyAdapter(List<FamilyMember> familyList, Context context) {
        this.familyList = familyList;
        this.context = context;
    }

    @NonNull
    @Override
    public FamilyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_family_member, parent, false);
        return new FamilyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FamilyViewHolder holder, int position) {
        FamilyMember member = familyList.get(position);

        holder.tvName.setText(member.getEmailFamily());
        holder.tvRelation.setText(member.getRelation());

        holder.btnRemove.setOnClickListener(v -> {
            Toast.makeText(context, "Eliminar familiar: " + member.getEmailFamily(), Toast.LENGTH_SHORT).show();
            // Implementa aquí la lógica para eliminar al familiar
        });
    }

    @Override
    public int getItemCount() {
        return familyList.size();
    }

    static class FamilyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvRelation;
        Button btnRemove;

        public FamilyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvFamilyName);
            tvRelation = itemView.findViewById(R.id.tvRelation);
            btnRemove = itemView.findViewById(R.id.btnRemoveFamily);
        }
    }
}
