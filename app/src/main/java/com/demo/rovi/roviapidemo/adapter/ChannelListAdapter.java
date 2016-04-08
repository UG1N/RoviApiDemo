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
import com.demo.rovi.roviapidemo.model.channels.Channel;
import com.demo.rovi.roviapidemo.model.channels.WindowChannel;
import com.demo.rovi.roviapidemo.widget.ChannelsHorizontalListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChannelListAdapter extends RecyclerView.Adapter<ChannelListAdapter.ViewHolder> {

    private final List<Channel> mChannelList;
    private final ChannelsHorizontalListView.ChannelItemSelectionListener mChannelItemSelectionListener;

    public ChannelListAdapter(List<Channel> channelList, ChannelsHorizontalListView.ChannelItemSelectionListener channelItemSelectionListener) {
        mChannelList = new ArrayList<>(channelList);
        mChannelItemSelectionListener = channelItemSelectionListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_item, parent, false);
        return new ViewHolder(parent.getContext(), view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Channel channelItem = getItemByPosition(position);
        holder.bind(channelItem, mChannelItemSelectionListener);
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

        void bind(Channel channelItem, final ChannelsHorizontalListView.ChannelItemSelectionListener channelItemSelectionListener) {
            mChannelLogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    channelItemSelectionListener.onChannelClick(getAdapterPosition() + 1);
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
