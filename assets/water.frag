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

// An expensive way to create a water shader!

uniform sampler3D myTexture;
varying vec3 vN;
varying vec3 vL;
varying vec3 vP;
varying vec3 vOP;

uniform float animation;

#define cloudsize 0.04
#define fm 2.0

float noise(vec3 NV)
{
    return float(2.0 * texture3D(myTexture,NV) - 1.0);
}

#define shininess 20.0

vec4 caustic(vec3 wind, float nf, float a, float b, float c, float s, vec3 BG, vec3 CC)
{
    float n = noise(nf*(vOP+animation*wind));
    if (n>0.5) n=2.0*(1.0-n); else n = 2.0*n;
    n = 0.3+ 0.8*n;
    vec3 outcolor = abs(n+0.4)*BG;
    if (n>c)
    {
     n = (n-c)/(1.5-c); n = n*(1.0+s);
     if (n>1.0) n=1.0;
     outcolor = n*CC + (1.0-n)*BG;
    }
    return vec4(outcolor,1.0);
}


void main (void)
{
    //vec3 E = normalize(-vP);
    //vec3 R = normalize(2.0 * dot(vN,vL) * vN - vL);

    //float diffuse = max(dot(vN,vL),0.0);
    //float spec = pow(max(dot(R,E),0.0), shininess);

    vec4 o1 = caustic(vec3(1.0,1.0,1.0), 0.05, 0.3, 0.8, 0.3, 2.5, vec3(0.0,0.0,0.6), vec3(0.0,0.3,0.5));
    vec4 o2 = caustic(vec3(0.8,0.1,0.1), 0.02, 0.4, 0.9, 0.5, 1.5, vec3(0.2,0.0,0.6), vec3(0.4,0.2,0.6));
    vec4 o3 = caustic(vec3(0.1,0.8,0.1), 0.08, 0.2, 0.6, 0.5, 2.0, vec3(0.0,0.6,0.4), vec3(0.4,0.0,0.6));

    
    gl_FragColor = 0.33*(o1+o2+o3);
     
}
