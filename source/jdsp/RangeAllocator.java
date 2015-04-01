
package jdsp;

public class RangeAllocator {
  private ObjectAllocator<Range> rangeObjectAllocator;
  private ObjectAllocator<ComplexRange> complexRangeObjectAllocator;
  private double[] array;
  private int used;
  private int[] stack;
  private int stackIndex;
  
  public RangeAllocator(int size, int objectCount) {
    this(size, objectCount, objectCount);
  }
  
  public RangeAllocator(int size, int realObjectCount, int complexObjectCount) {
    this.rangeObjectAllocator = new ObjectAllocator<Range>(realObjectCount, Range.class);
    this.complexRangeObjectAllocator = new ObjectAllocator<ComplexRange>(complexObjectCount, ComplexRange.class);
    this.array = new double[size];
    this.used = 0;
    this.stack = new int[realObjectCount + complexObjectCount];
    this.stackIndex = 0;
  }
  
  public Range makeRange(int size) {
    assert (this.used + size <= this.array.length);
    
    Range result = rangeObjectAllocator.allocate();
    result.remake(this, this.array, this.used, this.used + size, 1);
    this.used += size;

    return result;
  }
  
  public Range makeRange(int start, int end, int stride) {
    Range result = rangeObjectAllocator.allocate();
    result.remake(this, this.array, start, end, stride);
    
    return result;
  }
  
  public int used() {
    return this.used;
  }
  
  public int unused() {
    return this.array.length - this.used;
  }
  
  public void push() {
    pushValueState();
    
    rangeObjectAllocator.push();
  }
  
  public void pushValueState() {
    assert (this.stackIndex < stack.length);
    this.stack[this.stackIndex++] = this.used;  
  }
  
  public void pushRangeObjectState() {
    rangeObjectAllocator.push();
    complexRangeObjectAllocator.push();
  }
  
  public void pop() {
    popValueState();
    popRangeObjectState();
  }
  
  public void popValueState() {
    assert (this.stackIndex > 0);
    this.used = stack[--this.stackIndex];
  }
  
  public void popRangeObjectState() {
    rangeObjectAllocator.pop();
    complexRangeObjectAllocator.pop();
  }
  
  public void reset() {
    this.used = 0;
    this.stackIndex = 0;
    rangeObjectAllocator.reset();
    complexRangeObjectAllocator.reset();
  }
}
