package hk.bn.com.colorroed;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class MainActivity extends Activity {
    static final int START_GAME=0;
    static final int STOP_GAME=0;
    private MySurfaceView mGLSurfaceView;
    private TipHelper tipHelper;
    Handler MainGame;//消息处理器
    Handler OverGame;
    static MediaPlayer MusicA;//背景音乐
    static MediaPlayer MusicB;//碰撞得分小球音效
    static MediaPlayer MusicC;//撞击障碍物小球音效
    static MediaPlayer MusicD;//跳跃音效
    static SoundPool soundPool;//声音池
    static HashMap<Integer,Integer> soundPoolMap;//声音池中声音的ID和定义声音的ID
    boolean Points = false;//是否正确碰撞加分


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //设置为全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //设置为竖屏模式
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MainGame = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);

                switch(msg.what)
                {
                    case START_GAME:
                        //初始化GLSurfaceView
                        initSound();//初始化声音
                        mGLSurfaceView = new MySurfaceView(MainActivity.this);
                        setContentView(mGLSurfaceView);
                        mGLSurfaceView.requestFocus();//获取焦点
                        mGLSurfaceView.setFocusableInTouchMode(true);//设置为可触控
                        break;
                }
            }
        };
        OverGame = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);
                switch (msg.what)
                {
                    case STOP_GAME:
                        setContentView(R.layout.end);
                        TipHelper.Vibrate(MainActivity.this,500);
                        //TipHelper.Vibrate(MainActivity.this, new long[]{800, 1000, 800, 1000, 800, 1000}, true);
                        break;
                }
            }


        };

        setContentView(R.layout.load);

        new Thread()
        {
            @Override
            public void run()
            {
                waitTwoSeconds();
                MainGame.sendEmptyMessage(START_GAME);
            }
        }.start();


    }

    public void waitTwoSeconds()
    {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void initSound()
    {
        if (MusicA != null)
        {
            return;
        }
        //背景音乐
        MusicA = MediaPlayer.create(this,R.raw.musica);
        MusicA.setLooping(true);
        MusicA.start();

//        //得分音乐
//        MusicB = MediaPlayer.create(this,R.raw.musicb);
//        MusicB.setLooping(true);

//        //碰撞得分音乐
//        MusicC = MediaPlayer.create(this,R.raw.musicc);
//        MusicC.setLooping(true);

        //跳跃音乐
        MusicD = MediaPlayer.create(this,R.raw.musicd);
        MusicD.setLooping(true);

        //创建声音池
        SoundPool.Builder spb = new SoundPool.Builder();
        //设置同时的最大数量
        spb.setMaxStreams(4);
        //准备声音池属性
        AudioAttributes.Builder aab = new AudioAttributes.Builder();
        //声音类型
        aab.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC);
        //声音用途
        aab.setUsage(AudioAttributes.USAGE_GAME);
        //设置声音池属性
        spb.setAudioAttributes(aab.build());
        //创建声音池
        soundPool = spb.build();
        soundPoolMap = new HashMap<Integer, Integer>();

        soundPoolMap.put(1,soundPool.load(this,R.raw.musicb,1));
    }
    //触摸事件回调方法
    @Override
    public boolean onTouchEvent(MotionEvent e)
    {

        mGLSurfaceView.xm=e.getX();
        switch (e.getAction()) {
        case MotionEvent.ACTION_MOVE:

            mGLSurfaceView.mPreviousX=mGLSurfaceView.xm;
            mGLSurfaceView.requestRender();//重绘画面
            if(mGLSurfaceView.peng)
            {
                playSound(1,0);
                mGLSurfaceView.peng=false;
               // System.out.println("音乐");
            }
            if(mGLSurfaceView.over==1)
            {
                MusicA.stop();
                OverGame.sendEmptyMessage(STOP_GAME);
                mGLSurfaceView.over = 0;
            }
            mGLSurfaceView.mPreviousX = mGLSurfaceView.xm;//记录触控笔位置

            break;
        }




        return true;
    }


    public void playSound(int sound, int loop)
    {
        AudioManager mgr = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
        float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = streamVolumeCurrent / streamVolumeMax;

        soundPool.play(soundPoolMap.get(sound), volume, volume, 1, loop, 1f);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mGLSurfaceView != null)
        {
            mGLSurfaceView.onResume();
            MusicA.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGLSurfaceView != null)
        {
            mGLSurfaceView.onPause();
        }
        if(MusicA != null)
        {
            MusicA.pause();
            MusicA=null;
        }
//        mGLSurfaceView.onPause();
    }
}
