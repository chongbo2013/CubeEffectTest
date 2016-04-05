package xu.ferris.cubeeffecttest;

import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import  static xu.ferris.cubeeffecttest.Constant.UNIT_SIZE;
import  static xu.ferris.cubeeffecttest.Constant.UNIT_SIZE_ZERO;
import  static xu.ferris.cubeeffecttest.Constant.UNIT_SIZE_TAB;
//颜色矩形
public class PageRect {
    int mProgram;//自定义渲染管线着色器程序id
    int muMVPMatrixHandle;//总变换矩阵引用
    int muMMatrixHandle;//位置、旋转变换矩阵引用
    int maPositionHandle; //顶点位置属性引用 

    private int mTextureUniformHandle;
    private int mTextureUniformLightHandle;
    private int mTextureCoordinateHandle;

    String mVertexShader;//顶点着色器代码脚本  	 
    String mFragmentShader;//片元着色器代码脚本

    FloatBuffer mVertexBuffer;//顶点坐标数据缓冲

    FloatBuffer mCubeTextureCoordinates;//顶点着色数据缓冲
    int vCount = 0;

    int muRHandle;
    int muAlpha;

    public PageRect(MySurfaceView mv) {
        //初始化顶点坐标与着色数据
        initVertexData();
        //初始化shader
        initShader(mv);

        initTexture(mv);
    }
    private int textures[]=new int[2];
    private void initTexture(MySurfaceView mv) {
        textures[0]=TextureHelper.loadTexture(mv.getContext(),R.drawable.gl_crystal_font);

        textures[1]=TextureHelper.loadTexture(mv.getContext(),R.drawable.gl_crystal_font);
    }

    //初始化顶点坐标与着色数据的方法
    public void initVertexData() {
        //顶点坐标数据的初始化================begin============================
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
        vbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mVertexBuffer = vbb.asFloatBuffer();//转换为Float型缓冲
        mVertexBuffer.put(vertices);//向缓冲区中放入顶点坐标数据
        mVertexBuffer.position(0);//设置缓冲区起始位置


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
        vtt.order(ByteOrder.nativeOrder());//设置字节顺序
        mCubeTextureCoordinates = vtt.asFloatBuffer();//转换为Float型缓冲
        mCubeTextureCoordinates.put(cubeTextureCoordinateData);//向缓冲区中放入顶点着色数据
        mCubeTextureCoordinates.position(0);//设置缓冲区起始位置
    }

    //初始化shader
    public void initShader(MySurfaceView mv) {
        //加载顶点着色器的脚本内容
        mVertexShader = ShaderUtil.loadFromAssetsFile("vertex.sh", mv.getResources());
        //加载片元着色器的脚本内容
        mFragmentShader = ShaderUtil.loadFromAssetsFile("frag.sh", mv.getResources());
        //基于顶点着色器与片元着色器创建程序
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //获取程序中顶点位置属性引用id  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //获取程序中总变换矩阵引用id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        //获取位置、旋转变换矩阵引用id
        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");

        mTextureUniformHandle = GLES20.glGetUniformLocation(mProgram, "u_Texture");
        mTextureUniformLightHandle = GLES20.glGetUniformLocation(mProgram, "u_TextureLight");


        mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgram, "a_TexCoordinate");

        muRHandle = GLES20.glGetUniformLocation(mProgram, "isNormal");

        muAlpha= GLES20.glGetUniformLocation(mProgram, "uAlpha");
    }

    /**
     * 绘制offset 0状态
     * @param mGlSurfaceTextureId
     */
    public void drawSelfNoval(int mGlSurfaceTextureId) {
        //制定使用某套shader程序
        GLES20.glUseProgram(mProgram);
        //将最终变换矩阵传入shader程序
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        //将位置、旋转变换矩阵传入shader程序
        GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);


        mVertexBuffer.position(0);
        //为画笔指定顶点位置数据
        GLES20.glVertexAttribPointer( maPositionHandle,3,GLES20.GL_FLOAT,false,
                3 * 4,mVertexBuffer);

        mCubeTextureCoordinates.position(0);
        //绑定纹理位置
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, 2, GLES20.GL_FLOAT, false,
                2*4, mCubeTextureCoordinates);

        //允许顶点位置数据数组
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);


        GLES20.glUniform1i(muRHandle, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,  mGlSurfaceTextureId);
        GLES20.glUniform1i(mTextureUniformHandle, 0);
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }


    //绘制offset 非0状态
    public void drawSelf(int mGlSurfaceTextureId) {
        //制定使用某套shader程序
        GLES20.glUseProgram(mProgram);
        //将最终变换矩阵传入shader程序
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        //将位置、旋转变换矩阵传入shader程序
        GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);


        mVertexBuffer.position(0);
        //为画笔指定顶点位置数据
        GLES20.glVertexAttribPointer( maPositionHandle,3,GLES20.GL_FLOAT,false,
                3 * 4,mVertexBuffer);

        mCubeTextureCoordinates.position(0);
        //绑定纹理位置
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, 2, GLES20.GL_FLOAT, false,
                2*4, mCubeTextureCoordinates);

        //允许顶点位置数据数组
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);



        //绘制前页
        GLES20.glUniform1i(muRHandle, 1);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,  mGlSurfaceTextureId);
        GLES20.glUniform1i(mTextureUniformHandle, 0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        GLES20.glUniform1i(mTextureUniformLightHandle, 1);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        //绘制右页面 tab
        GLES20.glUniform1i(muRHandle, 2);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[1]);
        GLES20.glUniform1i(mTextureUniformLightHandle, 2);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 4, 4);

        //绘制背面
        GLES20.glUniform1i(muRHandle, 2);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        GLES20.glUniform1i(mTextureUniformLightHandle, 1);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 8, 4);

        //绘制左边
        GLES20.glUniform1i(muRHandle, 2);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[1]);
        GLES20.glUniform1i(mTextureUniformLightHandle, 2);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 12, 4);


        //绘制顶面
        GLES20.glUniform1i(muRHandle, 2);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[1]);
        GLES20.glUniform1i(mTextureUniformLightHandle, 2);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 16, 4);


        //绘制底面
        GLES20.glUniform1i(muRHandle, 2);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[1]);
        GLES20.glUniform1i(mTextureUniformLightHandle, 2);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 20, 4);

    }
}
