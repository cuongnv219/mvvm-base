package com.base.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

/**
 * cuongnv
 * 5/30/18
 */
public final class ImageLoaderUtils {

    public ImageLoaderUtils() {

    }

    public static void show(ImageView iv, String url) {
        Context c = iv.getContext();

        RequestOptions options = new RequestOptions()
                .centerCrop()
//                .error(R.drawable.image_not_found)
                .transform(new RoundedCorners(5))
                .priority(Priority.HIGH);
        Glide.with(c).load(url).apply(options)
                .into(iv);
    }

    public static void show(ImageView iv, int url) {
        Context c = iv.getContext();

        RequestOptions options = new RequestOptions()
                .centerCrop()
//                .error(R.drawable.bg_world_cup)
                .transform(new RoundedCorners(5))
                .priority(Priority.HIGH);
        Glide.with(c).load(url).apply(options)
                .into(iv);
    }
}
