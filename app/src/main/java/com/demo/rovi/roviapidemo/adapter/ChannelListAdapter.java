package com.demo.rovi.roviapidemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.damnhandy.uri.template.UriTemplate;
import com.demo.rovi.roviapidemo.R;
import com.demo.rovi.roviapidemo.application.RoviApplication;
import com.demo.rovi.roviapidemo.model.TvChannels.Channel;
import com.demo.rovi.roviapidemo.model.TvChannels.WindowChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChannelListAdapter extends RecyclerView.Adapter<ChannelListAdapter.ViewHolder> {

    public interface ChannelLogoClickListener {
        // TODO: 31.03.2016 pass entity instead of index
        void onChannelClick(int i);
    }

    private final List<Channel> mChannelList;
    private final Context mContext;
    private final ChannelLogoClickListener mChannelLogoClickListener;

    public ChannelListAdapter(Context context, List<Channel> channelList, ChannelLogoClickListener channelLogoClickListener) {
        mContext = context;
        mChannelList = new ArrayList<>(channelList);
        mChannelLogoClickListener = channelLogoClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.channel_item, parent, false);
        return new ViewHolder(mContext, view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Channel channelItem = getItemByPosition(position);
        holder.bind(channelItem, mChannelLogoClickListener);
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        holder.unbind();
    }

    @Override
    public int getItemCount() {
        return mChannelList.size();
    }

    private Channel getItemByPosition(int position) {
        return mChannelList.get(position);
    }

    public void addNewChannels(List<Channel> channelList) {
        mChannelList.addAll(channelList);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.channel_logo)
        ImageView mChannelLogo;

        private final Context mContext;

        public ViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            ButterKnife.bind(this, itemView);
        }

        void bind(Channel channelItem, final ChannelLogoClickListener channelLogoClickListener) {
            mChannelLogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    channelLogoClickListener.onChannelClick(getAdapterPosition() + 1);
                }
            });

            loadChannelIcon(channelItem.getWindowChannels()[0]);
        }

        private void loadChannelIcon(WindowChannel windowChannel) {
            String logoUrl = null;
            if (windowChannel.getDataSources().getLogo() != null) {
                logoUrl = UriTemplate
                        .fromTemplate(RoviApplication.getInstance().getTemplateFile().getTemplate().getMediaLogo())
                        .set("style", "small")
                        .set("id", windowChannel.getDataSources().getLogo().getLogoReferences().getId())
                        .expand();
//                 TODO: 04.04.2016 need to remove + add constants
//                "http://cloud.rovicorp.com/media/v1/logo/small/" +
//                        windowChannel.getDataSources().getLogo().getLogoReferences().getId() +
//                        ".png";
            }
            Log.e("VHTAG", "URL -> " + logoUrl);
            Glide.with(mContext).load(logoUrl)
                    .placeholder(R.drawable.logo_3ss_preview)
                    .fitCenter()
                    .error(R.drawable.no_title)
                    .into(mChannelLogo);
        }

        public void unbind() {
            if (mChannelLogo != null) {
                Glide.clear(mChannelLogo);
            }
        }
    }
}
