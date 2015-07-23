package com.example.dibyenduroy.livingsocial.helper;

import android.content.Context;
import android.widget.ImageView;

import com.example.dibyenduroy.livingsocial.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

/**
 * Created by dibyenduroy on 2/23/15.
 */
public class PicassoHelper {

    public static class PicassoBuilder {


        int placeHolderResource = Integer.MIN_VALUE;
        int errorResource = Integer.MIN_VALUE;
        Context context;
        Callback callback;
        Target target;

        String url;
        ImageView imageView;

        public PicassoBuilder(Context context){
            this.context = context;
        }

        public PicassoBuilder setPlaceHolderResource(int placeHolderResource) {
            this.placeHolderResource = placeHolderResource;
            return this;
        }

        public PicassoBuilder setErrorResource(int errorResource) {
            this.errorResource = errorResource;
            return this;
        }

        public PicassoBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        public PicassoBuilder setImageView(ImageView imageView) {
            this.imageView = imageView;
            return this;
        }

        public PicassoBuilder setCallback(Callback target){
            this.callback = target;
            return this;
        }

        public PicassoBuilder setTarget(Target target){
            this.target = target;
            return this;
        }

        public void build(){
            if(url != null && !url.equals("")){
                RequestCreator creator = Picasso.with(context).load(url);
                if(errorResource != Integer.MIN_VALUE){
                    creator.error(errorResource);
                }

                if(placeHolderResource != Integer.MIN_VALUE){
                    creator.placeholder(placeHolderResource);
                }
                if(imageView != null){
                    if(callback != null){
                        creator.into(imageView, callback);
                    }else{
                        creator.into(imageView);
                    }
                    imageView.setTag(R.string.imageview_url, url);
                }else if(target != null){
                    creator.into(target);
                }
            }else{
                if(imageView != null){
                    if(errorResource != Integer.MIN_VALUE){
                        imageView.setImageResource(errorResource);
                    }else{
                        imageView.setImageResource(0);
                    }
                    imageView.setTag(R.string.imageview_url, null);
                    if(callback != null){
                        callback.onError();
                    }
                }else if(target != null){
                    target.onBitmapFailed(null);
                }


            }
        }
    }
}
