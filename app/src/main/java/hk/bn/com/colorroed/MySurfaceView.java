package hk.bn.com.colorroed;//声明包

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static hk.bn.com.colorroed.BallGroup.Rand;
import static hk.bn.com.colorroed.BallGroup.l;
import static hk.bn.com.colorroed.Constant.RATIO;
import static hk.bn.com.colorroed.Constant.threadFlag;

class MySurfaceView extends GLSurfaceView
{
    public SceneRenderer mRenderer;//场景渲染器

    public boolean peng=false;

    public float mPreviousX=500;//上次的触控位置X坐标
    public int objectCount=0;//已碰撞小球的数量
    int textureId;//系统分配的纹理id
    int textureId0;
    int textblueId;
    int textredId;
    public int numbitId;//数量纹理ID
    public int over=0;
    Road rd;
    Road2 rd2;

    static float bx;//摄像机x坐标
    static float by;//摄像机y坐标
    static float bz;//摄像机z坐标

    static float cx;//摄像机x坐标
    static float cy;//摄像机y坐标
    static float cz;//摄像机z坐标

    static float tx;//观察目标点x坐标
    static float ty;//观察目标点y坐标
    static float tz;//观察目标点z坐标
    static int roadv=3;

    float xm=0;
     static  int decide=1;
    static boolean or=true;
    static boolean paint_next=true;
    static float z0=0;

    static long time=70;






    public MySurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(3); //设置使用OPENGL ES3.0
        mRenderer = new SceneRenderer();	//创建场景渲染器
        setRenderer(mRenderer);				//设置渲染器		        
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//设置渲染模式为主动渲染
    }
	



    public boolean IsCollision(float x1,int row )
    {
         // System.out.println("   x= "+x1);
        if(mRenderer.bg.objectMap[row][0]==0||mRenderer.bg.objectMap[row][1]==0||mRenderer.bg.objectMap[row][2]==0)
        {
            return true;
        }
        else
        {

            float x = x1;

            if(or)
            {
                if (x <= rd.vertices1[roadv] + 500 - 240) {

                    if (mRenderer.bg.objectMap[row][0] == decide) {
                        objectCount++;
                        mRenderer.bg.objectMap[row][0] = 0;

                        peng = true;

                        return true;
                    } else {
                         over = 1;

                        return true;
                    }

                    //System.out.println("1");
                } else if (rd.vertices1[roadv] + 500 - 175 <= x && x <= rd.vertices1[roadv] + 500 + 135) {

                    if (mRenderer.bg.objectMap[row][1] == decide) {
                        objectCount++;
                        mRenderer.bg.objectMap[row][1] = 0;

                        peng = true;

                        return true;
                    } else {
                         over = 1;

                        return true;
                    }

                    // System.out.println("2");
                } else if (rd.vertices1[roadv] + 500 + 200 <= x) {
                    if (mRenderer.bg.objectMap[row][2] == decide) {
//                        System.out.println("变化前第一个" + mRenderer.bg.objectMap[row][0]);
//                        System.out.println("变化前第三个" + mRenderer.bg.objectMap[row][2]);
                        mRenderer.bg.objectMap[row][2] = 0;
//                        System.out.println("变化后第一个" + mRenderer.bg.objectMap[row][0]);
//                        System.out.println("变化后第三个" + mRenderer.bg.objectMap[row][2]);


                        objectCount++;
                        peng = true;

                        return true;
                    } else {
                          over = 1;

                        return true;
                    }

                    // System.out.println("3");
                }
            }else {
                if (x <= rd2.vertices1[roadv]+500-240)
                {

                    if (mRenderer.bg.objectMap[row][0]== decide)
                    {
                        objectCount++;
                        mRenderer.bg.objectMap[row][0] = 0;

                        peng=true;

                        return true;
                    }else {
                         over = 1;

                        return true;
                    }

                    //System.out.println("1");
                }else if (rd2.vertices1[roadv]+500-175<= x && x <= rd2.vertices1[roadv]+500+135)
                {

                    if (mRenderer.bg.objectMap[row][1] == decide)
                    {
                        objectCount++;
                        mRenderer.bg.objectMap[row][1] = 0;

                        peng=true;

                        return true;
                    }else {
                         over = 1;

                        return true;
                    }

                    // System.out.println("2");
                } else if (rd2.vertices1[roadv]+500+200 <= x)
                {
                    if (mRenderer.bg.objectMap[row][2] == decide)
                    {
//                        System.out.println("变化前第一个"+mRenderer.bg.objectMap[row][0]);
//                        System.out.println("变化前第三个"+mRenderer.bg.objectMap[row][2]);
                        mRenderer.bg.objectMap[row][2] = 0;
//                        System.out.println("变化后第一个"+mRenderer.bg.objectMap[row][0]);
//                        System.out.println("变化后第三个"+mRenderer.bg.objectMap[row][2]);


                        objectCount++;
                        peng=true;

                        return true;
                    }else {
                          over = 1;

                        return true;
                    }

                    // System.out.println("3");
                }

            }

            peng=false;
            return false;
        }
    }

	public  class SceneRenderer implements Renderer {

        //从指定的obj文件中加载的对象
        LoadedObjectVertexNormalTexture ball;


        BallGroup bg;

        Score scorebit;//当前获得的晶体数量


        public void onDrawFrame(GL10 gl) {

            float bxTemp=bx;float byTemp=by;float bzTemp=bz;
            float cxTemp=cx;float cyTemp=cy;float czTemp=cz;
            float txTemp=tx;float tyTemp=ty;float tzTemp=tz;


            //清除深度缓冲与颜色缓冲
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            MatrixState.setCamera(
                    cxTemp,   //人眼位置的X
                    cyTemp, 	//人眼位置的Y
                    czTemp,   //人眼位置的Z
                    txTemp, 	//人眼球看的点X
                    tyTemp,   //人眼球看的点Y
                    tzTemp,

                    0f,1.0f,0.0f);

            //坐标系推远
            MatrixState.pushMatrix();
            //MatrixState.translate(0, -2f, -5f);

            MatrixState.translate(bxTemp ,byTemp, bzTemp);
            MatrixState.translate(mPreviousX/50 -10f, 0, 0);



            //若加载的物体不为空则绘制物体
            if (ball != null) {
                switch (decide)
                {
                    case 1:ball.drawSelf(textureId);break;
                    case 2:ball.drawSelf(textredId);break;
                    case 3:ball.drawSelf(textblueId);break;
                }

            }

            MatrixState.popMatrix();



            //绘制轨道
            MatrixState.pushMatrix();//保护现场

            MatrixState.translate(0, -10f, 0f);//执行平移 Y X Z

            rd.drawSelf();

            MatrixState.popMatrix();  //恢复现场

            MatrixState.pushMatrix();//保护现场

            MatrixState.translate(0, -10f, 0f);//执行平移 Y X Z

            rd2.drawSelf();

            MatrixState.popMatrix();  //恢复现场





            MatrixState.pushMatrix();//gl.glPushMatrix();

            MatrixState.translate(0f,-9.5f,0 );

            if(or)
            {

                bg.drawSelf(textureId,textredId,textblueId,rd.vertices1,mPreviousX);
            }else {
                bg.drawSelf(textureId,textredId,textblueId,rd2.vertices1,mPreviousX);
            }

            MatrixState.popMatrix();



            //开启混合
            GLES30.glEnable(GLES30.GL_BLEND);
//            //设置源混合因子与目标混合因子
            MatrixState2D.pushMatrix();
            MatrixState2D.scale(3f,4f,2f);


            MatrixState2D.translate(-0.02f, 0.11f, 0);

                scorebit.drawSelf();//绘制得分

            MatrixState2D.popMatrix();

//            //禁止混合
            GLES30.glDisable(GLES30.GL_BLEND);
        }  

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //设置视窗大小及位置 
        	GLES30.glViewport(0, 0, width, height);
        	//计算GLSurfaceView的宽高比
            float ratio = (float) width / height;
            RATIO = (float) width / height;
            //调用此方法计算产生透视投影矩阵
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 2, 800);
            //调用此方法产生摄像机9参数位置矩阵
            MatrixState.setCamera(

                    cx,   //人眼位置的X
                    cy, 	//人眼位置的Y
                    cz,   //人眼位置的Z
                    tx, 	//人眼球看的点X
                    ty,   //人眼球看的点Y
                    tz,

                    0f,1.0f,0.0f);
            MatrixState2D.setProjectOrtho(-RATIO, RATIO, -1, 1, 0, 40);
            //设置照相机
            MatrixState2D.setCamera
                    (

                            0,   //人眼位置的X
                            0,    //人眼位置的Y
                            0,   //人眼位置的Z
                            0,    //人眼球看的点X
                            0,   //人眼球看的点Y
                            -10,   //人眼球看的点Z
                            0,
                            1,
                            0
                    );

            //启动一个线程定时移动轨道
            new Thread()
            {
                public void run()
                {
                    while(threadFlag)
                    {



                        if (over == 1)
                        {
                            threadFlag=false;
                        }
                        roadv=roadv+3;
                        if(roadv==54)
                        {
                            //z0=z0+60;
                            if(paint_next)
                            {
                                float x0=rd.vertices1[207]/15;//(float)(Math.random()*20-10);
                                float x1=(float)(Math.random()*20-10);
                                float y0=rd.vertices1[208]/15;//(float)(Math.random()*10-5);
                                float y1=(float)(Math.random()*10-5);
//                                System.out.println(" x0= " + rd.vertices1[0] + "  y0=  " + rd.vertices1[1] + "  z0=  " + rd.vertices1[2]);
//                                System.out.println(" x0= " + rd.vertices1[207] + "  y0=  " + rd.vertices1[208] + "  z0=  " + rd.vertices1[209]);
                                rd2.initVertexData(x0,x1,y0,y1,z0+=59.1);//-rd2.vertices1[209]*15
                                paint_next=false;
                            }else {
                                float x0= rd2.vertices1[207]/15;//(float)(Math.random()*20-10);
                                float x1=(float)(Math.random()*20-10);
                                float y0=rd2.vertices1[208]/15;//(float)(Math.random()*10-5);
                                float y1=(float)(Math.random()*10-5);
//                                System.out.println(" x0= " + rd2.vertices1[0] + "  y0=  " + rd2.vertices1[1] + "  z0=  " + rd2.vertices1[2]);
//                                System.out.println(" x0= " + rd2.vertices1[207] + "  y0=  " + rd2.vertices1[208] + "  z0=  " + rd2.vertices1[209]);
                                rd.initVertexData(x0,x1,y0,y1, z0+=59.1);//z0+=59.1458  -rd2.vertices1[209]*15
                                paint_next=true;
                            }
                        }

                        if(roadv==207)
                        {
                            roadv=0;
                            mRenderer.bg.objectMap=bg.Rand(decide);


                            if (or)
                            {
                                or=false;
                            }else {
                                or=true;
                            }

                        }
                        if(or)
                        {
                            bx= rd.vertices1[roadv];
                            by=rd.vertices1[roadv+1]-10;
                            bz=rd.vertices1[roadv+2];

                            //cx= rd.vertices1[roadv];
                            cx= rd.vertices1[roadv]-(rd.vertices1[roadv+3]-rd.vertices1[roadv])*(60)/(rd.vertices1[roadv+2]-rd.vertices1[roadv+5]);
                            cy=rd.vertices1[roadv+1]+20-(rd.vertices1[roadv+4]-rd.vertices1[roadv+1])*(60)/(rd.vertices1[roadv+2]-rd.vertices1[roadv+5]);
                            cz=rd.vertices1[roadv+2]+60;

                            tx=rd.vertices1[roadv];
                            ty=rd.vertices1[roadv+1]+15;
                            tz=rd.vertices1[roadv+2];

                            if(rd.vertices1[roadv+2]<=rd.vertices1[92]+5&&rd.vertices1[roadv+2]>=rd.vertices1[92]-5)
                            {

                                IsCollision(mPreviousX+rd.vertices1[roadv],0);

                            }

                            if(rd.vertices1[roadv+2]<=rd.vertices1[110]+5&&rd.vertices1[roadv+2]>=rd.vertices1[110]-5)
                            {

                                IsCollision(mPreviousX+rd.vertices1[roadv],1);

                            }
                            if(rd.vertices1[roadv+2]<=rd.vertices1[128]+5&&rd.vertices1[roadv+2]>=rd.vertices1[128]-5)
                            {

                                IsCollision(mPreviousX+rd.vertices1[roadv],2);

                            }
                            if(rd.vertices1[roadv+2]<=rd.vertices1[146]+5&&rd.vertices1[roadv+2]>=rd.vertices1[146]-5)
                            {

                                IsCollision(mPreviousX+rd.vertices1[roadv],3);

                            }
                            if (rd.vertices1[roadv+2]==rd.vertices1[191])
                            {
                                if (mRenderer.bg.objectMap[5][1]==4)
                                {
                                    decide=l;
                                }
                            }
                        }else {
                            bx= rd2.vertices1[roadv];
                            by=rd2.vertices1[roadv+1]-10;
                            bz=rd2.vertices1[roadv+2];

                            //cx= rd.vertices1[roadv];
                            cx= rd2.vertices1[roadv]-(rd2.vertices1[roadv+3]-rd2.vertices1[roadv])*(60)/(rd2.vertices1[roadv+2]-rd2.vertices1[roadv+5]);
                            cy=rd2.vertices1[roadv+1]+20-(rd2.vertices1[roadv+4]-rd2.vertices1[roadv+1])*(60)/(rd2.vertices1[roadv+2]-rd2.vertices1[roadv+5]);
                            cz=rd2.vertices1[roadv+2]+60;

                            tx=rd2.vertices1[roadv];
                            ty=rd2.vertices1[roadv+1]+15;
                            tz=rd2.vertices1[roadv+2];

                            if(rd2.vertices1[roadv+2]<=rd2.vertices1[92]+5&&rd2.vertices1[roadv+2]>=rd2.vertices1[92]-5)
                            {

                                IsCollision(mPreviousX+rd2.vertices1[roadv],0);

                            }

                            if(rd2.vertices1[roadv+2]<=rd2.vertices1[110]+5&&rd2.vertices1[roadv+2]>=rd2.vertices1[110]-5)
                            {

                                IsCollision(mPreviousX+rd2.vertices1[roadv],1);

                            }
                            if(rd2.vertices1[roadv+2]<=rd2.vertices1[128]+5&&rd2.vertices1[roadv+2]>=rd2.vertices1[128]-5)
                            {

                                IsCollision(mPreviousX+rd2.vertices1[roadv],2);

                            }
                            if(rd2.vertices1[roadv+2]<=rd2.vertices1[146]+5&&rd2.vertices1[roadv+2]>=rd2.vertices1[146]-5)
                            {
                                                 // System.out.println(rd2.vertices1[roadv+2]+"  "+rd2.vertices1[146]);
                                IsCollision(mPreviousX+rd2.vertices1[roadv],3);

                            }
                            if (rd2.vertices1[roadv+2]==rd2.vertices1[191])
                            {
                                if (mRenderer.bg.objectMap[5][1]==4)
                                {
                                    decide=l;
                                }
                            }
                        }


                        if(time>=20)
                        {
                            time=time-1;
                        }
                        try {
                            Thread.sleep(time);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();



        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config)
        {
            //设置屏幕背景色RGBA
            GLES30.glClearColor(1f,1f,1f,1.0f);
            //打开深度检测
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            //打开背面剪裁   
            //GLES30.glEnable(GLES30.GL_CULL_FACE);
            GLES30.glDisable(GLES30.GL_CULL_FACE);
            //初始化变换矩阵
            MatrixState.setInitStack();
            MatrixState2D.setInitStack();
            //初始化光源位置
            MatrixState.setLightLocation(40, 10, 20);
            //加载要绘制的物体
            ball= LoadUtil.loadFromFile("qiu5.obj", MySurfaceView.this.getResources(),MySurfaceView.this);

            bg=new BallGroup(MySurfaceView.this);
            rd=new Road(MySurfaceView.this,8,-6,5,-5,z0);
            rd2=new Road2(MySurfaceView.this,-6,8,-5,5,z0+59.1458f);


            //加载纹理图
            textureId=initTexture(R.drawable.green);
            textureId0=initTexture(R.drawable.malu);
            textblueId=initTexture(R.drawable.blue);
            textredId=initTexture(R.drawable.red);
            TextureManager.loadingTexture(MySurfaceView.this, 0, 1);
            numbitId = TextureManager.getTextures("num.png");
            scorebit = new Score(numbitId, MySurfaceView.this);
        }
    }

  	public int initTexture(int drawableId)//textureId
	{
		//生成纹理ID
		int[] textures = new int[1];
		GLES30.glGenTextures
		(
				1,          //产生的纹理id的数量
				textures,   //纹理id的数组
				0           //偏移量
		);    
		int textureId=textures[0];    
		GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_REPEAT);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_REPEAT);
        
        //通过输入流加载图片===============begin===================
        InputStream is = this.getResources().openRawResource(drawableId);
        Bitmap bitmapTmp;
        try 
        {
        	bitmapTmp = BitmapFactory.decodeStream(is);
        } 
        finally 
        {
            try 
            {
                is.close();
            } 
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        //通过输入流加载图片===============end===================== 
	   	GLUtils.texImage2D
	    (
	    		GLES30.GL_TEXTURE_2D, //纹理类型
	     		0, 
	     		GLUtils.getInternalFormat(bitmapTmp),
	     		bitmapTmp, //纹理图像
	     		GLUtils.getType(bitmapTmp),
	     		0 //纹理边框尺寸
	     );
	    bitmapTmp.recycle(); 		  //纹理加载成功后释放图片
        return textureId;
	}
}
