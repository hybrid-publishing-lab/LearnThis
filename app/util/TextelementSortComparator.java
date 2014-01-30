package util;

import java.util.Comparator;

import models.Textelement;

public class TextelementSortComparator implements Comparator<Textelement>{
    
    @Override
    public int compare(Textelement o1, Textelement o2) {
        if (o1.sort == null && o2.sort == null) {
            return 0;
          }
          if (o2.sort == null) {
            return 1;
          }
          if (o1.sort == null) {
            return -1;
          }
          return o2.sort.compareTo(o1.sort);
    }

}
