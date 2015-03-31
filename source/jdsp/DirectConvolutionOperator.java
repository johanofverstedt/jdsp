
package jdsp;

public class DirectConvolutionOperator
  implements UnaryOperator {
 
  private Range impulseResponse;
  private RangeAllocator allocator;
  private int allocatorPushHandle;
  private Range tmpBuf;
  private int index;
  
  public DirectConvolutionOperator(Range impulseResponse, RangeAllocator allocator) {
    this.impulseResponse = impulseResponse;
    this.allocator = allocator;
  }
  
  @Override
  public void begin(int size) {
    this.allocatorPushHandle = this.allocator.push();
    this.tmpBuf = this.allocator.makeRange(this.impulseResponse.size() + size - 1);
    this.index = 0;
  }
  @Override
  public void end(int size) {
    this.allocator.pop(allocatorPushHandle);
    allocatorPushHandle = 0;
    tmpBuf = null;
  }
  
  @Override
  public double apply(double input) {
    int size = impulseResponse.size();
    int writeIndex = index;
    for(int i = 0; i < size; ++i, writeIndex++) {
      this.tmpBuf.set(writeIndex, input * impulseResponse.get(i));
    }
    return input;
  }
}
