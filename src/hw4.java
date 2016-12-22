import java.util.ArrayList;
import java.util.Scanner;

public class hw4
{
    private static ArrayList<Mage> players;
    private static ArrayList<Injury> injury;

    public static void printHelp()
    {
        System.out.println("可使用指令：");
        System.out.println("  attack [咒語]：以[咒語]攻擊");
        System.out.println("  learn [咒語]：學習[咒語]");
        System.out.println("  recover：使用魔藥回復");
        System.out.println("  pass：跳過此回合");
        System.out.println("  game over：結束遊戲");
        System.out.print("咒語列表：");
        int lineSpace=0;
        for(Spell spell : Spell.values())
        {
            if(lineSpace == 0)
            {
                lineSpace = 3;
                System.out.print("\n  ");
            }
            System.out.print("[" + spell + "]");
            lineSpace--;
        }
        System.out.println();
    }
    private static boolean takeAction(Mage mage, String[] commands)
    {
        boolean isValid;
        String arg = "";
        for(int i=1; i<commands.length; i++)
        {
            if(i!=1)
                arg += "_";
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
                        isValid = mage.learnSpellCheck(Spell.valueOf(arg.toUpperCase()));
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
            case "pass":
                isValid = true;
                injury.add(Injury.noInjury());
                break;
            case "help":
                printHelp();
                isValid = false;
                break;
            default:
                isValid = false;
                System.out.println("["+mage.getName()+"]找不到你輸入的指令呢，請確認拼字重新輸入試試看喔");
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
        SuddenEventHandler events = new SuddenEventHandler("/text/event.txt");
        System.out.println("歡迎各位參加巫師鬥法大賽～");
        System.out.println("本比賽由以下四個可參賽的學院贊助：");
        System.out.print("  ");
        for(Hogwarts college : Hogwarts.values())
            System.out.print("[ " + college + " ] ");
        System.out.println();
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
                    if(scanner.hasNextLine())
                        scanner.nextLine();
                }
            }while(error);
        }
        if(scanner.hasNextLine())
            scanner.nextLine();
        int nowTurn = 0;
        boolean gameEnd = false;
        //做出分隔線字串，以取代char陣列中的null為"="的方式實現
        String separateLine = new String(new char[79]).replace("\0", "=");
        
        printHelp();
        Game:
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
                        break Game;
                    }
                    else
                    {
                        String commands[] = command.split(" ");
                        isValid = takeAction(player, commands);
                    }
                }while(!isValid);
            }
            if(events.hasEvent(nowTurn))
                events.dealWithEvent(nowTurn, players);
            System.out.println(separateLine);
            for(int i=0; i < playerNum; i++)
            {
                int injurySource = (i+1<playerNum)?i+1:0;
                gameEnd |= players.get(i).turnEnd(injury.get(injurySource));
                System.out.println(separateLine);
            }
        }
        //檢查勝利者
        System.out.print("勝利者為：");
        //先假定勝利者，再來逐一比對
        Mage winner = players.get(0);
        //設立一變數表示體力值相等的參賽者，是為了讓以後能方便增加玩家人數才這樣寫的（茶。當此值與玩家人數相等帶所有人平手，除此之外此變數無意義
        int equalPlayer = 0;
        for(Mage player : players)
        {
            if(player.getStamina() > winner.getStamina())
                winner = player;
            else if(player.getStamina() == winner.getStamina())
                equalPlayer++;
        }
        if(equalPlayer == players.size())
            System.out.println("從缺，兩人平手！");
        else
            System.out.println(winner.getName()+"！");
        System.out.println("感謝各位參加巫師鬥法大賽，期待您們下次的大展身手");
        scanner.close();
    }
}
