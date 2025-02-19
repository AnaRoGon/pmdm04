package dam.pmdm.spyrothedragon.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dam.pmdm.spyrothedragon.MainActivity;
import dam.pmdm.spyrothedragon.R;
import dam.pmdm.spyrothedragon.databinding.ActivityMainBinding;
import dam.pmdm.spyrothedragon.databinding.VideoBinding;
import dam.pmdm.spyrothedragon.models.Collectible;

public class CollectiblesAdapter extends RecyclerView.Adapter<CollectiblesAdapter.CollectiblesViewHolder> {
    private final Context context;
    private List<Collectible> list;
    private int clickCount = 0;
    private long startTime = 0;
    private static final int REQUIRED_CLICKS = 4;
    private static final long TIME_LIMIT = 2000;


    public CollectiblesAdapter(List<Collectible> collectibleList, Context context) {
        this.context = context;
        this.list = collectibleList;
    }

    @Override
    public CollectiblesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new CollectiblesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CollectiblesViewHolder holder, int position) {
        Collectible collectible = list.get(position);
        holder.nameTextView.setText(collectible.getName());

        // Cargar la imagen (simulado con un recurso drawable)
        int imageResId = holder.itemView.getContext().getResources().getIdentifier(collectible.getImage(), "drawable", holder.itemView.getContext().getPackageName());
        holder.imageImageView.setImageResource(imageResId);
        holder.itemView.setOnClickListener(view -> onClick(collectible, view));
    }

    private void onClick(Collectible collectible, View view) {

        if (collectible.getName().equals("Gemas")) {
            long currentTime = System.currentTimeMillis();
            if (clickCount == 0) {
                startTime = currentTime;
            }
            if (currentTime - startTime <= TIME_LIMIT) {
                clickCount++;
                if (clickCount == REQUIRED_CLICKS) {
                    //Se ha pulsado 5 veces dentro del tiempo limite establecido
                    //Toast.makeText(view.getContext(), "EasterEgg activado", Toast.LENGTH_SHORT).show();
                    //Se muestra el layout del video
                    ((MainActivity) context).initializeVideo();
                    clickCount = 0;
                }
            } else {
                //Se reinicia si se ha superado el tiempo límite
                clickCount = 0;
                startTime = currentTime;
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CollectiblesViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        ImageView imageImageView;

        public CollectiblesViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            imageImageView = itemView.findViewById(R.id.image);
        }
    }
}
