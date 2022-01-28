package ws.billy.app.Code.Activities.JournalList;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

import ws.billy.app.Code.Utility.Journal;
import ws.billy.app.tools.ImageViewCircleTransform;
import ws.billy.app.R;

import java.util.ArrayList;
import java.util.Objects;

public class JournalListAdapter extends FirestoreRecyclerAdapter<Journal, JournalListAdapter.ItemViewHolder> {
    private LayoutInflater mInflater;
    private Context context;
    private JournalClickListener clicklistener = null;

    public JournalListAdapter(Context ctx, FirestoreRecyclerOptions<Journal> options) {
        super(options);
        context = ctx;
        mInflater = LayoutInflater.from(context);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView image;
        private ImageView imgProfile;

        TextView name;
        TextView dateAdded;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            image = (ImageView) itemView.findViewById(R.id.imageMain);
            imgProfile = (ImageView) itemView.findViewById(R.id.imgProfile);
            dateAdded = itemView.findViewById(R.id.timeAgo);
            name = itemView.findViewById(R.id.textName);

        }

        @Override
        public void onClick(View v) {

            if (clicklistener != null) {
                clicklistener.itemClicked(v, getAdapterPosition());
            }
        }
    }

    public void setClickListener(JournalClickListener listener) {
        this.clicklistener = listener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.journallayout, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull Journal model) {

        System.out.println(model);

        holder.name.setText(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName());
        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(model.getTimestamp().getSeconds() *1000);
        holder.dateAdded.setText(timeAgo);

        Glide.with(context)
                .load(model.getUrl())
                .thumbnail(0.01f)
                .fitCenter()
                .into(holder.image);

        String profilerUrl;
        if(model.getProurl() == null) {
            profilerUrl = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png"; // default avatar
        } else {
            profilerUrl = model.getProurl();
        }
        Glide.with(context)
                .load(profilerUrl)
                .transform(new ImageViewCircleTransform(context))
                .into(holder.imgProfile);
    }

}


