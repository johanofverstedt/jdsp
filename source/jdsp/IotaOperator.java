
package jdsp;

public final class IotaOperator
  implements UnaryOperator {
 
  private int start;
  private int stride;
  private int current;
  
  public IotaOperator(int start) { this(start, 1); }
  
  public IotaOperator(int start, int stride) {
    this.start = start;
    this.stride = stride;
    this.current = start;
  }

  public void reset() {
    this.current = start;
  }
  
  @Override
  public double apply(double input) {
    int result = current;
    current += stride;
    
    return (double)result;
  }
}
