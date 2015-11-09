/*
 * Copyright (C) 2015 Lefteris Paraskevas
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
package edmodule.utils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author  Lefteris Paraskevas
 * @version 2015.10.29_1931_planet1
 */
public class Dataset {
    
    public int corpusSize;
    public ArrayList<String> dataset = new ArrayList<>();
    public ArrayList<String> stopWords = new ArrayList<>();
    public HashMap<String, Integer> termFrequencies = new HashMap<>();
    
    /**
     * 
     */
    public void updateTermFrequenciy() {
        ///
    }
    
    public Short[] getDocumentsTermFrequency(int term) {
        return null;
    }
    
    public Integer[] getNumberOfDocuments() {
        return null;
    }
}