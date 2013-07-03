/********************************************************************************
Copyright (c) 2004-2005, Martin Christen. All rights reserved.
Contact the author at: christen@clockworkcoders.com

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
Redistributions of source code must retain the above copyright notice, this
list of conditions and the following disclaimer. Redistributions in binary form
must reproduce the above copyright notice, this list of conditions and the
following disclaimer in the documentation and/or other materials provided with
the distribution. Neither the name of CWC nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
********************************************************************************/


uniform sampler3D myTexture;
varying vec3 vN;
varying vec3 vL;
varying vec3 vP;
varying vec3 vOP;

uniform float animation;

#define cloudsize 0.15
#define fm 2.5

float noise(vec3 NV)
{
    return float(2.0 * texture3D(myTexture,NV) - 1.0);
}

float cloud(vec3 Q)
{
    float value = 0.0;
    float f =cloudsize;
    float i;

    for (i=0.0; i<4.0; i+=1.0)
    {
        value += (noise(vec3(0.25 * Q * f ))) / f ;
        f = f*fm; 
    }
    return value;
}

#define c_diffuse vec4(0.0,0.0,1.0,1.0)
#define c_specular vec4(0.0,0.0,0.0,1.0) 
#define color1 vec4(1.0,1.0,1.0,1.0)

#define shininess 10.0

void main (void)
{
    vec3 E = normalize(-vP);
    vec3 R = normalize(2.0 * dot(vN,vL) * vN - vL);

    float diffuse = max(dot(vN,vL),0.0);
    float spec = pow(max(dot(R,E),0.0), shininess);

    // Sky Shader:
    float c = cloud(vOP+animation);
    c = clamp(c,0.0,1.0);
    gl_FragColor = c*color1 + diffuse * c_diffuse + spec * c_specular; 
    
}
