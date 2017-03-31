package TaskClass;

import java.util.Comparator;

/**
 * Created by Administrator on 2016-03-10.
 */
public class BoxCombyOrder implements Comparator<Box> {
    @Override
    public int compare(Box box, Box t1) {

        String Boxorder1 = box.getBoxOrder();
        String Boxorder2 = t1.getBoxOrder();


        if (Boxorder1 == null || Boxorder1.equals("")){
            Boxorder1 = "0";
        }
        if (Boxorder2 == null || Boxorder2.equals("")){
            Boxorder2 = "0";
        }

        Integer o1 =Integer.parseInt(Boxorder1) ;
        Integer o2 = Integer.parseInt(Boxorder2);

        return o1.compareTo(o2);
    }
}
