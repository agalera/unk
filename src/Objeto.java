
public class Objeto {
	
	private int id;
	private int type;
	private int armor;
	private int value;
	private int effect;
	private int damage;
	private int speed;
	private int type_weapon;
	
	// type = 0 armor, 1 weapon, 2 potion
	
	//armor
	public Objeto(int id, int armor){
		
		this.id = id;
		this.type = 0;
		
	}
	//potion
	public Objeto(int id, int effect, int value){
		this.id = id;
		this.type = 2;
		this.effect = 0;
		this.value = 10;
		
	}
	
	//weapon
	public Objeto(int id, int damage, int speed, int type_weapon){
		this.id = id;
		this.type = 1;
		this.damage = damage;
		this.speed = speed;
		this.type_weapon = type_weapon;
		
	}
	public int get_type(){
		return this.type;
	}
	
}
