
package jdsp;

public final class Range {
  private double[] array;
  private int start;
  private int end;
  private int stride;
  private RangeAllocator allocator;
  
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
  
  public boolean isEmpty() {
    return start == end;
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
    java.lang.StringBuffer sb = new java.lang.StringBuffer(1024);
    sb.append("[");

    int start = this.start;
    int end = this.end;
    int stride = this.stride;
    
    int i = start;
    if(i != end) {
      sb.append(Double.toString(array[i]));
      i += stride;
    }
    
    for(; i != end; i += stride) {
      sb.append(", ");
      sb.append(Double.toString(array[i]));
    }
    
    sb.append("]");
    return sb.toString();
  }
  
  public Range take(int count) {
    Range result = null;
    int size = size();
    if(count > size)
      count = size;
    
    result = shallowCopy(start, end - (stride * (size - count)), stride);
    return result;
  }
  
  public Range drop(int count) {
    Range result = null;
    int size = size();
    if(count > size)
      count = size;
    
    result = shallowCopy(start + stride * count, end, stride);
    return result;    
  }
  
  public Range copy() {
    Range result = null;
    int size = size();
    if(allocator != null) {
      result = allocator.makeRange(size);
    } else {
      result = new Range(new double[size],
                       0,
                       size,
                       1);
    }
    double[] outArray = result.array;
    int outIndex = result.start;
    
    for(int i = start; i != end; i+=stride, ++outIndex) {
      outArray[outIndex] = this.array[i];
    }
    
    return result;  
  }
  
  public Range reverse() {
    if(allocator != null) {
      return allocator.makeRange(this.end - this.stride, 
                                 this.start - this.stride, 
                                 -this.stride);
    } else {
      return new Range(this.array,
                       end - stride,
                       start - stride,
                       -stride);
    }
  }
  
  public Range reverseCopy() {
    Range result = null;
    if(allocator != null) {
      result = allocator.makeRange(size());
    } else {
      result = new Range(new double[size()], 0, size(), 1);
    }
    int outIndex = size()-1;
    for(int i = start; i != end; i+=stride) {
      result.set(outIndex--, this.array[i]);
    }
    
    return result;
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
    
    return out;
  }
  
  public void applyMutable(UnaryOperator op) {
    int start = this.start;
    int end = this.end;
    int stride = this.stride;
    
    int size = size();
    
    for(int i = start; i != end; i += stride) {
      array[i] = op.apply(array[i]);
    }  

  }
  
  //Allocator support
  
  public static ObjectAllocator<Range> makeAllocator(int size) {
    ObjectAllocator<Range> result = new ObjectAllocator<>(size, Range.class);
    return result;
  }
  /*
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
  }*/
  
  void remake(RangeAllocator allocator, double[] array, int start, int end, int stride) {
    int count = (end-start)/stride;
    end = start + stride * count;
    assert(count >= 0);
    
    this.allocator = allocator;
    this.array = array;
    this.start = start;
    this.end = end;
    this.stride = stride;
  }
  
  //
  // Mathematical operators
  //
  
  public Range add(double value) {
    int start = this.start;
    int end = this.end;
    int stride = this.stride;
    double[] array = this.array;
    
    for(int i = start; i != end; i += stride) {
      array[i] += value;
    }
    
    return this;
  }
  
  public Range addCopy(double value) {
    Range result = beginDeepCopy();
    
    int start = this.start;
    int end = this.end;
    int stride = this.stride;
    int outStart = result.start;
    int outEnd = result.end;
    double[] inArray = this.array;
    double[] outArray = result.array;
    
    int inIndex = start;
    int outIndex = outStart;
    
    for(; inIndex != end; inIndex += stride, ++outIndex) {
      outArray[outIndex] = inArray[inIndex] + value;
    }
    
    return result;
  }
  
  private Range shallowCopy(int start, int end, int stride) {
    Range result = null;
    
    if(allocator != null) {
      result = allocator.makeRange(start, end, stride);
    } else {
      result = new Range(this.array, start, end, stride);
    }
    
    assert (result.size() <= size());
    
    return result;
  }
  
  private Range beginDeepCopy() {
    Range result = null;
    int size = size();
    
    if(allocator != null) {
      result = allocator.makeRange(size);
    } else {
      result = new Range(new double[size], 0, size, 1);
    }
    
    return result;
  }
}
