
package jdsp;

public class ComplexRange {
  private double[] array;
  private int start;
  private int end;
  private int stride;
  
  public ComplexRange() {
    this(null, 0, 0, 2);
  }

  public ComplexRange(double[] array, int start, int end, int stride) {
    this.array = array;
    this.start = start;
    this.end = end;
    this.stride = stride;
  }
}
