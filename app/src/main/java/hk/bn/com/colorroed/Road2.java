package hk.bn.com.colorroed;

import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

//颜色条状物
public class Road2
{
	int mProgram;//自定义渲染管线着色器程序id
    int muMVPMatrixHandle;//总变换矩阵引用
    int maPositionHandle; //顶点位置属性引用
    int maColorHandle; //顶点颜色属性引用
    String mVertexShader;//顶点着色器代码脚本
    String mFragmentShader;//片元着色器代码脚本

	FloatBuffer mVertexBuffer;//顶点坐标数据缓冲
	FloatBuffer mColorBuffer;//顶点着色数据缓冲
    int vCount=0;
    int n=70;
    float f1,f2,f3,f4;
    float deltaT = (float) 1.0 / n;
    float T;
    public static  float vertices1[] = new float[70*3];


    public Road2(MySurfaceView mv, float x0, float x1, float y0, float y1, float z0)
    {    	
    	//初始化顶点坐标与着色数据
    	initVertexData(x0,x1,y0,y1,z0);
    	//初始化shader        
    	initShader(mv);
    }
    
    //初始化顶点坐标与着色数据的方法
    public void initVertexData(float x0,float x1,float y0,float y1,float z0)
    {
    	//顶点坐标数据的初始化================begin============================


        int count=0;

        float xPos[]=new float[]
                {
                x0* Constant.UNIT_SIZE,
                x0* Constant.UNIT_SIZE,
                x1* Constant.UNIT_SIZE,
                x1* Constant.UNIT_SIZE,

                };
        float yPos[]=new float[]//正数向下，负数向上
                {
                y0* Constant.UNIT_SIZE,
                y0* Constant.UNIT_SIZE,
                y1* Constant.UNIT_SIZE,
                y1* Constant.UNIT_SIZE,
                };
        float zPos[]=new float[]
                {
                -z0* Constant.UNIT_SIZE,
                -(z0+20f)* Constant.UNIT_SIZE,
                -(z0+40f)* Constant.UNIT_SIZE,
                -(z0+60f)* Constant.UNIT_SIZE,

        };




        for (int i = 0;i <n; i++) {
            T = i * deltaT;
            f1 = (1 - T) * (1 - T) * (1 - T);
            f2 = 3 * T * (1 - T) * (1 - T);
            f3 = 3 * T * T * (1 - T);
            f4 = T * T * T;

            vertices1[count++] = f1 * xPos[0] + f2 * xPos[1] + f3 * xPos[2] + f4 * xPos[3];
            vertices1[count++] = f1 * yPos[0] + f2 * yPos[1] + f3 * yPos[2] + f4 * yPos[3];
            vertices1[count++] = f1 * zPos[0] + f2 * zPos[1] + f3 * zPos[2] + f4 * zPos[3];
           // System.out.println("x= "+vertices1[count-2]+"  y=  "+vertices1[count-3]+"  z=  "+vertices1[count-1]+"    "+(count-1)+"222222222222");

        }

        vCount=n*2+2;	//计算总顶点数

        

		float[] vertices=new float[vCount*3];//顶点坐标数据
		
		//坐标数据初始化
		 count=0;
	for(int i=0;i<vertices1.length;i=i+3)
    {

        vertices[count++]=vertices1[i]+ Constant.UNIT_SIZE;
        vertices[count++]=vertices1[i+1];
        vertices[count++]=vertices1[i+2];
        vertices[count++]=vertices1[i]- Constant.UNIT_SIZE;
        vertices[count++]=vertices1[i+1];
        vertices[count++]=vertices1[i+2];




    }
		
		//重复第一批三角形的最后一个顶点
		vertices[count++]=vertices[count-4];
		vertices[count++]=vertices[count-4];
		vertices[count++]=vertices[count-4];
		

        //创建顶点坐标数据缓冲
        //vertices.length*4是因为一个整数四个字节
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mVertexBuffer = vbb.asFloatBuffer();//转换为Float型缓冲
        mVertexBuffer.put(vertices);//向缓冲区中放入顶点坐标数据
        mVertexBuffer.position(0);//设置缓冲区起始位置
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点坐标数据的初始化================end============================
       
    	//顶点颜色值数组，每个顶点4个色彩值RGBA
       	count = 0;
        float colors[]=new float[vCount*4];
        for(int i=0; i<colors.length; i+=8){
        	colors[count++] = 0;
        	colors[count++] = 0;
        	colors[count++] = 0;
        	colors[count++] = 0;
        	
        	colors[count++] = 0;
        	colors[count++] = 0;
        	colors[count++] = 0;
        	colors[count++] = 0;
        }
        
        //创建顶点着色数据缓冲
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
        cbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mColorBuffer = cbb.asFloatBuffer();//转换为Float型缓冲
        mColorBuffer.put(colors);//向缓冲区中放入顶点着色数据
        mColorBuffer.position(0);//设置缓冲区起始位置
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点着色数据的初始化================end============================
    }
    //初始化着色器
    public void initShader(MySurfaceView mv)
    {
    	//加载顶点着色器的脚本内容
        mVertexShader= ShaderUtil.loadFromAssetsFile("shader/vertex_w.sh", mv.getResources());
        //加载片元着色器的脚本内容
        mFragmentShader= ShaderUtil.loadFromAssetsFile("shader/frag_w.sh", mv.getResources());
        //基于顶点着色器与片元着色器创建程序
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //获取程序中顶点位置属性引用id  
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //获取程序中顶点颜色属性引用id  
        maColorHandle= GLES30.glGetAttribLocation(mProgram, "aColor");
        //获取程序中总变换矩阵引用id
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
    }
    
    public void drawSelf()
    {        
    	//指定使用某套着色器程序
    	 GLES30.glUseProgram(mProgram);
    	//将最终变换矩阵传入渲染管线
         GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
       //将顶点位置数据送入渲染管线
         GLES30.glVertexAttribPointer
         (
         		maPositionHandle,   
         		3, 
         		GLES30.GL_FLOAT,
         		false,
                3*4,   
                mVertexBuffer
         );       
       //将顶点颜色数据送入渲染管线
         GLES30.glVertexAttribPointer
         (
        		maColorHandle, 
         		4, 
         		GLES30.GL_FLOAT,
         		false,
                4*4,   
                mColorBuffer
         );   
       //启用顶点位置数据数组
         GLES30.glEnableVertexAttribArray(maPositionHandle);
       //启用顶点颜色数据数组
         GLES30.glEnableVertexAttribArray(maColorHandle);
         //绘制条状物
         GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0 , vCount); //采用三角形条带方式绘制
    }
}
