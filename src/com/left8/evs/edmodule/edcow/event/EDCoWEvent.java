/* 
 * Copyright (C) 2016 Adrien Guille <adrien.guille@univ-lyon2.fr>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.left8.evs.edmodule.edcow.event;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.io.Serializable;

/**
 *
 * @author  Adrien GUILLE, Laboratoire ERIC, Université Lumière Lyon 2
 * @author  Lefteris Paraskevas
 * @version 2016.03.27_2349
 */
public class EDCoWEvent implements Serializable {
    private SimpleStringProperty textualDescription;
    private SimpleStringProperty temporalDescription;
    private SimpleStringProperty physicalDescription;
    private SimpleDoubleProperty score;

    public EDCoWEvent(String text, String temp, String physical) {
        this(text, temp, physical, 0);
    }

    public EDCoWEvent(String text, String temp, String physical, double score) {
        textualDescription = new SimpleStringProperty(text);
        temporalDescription = new SimpleStringProperty(temp);
        physicalDescription = new SimpleStringProperty(physical);
        this.score = new SimpleDoubleProperty(score);
    }

    public String getTextualDescription() {
        return textualDescription.get();
    }

    public String getTemporalDescription() {
        return temporalDescription.get();
    }
    
    public String getPhysicalDescription() {
        return physicalDescription.get();
    }
    
    public String getTemporalDescriptionLowerBound() {
        return temporalDescription.getValue().split(",")[0];
    }
    
    public String getTemporalDescriptionUpperBound() {
        return temporalDescription.getValue().split(",")[1];
    }

    public void setTextualDescription(String newText) {
        textualDescription.set(newText);
    }

    public void setTemporalDescription(String newTemp) {
        temporalDescription.set(newTemp);
    }

    public double getScore() {
        return score.getValue();
    }

    public void setScore(double score) {
        this.score.set(score);
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeObject(textualDescription.get());
        out.writeObject(temporalDescription.get());
        out.writeDouble(score.get());
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException {
        try {
            textualDescription = new SimpleStringProperty((String)in.readObject());
            temporalDescription = new SimpleStringProperty((String)in.readObject());
            score = new SimpleDoubleProperty(in.readDouble());
        } catch (ClassNotFoundException ignored) {
            throw new IOException(ignored);
        }
    }
}