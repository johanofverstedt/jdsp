
package jdsp.filter;

import jdsp.*;

public class BiquadFilter {
  private double b0;
  private double b1;
  private double b2;
  private double a1;
  private double a2;
  
  public BiquadFilter() {
    this.b0 = 1.0;
  }
  
  public void makeLowPass(double fc, double sampleRate, double q) {
    
  }
  
  public void apply(BiquadFilterState2 state, Range input, Range output) {
    double b0 = this.b0;
    double b1 = this.b1;
    double b2 = this.b2;
    double a1 = this.a1;
    double a2 = this.a2;
    
    double w1 = state.getW1();
    double w2 = state.getW2();
    
  }
}
