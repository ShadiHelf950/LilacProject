package LilacClasses;

public class Time
{
  private int hour;
  private int minute;
  
  public Time(int h, int m)
  {
	  hour = h;
	  minute = m;
  }
  
  // getters and setters for private fields
  
  public int getHour() { return hour; }
  public void setHour(int h) { hour = h; }
  
  public int getMinute()  { return minute; }
  public void setMinute(int m) { minute = m; }
}
