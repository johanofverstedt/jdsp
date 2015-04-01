
package jdsp;

public class ObjectAllocator<T> {
  private Object[] array;
  private Class<T> cls;
  private int used;
  private int[] stack;
  private int stackIndex;
  
  public ObjectAllocator(int size, Class<T> cls) {
    this(size, size, cls);
  }
  
  public ObjectAllocator(int size, int stackSize, Class<T> cls) {
    this.cls = cls;
    this.array = new Object[size];
    try {
      for(int i = 0; i < size; ++i)
        this.array[i] = cls.newInstance();
    } catch(InstantiationException e) {
      throw new RuntimeException("Can't create ObjectAllocator");
    } catch(IllegalAccessException e) {
      throw new RuntimeException("Can't create ObjectAllocator");
    }
    this.used = 0;
    
    stack = new int[stackSize];
    stackIndex = 0;
  }
    
  public T allocate() {
    T result = null;
    Object o = this.array[this.used];
    
    if(cls.isAssignableFrom(o.getClass())) {
      result = cls.cast(o);
      this.used++;
    }
    
    return result;
  }
  
  public void push() {
    assert (stackIndex < stack.length);
    
    stack[stackIndex++] = this.used;
  }
  
  public void pop() {
    assert (stackIndex > 0);
    
    this.used = stack[--stackIndex];
  }
  
  public void reset() {
    this.used = 0;
    this.stackIndex = 0;
  }
}
