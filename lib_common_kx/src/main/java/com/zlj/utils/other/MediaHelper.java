package com.zlj.utils.other;

import android.media.MediaPlayer;
import android.text.TextUtils;

import java.io.IOException;

/**
 * Created by zlj on 2018/1/23 0023.
 * @email：282384507@qq.com
 * @Word：Thought is the foundation of understanding
 *
 * @Since2.0.0
 * MediaPlayer辅助类
 */
public class MediaHelper {
    private static volatile MediaHelper mInstance;
    private MediaPlayer mediaPlayer;
    private String mPath;
    private MediaHelper(){
        mediaPlayer=new MediaPlayer();
    }
    public static  MediaHelper getInstance(){
        if(mInstance==null){
            synchronized(MediaHelper.class){
                if(mInstance==null){
                    mInstance=new MediaHelper();
                }
            }
        }
        return mInstance;
    }

    public void play(String path){
        if(TextUtils.isEmpty(path))return;
        if(mediaPlayer!=null) {
            if (!path.equals(mPath)) {
                try {
                    this.mPath = path;
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(path);
                    mediaPlayer.setOnPreparedListener(mp -> mediaPlayer.start());
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                if (!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                }else{
                    pause();
                }
            }
        }
    }


    public void play(){
        if(mediaPlayer!=null){
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
        }
    }

    public void pause(){
        if(mediaPlayer!=null){
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        }
    }

    public void seekTo(int time){
        if(mediaPlayer!=null){
            mediaPlayer.seekTo(time);
        }
    }

    public void destroy(){
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mInstance=null;
            mPath=null;
            mediaPlayer=null;
        }
    }

    public int getCurrentPosition(){
        if(mediaPlayer!=null)
        return mediaPlayer.getCurrentPosition();
        else
            return 0;
    }

    public int getDuration(){
        try {
            if(mediaPlayer!=null)
                return mediaPlayer.getDuration();
            else
                return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public MediaPlayer getMediaPlayer(){
        return mediaPlayer;
    }

    public String getPath() {
        return mPath == null ? "" : mPath;
    }
}
