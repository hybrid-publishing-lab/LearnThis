package util;

import java.util.Comparator;

import models.Card;

public class CardSortComparator implements Comparator<Card>{
    
    @Override
    public int compare(Card o1, Card o2) {
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
