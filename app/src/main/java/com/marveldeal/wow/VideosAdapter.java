package com.marveldeal.wow;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import im.ene.lab.toro.ToroVideoViewHolder;
import im.ene.lab.toro.widget.ToroVideoView;
public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {
        private List<WowAPI.VideoUploadModel> mDataset;
        Context c;
    public class VideoViewHolder extends ToroVideoViewHolder {
        TextView title;
        TextView desc;
        TextView tags;
        TextView uploadedOn;
        public VideoViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.title);
            desc = (TextView)itemView.findViewById(R.id.description);
            tags = (TextView)itemView.findViewById(R.id.tags);
            uploadedOn = (TextView)itemView.findViewById(R.id.datetime);
        }
        @Override protected ToroVideoView findVideoView(View itemView) {
            return (ToroVideoView) itemView.findViewById(R.id.video);
        }
         @Nullable
         @Override public String getVideoId() {
            return "my awesome video's id and its order: " + getAdapterPosition();
         }
         @Override public void bind(@Nullable Object object) {
            if (object != null && object instanceof WowAPI.VideoUploadModel) {
                mVideoView.setVideoPath(((WowAPI.VideoUploadModel) object).video_URL);
            }
          }
        }
        public VideosAdapter(Context c) {
            mDataset = new ArrayList<>();
            this.c = c;
        }
        public WowAPI.VideoUploadModel getDataAt(int i){
            return mDataset.get(i);
        }
        public void set(List<WowAPI.VideoUploadModel> mDataset){
            this.mDataset.addAll(mDataset);
            notifyDataSetChanged();
        }
        @Override
        public VideoViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_video, parent, false);
            return new VideoViewHolder(v);
        }
        @Override
        public void onBindViewHolder(VideoViewHolder holder, int position) {
            WowAPI.VideoUploadModel i = getDataAt(position);
            holder.bind(i);
            Log.d("wow", "data:-"+i.title+i.description+i.video_URL+i.tags);
            holder.title.setText(i.title);
            holder.desc.setText(i.description);
            holder.tags.setText(i.tags);
            holder.uploadedOn.setText(i.datetime);
        }
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
}
