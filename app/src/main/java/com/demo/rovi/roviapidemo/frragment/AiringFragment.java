package com.demo.rovi.roviapidemo.frragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.damnhandy.uri.template.UriTemplate;
import com.demo.rovi.roviapidemo.R;
import com.demo.rovi.roviapidemo.application.RoviApplication;
import com.demo.rovi.roviapidemo.model.BackendConstants;

public class AiringFragment extends Fragment {
    private static final String IMAGE_ID = "image_id";
    public static final String TAG = "AiringFragment";

    private String mImageIdArgument;

    public AiringFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static AiringFragment newInstance(String imgId) {
        AiringFragment fragment = new AiringFragment();
        Bundle args = new Bundle();
        args.putString(IMAGE_ID, imgId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImageIdArgument = getArguments().getString(IMAGE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_airing, container, false);
        ImageView firstText = (ImageView) view.findViewById(R.id.first_text);
        String getUrl = UriTemplate.fromTemplate(RoviApplication.getInstance().getTemplateFile().getTemplate().getAirImage().getAirMediaImageUrl())
                .set(BackendConstants.STRING_ID, mImageIdArgument)
                .set("aspect", "4x3")
                .set("size", "medium")
                .set("zoom", "std")
                .expand();

        Log.e(TAG, getUrl);

        Glide.with(this).load(getUrl).error(R.drawable.logo_3ss_preview).into(firstText);
        return view;
    }
}
