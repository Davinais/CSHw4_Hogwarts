import java.util.ArrayList;
import java.util.Scanner;

public class hw4
{
    private static ArrayList<Mage> players;
    private static ArrayList<Injury> injury;
    private static boolean takeAction(Mage mage, String[] commands)
    {
        boolean isValid;
        String arg = "";
        for(int i=1; i<commands.length; i++)
        {
            arg += commands[i];
        }
        switch(commands[0])
        {
            case "attack":
                if(mage.isTired())
                {
                    isValid = false;
                    System.out.println("["+mage.getName()+"]體力…好像有點不太夠呢…，換個行動吧");
                }
                else if(mage.isInStatus(SpecialEffect.DISARMED))
                {
                    isValid = false;
                    System.out.println("["+mage.getName()+"]魔杖不在手，根本沒辦法攻擊呢…換個行動吧");
                }
                else
                {
                    try
                    {
                        injury.add(mage.attack(Spell.valueOf(arg.toUpperCase())));
                        isValid = true;
                    }
                    catch(IllegalArgumentException | NullPointerException e)
                    {
                        System.out.println("["+mage.getName()+"]看起來找不到這個咒語呢…，請確認拼字重新輸入試試看喔");
                        isValid = false;
                    }
                }
                break;
            case "learn":
                if(mage.isTired())
                {
                    isValid = false;
                    System.out.println("["+mage.getName()+"]體力…好像有點不太夠呢…，換個行動吧");
                }
                else
                {
                    try
                    {
                        isValid = mage.learnSpell(Spell.valueOf(arg.toUpperCase()));
                        if(isValid)
                            injury.add(Injury.noInjury());
                    }
                    catch(IllegalArgumentException | NullPointerException e)
                    {
                        System.out.println("["+mage.getName()+"]看起來找不到這個咒語呢…，請確認拼字重新輸入試試看喔");
                        isValid = false;
                    }
                }
                break;
            case "recover":
                isValid = mage.usePotion();
                if(!isValid)
                    System.out.println("["+mage.getName()+"]魔藥數量不足所以無法使用呢…，換個行動吧。是說遊戲結束後可以去香霖堂找找看喔");
                else
                    injury.add(Injury.noInjury());
                break;
            default:
                isValid = false;
                break;
        }
        return isValid;
    }
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        int playerNum = 2;
        players = new ArrayList<>();
        injury = new ArrayList<>();
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
        if(scanner.hasNextLine())
            scanner.nextLine();
        int nowTurn = 0;
        boolean gameEnd = false;
        while(!gameEnd)
        {
            System.out.println("[回合"+ (++nowTurn) +"]");
            injury.clear();
            for(Mage player : players)
            {
                if(player.isInStatus(SpecialEffect.PETRIFIED))
                {
                    System.out.println("["+player.getName()+"]完全無法動彈呢…，只好任對方宰割了");
                    continue;
                }
                boolean isValid = false;
                do
                {
                    System.out.println("["+player.getName()+"]請輸入想要執行的動作");
                    String command = scanner.nextLine();
                    if(command.equals("game over"))
                    {
                        gameEnd = true;
                    }
                    else
                    {
                        String commands[] = command.split(" ");
                        isValid = takeAction(player, commands);
                    }
                }while(!isValid);
            }
            for(int i=0; i < playerNum; i++)
            {
                int injurySource = (i+1<playerNum)?i+1:0;
                gameEnd |= players.get(i).turnEnd(injury.get(injurySource));
            }
        }
        scanner.close();
    }
}
