
public class shots {
	private float x;
	private float y;
	private float init_x;
	private float init_y;
	private float angle;
	private float speed;
	
	public shots(float init_x, float init_y,float angle)
	{
		this.x = init_x;
		this.y = init_y;
		this.init_x = init_x;
		this.init_y = init_y;
		this.angle = angle;
		this.speed = 0.001f;
	}

	public boolean move_shot(float delta)
	{
	    this.x -= this.speed * delta * (float)Math.sin(Math.toRadians(this.angle));
	    this.y += this.speed * delta * (float)Math.cos(Math.toRadians(this.angle));		
		return false;
	}
	public float get_x()
	{
		
		return this.x;
	}
	public float get_y()
	{
		return this.y;
	}
	public float get_angle()
	{
		return this.angle;
	}

}
