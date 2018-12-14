package com.handzapassignment.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.handzapassignment.R;
import com.handzapassignment.model.PostMedia;
import com.handzapassignment.utils.MediaUtils;
import java.util.ArrayList;

public class PostAttachmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<PostMedia> mMediaTypesList;
    private Context mContext;
    private RecyclerView rcView;
    public static final int THUMB_SIZE = 96;

    public PostAttachmentAdapter(Context context) {
        this.mContext = context;
        this.mMediaTypesList = new ArrayList<>();
    }

    public void setRecyclerViewReference(RecyclerView rcView){
        this.rcView = rcView;
    }

    public void addData(PostMedia postMedia) {
        mMediaTypesList.add(0, postMedia);
        if(mMediaTypesList.size() != 0){
            notifyItemInserted(0);
            rcView.scrollToPosition(0);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_post_attachment, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        PostMedia postMedia = mMediaTypesList.get(i);
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.imgPlayIcon.setVisibility(View.GONE);
        if (postMedia.mediaType == MediaUtils.MediaType.IMAGE) {
            Bitmap thumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(postMedia.mediaPath), THUMB_SIZE, THUMB_SIZE);
            holder.imgAttachedMedia.setImageBitmap(thumbImage);
        } else if (postMedia.mediaType == MediaUtils.MediaType.VIDEO) {
            holder.imgPlayIcon.setVisibility(View.VISIBLE);
            holder.imgAttachedMedia.setImageBitmap(ThumbnailUtils.createVideoThumbnail(postMedia.mediaPath, MediaStore.Video.Thumbnails.MICRO_KIND));
        } else if (postMedia.mediaType == MediaUtils.MediaType.DOCUMENT) {
            holder.imgAttachedMedia.setImageResource(R.drawable.ic_document);
        }
    }

    @Override
    public int getItemCount() {
        return mMediaTypesList != null ? mMediaTypesList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private ImageView imgAttachedMedia;
        private ImageView imgPlayIcon;

        public ViewHolder(View v) {
            super(v);
            imgAttachedMedia = v.findViewById(R.id.imgAttachment);
            imgPlayIcon = v.findViewById(R.id.imgPlayIcon);
        }
    }
}
