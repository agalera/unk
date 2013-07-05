import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
//import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import de.matthiasmann.twl.utils.PNGDecoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

//import java.util.Random;

import utility.*;

public class MainCode {
	//private static float[] lightPosition = {-12.19f, 1.36f, 1.45f, 110.1f};
	private List<Integer> List_models_public = new ArrayList<Integer>();
	private List<shots> shots_list = new ArrayList<shots>();
	private List<Player> enemy_list = new ArrayList<Player>();
	//private shots shots;
	private Player player;
	private chunk mapa_temporal[][] = new chunk[128][128];
	//private Map<Integer,Integer> mapa_temporal = new HashMap<Integer,Integer>();
    
    private Objeto[] items;
    //private String[][] map_info;
    float textureXOffset;
    float textureYOffset;
    float textureHeight;
    float textureWidth;
    float bullet_x;
    float bullet_y;
    /** angle of quad rotation */
    float rotation = 0;
    int acely = 0;
    int acelx = 0;
    int ResWidth = 1920;
    int ResHeight = 1080;
    /** time at last frame */
    long lastFrame;
    int x_map;
    int y_map;
    int chunk_x;
    int chunk_y;
    int chunk_x_old;
    int chunk_y_old;
    /** frames per second */
    int fps;
    /** last fps time */
    long lastFPS;
    /** is VSync Enabled */
    boolean vsync;
    private int bag_auv;
    private int tex;
    private int texture_player;
    private int texture_items;
    private int texture_fires;
    private int texture_tree;
    int id_sprite;
    int sprite_timming=0;
    private int bagDisplayList;
    int timefire = 0;
    int shaderProgram = 0;
    public void start()
    {
        try {
            Display.setDisplayMode(new DisplayMode(ResWidth, ResHeight));
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        
        tex = setupTextures("assets/stGrid1.png");
        bag_auv = setupTextures("src/bag_auv.png");
        texture_player = setupTextures("assets/player.png");
        texture_items = setupTextures("assets/items.png");
        texture_fires = setupTextures("assets/fires.png");
        texture_tree = setupTextures("assets/tree.png");
        initGL(); // init OpenGL
        getDelta(); // call once before loop to initialise lastFrame
        lastFPS = getTime(); // call before loop to initialise fps timer
        load_items();
        
        create_player();
        
        //bags
        //setUpDisplayLists("src/bag.obj", bag_auv, 1);
        //player
        //setUpDisplayLists("src/player.obj", texture_player, 1);
        setUpDisplayLists("src/tree.obj", texture_tree, 1);
        load_map_init(0,0);
        //setUpLighting();
        //Box box = new Box();
        while (!Display.isCloseRequested()) {
            int delta = getDelta();
            try
            {
            	update(delta);
            }
            catch(Exception e)
            {
            	
            }
            try
            {
            	calculate_shots(delta);
            }
            catch(Exception e)
            {
            	Iterator<shots> it=shots_list.iterator();
    	    	while(it.hasNext())
    	        {
    	          it.next();
    	          it.remove();
    	        }
            }
            
            renderGL();
            //box.draw();
            
            //System.out.println("id: "+this.tex);
            Display.sync(60); // cap fps to 60fps
            Display.update();
        }
		
        Display.destroy();
    }
    private void load_map_init(int x, int y)
    {
		System.out.println("acaba de iniciar el juego: "+ x);
	
	
    	if (x != 0)
    	{
    	    mapa_temporal[x-1][y] = new chunk(x-1,y,tex, List_models_public, rotation);
    	    mapa_temporal[x-1][1+y] = new chunk(x-1,1+y,tex, List_models_public, rotation);
    	}
    	if (x != 0 && y != 0)
    	{
    		mapa_temporal[x-1][y-1] = new chunk(x-1,y-1,tex, List_models_public, rotation);
    	}
    	if (y != 0)
    	{
	    	mapa_temporal[x][y-1] = new chunk(x,y-1,tex, List_models_public, rotation);
		    mapa_temporal[1+x][y-1] = new chunk(1+x,y-1,tex, List_models_public, rotation);
    	}

    	mapa_temporal[x][y] = new chunk(x,y,tex, List_models_public, rotation);
    	mapa_temporal[1+x][y] = new chunk(1+x,y,tex, List_models_public, rotation);


    	mapa_temporal[x][1+y] = new chunk(x,1+y,tex, List_models_public, rotation);
    	mapa_temporal[1+x][1+y] = new chunk(1+x,1+y,tex, List_models_public, rotation);
    }
    private void load_map(int x, int y)
    {
    	System.out.println("x: "+ x);
    	System.out.println("y: "+ y);
    	
		if (x != 0)
		{
		    if (mapa_temporal[x-1][y] == null)
		    {
		    	mapa_temporal[x-1][y] = new chunk(x-1,y,tex, List_models_public, rotation);
		    }
		    if (mapa_temporal[x-1][1+y] == null)
		    {
		    	mapa_temporal[x-1][1+y] = new chunk(x-1,1+y,tex, List_models_public, rotation);
		    }
		}
		if (x != 0 && y != 0)
		{
			if (mapa_temporal[x-1][y-1] == null)
			{
				mapa_temporal[x-1][y-1] = new chunk(x-1,y-1,tex, List_models_public, rotation);
			}
		}
		if (y != 0)
		{
	    	if (mapa_temporal[x][y-1] == null)
	    	{
	    		mapa_temporal[x][y-1] = new chunk(x,y-1,tex, List_models_public, rotation);
	    	}
		    if (mapa_temporal[1+x][y-1] == null)
		    {
		    	mapa_temporal[1+x][y-1] = new chunk(1+x,y-1,tex, List_models_public, rotation);
		    }
		}
	
		if (mapa_temporal[x][y] == null)
		{
			mapa_temporal[x][y] = new chunk(x,y,tex, List_models_public, rotation);
		}
		if (mapa_temporal[1+x][y] == null)
		{
			mapa_temporal[1+x][y] = new chunk(1+x,y,tex, List_models_public, rotation);
		}
	
	
		if (mapa_temporal[x][1+y] == null)
		{
			mapa_temporal[x][1+y] = new chunk(x,1+y,tex, List_models_public, rotation);
		}
		if (mapa_temporal[1+x][1+y] == null)
		{
			mapa_temporal[1+x][1+y] = new chunk(1+x,1+y,tex, List_models_public, rotation);
		}
    }
    private void unload_map(int x, int y)
    {
    	mapa_temporal[x][y] = null;
    }
    private void setUpDisplayLists(String MODEL_LOCATION,int texture_url, int genlist) {
        bagDisplayList = GL11.glGenLists(genlist);
        
		List_models_public.add(bagDisplayList);
        GL11.glNewList(bagDisplayList, GL11.GL_COMPILE);
        {
    		GL11.glEnable(GL11.GL_TEXTURE_2D);
    		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture_url);
    		GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
         	GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
         	GL11.glColor4ub((byte)255, (byte)255, (byte)255, (byte)255); // You can fade something out in its entirety by altering alpha here too
            GL11.glRotatef(90f, 1f, 0f, 0f);
            //azul
            Model m = null;
            try {
                m = OBJLoader.loadModel(new File(MODEL_LOCATION));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Display.destroy();
                System.exit(1);
            } catch (IOException e) {
                e.printStackTrace();
                Display.destroy();
                System.exit(1);
            }
            GL11.glColor3f(1f, 1f, 1f);
            
            GL11.glBegin(GL11.GL_TRIANGLES);
            for (Face face : m.faces) {
            	Vector2f vt1 = m.textures.get((int) face.texture.x - 1);
            	GL11.glTexCoord2f(vt1.x, vt1.y);
            	Vector3f n1 = m.normals.get((int) face.normal.x - 1);
            	GL11.glNormal3f(n1.x, n1.y, n1.z);
            	Vector3f v1 = m.vertices.get((int) face.vertex.x - 1);
            	GL11.glVertex3f(v1.x, v1.y, v1.z);
            	
            	Vector2f vt2 = m.textures.get((int) face.texture.y - 1);
            	GL11.glTexCoord2f(vt2.x, vt2.y);
            	Vector3f n2 = m.normals.get((int) face.normal.y - 1);
            	GL11.glNormal3f(n2.x, n2.y, n2.z);
            	Vector3f v2 = m.vertices.get((int) face.vertex.y - 1);
            	GL11.glVertex3f(v2.x, v2.y, v2.z);
            	
            	Vector2f vt3 = m.textures.get((int) face.texture.z - 1);
            	GL11.glTexCoord2f(vt3.x, vt3.y);
            	Vector3f n3 = m.normals.get((int) face.normal.z - 1);
            	GL11.glNormal3f(n3.x, n3.y, n3.z);
            	Vector3f v3 = m.vertices.get((int) face.vertex.z - 1);
            	GL11.glVertex3f(v3.x, v3.y, v3.z);
            }
            GL11.glEnd();
            GL11.glRotatef(-90f, 1f, 0f, 0f);
        }
        GL11.glEndList();
    }

    private void load_items()
    {
       
    	items = new Objeto[7];
    	
    	items[1] = new Objeto(1,10,12, 1);
    	items[2] = new Objeto(2,14,11, 2);
    	
    	items[3] = new Objeto(3,40);
    	items[4] = new Objeto(4,50);
    	
    	items[5] = new Objeto(5,8,9, 3);
    	items[6] = new Objeto(6,5,8, 4);
    	
    }
    private void create_player()
    {
        float[] p_coordenate = {1,1,0};
    	int hp = 100;
    	int mp = 100;
    	int ft = 100;
    	float speed = 0.0006f;
    	player = new Player(p_coordenate,hp,mp,ft,speed);
    }
    
	private int setupTextures(String filename) {
        IntBuffer tmp = BufferUtils.createIntBuffer(1);
        GL11.glGenTextures(tmp);
        tmp.rewind();
        try {
            InputStream in = new FileInputStream(filename);
            PNGDecoder decoder = new PNGDecoder(in);

            textureWidth = decoder.getWidth();
            textureHeight = decoder.getHeight();
            System.out.println("width=" + textureWidth );
            System.out.println("height=" + textureHeight );

            ByteBuffer buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
            buf.flip();
            

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, tmp.get(0));
            
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
                    GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
                    GL11.GL_LINEAR);
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 4);
            
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
            
            int unsigned = (buf.get(0) & 0xff);
            System.out.println(unsigned);
            System.out.println(buf.get(1));
            System.out.println(buf.get(2));
            System.out.println(buf.get(3));
            

        } catch (java.io.FileNotFoundException ex) {
            System.out.println("Error " + filename + " not found");
        } catch (java.io.IOException e) {
            System.out.println("Error decoding " + filename);
        }
        tmp.rewind();
        
        return tmp.get(0);
    }
	private static void setUpLighting() {
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_LIGHT0);

        //GL11.glEnable(GL11.GL_CULL_FACE);
        //GL11.glCullFace(GL11.GL_BACK);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE);
    }
	public void recalculate_rotation()
	{
		if (chunk_x != 0)
    	{
        	mapa_temporal[chunk_x-1][chunk_y].set_rotation(rotation);
	    	mapa_temporal[chunk_x-1][1+chunk_y].set_rotation(rotation);
    	}
        if (chunk_y != 0)
    	{
	    	mapa_temporal[chunk_x][chunk_y-1].set_rotation(rotation);
	    	mapa_temporal[1+chunk_x][chunk_y-1].set_rotation(rotation);
    	}
        if (chunk_x != 0 && chunk_y != 0)
    	{
	    	mapa_temporal[chunk_x-1][chunk_y-1].set_rotation(rotation);
    	}

        mapa_temporal[chunk_x][chunk_y].set_rotation(rotation);
        
    	mapa_temporal[1+chunk_x][chunk_y].set_rotation(rotation);
    	
    	mapa_temporal[chunk_x][1+chunk_y].set_rotation(rotation);
    	mapa_temporal[1+chunk_x][1+chunk_y].set_rotation(rotation);
	}
    public void update(int delta) {
        // rotate quad
	   
    	float[] p_coordenate = player.get_coordenate();
    	float speed = player.get_speed();
        float x_new = p_coordenate[0];
        float y_new = p_coordenate[1];
        
        
        float angle = (float) (Math.atan2(-((ResHeight/2)-Mouse.getY()), (ResWidth/2)-Mouse.getX()) * 57.295795f) + 90f;
        
        float actual_speed;
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
        {
        	if (player.use_ft())
        	{
        		actual_speed = speed + 0.0004f;
        	}
        	else
        	{
        		actual_speed = speed;
        	}
        }
        else
        {
        	actual_speed = speed;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A))
        {
            x_new -= actual_speed * delta;
            if (acelx < 40)
            {
                acelx += 1;
            }
            
            if (id_sprite < 3 || id_sprite > 4)
            {
            	id_sprite = 3;
            }
            
            if (sprite_timming == 16)
            {
                id_sprite ++;
                sprite_timming = 0;
            }
            else
            {
                sprite_timming ++;
            }
            
            if (id_sprite == 5)
            {
                id_sprite = 3;
            }
        }
        else if (Keyboard.isKeyDown(Keyboard.KEY_D))
        {
            x_new += actual_speed * delta;
            if (acelx > -40)
            {
                acelx -= 1;
            }
            
            if (id_sprite < 5 || id_sprite > 8)
            {
            	id_sprite = 6;
            }
            
            if (sprite_timming == 16)
            {
                id_sprite ++;
                sprite_timming = 0;
            }
            else
            {
                sprite_timming ++;
            }
            if (id_sprite == 8)
            {
                id_sprite = 6;
            }
        }
        else
        {
            if (acelx > 0 && acelx != 0)
            {
                acelx -= 1;
            }
            else if (acelx < 1 && acelx != 0)
            {
                acelx += 1;
            }

        }
        if (Keyboard.isKeyDown(Keyboard.KEY_Y))
        {
        	float [] temp_coordenate = {1,2,3};
        	enemy_list.add(new Player(temp_coordenate, 100, 1, 1, 0.001f));
        }
        /*if (Keyboard.isKeyDown(Keyboard.KEY_UP))
        {
        	shots_list.add(new shots(x_new, y_new, 0, 0));
        }
        else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
        {
    	   shots_list.add(new shots(x_new, y_new, 0, 1));
        }
        else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
        {
    	   shots_list.add(new shots(x_new, y_new, 0, 2));
        }
        else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
        {
    	   shots_list.add(new shots(x_new, y_new, 0, 3));
        }*/
        /*public double getAngleBetween(double x1, double y1, double x2, double y2){
    		return(Math.atan2(y1-y2, x1-x2));
    	}*/
        //System.out.println("timefire: "+timefire);
        if (timefire != 0)
        {
        	timefire --;
        }
        if (Mouse.isButtonDown(0))
        {
        	if (timefire == 0)
	        {
        		System.out.println("angle: "+ angle);
        		System.out.println("rotation: " + rotation );
        		System.out.println("rotation + angle: " + (rotation + angle));
        		System.out.println("rotation - angle: " + (rotation - angle));
        		timefire = 30;
        		shots_list.add(new shots(x_new, y_new, (angle-rotation)));
        		
	        }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_W))
        {
            y_new -= actual_speed * delta;
            if (acely < 40)
            {
                acely += 1;
            }
            id_sprite = 1;
        }
        else if (Keyboard.isKeyDown(Keyboard.KEY_S))
        {
            y_new += actual_speed * delta;
            if (acely > -40)
            {
                acely -= 1;
            }
            id_sprite = 0;
        }
        else
        {
            if (acely > 0 && acely != 0)
            {
                acely -= 1;
            }
            else if (acely < 1 && acely != 0)
            {
                acely += 1;
            }
        }
    
        
        chunk_x = (int) (x_new / 2.56f);
        chunk_y = (int) (y_new / 2.56f);


        x_map = (int) (x_new*6.25f) - (chunk_x*16);
        y_map = (int) (y_new*6.25f) - (chunk_y*16);
        //System.out.println("x_map" + x_map);
        /*
    	System.out.println("chunk_x: " + (chunk_x));

    	y_map = (int) (y_new*6.25f) - (chunk_y*16);
        
        /*int x_map_1 = (int) ((x_new*6.25f)-0.24f);
        int y_map_1 = (int) ((y_new*6.25f)-0.24f);

        int x_map_2 = (int) ((x_new*6.25f)+0.24f);
        int y_map_2 = (int) ((y_new*6.25f)+0.24f);*/
        boolean success = true;
        try {
	        /*if (!mapa_temporal[x_map_1/16][y_map_1/16].get_blocked(x_map_1-x_map_1/16,y_map_1-y_map_1/16))
	    	{
	    	    if (!mapa_temporal[x_map_2/16][y_map_2/16].get_blocked(x_map_2-x_map_2/16,y_map_2-y_map_2/16))
	    	    {
	    	        if (!mapa_temporal[x_map_1/16][y_map_2/16].get_blocked(x_map_1-x_map_1/16,y_map_2-y_map_2/16))
	    	        {
	    	            if (!mapa_temporal[x_map_2/16][y_map_1/16].get_blocked(x_map_2-x_map_2/16,y_map_1-y_map_1/16))
	    	            {
	    	                player.set_coordenate(x_new,y_new,0);
	    	            }
	    	        }
	    	    }
	    	}*/
        	if (!mapa_temporal[chunk_x][chunk_y].get_blocked(chunk_x,chunk_y))
	        {
        		player.set_coordenate(x_new,y_new,0);
	        }
        	if (Keyboard.isKeyDown(Keyboard.KEY_Q))
        	{
            	rotation = rotation + 1.00f;
            	//azur
            	recalculate_rotation();
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_E))
            {
            	rotation = rotation - 1.00f;
            	recalculate_rotation();
            }
        	
        } catch (Exception e) {
        	System.out.println("e:" + e);
            success = false;
            // other exception handling
        } finally {
            if (success) {
                // equivalent of Python else goes here

                // putting this in a finally-block is especially
                // important if there is a return in the try-block
            }
        }
        
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                if (Keyboard.getEventKey() == Keyboard.KEY_F) {
                    setDisplayMode(ResWidth, ResHeight, !Display.isFullscreen());
                }
                else if (Keyboard.getEventKey() == Keyboard.KEY_L) {
                    vsync = !vsync;
                    Display.setVSyncEnabled(vsync);
                }
                
                if (Keyboard.getEventKey() == Keyboard.KEY_R)
    	        {
                	if (x_map > 0 && x_map < 15 && y_map > 0 && y_map < 15 )
                	{
                		mapa_temporal[chunk_x][chunk_y].down_select(x_map-1,y_map-1,1);
                		mapa_temporal[chunk_x][chunk_y].down_select(x_map,y_map-1,2);
                		mapa_temporal[chunk_x][chunk_y].down_select(x_map+1,y_map-1,3);
                		mapa_temporal[chunk_x][chunk_y].down_select(x_map-1,y_map,4);
                		mapa_temporal[chunk_x][chunk_y].down_select(x_map,y_map,5);
                		mapa_temporal[chunk_x][chunk_y].down_select(x_map+1,y_map,6);
                		mapa_temporal[chunk_x][chunk_y].down_select(x_map-1,y_map+1,7);
                		mapa_temporal[chunk_x][chunk_y].down_select(x_map,y_map+1,8);
                		mapa_temporal[chunk_x][chunk_y].down_select(x_map+1,y_map+1,9);
                	}
                	else
                	{
                		if(x_map == 0)
	                	{
                			mapa_temporal[chunk_x-1][chunk_y].down_select(15,y_map,4);
                			mapa_temporal[chunk_x][chunk_y].down_select(x_map+1,y_map,6);
	                		mapa_temporal[chunk_x-1][chunk_y].change_down();
	                	}
                		else if(x_map == 15)
	                	{
	                		mapa_temporal[chunk_x][chunk_y].down_select(x_map-1,y_map,4);
	                		mapa_temporal[chunk_x+1][chunk_y].down_select(0,y_map,6);
	                		mapa_temporal[chunk_x+1][chunk_y].change_down();
	                	}
                		else
	                	{
	                		mapa_temporal[chunk_x][chunk_y].down_select(x_map-1,y_map,4);
	                		mapa_temporal[chunk_x][chunk_y].down_select(x_map+1,y_map,6);
	                	}
	                	if(y_map == 0)
	                	{
	                		mapa_temporal[chunk_x][chunk_y-1].down_select(x_map,15,2);
	                		mapa_temporal[chunk_x][chunk_y].down_select(x_map,y_map+1,8);
	                		mapa_temporal[chunk_x][chunk_y-1].change_down();
	                	}
	                	else if(y_map == 15)
	                	{
	                		mapa_temporal[chunk_x][chunk_y].down_select(x_map,y_map-1,2);
	                		mapa_temporal[chunk_x][chunk_y+1].down_select(x_map,0,8);
	                		mapa_temporal[chunk_x][chunk_y+1].change_down();
	                	}
	                	else
	                	{
	                		mapa_temporal[chunk_x][chunk_y].down_select(x_map,y_map-1,2);
	                		mapa_temporal[chunk_x][chunk_y].down_select(x_map,y_map+1,8);
	                	}
	                	if (x_map == 0 && y_map != 15 && y_map != 0)
	                	{
	                		mapa_temporal[chunk_x-1][chunk_y].down_select(15,y_map-1,1);
	                		mapa_temporal[chunk_x][chunk_y].down_select(x_map+1,y_map-1,3);
	                		
	                		mapa_temporal[chunk_x-1][chunk_y].down_select(15,y_map+1,7);
	                		mapa_temporal[chunk_x][chunk_y].down_select(x_map+1,y_map+1,9);
	                		
	                		mapa_temporal[chunk_x-1][chunk_y].change_down();
	                	}
	                	if (x_map == 15 && y_map != 15 && y_map != 0)
	                	{
	                		mapa_temporal[chunk_x][chunk_y].down_select(x_map-1,y_map-1,1);
	                		mapa_temporal[chunk_x+1][chunk_y].down_select(0,y_map-1,3);
	                		
	                		mapa_temporal[chunk_x][chunk_y].down_select(x_map-1,y_map+1,7);
	                		mapa_temporal[chunk_x+1][chunk_y].down_select(0,y_map+1,9);
	                		
	                		mapa_temporal[chunk_x+1][chunk_y].change_down();
	                	}
	                	if (y_map == 0 && x_map != 15 && x_map != 0)
	                	{
	                		mapa_temporal[chunk_x][chunk_y-1].down_select(x_map-1,15,1);
	                		mapa_temporal[chunk_x][chunk_y-1].down_select(x_map+1,15,3);
	                		
	                		mapa_temporal[chunk_x][chunk_y].down_select(x_map-1,y_map+1,7);
	                		mapa_temporal[chunk_x][chunk_y].down_select(x_map+1,y_map+1,9);
	                		
	                		mapa_temporal[chunk_x][chunk_y-1].change_down();
	                	}
	                	if (y_map == 15 && x_map != 15 && x_map != 0)
	                	{
	                		mapa_temporal[chunk_x][chunk_y].down_select(x_map-1,y_map-1,1);
	                		mapa_temporal[chunk_x][chunk_y].down_select(x_map+1,y_map-1,3);
	                		
	                		mapa_temporal[chunk_x][chunk_y+1].down_select(x_map-1,0,7);
	                		mapa_temporal[chunk_x][chunk_y+1].down_select(x_map+1,0,9);
	                		
	                		mapa_temporal[chunk_x][chunk_y+1].change_down();
	                	}
	                	if(x_map == 0 && y_map == 0)
	                	{
	                		mapa_temporal[chunk_x-1][chunk_y-1].down_select(15,15,1);
	                		mapa_temporal[chunk_x][chunk_y-1].down_select(x_map+1,15,3);
	                		
	                		mapa_temporal[chunk_x-1][chunk_y].down_select(15,y_map+1,7);
	                		mapa_temporal[chunk_x][chunk_y].down_select(x_map+1,y_map+1,9);
	                		
	                		mapa_temporal[chunk_x-1][chunk_y-1].change_down();
	                		mapa_temporal[chunk_x][chunk_y-1].change_down();
	                		mapa_temporal[chunk_x-1][chunk_y].change_down();
	                	}
	                	if(x_map == 15 && y_map == 15)
	                	{
	                		mapa_temporal[chunk_x][chunk_y].down_select(x_map-1,y_map-1,1);
	                		mapa_temporal[chunk_x+1][chunk_y].down_select(0,y_map-1,3);
	                		
	                		mapa_temporal[chunk_x][chunk_y+1].down_select(x_map-1,0,7);
	                		mapa_temporal[chunk_x+1][chunk_y+1].down_select(0,0,9);
	                		
	                		mapa_temporal[chunk_x+1][chunk_y].change_down();
	                		mapa_temporal[chunk_x+1][chunk_y+1].change_down();
	                		mapa_temporal[chunk_x][chunk_y+1].change_down();
	                	}
	                	if(x_map == 15 && y_map == 0)
	                	{
	                		mapa_temporal[chunk_x][chunk_y-1].down_select(x_map-1,15,1);
	                		mapa_temporal[chunk_x+1][chunk_y-1].down_select(0,15,3);
	                		
	                		mapa_temporal[chunk_x][chunk_y].down_select(x_map-1,y_map+1,7);
	                		mapa_temporal[chunk_x+1][chunk_y].down_select(0,y_map+1,9);
	                		
	                		mapa_temporal[chunk_x][chunk_y-1].change_down();
	                		mapa_temporal[chunk_x+1][chunk_y-1].change_down();
	                		mapa_temporal[chunk_x+1][chunk_y].change_down();
	                	}
	                	if(x_map == 0 && y_map == 15)
	                	{
	                		mapa_temporal[chunk_x-1][chunk_y].down_select(15,y_map-1,1);
	                		mapa_temporal[chunk_x][chunk_y].down_select(x_map+1,y_map-1,3);
	                		
	                		mapa_temporal[chunk_x-1][chunk_y+1].down_select(15,0,7);
	                		mapa_temporal[chunk_x][chunk_y+1].down_select(x_map+1,0,9);
	                		
	                		mapa_temporal[chunk_x-1][chunk_y+1].change_down();
	                		mapa_temporal[chunk_x-1][chunk_y].change_down();
	                		mapa_temporal[chunk_x][chunk_y+1].change_down();
	                	}
	                	mapa_temporal[chunk_x][chunk_y].down_select(x_map,y_map,5);
                	}
    	        	//mapa_temporal[chunk_x][chunk_y].down_select(x_map,y_map);
    	        	mapa_temporal[chunk_x][chunk_y].change_down();
    	        	mapa_temporal[chunk_x][chunk_y].set_Tiled(x_map,y_map,2);
    	        	
    	        }
    	        else if(Keyboard.getEventKey() == Keyboard.KEY_T)
    	        {
    	        	if (x_map > 0 && x_map < 15 && y_map > 0 && y_map < 15 )
                	{
                		mapa_temporal[chunk_x][chunk_y].up_select(x_map-1,y_map-1,1);
                		mapa_temporal[chunk_x][chunk_y].up_select(x_map,y_map-1,2);
                		mapa_temporal[chunk_x][chunk_y].up_select(x_map+1,y_map-1,3);
                		mapa_temporal[chunk_x][chunk_y].up_select(x_map-1,y_map,4);
                		mapa_temporal[chunk_x][chunk_y].up_select(x_map,y_map,5);
                		mapa_temporal[chunk_x][chunk_y].up_select(x_map+1,y_map,6);
                		mapa_temporal[chunk_x][chunk_y].up_select(x_map-1,y_map+1,7);
                		mapa_temporal[chunk_x][chunk_y].up_select(x_map,y_map+1,8);
                		mapa_temporal[chunk_x][chunk_y].up_select(x_map+1,y_map+1,9);
                	}
                	else
                	{
                		if(x_map == 0)
	                	{
                			mapa_temporal[chunk_x-1][chunk_y].up_select(15,y_map,4);
                			mapa_temporal[chunk_x][chunk_y].up_select(x_map+1,y_map,6);
	                		mapa_temporal[chunk_x-1][chunk_y].change_up();
	                	}
                		else if(x_map == 15)
	                	{
	                		mapa_temporal[chunk_x][chunk_y].up_select(x_map-1,y_map,4);
	                		mapa_temporal[chunk_x+1][chunk_y].up_select(0,y_map,6);
	                		mapa_temporal[chunk_x+1][chunk_y].change_up();
	                	}
                		else
	                	{
	                		mapa_temporal[chunk_x][chunk_y].up_select(x_map-1,y_map,4);
	                		mapa_temporal[chunk_x][chunk_y].up_select(x_map+1,y_map,6);
	                	}
	                	if(y_map == 0)
	                	{
	                		mapa_temporal[chunk_x][chunk_y-1].up_select(x_map,15,2);
	                		mapa_temporal[chunk_x][chunk_y].up_select(x_map,y_map+1,8);
	                		mapa_temporal[chunk_x][chunk_y-1].change_up();
	                	}
	                	else if(y_map == 15)
	                	{
	                		mapa_temporal[chunk_x][chunk_y].up_select(x_map,y_map-1,2);
	                		mapa_temporal[chunk_x][chunk_y+1].up_select(x_map,0,8);
	                		mapa_temporal[chunk_x][chunk_y+1].change_up();
	                	}
	                	else
	                	{
	                		mapa_temporal[chunk_x][chunk_y].up_select(x_map,y_map-1,2);
	                		mapa_temporal[chunk_x][chunk_y].up_select(x_map,y_map+1,8);
	                	}
	                	if (x_map == 0 && y_map != 15 && y_map != 0)
	                	{
	                		mapa_temporal[chunk_x-1][chunk_y].up_select(15,y_map-1,1);
	                		mapa_temporal[chunk_x][chunk_y].up_select(x_map+1,y_map-1,3);
	                		
	                		mapa_temporal[chunk_x-1][chunk_y].up_select(15,y_map+1,7);
	                		mapa_temporal[chunk_x][chunk_y].up_select(x_map+1,y_map+1,9);
	                		
	                		mapa_temporal[chunk_x-1][chunk_y].change_up();
	                	}
	                	if (x_map == 15 && y_map != 15 && y_map != 0)
	                	{
	                		mapa_temporal[chunk_x][chunk_y].up_select(x_map-1,y_map-1,1);
	                		mapa_temporal[chunk_x+1][chunk_y].up_select(0,y_map-1,3);
	                		
	                		mapa_temporal[chunk_x][chunk_y].up_select(x_map-1,y_map+1,7);
	                		mapa_temporal[chunk_x+1][chunk_y].up_select(0,y_map+1,9);
	                		
	                		mapa_temporal[chunk_x+1][chunk_y].change_up();
	                	}
	                	if (y_map == 0 && x_map != 15 && x_map != 0)
	                	{
	                		mapa_temporal[chunk_x][chunk_y-1].up_select(x_map-1,15,1);
	                		mapa_temporal[chunk_x][chunk_y-1].up_select(x_map+1,15,3);
	                		
	                		mapa_temporal[chunk_x][chunk_y].up_select(x_map-1,y_map+1,7);
	                		mapa_temporal[chunk_x][chunk_y].up_select(x_map+1,y_map+1,9);
	                		
	                		mapa_temporal[chunk_x][chunk_y-1].change_up();
	                	}
	                	if (y_map == 15 && x_map != 15 && x_map != 0)
	                	{
	                		mapa_temporal[chunk_x][chunk_y].up_select(x_map-1,y_map-1,1);
	                		mapa_temporal[chunk_x][chunk_y].up_select(x_map+1,y_map-1,3);
	                		
	                		mapa_temporal[chunk_x][chunk_y+1].up_select(x_map-1,0,7);
	                		mapa_temporal[chunk_x][chunk_y+1].up_select(x_map+1,0,9);
	                		
	                		mapa_temporal[chunk_x][chunk_y+1].change_up();
	                	}
	                	if(x_map == 0 && y_map == 0)
	                	{
	                		mapa_temporal[chunk_x-1][chunk_y-1].up_select(15,15,1);
	                		mapa_temporal[chunk_x][chunk_y-1].up_select(x_map+1,15,3);
	                		
	                		mapa_temporal[chunk_x-1][chunk_y].up_select(15,y_map+1,7);
	                		mapa_temporal[chunk_x][chunk_y].up_select(x_map+1,y_map+1,9);
	                		
	                		mapa_temporal[chunk_x-1][chunk_y-1].change_up();
	                		mapa_temporal[chunk_x][chunk_y-1].change_up();
	                		mapa_temporal[chunk_x-1][chunk_y].change_up();
	                	}
	                	if(x_map == 15 && y_map == 15)
	                	{
	                		mapa_temporal[chunk_x][chunk_y].up_select(x_map-1,y_map-1,1);
	                		mapa_temporal[chunk_x+1][chunk_y].up_select(0,y_map-1,3);
	                		
	                		mapa_temporal[chunk_x][chunk_y+1].up_select(x_map-1,0,7);
	                		mapa_temporal[chunk_x+1][chunk_y+1].up_select(0,0,9);
	                		
	                		mapa_temporal[chunk_x+1][chunk_y].change_up();
	                		mapa_temporal[chunk_x+1][chunk_y+1].change_up();
	                		mapa_temporal[chunk_x][chunk_y+1].change_up();
	                	}
	                	if(x_map == 15 && y_map == 0)
	                	{
	                		mapa_temporal[chunk_x][chunk_y-1].up_select(x_map-1,15,1);
	                		mapa_temporal[chunk_x+1][chunk_y-1].up_select(0,15,3);
	                		
	                		mapa_temporal[chunk_x][chunk_y].up_select(x_map-1,y_map+1,7);
	                		mapa_temporal[chunk_x+1][chunk_y].up_select(0,y_map+1,9);
	                		
	                		mapa_temporal[chunk_x][chunk_y-1].change_up();
	                		mapa_temporal[chunk_x+1][chunk_y-1].change_up();
	                		mapa_temporal[chunk_x+1][chunk_y].change_up();
	                	}
	                	if(x_map == 0 && y_map == 15)
	                	{
	                		mapa_temporal[chunk_x-1][chunk_y].up_select(15,y_map-1,1);
	                		mapa_temporal[chunk_x][chunk_y].up_select(x_map+1,y_map-1,3);
	                		
	                		mapa_temporal[chunk_x-1][chunk_y+1].up_select(15,0,7);
	                		mapa_temporal[chunk_x][chunk_y+1].up_select(x_map+1,0,9);
	                		
	                		mapa_temporal[chunk_x-1][chunk_y+1].change_up();
	                		mapa_temporal[chunk_x-1][chunk_y].change_up();
	                		mapa_temporal[chunk_x][chunk_y+1].change_up();
	                	}
	                	mapa_temporal[chunk_x][chunk_y].up_select(x_map,y_map,5);
                	}
    	        	//mapa_temporal[chunk_x][chunk_y].up_select(x_map,y_map);
    	        	mapa_temporal[chunk_x][chunk_y].change_up();
    	        	mapa_temporal[chunk_x][chunk_y].set_Tiled(x_map,y_map,1);
    	        	
    	        }
                
                if (Keyboard.getEventKey() == Keyboard.KEY_Z)
                {
                    pickObject(x_map,y_map,0);
                }
                else if (Keyboard.getEventKey() == Keyboard.KEY_F1)
                {
                    pickObject(x_map,y_map,0);
                }
                else if (Keyboard.getEventKey() == Keyboard.KEY_F2)
                {
                	pickObject(x_map,y_map,1);
                }
                else if (Keyboard.getEventKey() == Keyboard.KEY_F3)
                {
                	pickObject(x_map,y_map,2);
                }
                else if (Keyboard.getEventKey() == Keyboard.KEY_F4)
                {
                	pickObject(x_map,y_map,3);
                }
                else if (Keyboard.getEventKey() == Keyboard.KEY_F5)
                {
                	pickObject(x_map,y_map,4);
                }
                else if (Keyboard.getEventKey() == Keyboard.KEY_F6)
                {
                	pickObject(x_map,y_map,5);
                }
                else if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
                {
                	id_sprite = 4;
                	//int value = player.drop_next_item();
                	//if (value != 0)
                	//{
                	int item_id = 0;
                	if (Keyboard.getEventKey() == Keyboard.KEY_V)
                    {
                		item_id = player.drop_next_item();
                    }
                	else if (Keyboard.getEventKey() == Keyboard.KEY_1)
                    {
                		item_id = player.drop_item_slot(0);
                    }
                	else if (Keyboard.getEventKey() == Keyboard.KEY_2)
                	{
                		item_id = player.drop_item_slot(1);
                	}
                	else if (Keyboard.getEventKey() == Keyboard.KEY_3)
                	{
                		item_id = player.drop_item_slot(2);
                	}
                	else if (Keyboard.getEventKey() == Keyboard.KEY_4)
                	{
                		item_id = player.drop_item_slot(3);
                	}
                	else if (Keyboard.getEventKey() == Keyboard.KEY_5)
                	{
                		item_id = player.drop_item_slot(4);
                	}
                	else if (Keyboard.getEventKey() == Keyboard.KEY_6)
                	{
                		item_id = player.drop_item_slot(5);
                	}
                	if (item_id != 0)
            		{
            			dropObject(x_map,y_map,item_id);
            		}
                }
                else
                {
                	if (Keyboard.getEventKey() == Keyboard.KEY_1)
                    {
                        use_item(0);
                    }
                    else if (Keyboard.getEventKey() == Keyboard.KEY_2)
                    {
                        use_item(1);
                    }
                    else if (Keyboard.getEventKey() == Keyboard.KEY_3)
                    {
                        use_item(2);
                    }
                    else if (Keyboard.getEventKey() == Keyboard.KEY_4)
                    {
                        use_item(3);
                    }
                    else if (Keyboard.getEventKey() == Keyboard.KEY_5)
                    {
                        use_item(4);
                    }
                    else if (Keyboard.getEventKey() == Keyboard.KEY_6)
                    {
                        use_item(5);
                    }
                }
            }
            
        }

        // keep quad on the screen
        player.recovery_ft();
        
        updateFPS(); // update FPS Counter
    }
    public void calculate_shots(float delta)
    {
    	//object_temp = null;
    	boolean borrar_disparo = false;
    	if (shots_list != null)
    	{
    		
	    	Iterator<shots> it=shots_list.iterator();
	    	while(it.hasNext())
	        {
	          shots value=it.next();
	          if (true == value.move_shot(delta))
	          {
	        	  //System.out.println("borra");
	        	  it.remove();
	          }
	          else
	          {
	        	  int temp_x_chunk = (int) (value.get_x()/2.56f);
	              int temp_y_chunk = (int) (value.get_y()/2.56f);
	              
	        	  int temp_x_casilla = (int) ((value.get_x()*6.25f) - (temp_x_chunk*16));
	        	  int temp_y_casilla = (int) ((value.get_y()*6.25f) - (temp_y_chunk*16));
	              
	              if (mapa_temporal[temp_x_chunk][temp_y_chunk].get_blocked(temp_x_casilla,temp_y_casilla))
	              {
	            	  it.remove();
	              }
	              else
	              {
	            	  if (enemy_list != null)
	              	{
	              		
	          	    	Iterator<Player> it2=enemy_list.iterator();
	          	    	while(it2.hasNext())
	          	        {
	          	    		Player value2=it2.next();
	                    
	          	    		int result = value2.check_damage(value.get_x(),value.get_y(),1);
	                        if (result == 1)
	                        {
	                          borrar_disparo = true;
	                        }
	                        if (result == 2)
	                        {
	                          System.out.println("borra enemigo");
	                      	  it2.remove();
	                        }
	                  	  //System.out.println("sigue");
	          	          
	          	        }
	          	    	if (borrar_disparo)
	          	    	{
	          	    		it.remove();
	          	    		borrar_disparo = false;
	          	    	}
	              	}
	              }
	        	  //System.out.println("sigue");
	          }
	        }
    	}
    	
    }


	/**
     * Set the display mode to be used
     *
     * @param width The width of the display required
     * @param height The height of the display required
     * @param fullscreen True if we want fullscreen mode
     */
    public void use_item(int slot_player)
    {
    	if (player.get_inventary_item(slot_player) != 0)
    	{
    		int type = items[player.get_inventary_item(slot_player)].get_type();
    		switch(type)
    		{
            case 0:
            	player.set_armor(slot_player);
            	break;
            case 1:
            	player.set_weapon(slot_player);
            	break;
    		}
    		
    	}
    }
    
    public void setDisplayMode(int width, int height, boolean fullscreen) {

        // return if requested DisplayMode is already set
                if ((Display.getDisplayMode().getWidth() == width) &&
            (Display.getDisplayMode().getHeight() == height) &&
            (Display.isFullscreen() == fullscreen)) {
            return;
        }

        try {
            DisplayMode targetDisplayMode = null;

            if (fullscreen) {
                DisplayMode[] modes = Display.getAvailableDisplayModes();
                int freq = 0;

                for (int i=0;i<modes.length;i++) {
                    DisplayMode current = modes[i];

                    if ((current.getWidth() == width) && (current.getHeight() == height)) {
                        if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
                            if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
                                targetDisplayMode = current;
                                freq = targetDisplayMode.getFrequency();
                            }
                        }

                        // if we've found a match for bpp and frequence against the
                        // original display mode then it's probably best to go for this one
                        // since it's most likely compatible with the monitor
                        if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) &&
                            (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
                            targetDisplayMode = current;
                            break;
                        }
                    }
                }
            } else {
                targetDisplayMode = new DisplayMode(width,height);
            }

            if (targetDisplayMode == null) {
                System.out.println("Failed to find value mode: "+width+"x"+height+" fs="+fullscreen);
                return;
            }

            Display.setDisplayMode(targetDisplayMode);
            Display.setFullscreen(fullscreen);

        } catch (LWJGLException e) {
            System.out.println("Unable to setup mode "+width+"x"+height+" fullscreen="+fullscreen + e);
        }
    }

    /**
     * Calculate how many milliseconds have passed
     * since last frame.
     *
     * @return milliseconds passed since last frame
     */
    public int getDelta() {
        long time = getTime();
        int delta = (int) (time - lastFrame);
        lastFrame = time;

        return delta;
    }

    /**
     * Get the accurate system time
     *
     * @return The system time in milliseconds
     */
    public long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    /**
     * Calculate the FPS and set it in the title bar
     */
    public void updateFPS() {
        if (getTime() - lastFPS > 1000) {
            Display.setTitle("FPS: " + fps);
            fps = 0;
            lastFPS += 1000;
        }
        fps++;
    }
    public void initGL() {
        GL11.glDepthFunc(GL11.GL_LEQUAL);

        GL11.glViewport(0, 0, ResWidth, ResHeight);
        float invAspectRatio = ResHeight / (ResWidth*1f);
        GL11.glEnable(GL11.GL_DEPTH_TEST); //depth test enabled
        
        GL11.glAlphaFunc(GL11.GL_EQUAL, 1.0f);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        
        //GLU.gluPerspective (45.0f,invAspectRatio, 2f, -2f);
        GL11.glOrtho(-1, 1, -1*invAspectRatio, +1*invAspectRatio, 2, -2);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        shaderProgram = shaders.load_shader("shaders/shader.vs","shaders/shader.fs", "shaders/shader.gs");
    }
    public void Create_camera()
    {
    	GL11.glRotatef(0f, 0f, 0f, 0f);
    	GL11.glRotatef(45f, 1f, 0f, 0f);
    	GL11.glRotatef(rotation, 0f, 0f, 1f);
    	
    	float[] temp = player.get_coordenate();
        GL11.glTranslatef( -temp[0] , -temp[1], 0f);
        //float[] p_coordenate = player.get_coordenate();
        //p_coordenate[0]
        //lightPosition = new float[]{p_coordenate[0], p_coordenate[1], 1f, 0};
        //GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, BufferTools.asFlippedFloatBuffer(lightPosition));

    }
    /*
    public void Create_tile(float bx,float by,float bz,int id_title)
    {
        GL11.glPushMatrix();

            // R,G,B,A Set The Color To Blue One Time Only

            //GL11.glTranslatef(bx, by, 0);
            //GL11.glRotatef(br, 0f, 0f, 1f);
            //GL11.glTranslatef(-bx, -by, 0);
            // draw quad
            //
	        GL11.glEnable(GL11.GL_TEXTURE_2D);
	        GL11.glDisable(GL11.GL_BLEND);
	        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);
            GL11.glBegin(GL11.GL_QUADS);
            	
                int[] texture_info_temp = {id_title, 0};
                textureXOffset = (texture_info_temp[0]/8f);
                textureYOffset = (texture_info_temp[1]/8f);
                textureHeight  = 0.125f;
                textureWidth   = 0.125f;
            
                //System.out.println("id: "+id_title);
                //System.out.println("X: "+(texture_info_temp[0]/10f)+" Y: "+(texture_info_temp[1]/10f));
                GL11.glTexCoord2f(textureXOffset, textureYOffset);
               
                GL11.glVertex3f(bx,by, 0f);

                GL11.glTexCoord2f(textureXOffset + textureWidth, textureYOffset);
                GL11.glVertex3f(bx + 0.16f, by,0f);

                GL11.glTexCoord2f(textureXOffset + textureWidth, textureYOffset + textureHeight);
                GL11.glVertex3f(bx + 0.16f, by + 0.16f,0f);

                
                GL11.glTexCoord2f(textureXOffset, textureYOffset + textureHeight);
                GL11.glVertex3f(bx,by + 0.16f,0f);
                
            GL11.glEnd();
            //pipo
        GL11.glPopMatrix();
    }
	*/
    
    public void Create_player(int id_sprite)
    {
    	//GL11.glColor3f(1.0f,0.0f,0.0f);
        GL11.glPushMatrix();
        	/*GL11.glColor4f(1.0f, 1f, 1.0f, 0.1f);*/
        //GL20.glUseProgram(0);
        	//GL20.glUseProgram(shaderProgram);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture_player);
        	GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
        	//GL11.glColor4ub((byte)255, (byte)255, (byte)255, (byte)255); // You can fade something out in its entirety by altering alpha here too
  

            float[] p_coordenate = player.get_coordenate();
            
    		GL11.glTranslatef(p_coordenate[0], p_coordenate[1], 0.0f);
    		GL11.glRotatef(-(rotation), 0f, 0f, 1f);
    		GL11.glRotatef(45f, 1f, 0f, 0f);
    		//GL11.glTranslatef(-(positX+0.08f), -(positY+0.08f), -( 0.0f));
            //GL11.glScalef( 2.0f,  2.0f,  2.0f );
            //GL11.glLoadIdentity();
            
            GL11.glBegin(GL11.GL_QUADS);
            //id_sprite = 8;
            
                int[] texture_info_temp = {id_sprite, 0};
                textureXOffset = (texture_info_temp[0]/8f);
                textureYOffset = (texture_info_temp[1]/8f);
                textureHeight  = 0.128f;
                textureWidth   = 0.128f;

                
                
	            GL11.glTexCoord2f(textureXOffset, textureYOffset);
	            GL11.glVertex3f(-0.035f, 0.0f, 0.07f);
	            
	            GL11.glTexCoord2f(textureXOffset + textureWidth, textureYOffset);
	            GL11.glVertex3f(0.035f, 0.0f, 0.07f);
	            
	            GL11.glTexCoord2f(textureXOffset + textureWidth, textureYOffset + textureHeight);
	            GL11.glVertex3f(0.035f,0.0f, 0.0f);
	
	            GL11.glTexCoord2f(textureXOffset, textureYOffset + textureHeight);
	            GL11.glVertex3f(-0.035f,0.0f, 0.0f);
	            
	        GL11.glEnd();
	        //GL20.glUseProgram(0);
	        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	        //GL11.glTranslatef(-(p_coordenate[0]), -(p_coordenate[0]), -( 0.0f));
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
        
    }
    
    private void pickObject(int x, int y, int position)
    { 
    	List<Integer> object_temp = mapa_temporal[chunk_x][chunk_y].get_Objects(x,y);
    	//object_temp = null;
    	if (object_temp != null)
    	{
    		//System.out.println("object_tempssss :"+object_temp.add(8));
	    	Iterator<Integer> it=object_temp.iterator();
	    	int temp = 0;
	    	while(it.hasNext())
	        {
	          int value=(int)it.next();
	          if (temp == position)
	          {
		          if (player.pick_item(value) == true)
		          {
		        	  it.remove();
		        	  mapa_temporal[chunk_x][chunk_y].displaylist_regenerate_items();
		        	  //object_temp = null;
		          }
	          }
	          temp ++;
	        }
	    	
	    	
	    	
    	}
    }
    private void dropObject(int x_map2, int y_map2, int item_id)
    {
    	mapa_temporal[chunk_x][chunk_y].add_Objects(x_map2,y_map2,item_id);
		
	}

	public void renderGL() {
    	 // Clear The Screen And The Depth Buffer

		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glRotatef(180, 1.0f, 0.0f, 0.0f);
        
        
        renderWorld();
        
        if (enemy_list != null)
    	{
    		//System.out.println("object_tempssss :"+object_temp.add(8));
	    	Iterator<Player> it=enemy_list.iterator();
	    	while(it.hasNext())
	        {
	          Player value=it.next();	
	          float[] temp = value.get_coordenate();
	          Create_enemy(temp[0],temp[1],0.0f);
	          
	        }
    	}
        if (shots_list != null)
    	{
    		//System.out.println("object_tempssss :"+object_temp.add(8));
        	
	    	Iterator<shots> it=shots_list.iterator();
	    	while(it.hasNext())
	        {
	          shots value=it.next();
	          Create_fire(value.get_x(),value.get_y(),value.get_angle());
	          
	        }
    	}
        //GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        
        
        renderGUI();
        
        //System.out.println("MOUSE DOWN @ X: " + (((Mouse.getX()/960f)-1f)) );
       /* System.out.println("MOUSE DOWN @ Y: " + ((Mouse.getY()/540f)-1f) );
        Create_mouse(((Mouse.getX()/960f)-1f),-((Mouse.getY()/540f)-1f));*/
      //object_temp = null;
    	
        
        //System.out.println("bullet_x: " + bullet_x );
    }
	
	public void Create_enemy(float bx,float by, float bz)
    {

		GL11.glColor3f(1.0f, 1.0f, 1.0f);
           	GL11.glPointSize(40.0f);

            GL11.glBegin(GL11.GL_POINTS);
            GL11.glVertex3f(bx ,by,0.04f);
            GL11.glEnd();

    }
	public void Create_fire(float bx,float by, float angle)
    {
       	/*GL11.glPointSize(10.0f);
        GL11.glBegin(GL11.GL_POINTS);
        GL11.glVertex3f(bx ,by, 0.06f);
        GL11.glEnd();*/
    	
    	GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture_fires);
		GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
     	GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
     	GL11.glColor4ub((byte)255, (byte)255, (byte)255, (byte)255); // You can fade something out in its entirety by altering alpha here too
    	
    	GL11.glColor3f(1f, 1f, 1f);
    	
        float textureXOffset = 0f;
        float textureYOffset = 0f;
        float textureHeight  = 0.03125f;
        float textureWidth   = 0.03125f;
        //System.out.println("id: "+id_title);
        //System.out.println("X: "+(texture_info_temp[0]/10f)+" Y: "+(texture_info_temp[1]/10f));
        
        GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(textureXOffset, textureYOffset);
            GL11.glVertex3f(bx, by, 0.06f);
            
            GL11.glTexCoord2f(textureXOffset + textureWidth, textureYOffset);
            GL11.glVertex3f(bx + 0.04f, by, 0.06f);
            
            GL11.glTexCoord2f(textureXOffset + textureWidth, textureYOffset + textureHeight);
            GL11.glVertex3f(bx + 0.04f, by + 0.04f, 0.06f);

            GL11.glTexCoord2f(textureXOffset, textureYOffset + textureHeight);
            GL11.glVertex3f(bx,by + 0.04f, 0.06f);
        GL11.glEnd();
    }
    public void renderWorld()
    {
        GL11.glPushMatrix();
        
        Create_camera();

        //popo
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        
        if (chunk_x_old != chunk_x || chunk_y_old != chunk_y)
        {
        	chunk_x_old = chunk_x;
	        chunk_y_old = chunk_y;
	        load_map(chunk_x,chunk_y);
        }
        if (chunk_x != 0)
    	{
        	mapa_temporal[chunk_x-1][chunk_y].draw_chunk();
	    	mapa_temporal[chunk_x-1][1+chunk_y].draw_chunk();
    	}
        if (chunk_y != 0)
    	{
	    	mapa_temporal[chunk_x][chunk_y-1].draw_chunk();
	    	mapa_temporal[1+chunk_x][chunk_y-1].draw_chunk();
    	}
        if (chunk_x != 0 && chunk_y != 0)
    	{
	    	mapa_temporal[chunk_x-1][chunk_y-1].draw_chunk();
    	}

        mapa_temporal[chunk_x][chunk_y].draw_chunk();
        
    	mapa_temporal[1+chunk_x][chunk_y].draw_chunk();
    	
    	mapa_temporal[chunk_x][1+chunk_y].draw_chunk();
    	mapa_temporal[1+chunk_x][1+chunk_y].draw_chunk();
        Create_player(id_sprite);
    	
        //map.render(0,0);
        
    }

    public void renderGUI()
    {
    	
    	GL11.glPopMatrix();
    	
        //float aspectRatio = ResWidth / (ResHeight*1f);
        GL11.glScalef(1.0f, 1.0f, 1.0f);
        int[] items = player.get_inventary();
       
        for (int x=0;x<6;x++)
		{
        	create_gui_items(x,items[x]-1);
		}
       
        create_gui_bars(0, player.get_hp());
        create_gui_bars(1, player.get_mp());
        create_gui_bars(2, (player.get_ft()));
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        create_gui_pick();
        create_gui_armor(player.get_armor()-1);
        create_gui_weapon(player.get_weapon()-1);
        
    }
    public void create_gui_weapon(int id_weapon)
    {

        //GL11.glColor3f(1.0f,0.0f,0.0f);
        GL11.glPushMatrix();


        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture_items);
        
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    	GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
    	GL11.glColor4ub((byte)255, (byte)255, (byte)255, (byte)255); // You can fade something out in its entirety by altering alpha here too
            
            GL11.glTranslatef(0.80f, -0.20f, 1f);
            
            
            
           // GL11.glRotatef(rotation, 0f, 0f, 1f);
            //GL11.glScalef( 2.0f,  2.0f,  2.0f );
            //GL11.glLoadIdentity();
            GL11.glBegin(GL11.GL_QUADS);
            //id_sprite = 8;
            
                int[] texture_info_temp = {id_weapon, 0};
                textureXOffset = (texture_info_temp[0]/16f);
                textureYOffset = (texture_info_temp[1]/16f);
                textureHeight  = 0.062f;
                textureWidth   = 0.062f;
            

                GL11.glColor3f(1.0f, 1.0f, 1.0f);

                GL11.glTexCoord2f(textureXOffset, textureYOffset);
                GL11.glVertex2f(- 0.02f, - 0.02f);
                GL11.glTexCoord2f(textureXOffset + textureWidth, textureYOffset);
                GL11.glVertex2f(+ 0.02f, - 0.02f);
                GL11.glTexCoord2f(textureXOffset + textureWidth, textureYOffset + textureHeight);
                GL11.glVertex2f(+ 0.02f, + 0.02f);
                GL11.glTexCoord2f(textureXOffset, textureYOffset + textureHeight);
                GL11.glVertex2f(- 0.02f, + 0.02f);
            GL11.glEnd();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();

    }
    public void create_gui_armor(int id_armor)
    {

        //GL11.glColor3f(1.0f,0.0f,0.0f);
        GL11.glPushMatrix();


        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture_items);
        
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    	GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
    	GL11.glColor4ub((byte)255, (byte)255, (byte)255, (byte)255); // You can fade something out in its entirety by altering alpha here too
            
            GL11.glTranslatef(0.88f, -0.10f, 1f);
            
            
            
           // GL11.glRotatef(rotation, 0f, 0f, 1f);
            //GL11.glScalef( 2.0f,  2.0f,  2.0f );
            //GL11.glLoadIdentity();
            GL11.glBegin(GL11.GL_QUADS);
            //id_sprite = 8;
            
                int[] texture_info_temp = {id_armor, 0};
                textureXOffset = (texture_info_temp[0]/16f);
                textureYOffset = (texture_info_temp[1]/16f);
                textureHeight  = 0.062f;
                textureWidth   = 0.062f;
            

                GL11.glColor3f(1.0f, 1.0f, 1.0f);

                GL11.glTexCoord2f(textureXOffset, textureYOffset);
                GL11.glVertex2f(- 0.02f, - 0.02f);
                GL11.glTexCoord2f(textureXOffset + textureWidth, textureYOffset);
                GL11.glVertex2f(+ 0.02f, - 0.02f);
                GL11.glTexCoord2f(textureXOffset + textureWidth, textureYOffset + textureHeight);
                GL11.glVertex2f(+ 0.02f, + 0.02f);
                GL11.glTexCoord2f(textureXOffset, textureYOffset + textureHeight);
                
                
                
                GL11.glVertex2f(- 0.02f, + 0.02f);
            GL11.glEnd();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();

    }
    public void create_gui_pick()
    {
    	/*System.out.println("chunk_x"+chunk_x);
    	System.out.println("chunk_y"+chunk_y);
    	System.out.println("x_map"+x_map);
    	System.out.println("y_map"+y_map);*/
    	List<Integer> temp_list = mapa_temporal[chunk_x][chunk_y].get_Objects(x_map,y_map);
    	
    	Iterator<Integer> it=temp_list.iterator();
    	int temp2 = 0;
    	while(it.hasNext())
        {
          int value=(int)it.next();
	
		        //GL11.glColor3f(1.0f,0.0f,0.0f);
		        GL11.glPushMatrix();
		        
	
		        GL11.glEnable(GL11.GL_TEXTURE_2D);
		        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture_items);
		        
		        GL11.glEnable(GL11.GL_BLEND);
		        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		    	GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
		    	GL11.glColor4ub((byte)255, (byte)255, (byte)255, (byte)255); // You can fade something out in its entirety by altering alpha here too
	            //System.out.println("x"+x);
	            switch(temp2)
	            {
	            case 0:
	            	GL11.glTranslatef(0.82f,  0.24f, 1f);
	            	break;
	            case 1:
	            	GL11.glTranslatef(0.88f,  0.24f, 1f);
	            	break;
	            case 2:
	            	GL11.glTranslatef(0.94f,  0.24f, 1f);
	            	break;
	            case 3:
	            	GL11.glTranslatef(0.82f, 0.30f, 1f);
	            	break;
	            case 4:
	            	GL11.glTranslatef(0.88f, 0.30f, 1f);
	            	break;
	            case 5:
	            	GL11.glTranslatef(0.94f, 0.30f, 1f);
	            	break;
	
	            }
	            
	           // GL11.glRotatef(rotation, 0f, 0f, 1f);
	            //GL11.glScalef( 2.0f,  2.0f,  2.0f );
	            //GL11.glLoadIdentity();
	            GL11.glBegin(GL11.GL_QUADS);
	            //id_sprite = 8;
	            
	                int[] texture_info_temp = {value-1, 0};
	                textureXOffset = (texture_info_temp[0]/16f);
	                textureYOffset = (texture_info_temp[1]/16f);
	                textureHeight  = 0.062f;
	                textureWidth   = 0.062f;
	            
	
	                GL11.glColor3f(1.0f, 1.0f, 1.0f);
	
	                GL11.glTexCoord2f(textureXOffset, textureYOffset);
	                GL11.glVertex2f(- 0.02f, - 0.02f);
	                GL11.glTexCoord2f(textureXOffset + textureWidth, textureYOffset);
	                GL11.glVertex2f(+ 0.02f, - 0.02f);
	                GL11.glTexCoord2f(textureXOffset + textureWidth, textureYOffset + textureHeight);
	                GL11.glVertex2f(+ 0.02f, + 0.02f);
	                GL11.glTexCoord2f(textureXOffset, textureYOffset + textureHeight);
	                
	                
	                
	                GL11.glVertex2f(- 0.02f, + 0.02f);
	            GL11.glEnd();
	        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	        GL11.glDisable(GL11.GL_TEXTURE_2D);
	        GL11.glDisable(GL11.GL_BLEND);
	        GL11.glPopMatrix();
	        temp2 ++;
        }
    }
    public void create_gui_bars(int type, int percentage)
    {
    	GL11.glPushMatrix();
    	//System.out.println("x"+x);
       
        
       // GL11.glRotatef(rotation, 0f, 0f, 1f);
        //GL11.glScalef( 2.0f,  2.0f,  2.0f );
        //GL11.glLoadIdentity();
	    GL11.glDisable(GL11.GL_TEXTURE_2D);
	    GL11.glDisable(GL11.GL_BLEND);
	    switch(type)
        {
        case 0:
        	 GL11.glTranslatef(0.0f, 0.46f, 1f);
        	 GL11.glColor3f(1.0f, 0.0f, 0.0f);
        	break;
        case 1:
        	 GL11.glTranslatef(0.0f, 0.49f, 1f);
        	 GL11.glColor3f(0.0f, 0.0f, 1.0f);
        	break;
        case 2:
        	 GL11.glTranslatef(0.0f, 0.52f, 1f);
        	 GL11.glColor3f(0.0f, 1.0f, 0.0f);
        	break;
        }
        GL11.glBegin(GL11.GL_QUADS);
        //id_sprite = 8;       
           
            //GL11.glTexCoord2f(textureXOffset, textureYOffset);
            GL11.glVertex2f( -0.90f, - 0.01f);
            //GL11.glTexCoord2f(textureXOffset + textureWidth, textureYOffset);
            GL11.glVertex2f(- (0.90f-(percentage/1000f)), - 0.01f);
            //GL11.glTexCoord2f(textureXOffset + textureWidth, textureYOffset + textureHeight);
            GL11.glVertex2f(- (0.90f-(percentage/1000f)), + 0.01f);
            //GL11.glTexCoord2f(textureXOffset, textureYOffset + textureHeight);
            GL11.glVertex2f(- 0.90f, + 0.01f);
        GL11.glEnd();
	    GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

	    GL11.glPopMatrix();
    }
    public void create_gui_items(int x,int id_item)
    {
    	if (id_item == -1)
    	{
    		return;
    	}
        //GL11.glColor3f(1.0f,0.0f,0.0f);
        GL11.glPushMatrix();


        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture_items);
        
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    	GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
    	GL11.glColor4ub((byte)255, (byte)255, (byte)255, (byte)255); // You can fade something out in its entirety by altering alpha here too
            //System.out.println("x"+x);
            switch(x)
            {
            case 0:
            	GL11.glTranslatef(0.82f, 0, 1f);
            	break;
            case 1:
            	GL11.glTranslatef(0.88f, 0, 1f);
            	break;
            case 2:
            	GL11.glTranslatef(0.94f, 0, 1f);
            	break;
            case 3:
            	GL11.glTranslatef(0.82f, 0.06f, 1f);
            	break;
            case 4:
            	GL11.glTranslatef(0.88f, 0.06f, 1f);
            	break;
            case 5:
            	GL11.glTranslatef(0.94f, 0.06f, 1f);
            	break;

            }
            
           // GL11.glRotatef(rotation, 0f, 0f, 1f);
            //GL11.glScalef( 2.0f,  2.0f,  2.0f );
            //GL11.glLoadIdentity();
            GL11.glBegin(GL11.GL_QUADS);
            //id_sprite = 8;
            
                int[] texture_info_temp = {id_item, 0};
                textureXOffset = (texture_info_temp[0]/16f);
                textureYOffset = (texture_info_temp[1]/16f);
                textureHeight  = 0.062f;
                textureWidth   = 0.062f;
            

                GL11.glColor3f(1.0f, 1.0f, 1.0f);

                GL11.glTexCoord2f(textureXOffset, textureYOffset);
                GL11.glVertex2f(- 0.02f, - 0.02f);
                GL11.glTexCoord2f(textureXOffset + textureWidth, textureYOffset);
                GL11.glVertex2f(+ 0.02f, - 0.02f);
                GL11.glTexCoord2f(textureXOffset + textureWidth, textureYOffset + textureHeight);
                GL11.glVertex2f(+ 0.02f, + 0.02f);
                GL11.glTexCoord2f(textureXOffset, textureYOffset + textureHeight);
                
                
                
                GL11.glVertex2f(- 0.02f, + 0.02f);
            GL11.glEnd();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();

    }
    public static void main(String[] argv)
    {
        MainCode pepitoGame = new MainCode();
        pepitoGame.start();
    }
}