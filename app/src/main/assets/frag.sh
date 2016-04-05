#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES u_Texture;
uniform sampler2D u_TextureLight;
varying vec3 vPosition;
varying vec2 v_TexCoordinate;
uniform float uAlpha;
uniform int isNormal;
void main()
{
       if(isNormal == 0){//只绘制view
         gl_FragColor=texture2D(u_Texture,v_TexCoordinate);
       }if(isNormal == 1){//绘制view玻璃光
         vec4 baseColor;
         vec4 lightColor;
         baseColor=texture2D(u_Texture,v_TexCoordinate);
         lightColor=texture2D(u_TextureLight,v_TexCoordinate)*vec4(1,1,1,1);
         gl_FragColor= baseColor + lightColor;
       }else {//只绘制玻璃光
         gl_FragColor=texture2D(u_TextureLight,v_TexCoordinate);
       }
}