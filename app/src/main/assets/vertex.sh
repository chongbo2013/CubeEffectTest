uniform mat4 uMVPMatrix; //总变换矩阵
uniform mat4 uMMatrix; //变换矩阵
attribute vec3 aPosition;  //顶点位置
varying vec3 vPosition;//用于传递给片元着色器的顶点位置

attribute vec2 a_TexCoordinate; //纹理位置
varying vec2 v_TexCoordinate;//用于传递给片元着色器的纹理顶点位置
void main()  {                            		
   gl_Position = uMVPMatrix * vec4(aPosition,1); //根据总变换矩阵计算此次绘制此顶点位置
   vPosition=(uMMatrix * vec4(aPosition,1)).xyz;
   v_TexCoordinate = a_TexCoordinate;
}                      