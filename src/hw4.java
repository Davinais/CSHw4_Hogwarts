import java.util.ArrayList;
import java.util.Scanner;

public class hw4
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        int playerNum = 2;
        ArrayList<Mage> players = new ArrayList<>();
        for(int i=0; i<playerNum; i++)
        {
            boolean error = false;
            do
            {
                error = false;
                System.out.println("[玩家"+(i+1)+"]請輸入名字以及想加入的學院，以空格隔開");
                String name = scanner.next();
                String collegeName = scanner.next().toUpperCase();
                Hogwarts college;
                try
                {
                    college = Hogwarts.valueOf(collegeName);
                    players.add(new Mage(name, college));
                }
                catch(IllegalArgumentException | NullPointerException e)
                {
                    System.out.println("[玩家"+(i+1)+"]看起來找不到你想讀的學院呢…，請確認拼字重新輸入試試看喔");
                    error = true;
                }
            }while(error);
        }
        scanner.close();
    }
}
