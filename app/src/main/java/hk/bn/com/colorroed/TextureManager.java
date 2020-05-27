package hk.bn.com.colorroed;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class TextureManager
{
    static String[] texturesName={


            "num.png",

    };//纹理图的名称

    static HashMap<String,Integer> texList=new HashMap<String,Integer>();//放纹理图的列表
    @SuppressLint("NewApi")
    public static int initTexture(MySurfaceView mv, String texName, boolean isRepeat)//生成纹理id
    {
        int[] textures=new int[1];
        GLES30.glGenTextures
                (
                        1,//产生的纹理id的数量
                        textures,//纹理id的数组
                        0//偏移量
                );
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textures[0]);//绑定纹理id
        //设置MAG时为线性采样
        GLES30.glTexParameterf
                (
                        GLES30.GL_TEXTURE_2D,
                        GLES30.GL_TEXTURE_MAG_FILTER,
                        GLES30.GL_LINEAR
                );
        //设置MIN时为最近点采样
        GLES30.glTexParameterf
                (
                        GLES30.GL_TEXTURE_2D,
                        GLES30.GL_TEXTURE_MIN_FILTER,
                        GLES30.GL_NEAREST
                );
        if(isRepeat)
        {
            //设置S轴的拉伸方式为重复拉伸
            GLES30.glTexParameterf
                    (
                            GLES30.GL_TEXTURE_2D,
                            GLES30.GL_TEXTURE_WRAP_S,
                            GLES30.GL_REPEAT
                    );
            //设置T轴的拉伸方式为重复拉伸
            GLES30.glTexParameterf
                    (
                            GLES30.GL_TEXTURE_2D,
                            GLES30.GL_TEXTURE_WRAP_T,
                            GLES30.GL_REPEAT
                    );
        }else
        {
            //设置S轴的拉伸方式为截取
            GLES30.glTexParameterf
                    (
                            GLES30.GL_TEXTURE_2D,
                            GLES30.GL_TEXTURE_WRAP_S,
                            GLES30.GL_CLAMP_TO_EDGE
                    );
            //设置T轴的拉伸方式为截取
            GLES30.glTexParameterf
                    (
                            GLES30.GL_TEXTURE_2D,
                            GLES30.GL_TEXTURE_WRAP_T,
                            GLES30.GL_CLAMP_TO_EDGE
                    );
        }
        String path="pic/"+texName;//定义图片路径
        InputStream in = null;
        try {
            in = mv.getResources().getAssets().open(path);
        }catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap= BitmapFactory.decodeStream(in);//从流中加载图片内容
        GLUtils.texImage2D
                (
                        GLES30.GL_TEXTURE_2D,//纹理类型，在OpenGL ES中必须为GL10.GL_TEXTURE_2D
                        0,//纹理的层次，0表示基本图像层，可以理解为直接贴图
                        bitmap,//纹理图像
                        0//纹理边框尺寸
                );
        bitmap.recycle();//纹理加载成功后释放内存中的纹理图
        return textures[0];
    }

    public static void loadingTexture(MySurfaceView mv, int start, int picNum)//加载所有纹理图
    {
        for(int i=start;i<start+picNum;i++)
        {
            int texture=0;
                texture=initTexture(mv,texturesName[i],true);
            texList.put(texturesName[i],texture);//将数据加入到列表中
        }
    }
    public static int getTextures(String texName)//获得纹理图
    {
        int result=0;
        if(texList.get(texName)!=null)//如果列表中有此纹理图
        {
            result=texList.get(texName);//获取纹理图
        }else
        {
            result=-1;
        }
        return result;
    }
}

