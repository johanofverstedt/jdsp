
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
  }
  
  @Override
  public void begin(int size) { current = start; }
  @Override
  public void end(int size) {}
  
  @Override
  public double apply(double input) {
    int result = current;
    current += stride;
    
    return (double)result;
  }
}
