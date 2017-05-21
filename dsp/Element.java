/*
Dascalescu Andreea-Cristina
Homework nr. 2 - December 2016
*/

package dsp;

import java.util.Arrays;
//Observation regarding the output variable:
//If used in the derrived classes from the abstract class Element, I really
//could not extract it.
abstract class Element {

    protected Signal output;

    abstract void compute();

}
//I have used flagConstructor variable in order to make a differentiation between
//the 2 constructors which have different input arguments
class Adder extends Element {

    private Signal inOne;
    private Signal inTwo;
    private Signal[] inArr;
    private boolean flagConstructor;

    public Adder(Signal op, Signal in1, Signal in2) {
        super.output = op;
        this.inOne = in1;
        this.inTwo = in2;
        flagConstructor = true;
    }

    public Adder(Signal op, Signal[] in) {
        super.output = op;
        inArr = Arrays.copyOf(in, in.length);
        flagConstructor = false;
    }

    void compute() {
        Signal tmpSgn = new Signal();
        if(flagConstructor) {
            output = inOne.add(inTwo);
        } else {
            output = addArr(inArr);
        }
    }
    //This method calls add method a number of times, based on input Signal length
    //of values array.
    protected Signal addArr(Signal[] sgnArr){
        Signal tmpSgn = new Signal();
        
        for (int i = 0; i < sgnArr.length-1; i++){
            if (i == 0){
                tmpSgn = sgnArr[i].add(sgnArr[i+1]);
            } else {
                tmpSgn = tmpSgn.add(sgnArr[i+1]);
            }
        }
        
        return tmpSgn;
    }
}

class Gain extends Element {

    private Signal input;
    private double gain;

    public Gain(Signal op, Signal in, double gain) {
        super.output = op;
        this.input = in;
        this.gain = gain;
    }

    void compute() {
        output = input.scale(gain);
    }
}

class Delay extends Element {

    private Signal input;
    private int delay;

    public Delay(Signal op, Signal in, int delay) {
        super.output = op;
        this.input = in;
        this.delay = delay;
    }

    void compute() {
        output = input.delay(delay);
    }
}

class Filter extends Element {

    private Signal input;
    private double[] fir;

    public Filter(Signal op, Signal in, double[] fir) {
        super.output = op;
        this.input = in;
        this.fir = Arrays.copyOf(fir, fir.length);
    }

    void compute() {
        Signal tmpSgn = new Signal(fir);
        output = input.convolve(tmpSgn);
    }
}
