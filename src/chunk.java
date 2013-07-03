//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

//import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
//import org.lwjgl.util.vector.Vector2f;
//import org.lwjgl.util.vector.Vector3f;



public class chunk {
	private int chunkDisplayList;
	private int itemsDisplayList;
	private int treeDisplayList;
	private float rotation = 0f;
	private Casilla[][] casilla;
	private List<Integer> List_models_public = new ArrayList<Integer>();
	private int tex;
	private float x;
	private float y;
	private List<Integer> require_generate_displaylist = new ArrayList<Integer>();
	private HashSet<Integer> remove_duplicates = new HashSet<Integer>();
	public chunk(int x, int y, int tex,List<Integer> List_models_public )
	{
		this.x = x * 2.56f;
		this.y = y * 2.56f;
		
		this.tex = tex;
		this.List_models_public = List_models_public;
		System.out.println("new chunk-> x:"+ x +" y: "+y);
		// true = map; false = items;
		this.require_generate_displaylist.add(0);
		this.require_generate_displaylist.add(1);
		this.require_generate_displaylist.add(2);

		casilla = new Casilla[16][16];
        int tileID = 1;
        boolean value = true;
        
        for (int x1=0;x1<16;x1++)
        {
            for (int y1=0;y1<16;y1++)
            {
                
               
                
                casilla[x1][y1] = new Casilla(tileID, 0.0f,0.0f,0.0f,0.0f);
                
                if (value == true) {
                	casilla[x1][y1].set_blocked(false);
                	
                }
                else
                {
                	//casilla[x][y][z].add_Objects(4);
                }
            }
        }
        add_Tree(10,10,1);
        add_Objects(11,10,1);
        add_Objects(11,9,1);
        add_Objects(9,11,1);
        add_Objects(10,11,1);
        add_Objects(11,11,1);
        add_Objects(11,11,2);
        add_Objects(11,11,3);
        add_Objects(11,11,4);
        add_Objects(11,11,5);
        add_Objects(11,11,6);
    }
	private void Create_tile(float bx,float by, int id_title, float[] vertices)
    {
            // R,G,B,A Set The Color To Blue One Time Only

            //GL11.glTranslatef(bx, by, bz);
            //GL11.glRotatef(br, 0f, 0f, 1f);
            //GL11.glTranslatef(-bx, -by, 0);
            // draw quad
            //GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);
			GL11.glEnable(GL11.GL_BLEND);
	        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	     	GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
	     	GL11.glColor4ub((byte)255, (byte)255, (byte)255, (byte)255); // You can fade something out in its entirety by altering alpha here too
	     	GL11.glColor3f(1f, 1f, 1f);
     	
    	    int[] texture_info_temp = {id_title, 0};
            float textureXOffset = (texture_info_temp[0]/8f);
            float textureYOffset = (texture_info_temp[1]/8f);
            float textureHeight  = 0.124f;
            float textureWidth   = 0.124f;

            //System.out.println("id: "+id_title);
            //System.out.println("X: "+(texture_info_temp[0]/10f)+" Y: "+(texture_info_temp[1]/10f));
                
            GL11.glBegin(GL11.GL_QUADS);
                GL11.glTexCoord2f(textureXOffset, textureYOffset);
                GL11.glVertex3f(bx, by, vertices[0]);
                
                GL11.glTexCoord2f(textureXOffset + textureWidth, textureYOffset);
                GL11.glVertex3f(bx + 0.16f, by, vertices[1]);
                
                GL11.glTexCoord2f(textureXOffset + textureWidth, textureYOffset + textureHeight);
                GL11.glVertex3f(bx + 0.16f, by + 0.16f, vertices[2]);

                GL11.glTexCoord2f(textureXOffset, textureYOffset + textureHeight);
                GL11.glVertex3f(bx,by + 0.16f, vertices[3]);
            GL11.glEnd();
            
            //GL11.glDisable(GL11.GL_TEXTURE_2D);
        
    }
	private void Create_object(float bx,float by, int id_title)
    {
            // R,G,B,A Set The Color To Blue One Time Only
            GL11.glTranslatef(bx+0.08f, by+0.08f, 0f);
            GL11.glRotatef(-rotation, 0f, 0f, 1f);
            GL11.glRotatef(45f, 1f, 0f, 0f);
            
            // draw quad
            //GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);
    	    int[] texture_info_temp = {id_title, 0};
            float textureXOffset = (texture_info_temp[0]/8f);
            float textureYOffset = (texture_info_temp[1]/8f);
            float textureHeight  = 0.125f;
            float textureWidth   = 0.125f;

            //System.out.println("id: "+id_title);
            //System.out.println("X: "+(texture_info_temp[0]/10f)+" Y: "+(texture_info_temp[1]/10f));

            GL11.glBegin(GL11.GL_QUADS);
            	
	            GL11.glTexCoord2f(textureXOffset, textureYOffset);
	            GL11.glVertex3f(-0.08f, 0.0f, 0.16f);
	            
	            GL11.glTexCoord2f(textureXOffset + textureWidth, textureYOffset);
	            GL11.glVertex3f(0.08f, 0.0f, 0.16f);
	            
	            GL11.glTexCoord2f(textureXOffset + textureWidth, textureYOffset + textureHeight);
	            GL11.glVertex3f(0.08f, 0.0f, 0.0f);
	
	            GL11.glTexCoord2f(textureXOffset, textureYOffset + textureHeight);
	            GL11.glVertex3f(-0.08f,0.0f, 0.0f);
	            
	        GL11.glEnd();
            //GL11.glDisable(GL11.GL_TEXTURE_2D);
	        GL11.glRotatef(45f, -1f, 0f, 0f);
	        GL11.glRotatef(-rotation, 0f, 0f, -1f);
	        GL11.glTranslatef(-bx-0.08f, -by-0.08f, 0f);
	        
    }
	private void generate_DisplayList(int type_map)
	{
		switch(type_map)
		{
		 case 0:	
				chunkDisplayList = GL11.glGenLists(1);
				GL11.glNewList(chunkDisplayList, GL11.GL_COMPILE);
		        {
		        	calculate_map();
		        }
		        break;
		case 1:
				itemsDisplayList = GL11.glGenLists(4);
		        GL11.glNewList(itemsDisplayList, GL11.GL_COMPILE);
		        {
		        	calculate_items();
		        }
		        break;
		case 2:
				treeDisplayList = GL11.glGenLists(1);
		        GL11.glNewList(treeDisplayList, GL11.GL_COMPILE);
		        {
		        	calculate_tree();
		        }
		        break;
		}
		
		        
		GL11.glEndList();
	}
	private void calculate_map()
	{
         	
    	float positX= 0;
        float positY= 0;

        //System.out.println("init bla bla ");
   
        for (int v = 0; v < (16); v++)
        {
            for (int i = (int) 0; i < 16; i++)
            {
            	
                int tile_id_temp = casilla[i][v].get_Tiled();
                
                if (tile_id_temp != 0)
                {
	                if (i == 0 || v == 0 || i == 15 || v == 15)
	                {                	
	                	Create_tile(positX,positY,(tile_id_temp-1),casilla[i][v].get_vertices());

	                }
	                else
	                {
		                if (tile_id_temp != 0)
		                {
		                	Create_tile(positX,positY,(tile_id_temp-1),casilla[i][v].get_vertices());
		                }
	                }
                	
	                //
	                
                	
                }
                		            
                positX +=0.16f;
            }
            positX -= 16 * 0.16f;
            positY +=0.16f;
        }
    }

	private void calculate_items()
	{
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
     	GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
     	GL11.glColor4ub((byte)255, (byte)255, (byte)255, (byte)255); // You can fade something out in its entirety by altering alpha here too
    	
    	
    	float positX= 0;
        float positY= 0;
        
        //System.out.println("init bla bla ");

        for (int v = 0; v < (16); v++)
        {
            for (int i = (int) 0; i < 16; i++)
            {
            	
            	int object_id_temp = casilla[i][v].get_Objects_check();
            	//
            	if (object_id_temp != 0)
            	{
            		// al ser un array de posiciones, no hay que recorrerlo con un for, lo cual no se le puede
            		// pasar parametros, as� que lo posicionamos y rotamos antes de pintarlo, y volvemos a recolocar
            		// las posiciones para evitar que se descojone todo
            		
            		Create_object(positX,positY,4);
            		//GL11.glCallList(List_models_public.get(0));
                   
                    //Create_tile_object(positX,positY,4);
            	}
	            
                positX +=0.16f;
            }
            positX -= 16 * 0.16f;
            positY +=0.16f;
        }
	}
	private void calculate_tree()
	{
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);
		GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
     	GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
     	GL11.glColor4ub((byte)255, (byte)255, (byte)255, (byte)255); // You can fade something out in its entirety by altering alpha here too
    	
    	float positX= 0;
        float positY= 0;
        
        //System.out.println("init bla bla ");

        for (int v = 0; v < (16); v++)
        {
            for (int i = (int) 0; i < 16; i++)
            {
            	
            	int object_id_temp = casilla[i][v].get_Tree();
            	//
            	if (object_id_temp != 0)
            	{
            		// al ser un array de posiciones, no hay que recorrerlo con un for, lo cual no se le puede
            		// pasar parametros, as� que lo posicionamos y rotamos antes de pintarlo, y volvemos a recolocar
            		// las posiciones para evitar que se descojone todo
            		GL11.glTranslatef(positX+0.08f, positY+0.08f, 0.0f);
            		
            		GL11.glCallList(List_models_public.get(0));
                                       
                    GL11.glTranslatef(-(positX+0.08f), -(positY+0.08f), -( 0.0f));
                    //Create_tile_object(positX,positY,4);
            	}
            
	            
                positX +=0.16f;
            }
            positX -= 16 * 0.16f;
            positY +=0.16f;
        }

	}
	/*
	public void draw_chunk()
	{
		calculate_map();
	}
	*/
    public void draw_chunk()
    {
    	if (require_generate_displaylist.size() != 0)
    	{
    		System.out.println("size"+ require_generate_displaylist.size());
    	
	    	remove_duplicates.addAll(require_generate_displaylist);
	    	require_generate_displaylist.clear();
	    	Iterator<Integer> it = remove_duplicates.iterator();
	    	while (it.hasNext())
	    	{
	    		generate_DisplayList(it.next());
	    	}
	    	remove_duplicates.clear();
    	}
    	
    	 GL11.glPushMatrix();
         //azucar

         
         GL11.glTranslatef(x,y, 0);
         GL11.glCallList(treeDisplayList);
         GL11.glCallList(chunkDisplayList);
         GL11.glCallList(itemsDisplayList);
         
 	     //GL11.glTranslatef(-p_coordenate[0], -p_coordenate[1], p_coordenate[2]);
 	     
        GL11.glPopMatrix();
    }
    
    public List<Integer> get_Objects(int x,int y)
    {
    	return casilla[x][y].get_Objects();
    }
    public void displaylist_regenerate_items()
    {
    	this.require_generate_displaylist.add(1);
    }
	public void add_Objects(int x_map2, int y_map2, int item_id)
	{	
		System.out.println("add object-> x:"+ x +" y: "+y);
		casilla[x_map2][y_map2].add_Objects(item_id);
		this.require_generate_displaylist.add(1);
	}
	public void set_rotation(float rotation)
	{
		this.rotation = rotation;
		this.require_generate_displaylist.add(1);
	}
	public void add_Tree(int x_map2, int y_map2, int item_id)
	{
		System.out.println("add trees-> x:"+ x +" y: "+y);
		casilla[x_map2][y_map2].add_Tree(item_id);
	}
	public boolean get_blocked(int temp_x_map, int temp_y_map)
	{
		return casilla[temp_x_map][temp_y_map].get_blocked();
	}
	public void set_Tiled(int x_map, int y_map, int i)
	{
		casilla[x_map][y_map].set_Tiled(i);
		this.require_generate_displaylist.add(0);
	}
	//primero aplicar el blocked y luego el tiled
	public void set_blocked(int x_map, int y_map, boolean b)
	{
		casilla[x_map][y_map].set_blocked(b);
		//generate_DisplayList();
		
	}
	public void up(int x_map, int y_map) {
		// TODO Auto-generated method stub
		
		casilla[x_map-1][y_map-1].up(false,false,true,false);
		casilla[x_map][y_map-1].up(false,false,true,true);
		casilla[x_map+1][y_map-1].up(false,false,false,true);
		
		casilla[x_map-1][y_map].up(false,true,true,false);
		casilla[x_map][y_map].up(true,true,true,true);
		casilla[x_map+1][y_map].up(true,false,false,true);
		
		casilla[x_map-1][y_map+1].up(false,true,false,false);
		casilla[x_map][y_map+1].up(true,true,false,false);
		casilla[x_map+1][y_map+1].up(true,false,false,false);
		
	}
	public void down(int x_map, int y_map) {
		casilla[x_map-1][y_map-1].down(false,false,true,false);
		casilla[x_map][y_map-1].down(false,false,true,true);
		casilla[x_map+1][y_map-1].down(false,false,false,true);
		
		casilla[x_map-1][y_map].down(false,true,true,false);
		casilla[x_map][y_map].down(true,true,true,true);
		casilla[x_map+1][y_map].down(true,false,false,true);
		
		casilla[x_map-1][y_map+1].down(false,true,false,false);
		casilla[x_map][y_map+1].down(true,true,false,false);
		casilla[x_map+1][y_map+1].down(true,false,false,false);
	}
}