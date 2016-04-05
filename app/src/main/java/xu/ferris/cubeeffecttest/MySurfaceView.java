package xu.ferris.cubeeffecttest;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static xu.ferris.cubeeffecttest.Constant.ratio;

class MySurfaceView extends GLSurfaceView 
{
	private final float TOUCH_SCALE_FACTOR = 180.0f/320;//�Ƕ����ű���
    private SceneRenderer mRenderer;//������Ⱦ��
	
    private float mPreviousX;//�ϴεĴ���λ��X����
    
    float yAngle=0;//�ܳ�����y����ת�ĽǶ�

    public MySurfaceView(Context context) {
        super(context);
        init();
    }


    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init(){
        this.setEGLContextClientVersion(2); //����ʹ��OPENGL ES2.0
        mRenderer = new SceneRenderer();	//����������Ⱦ��
        setRenderer(mRenderer);				//������Ⱦ��
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//������ȾģʽΪ������Ⱦ
    }


    //�����¼��ص�����
    @Override 
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        switch (e.getAction()) {
        case MotionEvent.ACTION_MOVE:
            float dx = x - mPreviousX;//���㴥�ر�Xλ��
            yAngle += dx * TOUCH_SCALE_FACTOR;//���������ζ���y����ת�Ƕ�
        }
        mPreviousX=x;
        return true;
    }

    public ViewToGLRenderer getRenderer() {
        return mRenderer;
    }

    private class SceneRenderer extends ViewToGLRenderer
    {   
    	PageRect cube;//������
    	
        public void onDrawFrame(GL10 gl) 
        { super.onDrawFrame(gl);
        	//�����Ȼ�������ɫ����
            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            //�����ֳ�
            MatrixState.pushMatrix();
            //��Y����ת
            MatrixState.rotate(yAngle, 0, 1, 0);
            cube.drawSelf(getGLSurfaceTexture());
            //�ָ��ֳ�
            MatrixState.popMatrix();
        }  

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            super.onSurfaceChanged(gl,width,height);
            //�����Ӵ���С��λ�� 
        	GLES20.glViewport(0, 0, width, height); 
        	//����GLSurfaceView�Ŀ�߱�

            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 20, 100);
            //���ô˷������������9����λ�þ���
            MatrixState.setCamera(0,0,21,0f,0f,0f,0f,1.0f,0.0f);

            //��ʼ���任����
            MatrixState.setInitStack();
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            super.onSurfaceCreated(gl,config);
            //������Ļ����ɫRGBA
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            //�������������
            cube=new PageRect(MySurfaceView.this);
            //����ȼ��
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            //�򿪱������   
            GLES20.glEnable(GLES20.GL_CULL_FACE);

            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);
            GLES20.glDisable(GLES20.GL_DITHER);
        }
    }
}
