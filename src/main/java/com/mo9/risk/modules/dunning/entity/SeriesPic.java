package com.mo9.risk.modules.dunning.entity;

import java.awt.*;
import java.io.Serializable;

/**
 * Created by cyuan on 2017/12/20.
 */
public class SeriesPic implements Serializable ,Comparable{


    private static final long serialVersionUID = -4457695859691548691L;
    private Color color;
    private Double value;

    public SeriesPic(Color color, Double value) {
        this.color = color;
        this.value = value;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public int compareTo(Object o) {
        if(this == o){
            return 0;
        }
        if(!(o instanceof SeriesPic)){
            throw new ClassCastException("类型错误!");
        }
        SeriesPic o1 = (SeriesPic) o;
        int i = o1.getValue().compareTo(this.getValue());
        return i;
    }
}
