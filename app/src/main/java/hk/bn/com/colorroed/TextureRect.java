package hk.bn.com.colorroed;
import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

// 表示单个纹理矩形的类
public class TextureRect {
    private FloatBuffer mVertexBuffer;//顶点坐标数据缓冲
    private FloatBuffer mTextureBuffer;//顶点着色数据缓冲
    int vCount;
    int texId;
    int mProgram;// 自定义渲染管线程序id
    int muMVPMatrixHandle;// 总变换矩阵引用id
    int maPositionHandle; // 顶点位置属性引用id
    int maTexCoorHandle; // 顶点纹理坐标属性引用id
    int maNormalHandle; //顶点法向量属性引用
    int muMMatrixHandle;//位置、旋转变换矩阵
    String mVertexShader;// 顶点着色器
    String mFragmentShader;// 片元着色器
    MySurfaceView mv;
    boolean initFlag=false;

    public TextureRect(int texId, float X_UNIT_SIZE, float Y_UNIT_SIZE, float[] textures, MySurfaceView mv)
    {
        this.texId=texId;
        this.mv=mv;
        //顶点坐标数据的初始化================begin============================
        vCount=6;
        float vertices[]=new float[]
                {
                        -1*X_UNIT_SIZE,1*Y_UNIT_SIZE,0,
                        -1*X_UNIT_SIZE,-1*Y_UNIT_SIZE,0,
                        1*X_UNIT_SIZE,1*Y_UNIT_SIZE,0,

                        -1*X_UNIT_SIZE,-1*Y_UNIT_SIZE,0,
                        1*X_UNIT_SIZE,-1*Y_UNIT_SIZE,0,
                        1*X_UNIT_SIZE,1*Y_UNIT_SIZE,0
                };

        //创建顶点坐标数据缓冲
        //vertices.length*4是因为一个整数四个字节
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mVertexBuffer = vbb.asFloatBuffer();//转换为int型缓冲
        mVertexBuffer.put(vertices);//向缓冲区中放入顶点坐标数据
        mVertexBuffer.position(0);//设置缓冲区起始位置
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点坐标数据的初始化================end============================

        //顶点纹理数据的初始化================begin============================

        //创建顶点纹理数据缓冲
        ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length*4);
        tbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mTextureBuffer= tbb.asFloatBuffer();//转换为Float型缓冲
        mTextureBuffer.put(textures);//向缓冲区中放入顶点着色数据
        mTextureBuffer.position(0);//设置缓冲区起始位置
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点纹理数据的初始化================end============================
        //initShader(mv);
    }
    //初始化着色器
    public void initShader(MySurfaceView mv){
        //加载顶点着色器的脚本内容
        mVertexShader= ShaderUtil.loadFromAssetsFile("shader/vertex_nolight.sh", mv.getResources());
        //加载片元着色器的脚本内容
        mFragmentShader= ShaderUtil.loadFromAssetsFile("shader/frag_nolight.sh", mv.getResources());
        //基于顶点着色器与片元着色器创建程序
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //获取程序中顶点位置属性引用id
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //获取位置、旋转变换矩阵引用
        muMMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMMatrix");
        //获取程序中顶点法向量属性引用
        maNormalHandle= GLES30.glGetAttribLocation(mProgram, "aNormal");
        //获取程序中顶点纹理坐标属性引用id
        maTexCoorHandle= GLES30.glGetAttribLocation(mProgram, "aTexCoor");
        //BenWl= GLES30.glGetUniformLocation(mProgram, "sTextureHd");
        //获取程序中总变换矩阵引用id
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
    }
    public void drawSelf()
    {
        if(!initFlag)
        {
            //初始化着色器
            initShader(mv);
            initFlag=true;
        }
       GLES30.glEnable(GLES30.GL_BLEND);//打开混合
        //设置混合因子
        GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA);
        GLES30.glUseProgram(mProgram);//制定使用某套shader程序
        // 将最终变换矩阵传入shader程序
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,
                MatrixState2D.getFinalMatrix(), 0);
        // 为画笔指定顶点位置数据
        GLES30.glVertexAttribPointer(maPositionHandle, 3, GLES30.GL_FLOAT,
                false, 3 * 4, mVertexBuffer);
        // 为画笔指定顶点纹理坐标数据
        GLES30.glVertexAttribPointer(maTexCoorHandle, 2, GLES30.GL_FLOAT,
                false, 2 * 4, mTextureBuffer);
        // 允许顶点位置数据数组
        GLES30.glEnableVertexAttribArray(maPositionHandle);
        GLES30.glEnableVertexAttribArray(maTexCoorHandle);
        // 绑定纹理
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId);
        // 绘制纹理矩形
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount);

        //关闭混合
       GLES30.glDisable(GLES30.GL_BLEND);

//
    }
}