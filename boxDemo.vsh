#version 330
/**
 * This vertex shader includes a hard-coded very basic lighting model.
 * There is one light predefined in the object coordinate system.
 *     
 * The code includes an ambient component (kd) of 0.3 and a diffuse 
 * component of 0.7. 
 */

uniform mat4 projXview;    // this is projection * viewing matrix * scene
uniform mat4 uModel;     
uniform vec4 uColor;
uniform float unif_differentcolor;

in vec3 vPosition;
in vec3 vNormal;
in vec3 vColor;
out vec4 color;      // since only 1 out var, it will be at 0

//---------- local variables --------------
//   In a complete system these would be uniform variables associated
//   with this object.
float ka = 0.35f;
float kd = 0.9f;

vec3 lightedColor( vec3 objColor, vec3 vertexNorm )
{
	vec3 lightDir = normalize( vec3( 2, 3, 4 )); // In obj coord space to light 3 faces
	vec3 lightColor = normalize( vec3( 1, 0, 1));
    vec3 vNorm = vec3( normalize( vertexNorm ));    
    vec3 color = ka * objColor + kd * objColor * lightColor * dot( lightDir, vNorm );
    return color;
}

void main()
{
	//vec3 color3 = vec3( uColor.r, uColor.g, uColor.b );
	vec4 vPos4 = vec4( vPosition, 1 );
	gl_Position = projXview * uModel * vPos4;
	// color = vec4( lightedColor( color3, vNormal ), 1 );
    
    if(unif_differentcolor == 1)
    {
        vec3 color3 = vec3( uColor.r, uColor.g, uColor.b );
        color = vec4( lightedColor( color3, vNormal ), 1 );
        
        
    }
    else
    {
        vec3 color3 = vec3( vColor.r, vColor.g, vColor.b );
        color = vec4( lightedColor( color3, vNormal ), 1 );
        
    }
        
}
