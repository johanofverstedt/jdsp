
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
  public double apply(double input) {
    int size = impulseResponse.size();
    int writeIndex = index;
    for(int i = 0; i < size; ++i, writeIndex++) {
      this.tmpBuf.set(writeIndex, input * impulseResponse.get(i));
    }
    return input;
  }
}
