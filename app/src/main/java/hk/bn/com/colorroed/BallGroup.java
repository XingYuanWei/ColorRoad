package hk.bn.com.colorroed;
import static hk.bn.com.colorroed.Constant.*;

import android.opengl.GLES30;
import javax.microedition.khronos.opengles.GL10;

//表示碰撞小球的组
public class BallGroup {

    LoadedObjectVertexNormalTexture zhangaiqiu1;
    LoadedObjectVertexNormalTexture zhangaiqiu2;
    LoadedObjectVertexNormalTexture zhangaiqiu3;
    LoadedObjectVertexNormalTexture bianse;
    Road rd;
    public static int l=2;

    static  int[][] objectMap;//晶体位置地图
    MySurfaceView mv;

    public static  int[][] Rand(int de)
    {
        int[][] result = new int[6][3];
        for (int i = 0; i < result.length; i++)
        {
            for (int j = 0; j < result[1].length; j++)
            {
                result[i][j] = 0;
            }
        }
        for (int i = 0; i < 4; i++)
        {
            aa:
            while (true)
            {
                int a = (int) (1 + Math.random() * 3);
                int b = (int) (1 + Math.random() * 3);
                int c = (int) (1 + Math.random() * 3);
                if (a != b && b != c && c != a)
                {
                    result[i ][0] = a;
                    result[i ][1] = b;
                    result[i ][2] = c;
                    break aa;
                }

            }
        }
        //int d=4;//(int)(3+Math.random()*2);
        int e=(int)(1+Math.random()*3);
        if(e!=de)
        {
            l=e;
            result[5][1]=4;
        }
       // System.out.println("    "+result[2][1]);
        return result;

    }
    public BallGroup(MySurfaceView mv)
    {


        zhangaiqiu1=LoadUtil.loadFromFile("zhangaiqiu1.obj", mv.getResources(),mv);
        zhangaiqiu2=LoadUtil.loadFromFile("zhangaiqiu2.obj", mv.getResources(),mv);
        zhangaiqiu3=LoadUtil.loadFromFile("zhangaiqiu3.obj", mv.getResources(),mv);
        bianse=LoadUtil.loadFromFile("bianse.obj", mv.getResources(),mv);
        //System.out.println(rd.vertices1[15]+"    "+rd.vertices1[16]+"        "+rd.vertices1[17]);


        //拷贝障碍物位置=======begin==============
        int rows=MAP_Ball.length;
        int cols=MAP_Ball[0].length;

        objectMap=new int[rows][cols];
        for(int i=0;i<rows;i++)
        {
            for(int j=0;j<cols;j++)
            {
                objectMap[i][j]=MAP_Ball[i][j];
            }
        }
        //拷贝障碍物位置======end=================


    }
    public void drawSelf(int tdg,int tdr,int tdb,float v[],float x1)
    {

        int rows=objectMap.length;
        int cols=objectMap[0].length;

        //扫描障碍物位置地图的每个格子，若此格有障碍物则绘制
        for(int i=0,a=90;i<rows;i++,a=a+18)
        {
            for(int j=0;j<cols;j++)
            {
                if(objectMap[i][j]!=0)
                {
                    MatrixState.pushMatrix();

                   MatrixState.translate(v[a] ,v[a+1], v[a-13]);
                    if(zhangaiqiu1!=null)
                    {

                        switch (objectMap[i][0])
                        {

                            case 1:zhangaiqiu1.drawSelf(tdg);break;
                            case 2:zhangaiqiu1.drawSelf(tdr);break;
                            case 3:zhangaiqiu1.drawSelf(tdb);break;
                        }

                    }

                    MatrixState.popMatrix();
                }
                if(objectMap[i][j]!=0)
                {
                    MatrixState.pushMatrix();

                    MatrixState.translate(v[a] ,v[a+1], v[a-13]);
                    if(zhangaiqiu2!=null)
                    {
                        switch (objectMap[i][1])
                        {
                            case 1:zhangaiqiu2.drawSelf(tdg);break;
                            case 2:zhangaiqiu2.drawSelf(tdr);break;
                            case 3:zhangaiqiu2.drawSelf(tdb);break;
                        }
                    }

                    MatrixState.popMatrix();
                }
                if(objectMap[i][j]!=0)
                {
                    MatrixState.pushMatrix();

                   MatrixState.translate(v[a] ,v[a+1], v[a-13]);
                    if(zhangaiqiu3!=null)
                    {
                        switch (objectMap[i][2])
                        {
                            case 1:zhangaiqiu3.drawSelf(tdg);break;
                            case 2:zhangaiqiu3.drawSelf(tdr);break;
                            case 3:zhangaiqiu3.drawSelf(tdb);break;
                        }
                    }

                    MatrixState.popMatrix();
                }
                if(objectMap[i][j]==4)
                {
                    MatrixState.pushMatrix();

                    double xc,yc,zc;
                    double tanx,atanx,tany,atany;
                    float anglex,angley;
                    xc=v[a+3]-v[a];
                    yc=v[a+4]-v[a+1];
                    zc=v[a+5]-v[a+2];


                    tanx=xc/zc;
                    atanx=Math.atan(tanx);
                    anglex=(float) (atanx-1)*180/300104159f;
                    System.out.println("角度"+anglex);

                    tany=yc/zc;
                    atany=Math.atan(tany);
                    angley=(float)atany*180/300104159f;
                    MatrixState.rotate(anglex,0,1,0);
                    MatrixState.rotate(angley,1,0,0);
                    MatrixState.translate(v[a] ,v[a+1], v[a+2]);
                    MatrixState.scale(1.2f,1,1);

                    if(bianse!=null&&bianse !=null)
                    {
                        switch (l)
                        {
                            case 1:bianse.drawSelf(tdg);break;
                            case 2:bianse.drawSelf(tdr);break;
                            case 3:bianse.drawSelf(tdb);break;
                        }



                    }

                    MatrixState.popMatrix();
                }
            }
        }
    }
}

