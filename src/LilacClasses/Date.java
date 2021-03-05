package LilacClasses;

public class Date
{
  private int day;
  private int year;
  private int month;
	  
  public Date(int d, int y, int m)
  {
    day = d;
    year = y;
    month = m;
  }
  
  // copy constructor
  public Date(Date d)
  {
    day = d.day;
    year = d.year;
    month = d.month;
  }
	  
  // getters and setters for private fields
  
  public int getDay() { return day; }
  public void setDay(int d) { day = d; }
  
  public int getYear() { return year; }
  public void setYear(int y) { year = y; }
  
  public int getMonth() { return month; }
  public void setMonth(int m) { month = m; }
}
