package ws.billy.app.Code.Activities.Journal;

import android.content.Context;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import ws.billy.app.tools.ImageViewCircleTransform;
import ws.billy.app.R;

import java.util.ArrayList;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.ItemViewHolder> {
    private static ArrayList<Comment> dataList;
    private LayoutInflater mInflater;
    private Context context;

    public JournalAdapter(Context ctx, ArrayList<Comment> data) {
        context = ctx;
        dataList = data;
        mInflater = LayoutInflater.from(context);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgProfile;
        private TextView textName;

        public ItemViewHolder(View itemView) {
            super(itemView);

            imgProfile = (ImageView) itemView.findViewById(R.id.imgProfile);
            textName = (TextView) itemView.findViewById(R.id.textName);
        }

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.specificjournal, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        Spannable word = new SpannableString(dataList.get(position).getName());
        word.setSpan(new StyleSpan(Typeface.BOLD), 0, word.length(), 0);
        holder.textName.setText(word);

        Spannable wordTwo = new SpannableString(" " + dataList.get(position).getComment());
        holder.textName.append(wordTwo);

        Glide.with(context)
                .load(dataList.get(position).getProfileUrl())
                .transform(new ImageViewCircleTransform(context))
                .into(holder.imgProfile);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
