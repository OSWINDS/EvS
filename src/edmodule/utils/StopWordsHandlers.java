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

import preprocessingmodule.language.LanguageCodes;
import preprocessingmodule.nlp.stopwords.StopWords;

/**
 *
 * @author  Lefteris Paraskevas
 * @version 2015.11.25_2357_planet2
 */
public class StopWordsHandlers {
    
    private static StopWords swEn; //Engish
    private static StopWords swFr; //French
    private static StopWords swIt; //Italian
    private static StopWords swEs; //Spanish
    private static StopWords swDe; //German
    private static StopWords swGr; //Greek
    private static StopWords swFa; //Persian
    private static StopWords swAr; //Arabic
    
    /**
     * Initialize stopwords handlers.
     */
    public final static void initStopWordsHandlers() {
        swAr.loadStopWords(LanguageCodes.ar);
        swDe.loadStopWords(LanguageCodes.de);
        swEn.loadStopWords(LanguageCodes.en);
        swEs.loadStopWords(LanguageCodes.es);
        swFa.loadStopWords(LanguageCodes.fa);
        swFr.loadStopWords(LanguageCodes.fr);
        swGr.loadStopWords(LanguageCodes.gr);
        swIt.loadStopWords(LanguageCodes.it);
    }
    
    /**
     * Returns a stopwords handler for the appropriate language.
     * @param isoCode The ISO code of the language.
     * @return A StopWords handler.
     */
    public static StopWords getSWHandlerAccordingToLanguage(LanguageCodes isoCode) {
        if(isoCode.equals(LanguageCodes.ar)) {
            return swAr;
        } else if(isoCode.equals(LanguageCodes.de)) {
            return swDe;
        } else if(isoCode.equals(LanguageCodes.en)) {
            return swEn;
        } else if(isoCode.equals(LanguageCodes.es)) {
            return swEs;
        } else if(isoCode.equals(LanguageCodes.fa)) {
            return swFa;
        } else if(isoCode.equals(LanguageCodes.fr)) {
            return swFr;
        } else if(isoCode.equals(LanguageCodes.gr)) {
            return swGr;
        } else if(isoCode.equals(LanguageCodes.it)) {
            return swIt;
        } else {
            return swEn; //Fall back for unknown languages and english tweets
        }
    }
}
