import java.util.ArrayList;
import java.util.List;



public class Casilla {
	private int TileID;
	private int tree = 0;
	private int wall = 0;
	private int item = 0; // item hidden (1 ninguno)
	private boolean blocked = false;
	private float[] vertices = {0,0,0,0};
	private List<Integer> id_obj =new ArrayList<Integer>();  //lista de objetos cuando es un bag
	
	public Casilla(int TileID, float a, float b, float c, float d)
	{
		this.TileID = TileID;
		this.vertices[0] = a;
		this.vertices[1] = b;
		this.vertices[2] = c;
		this.vertices[3] = d;
		
		
	}
	/*TILED*/
	public void up(boolean a, boolean b,boolean c,boolean d)
	{
		if (a == true)
			this.vertices[0] += 0.02f;
		if (b == true)
			this.vertices[1] += 0.02f;
		if (c == true)
			this.vertices[2] += 0.02f;
		if (d == true)
			this.vertices[3] += 0.02f;
	}
	public void down(boolean a, boolean b,boolean c,boolean d)
	{
		if (a == true)
			this.vertices[0] -= 0.02f;
		if (b == true)
			this.vertices[1] -= 0.02f;
		if (c == true)
			this.vertices[2] -= 0.02f;
		if (d == true)
			this.vertices[3] -= 0.02f;
	}
	public void set_Tiled(int TileID)
	{
		this.TileID = TileID;
	}
	public int get_Tiled(){
		return this.TileID;
	}
	/*TILED*/
	public float get_Media()
	{
		return (vertices[0] + vertices[2])/2f;
	}
	/*OBJECTS*/
	public void add_Tree(int type)
	{
		if (blocked == false)
		{
			this.tree = type;
			set_blocked(true);
		}
	}
	public void add_Item(int item)
	{
		if (blocked == false && get_Objects_check() == 0)
		{
			this.item = item;
			
			//set_blocked(false);
		}
	}
	public void remove_Tree()
	{
		this.tree = 0;
	}
	public int get_Tree()
	{
		return this.tree;
	}
	public int get_Item()
	{
		return this.item;
	}
	public void add_Objects(int id_obj)
	{
		this.id_obj.add(id_obj);
	}
	public void remove_Objects()
	{
		this.id_obj = null;
	}
	public void remove_Item()
	{
		this.item = 0;
	}
	public void set_Objects(List<Integer> id_obj)
	{
		this.id_obj = id_obj;
	}
	public List<Integer> get_Objects()
	{
		//
		return this.id_obj;
	}
	public int get_Objects_check()
	{
		return this.id_obj.size();
	}
	/*OBJECTS*/
	
	/*BLOCKED*/
	public void set_blocked(boolean blocked)
	{
		this.blocked = blocked;
	}
	public boolean get_blocked()
	{
		return blocked;
	}
	/*BLOCKED*/
	public float[] get_vertices() {
		return vertices;
	}
	public void add_wall(int i) {
		if (blocked == false)
		{
			set_blocked(true);
			wall = i;
		}	
	}
	public int get_Wall()
	{
		return wall;
	}
	
	 /**
	 * mapa_objetos[1][5] = new Casilla(1,5,"pepe")
	 * 
	 * mapa_objetos[x][y].set_Tiled()
	 * 
	 * mapa_objetos[x][y]
	 * 
	 */
	

}
