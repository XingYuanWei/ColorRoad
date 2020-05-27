package hk.bn.com.colorroed;
import static hk.bn.com.colorroed.Constant.ICON_HEIGHT;
import static hk.bn.com.colorroed.Constant.ICON_WIDTH;

//表示碰撞小球数量的类
public class Score
{
    MySurfaceView mv;
    TextureRect[] numbers=new TextureRect[10];

    public Score(int texId, MySurfaceView mv)
    {
        this.mv=mv;

        //生成0-9十个数字的纹理矩形
        for(int i=0;i<10;i++)
        {
            numbers[i]=new TextureRect
                    (
                            texId,
                            ICON_WIDTH*0.7f/2,
                            ICON_HEIGHT*0.7f/2,
                            new float[]
                                    {
                                            0.1f*i,0, 0.1f*i,1, 0.1f*(i+1),0,
                                            0.1f*i,1, 0.1f*(i+1),1,  0.1f*(i+1),0
                                    },mv
                    );
        }
    }

    public void drawSelf()
    {
        String scoreStr=mv.objectCount+"";
        for(int i=0;i<scoreStr.length();i++)
        {//将得分中的每个数字字符绘制
            char c=scoreStr.charAt(i);
            MatrixState2D.pushMatrix();//保护现场
            MatrixState2D.translate(i*ICON_WIDTH*0.7f, 0, 0);
            numbers[c-'0'].drawSelf();
            MatrixState2D.popMatrix();
        }
    }
}
