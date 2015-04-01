
public final class BiquadFilterState2 {
  private double w1;
  private double w2;
  
  public BiquadFilterState2() {
    this.w1 = 0.0;
    this.w2 = 0.0;
  }
  
  public double getW1() { return this.w1; }
  public double getW2() { return this.w2; }
  
  public void setW1(double w1) {
    this.w1 = w1;
  }
  
  public void setW2(double w2) {
    this.w2 = w2;
  }
}
