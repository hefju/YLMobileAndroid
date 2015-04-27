package YLSystemDate;


import android.content.Context;
import android.media.MediaPlayer;

import ylescort.ylmobileandroid.R;

/**
 * Created by Administrator on 2015/4/27.
 */
public class YLMediaPlayer {

    public void SuccessOrFailMidia(String mediavoice,Context context) throws Exception{
        MediaPlayer mPlayer = new MediaPlayer();
        if (mediavoice.equals("success")){
            mPlayer = MediaPlayer.create(context, R.raw.msg);
            if(mPlayer.isPlaying()){
                return;
            }
        }else {
            mPlayer.setDataSource("/system/media/audio/notifications/Proxima.ogg");  //选用系统声音文件
            mPlayer.prepare();
        }
        mPlayer.start();
    }
}
