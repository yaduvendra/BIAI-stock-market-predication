/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package przemek.BIAI.projekt.neuroph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/**
 * Contain teh arrayList of SingleData and additional parameters. Represents the
 * specified data set, which can by use as an training or test set
 *
 * @author Przemek
 */
class MyDataSet {

    /**
     * Number of neural network inputs Konkretnie mamy kilka obiektów
     * SingleData. Watrtość ta mówi z ilu obiektów czytać dane ,które mają
     * stanowić pojedynczy zestaw dla wejścia sieci
     */
    int dataInputNumber;
    /**
     * Amount of values which are fetched from SingleData object to act as an
     * network's inputs Konkretnie znamy już ile obiektów SingleData ma stanowić
     * pojedyncze wejście dla sieci. Wartość tej zmiennej mówi nam ile wartości
     * wyciągnąć z obiektu singleData, będą one stanowić wejścia sieci. Ilość
     * wartości stanowiących pojedynczy zestaw wejść dla sieci z danego obiektu
     * MyDataSet = dataInputNumber * dataInputValuesNumber
     */
    int dataInputValuesNumber;

    /**
     * Number of neural network outputs , działają identycznie jak inputy
     */
    int dataOutputNumber;
    /**
     * Identycznie jak dla input
     */
    int dataOutputValuesNumber;

    /**
     * Length of SingleData [] values
     */
    int maxValuesNumber;

    /**
     * dataInputNumber + dataOuputNumber
     */
    int dataPortionNumber;
    /**
     * Maximum test or train elements which can be created by this dataSet
     * Maksymalna ilość zestawów danych, jakie jesteśmy wstanie utworzyć Maybe
     * it should be named maxTrainingElementNumber
     */
    int maxDataPortionNumber;

    double normalizationFactor;
    double[] maximumValues;

    /**
     * It represents the distance beetwen the data which sould be taken as an
     * input and data which sould be taken as an output. It should be minimum 0
     * to dont take the same value for input and output. If its zero the next
     * data after all datas taken as an input will be taken as an output
     */
    int inOutDataDistance;

    /**
     * Obiekty wczytywane tutaj musza miec identyczny rozmiar tablicy values[] -
     * czyli kazdy musi miec tyle samo dostepnych wartosci
     */
    ArrayList<SingleData> data;
    /**
     * signelDataValues.values descriptor
     */
    String[] singleDataValuesDescriptor;
    /**
     * MyDataSet descriptor
     */
    String descriptor;

    /**
     * tablice wskazujace, ktore wartosci z signleData.values maja być
     * wciągnięte do wejsc sieci
     */
    Integer[] inValuesIndicator;
    Integer[] outValuesIndicator;

    boolean isNormalization = false;

    /**
     *
     * @param dataSet
     */
    public MyDataSet(MyDataSet dataSet) {
        data = new ArrayList<SingleData>();
        this.dataInputNumber = dataSet.dataInputNumber;
        this.dataOutputNumber = dataSet.dataOutputNumber;
        this.dataPortionNumber = dataSet.dataPortionNumber;
        this.maxDataPortionNumber = dataSet.maxDataPortionNumber;
        this.normalizationFactor = dataSet.normalizationFactor;
        this.dataInputValuesNumber = dataSet.dataInputValuesNumber;
        this.dataOutputValuesNumber = dataSet.dataOutputValuesNumber;
        this.maxValuesNumber = dataSet.maxValuesNumber;
        this.descriptor = dataSet.descriptor;
        this.singleDataValuesDescriptor = dataSet.singleDataValuesDescriptor.clone();
        this.isNormalization = dataSet.isNormalization;
        this.inOutDataDistance = dataSet.inOutDataDistance;
        if (dataSet.inValuesIndicator != null) {
            this.inValuesIndicator = dataSet.inValuesIndicator.clone();
        }
        if (dataSet.maximumValues != null) {
            this.maximumValues = dataSet.maximumValues.clone();
        }
        if (dataSet.outValuesIndicator != null) {
            this.outValuesIndicator = dataSet.outValuesIndicator.clone();
        }

        for (SingleData singleData : dataSet.data) {
            SingleData newSingleData = new SingleData(singleData);
            data.add(newSingleData);
        }
    }

    public MyDataSet() {
        data = new ArrayList<SingleData>();

    }

    public void setInOutNumbers(int inNumber, Integer[] inValues, int outNumber, Integer[] outValues, int inOutDataDist) {
        try {

            if (inNumber <= 0 || inValues == null || outNumber <= 0 || outValues == null || inOutDataDist < 0) {
                throw new Exception("[MyDataSet,setInOutNumbers] Bad parametrs");
            }
            dataInputNumber = inNumber;
            dataOutputNumber = outNumber;
            inValuesIndicator = inValues.clone();
            outValuesIndicator = outValues.clone();
            dataInputValuesNumber = inValuesIndicator.length;
            dataOutputValuesNumber = outValuesIndicator.length;
            inOutDataDistance = inOutDataDist;

            dataPortionNumber = dataInputNumber + dataOutputNumber;
            maxDataPortionNumber = data.size() - dataPortionNumber - inOutDataDistance + 1;
            calculateMaxValuesNumber();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    
    /**
     * Sprawdzamy na ktorej pozycji w data secie znajduje sie single data o podanej jako parametr dacie 
     * @param date
     * @return
     * @throws Exception 
     */
    
     public Integer calculateStartingElement( String date) throws Exception {
        Integer i = 0;
        for (SingleData sd : data) {
            if (sd.date.equals(date)) {
                return i;
            }
            i++;
        }
        throw new Exception("[TrainingData,calculateStartindElement] , Recevied date not mach");
    }
    
    /*
    public void setInOutNumbers(int inNumber, int [] inValues) {
        setInOutNumbers(inNumber, inValues, 0, null);
    }
     */
    public int checkValuesIndicator() {
        for (int i : this.inValuesIndicator) {
            if (i > this.maxValuesNumber) {
                return 0;
            }
        }
        for (int i : this.inValuesIndicator) {
            if (i > this.maxValuesNumber) {
                return 0;
            }
        }
        return 1;
    }

    public void calculateMaxValuesNumber() {
        if (data.size() != 0) {
            maxValuesNumber = data.get(0).values.length;
        }
    }

//    public double findMaximumValue() {
//        double max=0;
//        for (SingleData singleData : data) {
//            max = singleData.findMax();
//            if (maximumValue<max) {
//                maximumValue = max;
//            }
//        }
//        return maximumValue;
//    }
    public void findMaximumValues() { // tu mozna wsaszic przed find max values number

        double[] max = new double[this.maxValuesNumber];
        System.out.println("Max initiation" + max[0]);
        for (SingleData singleData : data) {
            for (int i = 0; i < maxValuesNumber; i++) {
                if (singleData.values[i] > max[i]) {
                    max[i] = singleData.values[i];
                }
            }
        }
        maximumValues = max.clone();
    }

    /**
     * The maximum value used for normalization is different for all the values
     * for singleData.values Example for every singleValue from this.data the
     * values[0] refeer to the same maximum value , it means data[0].values[0]
     * refeer to the same maximum value what data[1].values[0] The value from
     * signelData.values() has same maximum value parametr used for
     * normalization if they are on the same position int the table
     *
     */
    public void normalize() {
        if (isNormalization == false) {
            findMaximumValues();
            for (SingleData singleData : data) {
                for (int i = 0; i < singleData.values.length; i++) {
                    singleData.values[i] = singleData.values[i] / maximumValues[i] * 0.8;
                    // System.out.println(singleData.values[i]);
                }
            }
        }
        isNormalization = true;
    }

    public void backNormalize() {
        if (isNormalization == true) {
            for (SingleData singleData : data) {
                for (int i = 0; i < maxValuesNumber; i++) {
                    singleData.values[i] = singleData.values[i] * maximumValues[i] / 0.8;
                    System.out.println(singleData.values[i]);
                }
            }
        }
        isNormalization = false;
    }

//    public void normalize() {
//        findMaximumValue();
//        for (SingleData singleData : data) {
//            for (int i=0;i<maxValuesNumber;i++) {
//                singleData.values[i] = singleData.values[i] / maximumValues[i] * 0.8;
//                System.out.println(singleData.values[i]);
//            }
//        }
//    }
//    public void backNormalization() {
//        for (SingleData singleData : data) {
//            for (int i=0;i<maxValuesNumber;i++) {
//                singleData.values[i] = singleData.values[i] / maximumValues[i] * 0.8;
//                System.out.println(singleData.values[i]);
//            }
//        }
//    }
    public void reverse() {
        Collections.reverse(data);
    }

    @Override
    public String toString() {
        String s = "MyDataSet{" + "dataInputNumber=" + dataInputNumber + ", dataInputValuesNumber=" + dataInputValuesNumber + ", dataOutputNumber=" + dataOutputNumber + ", dataOutputValuesNumber=" + dataOutputValuesNumber + ", maxValuesNumber=" + maxValuesNumber + ", dataPortionNumber=" + dataPortionNumber + ", maxDataPortionNumber=" + maxDataPortionNumber + ", normalizationFactor=" + normalizationFactor + ", maximumValue=" + maximumValues + ", data=" + data + ", descriptor=" + descriptor + ", singleDataValuesDescriptor ";
        for (String s1 : this.singleDataValuesDescriptor) {
            s += (s1 + " ");
        }
        return s;
    }
    
    

}

public class DataSetCollection {

    ArrayList<MyDataSet> dataSets;
    /**
     * Maximum test or train elements which can be created by single MyDataSet,
     * reachable for each MyDataSet of this DataSetCollection It is equal the
     * smallest value of: MyDataSet.maxDataPortionNumber of each MyDataSet of
     * this DataSetCollection
     */
    int maxDataPortionNumber;

    public DataSetCollection() {
        dataSets = new ArrayList<MyDataSet>();
    }

    /**
     * Adds the DataSet with specified parameters which detremines the
     * TrainingData how to handle with this dataSet.
     *
     * @param InputMyDataSet set of Data
     * @param dataInput means how much of single data should act as a single
     * input for the network
     * @param dataOutput means how much of single data should act as a singe
     * output for the network
     *
     */
    public void addDataSet(MyDataSet InputMyDataSet, int dataInputNumber, Integer[] dataInputIndicator, int dataOutputNumber, Integer[] dataOutputIndicator, int inOutDataDistance) throws Exception {

        InputMyDataSet.calculateMaxValuesNumber();

        int dataPortionNumber = dataInputNumber + dataOutputNumber;

        if (dataPortionNumber > InputMyDataSet.data.size()) {
            throw new Exception("[DataSetCollection,addDataSet] dataPortionNumber> MyDataSet.size()");
        } else if (dataInputIndicator.length > InputMyDataSet.maxValuesNumber || dataOutputIndicator.length > InputMyDataSet.maxValuesNumber) {
            throw new Exception("[DataSetCollection,addDataSet] valuesNumber>MyDataSet.maxValuesNumber");
        } else {
            MyDataSet dataSet = new MyDataSet(InputMyDataSet);
            dataSet.setInOutNumbers(dataInputNumber, dataInputIndicator, dataOutputNumber, dataOutputIndicator, inOutDataDistance);
            if (dataSet.checkValuesIndicator() == 1) {
                dataSets.add(dataSet);
            } else {
                throw new Exception("[DataSetCollection,addDataSet] dataSet.checkValuesIndicator error");
            }

        }

    }

    public void normalize() {
        for (MyDataSet dataSet : dataSets) {
            dataSet.normalize();
        }
    }

    public void backNormalization() {
        for (MyDataSet dataSet : dataSets) {
            dataSet.backNormalize();
        }
    }

    public void backNormalizatfionInResult(double[][] result) {
        ArrayList<Double> maximumValues = new ArrayList<Double>();
        for (MyDataSet dataSet : dataSets) {
            System.out.println("Values indicator lenght: " + dataSet.outValuesIndicator.length);
            System.out.println(dataSet.outValuesIndicator[0]);
            System.out.println(dataSet.maximumValues[dataSet.outValuesIndicator[0]]);
            for (int j = 0; j < dataSet.dataOutputValuesNumber; j++) {
                maximumValues.add(dataSet.maximumValues[dataSet.outValuesIndicator[j]]);
            }

        }
        System.out.println("Result set back normalization process");
        
        System.out.println("Result leng " + result.length);
        for (int i = 0; i < result.length; i++) {
            System.out.println("Row: ");
            System.out.println("Result[i] "+ result[i].length);
            int indicator = 0;
            for (int j = 0; j < result[i].length; j++) {

                result[i][j] = result[i][j] * maximumValues.get(indicator) / 0.8;   // tu jest walek przy makximum values jak jest wiecej niz jeden result zwracany, tzn np 1 wiersz resulta stanowi 2 dni
                System.out.print(result[i][j]+" ");
                indicator++;
                if(indicator == maximumValues.size()){
                    indicator = 0;
                }
            }

        }
    }

    public int findMaximumDataPortion() throws Exception {

        if (dataSets.size() == 0) {
            throw new Exception("[DataSetCollection,finMaximumDataPortion] dataSets.size=0");
        }
        int min = dataSets.get(0).maxDataPortionNumber;
        for (MyDataSet dataSet : dataSets) {
            if (dataSet.maxDataPortionNumber < min) {
                min = dataSet.maxDataPortionNumber;
            }
        }
        maxDataPortionNumber = min;
        return maxDataPortionNumber;

    }

    @Override
    public String toString() {
        return "DataSetCollection{" + "dataSets=" + dataSets + '}';
    }

}
