package xu.ferris.cubeeffecttest;

import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import  static xu.ferris.cubeeffecttest.Constant.UNIT_SIZE;
import  static xu.ferris.cubeeffecttest.Constant.UNIT_SIZE_ZERO;
import  static xu.ferris.cubeeffecttest.Constant.UNIT_SIZE_TAB;
//��ɫ����
public class PageRect {
    int mProgram;//�Զ�����Ⱦ������ɫ������id
    int muMVPMatrixHandle;//�ܱ任��������
    int muMMatrixHandle;//λ�á���ת�任��������
    int maPositionHandle; //����λ���������� 

    private int mTextureUniformHandle;
    private int mTextureUniformLightHandle;
    private int mTextureCoordinateHandle;

    String mVertexShader;//������ɫ������ű�  	 
    String mFragmentShader;//ƬԪ��ɫ������ű�

    FloatBuffer mVertexBuffer;//�����������ݻ���

    FloatBuffer mCubeTextureCoordinates;//������ɫ���ݻ���
    int vCount = 0;

    int muRHandle;
    int muAlpha;

    public PageRect(MySurfaceView mv) {
        //��ʼ��������������ɫ����
        initVertexData();
        //��ʼ��shader
        initShader(mv);

        initTexture(mv);
    }
    private int textures[]=new int[2];
    private void initTexture(MySurfaceView mv) {
        textures[0]=TextureHelper.loadTexture(mv.getContext(),R.drawable.gl_crystal_font);

        textures[1]=TextureHelper.loadTexture(mv.getContext(),R.drawable.gl_crystal_font);
    }

    //��ʼ��������������ɫ���ݵķ���
    public void initVertexData() {
        //�����������ݵĳ�ʼ��================begin============================
        vCount = 4*6;
        float vertices[] = new float[]
                {

                        // Front face
                        -UNIT_SIZE, UNIT_SIZE, UNIT_SIZE_TAB,
                        -UNIT_SIZE, -UNIT_SIZE, UNIT_SIZE_TAB,
                        UNIT_SIZE, UNIT_SIZE, UNIT_SIZE_TAB,
                        UNIT_SIZE, -UNIT_SIZE, UNIT_SIZE_TAB,

                        // Right face
                        UNIT_SIZE, UNIT_SIZE, UNIT_SIZE_TAB,
                        UNIT_SIZE, -UNIT_SIZE, UNIT_SIZE_TAB,
                        UNIT_SIZE, UNIT_SIZE, -UNIT_SIZE_TAB,
                        UNIT_SIZE, -UNIT_SIZE, -UNIT_SIZE_TAB,

                        // Back face
                        UNIT_SIZE, UNIT_SIZE, -UNIT_SIZE_TAB,
                        UNIT_SIZE, -UNIT_SIZE, -UNIT_SIZE_TAB,
                        -UNIT_SIZE, UNIT_SIZE, -UNIT_SIZE_TAB,
                        -UNIT_SIZE, -UNIT_SIZE, -UNIT_SIZE_TAB,

                        // Left face
                        -UNIT_SIZE, UNIT_SIZE, -UNIT_SIZE_TAB,
                        -UNIT_SIZE, -UNIT_SIZE, -UNIT_SIZE_TAB,
                        -UNIT_SIZE, UNIT_SIZE, UNIT_SIZE_TAB,
                        -UNIT_SIZE, -UNIT_SIZE, UNIT_SIZE_TAB,

                        // Top face
                        -UNIT_SIZE, UNIT_SIZE, -UNIT_SIZE_TAB,
                        -UNIT_SIZE, UNIT_SIZE, UNIT_SIZE_TAB,
                        UNIT_SIZE, UNIT_SIZE, -UNIT_SIZE_TAB,
                        UNIT_SIZE, UNIT_SIZE, UNIT_SIZE_TAB,

                        // Bottom face
                        UNIT_SIZE, -UNIT_SIZE, -UNIT_SIZE_TAB,
                        UNIT_SIZE, -UNIT_SIZE, UNIT_SIZE_TAB,
                        -UNIT_SIZE, -UNIT_SIZE, -UNIT_SIZE_TAB,
                        -UNIT_SIZE, -UNIT_SIZE, UNIT_SIZE_TAB,
                };


        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mVertexBuffer = vbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mVertexBuffer.put(vertices);//�򻺳����з��붥����������
        mVertexBuffer.position(0);//���û�������ʼλ��


         float[] cubeTextureCoordinateData =
                {
                        UNIT_SIZE_ZERO, UNIT_SIZE_ZERO,
                        UNIT_SIZE_ZERO, UNIT_SIZE,
                        UNIT_SIZE, UNIT_SIZE_ZERO,
                        UNIT_SIZE, UNIT_SIZE,

                        UNIT_SIZE_ZERO, UNIT_SIZE_ZERO,
                        UNIT_SIZE_ZERO, UNIT_SIZE,
                        UNIT_SIZE, UNIT_SIZE_ZERO,
                        UNIT_SIZE, UNIT_SIZE,

                        UNIT_SIZE_ZERO, UNIT_SIZE_ZERO,
                        UNIT_SIZE_ZERO, UNIT_SIZE,
                        UNIT_SIZE, UNIT_SIZE_ZERO,
                        UNIT_SIZE, UNIT_SIZE,

                        UNIT_SIZE_ZERO, UNIT_SIZE_ZERO,
                        UNIT_SIZE_ZERO, UNIT_SIZE,
                        UNIT_SIZE, UNIT_SIZE_ZERO,
                        UNIT_SIZE, UNIT_SIZE,

                        UNIT_SIZE_ZERO, UNIT_SIZE_ZERO,
                        UNIT_SIZE_ZERO, UNIT_SIZE,
                        UNIT_SIZE, UNIT_SIZE_ZERO,
                        UNIT_SIZE, UNIT_SIZE,

                        UNIT_SIZE_ZERO, UNIT_SIZE_ZERO,
                        UNIT_SIZE_ZERO, UNIT_SIZE,
                        UNIT_SIZE, UNIT_SIZE_ZERO,
                        UNIT_SIZE, UNIT_SIZE

                };

        ByteBuffer vtt = ByteBuffer.allocateDirect(cubeTextureCoordinateData.length * 4);
        vtt.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mCubeTextureCoordinates = vtt.asFloatBuffer();//ת��ΪFloat�ͻ���
        mCubeTextureCoordinates.put(cubeTextureCoordinateData);//�򻺳����з��붥����ɫ����
        mCubeTextureCoordinates.position(0);//���û�������ʼλ��
    }

    //��ʼ��shader
    public void initShader(MySurfaceView mv) {
        //���ض�����ɫ���Ľű�����
        mVertexShader = ShaderUtil.loadFromAssetsFile("vertex.sh", mv.getResources());
        //����ƬԪ��ɫ���Ľű�����
        mFragmentShader = ShaderUtil.loadFromAssetsFile("frag.sh", mv.getResources());
        //���ڶ�����ɫ����ƬԪ��ɫ����������
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //��ȡ�����ж���λ����������id  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //��ȡ�������ܱ任��������id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        //��ȡλ�á���ת�任��������id
        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");

        mTextureUniformHandle = GLES20.glGetUniformLocation(mProgram, "u_Texture");
        mTextureUniformLightHandle = GLES20.glGetUniformLocation(mProgram, "u_TextureLight");


        mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgram, "a_TexCoordinate");

        muRHandle = GLES20.glGetUniformLocation(mProgram, "isNormal");

        muAlpha= GLES20.glGetUniformLocation(mProgram, "uAlpha");
    }

    /**
     * ����offset 0״̬
     * @param mGlSurfaceTextureId
     */
    public void drawSelfNoval(int mGlSurfaceTextureId) {
        //�ƶ�ʹ��ĳ��shader����
        GLES20.glUseProgram(mProgram);
        //�����ձ任������shader����
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        //��λ�á���ת�任������shader����
        GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);


        mVertexBuffer.position(0);
        //Ϊ����ָ������λ������
        GLES20.glVertexAttribPointer( maPositionHandle,3,GLES20.GL_FLOAT,false,
                3 * 4,mVertexBuffer);

        mCubeTextureCoordinates.position(0);
        //������λ��
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, 2, GLES20.GL_FLOAT, false,
                2*4, mCubeTextureCoordinates);

        //������λ����������
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);


        GLES20.glUniform1i(muRHandle, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,  mGlSurfaceTextureId);
        GLES20.glUniform1i(mTextureUniformHandle, 0);
        //����������
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }


    //����offset ��0״̬
    public void drawSelf(int mGlSurfaceTextureId) {
        //�ƶ�ʹ��ĳ��shader����
        GLES20.glUseProgram(mProgram);
        //�����ձ任������shader����
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        //��λ�á���ת�任������shader����
        GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);


        mVertexBuffer.position(0);
        //Ϊ����ָ������λ������
        GLES20.glVertexAttribPointer( maPositionHandle,3,GLES20.GL_FLOAT,false,
                3 * 4,mVertexBuffer);

        mCubeTextureCoordinates.position(0);
        //������λ��
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, 2, GLES20.GL_FLOAT, false,
                2*4, mCubeTextureCoordinates);

        //������λ����������
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);



        //����ǰҳ
        GLES20.glUniform1i(muRHandle, 1);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,  mGlSurfaceTextureId);
        GLES20.glUniform1i(mTextureUniformHandle, 0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        GLES20.glUniform1i(mTextureUniformLightHandle, 1);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        //������ҳ�� tab
        GLES20.glUniform1i(muRHandle, 2);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[1]);
        GLES20.glUniform1i(mTextureUniformLightHandle, 2);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 4, 4);

        //���Ʊ���
        GLES20.glUniform1i(muRHandle, 2);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        GLES20.glUniform1i(mTextureUniformLightHandle, 1);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 8, 4);

        //�������
        GLES20.glUniform1i(muRHandle, 2);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[1]);
        GLES20.glUniform1i(mTextureUniformLightHandle, 2);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 12, 4);


        //���ƶ���
        GLES20.glUniform1i(muRHandle, 2);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[1]);
        GLES20.glUniform1i(mTextureUniformLightHandle, 2);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 16, 4);


        //���Ƶ���
        GLES20.glUniform1i(muRHandle, 2);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[1]);
        GLES20.glUniform1i(mTextureUniformLightHandle, 2);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 20, 4);

    }
}
