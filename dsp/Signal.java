/*
Dascalescu Andreea-Cristina
Homework nr. 2 - December 2016
*/

package dsp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Signal {
    double[] values;
    
    public Signal(){
        
    }
    
    public Signal(double[] smlValues){
        values = Arrays.copyOf(smlValues, smlValues.length);
    }
    //The following constructore get as argument the path for the already existing
    //file in order to read all data and add them in "values" field of the 
    //Signal object.
    public Signal(String splFile) throws FileNotFoundException{
        File newFile = new File(splFile);
        Scanner ns = new Scanner(newFile);
        int counter = 0;
        while (ns.hasNextDouble()){
            counter++;
            ns.nextDouble();
        }
        
        double[] tmpArr = new double[counter];
        Scanner s = new Scanner(newFile);
        
        for(int i = 0; i < tmpArr.length; i++){
            tmpArr[i] = s.nextDouble();
        }
        
        values = Arrays.copyOf(tmpArr, tmpArr.length);
    }
    //Using Arrays.copyOf method, I only copy all the data and ensure a minimum
    //length given also as argument for the variable where I copy all the values
    public void copy(Signal sgn){
        Signal tmpSgn = new Signal();
        tmpSgn.values = Arrays.copyOf(sgn.values, sgn.values.length);
        System.out.println("Copy of argument signal: " + tmpSgn.toString());
    }
    //For add method, it is verified the lengths for the 2 Signal objects, and
    //after that it is applied the liniar superposition method.
    public Signal add(Signal sgn){
        Signal tmpSgn = new Signal();
        Signal convSgn = new Signal();
        
        if(getSignalLength() > sgn.getSignalLength()){
            tmpSgn.values = new double[getSignalLength()];
            for(int i = 0; i < tmpSgn.getSignalLength(); i++){
                tmpSgn.values[i] = 0;
            }
            tmpSgn.values = Arrays.copyOf(sgn.values, getSignalLength());
            convSgn.values = new double[getSignalLength()];
            convSgn.values = initArr(convSgn.values);
            for(int i = 0, j = 0; i < getSignalLength(); i++, j++){
                convSgn.values[i] = values[i] + tmpSgn.values[j];
            }
        } else if(getSignalLength() < sgn.getSignalLength()){
            tmpSgn.values = new double[sgn.getSignalLength()];
            for(int i = 0; i < tmpSgn.getSignalLength(); i++){
                tmpSgn.values[i] = 0;
            }
            tmpSgn.values = Arrays.copyOf(values, sgn.getSignalLength());
            convSgn.values = new double[sgn.getSignalLength()];
            convSgn.values = initArr(convSgn.values);
            for(int i = 0, j = 0; i < sgn.getSignalLength(); i++, j++){
                convSgn.values[i] = tmpSgn.values[i] + sgn.values[j];
            }
        } else {
            tmpSgn.values = new double[getSignalLength()];
            tmpSgn.values = initArr(tmpSgn.values);
            tmpSgn.values = Arrays.copyOf(values, getSignalLength());
            convSgn.values = new double[getSignalLength()];
            convSgn.values = initArr(convSgn.values);
            for(int i = 0, j = 0; i < getSignalLength(); i++, j++){
                convSgn.values[i] = tmpSgn.values[i] + sgn.values[j];
            }
        }
        return convSgn;
    }
    
    public Signal scale(double gain){
        Signal tmpSgn = new Signal();
        tmpSgn.values = Arrays.copyOf(values, getSignalLength());
        for(int i = 0; i < tmpSgn.getSignalLength(); i++){
            tmpSgn.values[i] = tmpSgn.values[i] * gain;
        }
        return tmpSgn;
    }
    //In this piece of code, System.arraycopy is used for extracting a part of an
    //array values from a given start, and copy them in another array, also from a
    //given start index and a finish index.
    public Signal delay(int delay){
        Signal tmpSgn = new Signal();
        tmpSgn.values = new double[getSignalLength()];
        tmpSgn.values = initArr(tmpSgn.values);
        
        if (delay > 0){
            for(int i = delay, j = 0; i < tmpSgn.getSignalLength(); i++, j++){
                tmpSgn.values[i] = values[j];
            }
        } else if (delay < 0){
            System.arraycopy(values, 0 - delay, tmpSgn.values, 0, values.length + delay);
        } else {
            tmpSgn.values = Arrays.copyOf(values, values.length);
        }
        return tmpSgn;
    }
    
    public Signal convolve(Signal sgn){
        Signal tmpSgn = new Signal();
        Signal convSgn = new Signal();
        
        if(getSignalLength() > sgn.getSignalLength()){
            tmpSgn.values = new double[getSignalLength()];
            for(int i = 0; i < tmpSgn.getSignalLength(); i++){
                tmpSgn.values[i] = 0;
            }
            tmpSgn.values = Arrays.copyOf(sgn.values, getSignalLength());
            convSgn.values = new double[getSignalLength()];
            convSgn.values = initArr(convSgn.values);
            for(int i = 0; i < getSignalLength(); i++){
                convSgn.values[i] = calcElem(i, values, tmpSgn.values);
            }
        } else if(getSignalLength() < sgn.getSignalLength()){
            tmpSgn.values = new double[sgn.getSignalLength()];
            for(int i = 0; i < tmpSgn.getSignalLength(); i++){
                tmpSgn.values[i] = 0;
            }
            tmpSgn.values = Arrays.copyOf(values, sgn.getSignalLength());
            convSgn.values = new double[sgn.getSignalLength()];
            convSgn.values = initArr(convSgn.values);
            for(int i = 0; i < sgn.getSignalLength(); i++){
                convSgn.values[i] = calcElem(i, tmpSgn.values, sgn.values);
            }
        } else {
            tmpSgn.values = new double[getSignalLength()];
            tmpSgn.values = initArr(tmpSgn.values);
            tmpSgn.values = Arrays.copyOf(sgn.values, getSignalLength());
            convSgn.values = new double[getSignalLength()];
            convSgn.values = initArr(convSgn.values);
            for(int i = 0; i < getSignalLength(); i++){
                convSgn.values[i] = calcElem(i, tmpSgn.values, values);
            }
        }
        return convSgn;
    }
    //All the additional methods created were done in order to not overload the
    //code of the main public methods, and because they are protected, they can be
    //accessed only in this class.
    protected double[] initArr(double[] arr){
        for (int i = 0; i < arr.length; i++){
            arr[i] = 0;
        }
        return arr;
    }
    
    protected int getSignalLength(){
        return values.length;
    }
    
    protected double getSample(int i){
        return values[i];
    }
    
    protected double calcElem(int j, double[] samples1, double[] samples2){
        double s = 0;
        
        for(int i = 0; i <= j; i++){
            s += samples2[i]*samples1[j-i];
        }
        
        return s;
    }
    
    @Override
    public String toString(){
        return Arrays.toString(values);
    }
    
    public void save(String pathName) throws IOException{
        BufferedWriter output = null;
        try{
            File file = new File(pathName);
            output = new BufferedWriter(new FileWriter(file));
            for(int i = 0; i < values.length; i++){
                output.write(String.valueOf(values[i]) + " ");
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }
}
