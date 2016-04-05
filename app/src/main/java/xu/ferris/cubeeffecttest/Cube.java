package xu.ferris.cubeeffecttest;
import static xu.ferris.cubeeffecttest.Constant.UNIT_SIZE;

//������
public class Cube 
{
	//���ڻ��Ƹ��������ɫ����
	PageRect cr;
	
	public Cube(MySurfaceView mv)
	{
		//�������ڻ��Ƹ��������ɫ����
		cr=new PageRect(mv);
	}
	
	public void drawSelf(int mGlSurfaceTextureId)
	{
		//�ܻ���˼�룺ͨ����һ����ɫ������ת��λ��������ÿ�����λ��
		//�����������ÿ����
		
		//�����ֳ�
		MatrixState.pushMatrix();
		
		//����ǰС��
		MatrixState.pushMatrix();
		MatrixState.translate(0, 0, UNIT_SIZE);
		cr.drawSelf(mGlSurfaceTextureId);
		MatrixState.popMatrix();
		
		//���ƺ�С��
		MatrixState.pushMatrix();		
		MatrixState.translate(0, 0, -UNIT_SIZE);
		MatrixState.rotate(180, 0, 1, 0);
		cr.drawSelf(mGlSurfaceTextureId);
		MatrixState.popMatrix();
		
		//�����ϴ���
		MatrixState.pushMatrix();	
		MatrixState.translate(0,UNIT_SIZE,0);
		MatrixState.rotate(-90, 1, 0, 0);
		cr.drawSelf(mGlSurfaceTextureId);
		MatrixState.popMatrix();
		
		//�����´���
		MatrixState.pushMatrix();	
		MatrixState.translate(0,-UNIT_SIZE,0);
		MatrixState.rotate(90, 1, 0, 0);
		cr.drawSelf(mGlSurfaceTextureId);
		MatrixState.popMatrix();
		
		//�����Ҵ���
		MatrixState.pushMatrix();	
		MatrixState.translate(UNIT_SIZE,0,0);
		MatrixState.rotate(90, 0, 1, 0);
		cr.drawSelf(mGlSurfaceTextureId);
		MatrixState.popMatrix();
		
		//���������
		MatrixState.pushMatrix();				
		MatrixState.translate(-UNIT_SIZE,0,0);
		MatrixState.rotate(270, 0, 1, 0);
		cr.drawSelf(mGlSurfaceTextureId);
		MatrixState.popMatrix();
		
		//�ָ��ֳ�
		MatrixState.popMatrix();
	}
	

}
