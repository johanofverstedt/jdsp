
package jdsp;

public class RangeAllocator {
  double[] array;
  int used;
  
  public RangeAllocator(int size) {
    array = new double[size];
    used = 0;
  }
  
  public Range makeRange(int size) {
     Range result = new Range(this.array, this.used, this.used + size, 1);
     this.used += size;
     
     return result;
  }
  
  public Range makeRange(ObjectAllocator<Range> allocator,
                         int size) {
    Range result = allocator.allocate();
    result.remake(this.array, this.used, this.used + size, 1);
    this.used += size;

    return result;
  }
  
  public int push() {
    return this.used;
  }
  
  public void pop(int handle) {
     this.used = handle;
  }
}
