
package jdsp;

public interface UnaryOperator {
  public abstract void begin(int size);
  public abstract void end(int size);
  
  public abstract double apply(double input);
}