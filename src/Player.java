
public class Player {
	private float[] p_coordenate = {1,1,1}; 
	private int hp = 100;
	private int mp = 100;
	private int ft = 1000;
	private float speed = 0.0006f;
	private int[] inventary = {0,0,0,0,0,0};
	private int[] p_m_coordenate = {0,0,0};
	
	private int armor=0;
	private int weapon=0;
	
	
	public Player(float[] p_coordenate, int hp, int mp, int ft, float speed)
	{
		this.p_coordenate = p_coordenate; 
		this.hp = hp;
		this.mp = mp;
		this.ft = ft;
		this.speed = speed;
	}
	public int get_weapon()
	{
		return this.weapon;
	}
	public void set_weapon(int slot_player)
	{
		if (this.weapon == 0)
		{
				this.weapon = get_inventary_item(slot_player);
				this.inventary[slot_player]= 0;
		}
		else if (this.weapon != 0)
		{
			int temp_weapon = this.weapon;
			this.weapon = get_inventary_item(slot_player);
			this.inventary[slot_player]= temp_weapon;
		}
		
	}
	
	public int get_armor()
	{
		return this.armor;
	}
	public void set_armor(int slot_player)
	{
		if (this.armor == 0)
		{
				this.armor = get_inventary_item(slot_player);
				this.inventary[slot_player]= 0;
		}
		else if (this.armor != 0)
		{
			int temp_armor = this.armor;
			this.armor = get_inventary_item(slot_player);
			this.inventary[slot_player]= temp_armor;
		}
		
	}
	public float[] get_coordenate()
	{
		return p_coordenate;
	}
	public void set_coordenate(float x_new, float y_new, float r_new)
	{
		float[] p_coordenate = {x_new,y_new, r_new};
		this.p_coordenate = p_coordenate;
		
	}
	public void set_m_coordenate(int x_map, int y_map, int r_map)
	{
		int[] p_m_coordenate = {x_map,y_map, r_map};
		this.p_m_coordenate = p_m_coordenate;
		
	}
	public int[] get_m_coordenate()
	{
		
		return this.p_m_coordenate;
		
	}
	
	public int[] get_inventary()
	{
		return this.inventary;
	}
	public int get_inventary_item(int id)
	{
		return this.inventary[id];
	}
	public boolean pick_item(int item)
	{
		System.out.println("item :"+item);
		for (int x=0;x<6;x++)
		{
			if (this.inventary[x] == 0)
			{
				this.inventary[x] = item;
				return true;
			}
		}
		System.out.println("inventario lleno");
		return false;
	}
	public boolean delete_inventary_item(int item)
	{
		for (int x=0;x<6;x++)
		{
			if (this.inventary[x] == item)
			{
				this.inventary[x] = 0;
				return true;
			}
		}
		return false;
	}
	public int drop_item_slot(int slot)
	{
		if(this.inventary[slot] != 0) 
		{
			int temp = this.inventary[slot];
			this.inventary[slot] = 0;
			return temp;
		}
		return 0;
	}
	public int drop_next_item()
	{
		for (int x=5;-1<x;x--)
		{
			//System.out.println("number:"+ x);
			if (this.inventary[x] != 0)
			{
				System.out.println("number:"+ x);
				int value_return = this.inventary[x];
				this.inventary[x] = 0;
				return value_return;
			}
		}
		return 0;
	}
	
	public int get_hp()
	{
		return this.hp;
	}
	public void set_hp(int hp)
	{
		this.hp = hp;
	}
	
	public int get_mp()
	{
		return mp;
	}
	public void set_mp(int mp)
	{
		this.mp = mp;
	}
	
	public int get_ft()
	{
		return ft;
	}
	public void set_ft(int ft)
	{
		this.ft = ft;
	}
	
	public boolean use_ft()
	{
		if (this.ft > 0)
		{
			this.ft -=2;
			return true;
		}
		return false;
	}
	public boolean recovery_ft()
	{
		if (this.ft < 1000)
		{
			this.ft +=1;
			return true;
		}
		return false;
	}
	
	public float get_speed()
	{
		return speed;
	}
	public void set_speed(float speed)
	{
		this.speed = speed;
	}
	public int check_damage(float x,float y, int damage)
	{
		
		/*int x_map = (int) (this.p_coordenate[0]*6.25f);
        int y_map = (int) (this.p_coordenate[1]*6.25f);*/
		/*System.out.println("this.p_coordenate[1]" + this.p_coordenate[1]);
		System.out.println("y" + y);*/
		
        if (this.p_coordenate[0]-0.02f < x && this.p_coordenate[0]+0.02f > x && this.p_coordenate[1]-0.02f < y && this.p_coordenate[1]+0.02f > y)
        {
    	
    		this.hp -= damage;
    		System.out.println("hp"+ this.hp);
    		if (this.hp < 0)
    		{
        		//System.out.println("saldkajsdlkasjlkdjaskldj");
    			return 2;
    		}
    		else
    		{
    			return 1;
    		}
        
        }
        
        return 0;
	}

}
