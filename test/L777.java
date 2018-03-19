/**
 * tst
 *
 * @auther CalmLake
 * @create 2018/3/16  11:26
 */
public class L777 {

    public static void main(String[] args){
        try {
            Class c=Class.forName("com.thief.wcs.communication.msg.consumer.MsgConsumer30");

                    System.out.println(c.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
