uniform mat4 uMVPMatrix; //�ܱ任����
uniform mat4 uMMatrix; //�任����
attribute vec3 aPosition;  //����λ��
varying vec3 vPosition;//���ڴ��ݸ�ƬԪ��ɫ���Ķ���λ��

attribute vec2 a_TexCoordinate; //����λ��
varying vec2 v_TexCoordinate;//���ڴ��ݸ�ƬԪ��ɫ����������λ��
void main()  {                            		
   gl_Position = uMVPMatrix * vec4(aPosition,1); //�����ܱ任�������˴λ��ƴ˶���λ��
   vPosition=(uMMatrix * vec4(aPosition,1)).xyz;
   v_TexCoordinate = a_TexCoordinate;
}                      