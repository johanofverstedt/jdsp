
package jdsp;

public final class Range {
  private double[] array;
  private int start;
  private int end;
  private int stride;
  
  public Range() {
    stride = 1;
  }
  
  public Range(double[] array, int start, int end) {
    this(array, start, end, 1);
  }
  
  public Range(double[] array, int start, int end, int stride) {
    this.array = array;
    this.start = start;
    this.end = end;
    this.stride = stride;
  }
  
  public int size() {
    int result = (end - start)/stride;
    if(result <= 0)
      return 0;
    return result;
  }
  
  public double get(int index) {
    assert (index >= 0 && index < size());
    
    int arrayIndex = start + index * stride;
    double result = this.array[arrayIndex];
    
    return result;
  }
  
  public void set(int index, double value) {
    assert (index >= 0 && index < size());
    
    int arrayIndex = start + index * stride;
    this.array[arrayIndex] = value;
  }
  
  public void fill(double value) {
    int start = this.start;
    int end = this.end;
    int stride = this.stride;
    
    for(int i = start; i != end; i += stride) {
      array[i] = value;
    }
  }
  
  public void zero() {
    fill(0.0);
  }
  
  @Override
  public String toString() {
    String result = "[";
    int start = this.start;
    int end = this.end;
    int stride = this.stride;
    
    int i = start;
    if(i != end) {
      result += array[i];
      i += stride;
    }
    
    for(; i != end; i += stride) {
      result += ", " + array[i];
    }
    
    return result + "]";
  }
  
  public Range reverse() {
    return new Range(array, end - stride, start - stride, -stride);
  }
  
  public Range reverseMutable() {
    int newEnd = start;
    int newStart = end;
    int newStride = -stride;
    
    start = newStart + newStride;
    end = newEnd + newStride;
    stride = newStride;
    
    return this;
  }
  
  public Range apply(UnaryOperator op, Range out) {
    int start = this.start;
    int end = this.end;
    int stride = this.stride;
    int outStart = out.start;
    int outEnd = out.end;
    int outStride = out.stride;
    double[] outArray = out.array;
    
    int size = size();
    int outSize = out.size();
    int realSize = Math.min(size, outSize);
    
    op.begin(realSize);
    
    int inIndex = start;
    int outIndex = outStart;
    
    if(size == realSize) {
      for(; inIndex != end; inIndex += stride, outIndex += outStride) {
        outArray[outIndex] = op.apply(array[inIndex]);
      }
    } else {
      for(; outIndex != outEnd; inIndex += stride, outIndex += outStride) {
        outArray[outIndex] = op.apply(array[outIndex]);
      }
    }

    op.end(realSize);
    
    return out;
  }
  
  public void applyMutable(UnaryOperator op) {
    int start = this.start;
    int end = this.end;
    int stride = this.stride;
    
    int size = size();
    op.begin(size);
    
    for(int i = start; i != end; i += stride) {
      array[i] = op.apply(array[i]);
    }  

    op.end(size);
  }
  
  //Allocator support
  
  public static ObjectAllocator<Range> makeAllocator(int size) {
    ObjectAllocator<Range> result = new ObjectAllocator<>(size, Range.class);
    return result;
  }
  
  public static Range make(ObjectAllocator<Range> allocator,
                           double[] array,
                           int start,
                           int end) {
    
    return make(allocator, array, start, end, 1);
  }
  public static Range make(ObjectAllocator<Range> allocator,
                           double[] array,
                           int start,
                           int end,
                           int stride) {
    
    Range result = allocator.allocate();
    result.array = array;
    result.start = start;
    result.stride = stride;
    
    return result;
  }
  
  void remake(double[] array, int start, int end, int stride) {
    this.array = array;
    this.start = start;
    this.end = end;
    this.stride = stride;
  }
}
