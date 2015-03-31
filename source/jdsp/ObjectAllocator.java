
public class ObjectAllocator<T> {
  private Object[] array;
  private Class<T> cls;
  private int used;
  
  public ObjectAllocator(int size, Class<T> cls) {
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
  
  public int push() {
    return this.used;
  }
  
  public void pop(int handle) {
    this.used = handle;
  }
}
