package com.demo.rovi.roviapidemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.demo.rovi.roviapidemo.R;
import com.demo.rovi.roviapidemo.model.TvChannels.Channel;

import java.util.List;

public class ChannelListAdapter extends RecyclerView.Adapter<ChannelListAdapter.ViewHolder> {

    private List<Channel> mChannelList;
    private final Context mContext;
    private ChannelLogoClickListener mChannelLogoClickListener;

    public ChannelListAdapter(Context context, List<Channel> mChannelList) {
        this.mContext = context;
        this.mChannelList = mChannelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.channel_item, parent, false);
        return new ViewHolder(mContext, view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Channel channelItem = mChannelList.get(position);
        holder.bind(channelItem);
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        holder.unbind();
    }

    @Override
    public int getItemCount() {
        return mChannelList.size();
    }

    public void addNewChannels(List<Channel> channelList) {
        mChannelList.addAll(channelList);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        private TextView mChannelTitle;
        private ImageView mChannelLogo;
        private final Context mContext;

        public ViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
//            mChannelTitle = (TextView) itemView.findViewById(R.id.channel_name);
            mChannelLogo = (ImageView) itemView.findViewById(R.id.channel_logo);
            mChannelLogo.setOnClickListener(this);
        }

        void bind(Channel channelItem) {
//            mChannelTitle.setText(String.valueOf(channelItem.getWindowChannels()[0].getDataSources().getSourceRef().getId()));
            String logoUrl = "http://cloud.rovicorp.com/media/v1/logo/small/" +
                    channelItem.getWindowChannels()[0].getDataSources().getLogo().getLogoReferences().getId() +
                    ".png";

            Log.e("VHTAG", "URL -> " + logoUrl);
            Glide.with(mContext).load(logoUrl)
                    .placeholder(R.drawable.logo_3ss_preview)
                    .fitCenter()
                    .into(mChannelLogo);
        }

        public void unbind() {
            if (mChannelLogo != null) {
                Glide.clear(mChannelLogo);
            }
        }

        @Override
        public void onClick(View v) {

            if (mChannelLogoClickListener != null) {
                mChannelLogoClickListener.onChannelClick(getAdapterPosition() + 1);
            }
        }
    }

    public void setOnChannelClickListener(ChannelLogoClickListener channelLogoClickListener) {
        mChannelLogoClickListener = channelLogoClickListener;
    }

}
