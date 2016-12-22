import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class SuddenEventHandler
{
    private HashMap<Integer, SuddenEvent> events = new HashMap<>();
    public SuddenEventHandler(String filePath)
    {
        try
        {
            File eventFile = new File(getClass().getResource(filePath).toURI());
            try(Scanner eventScanner = new Scanner(eventFile))
            {
                while(eventScanner.hasNextLine())
                    events.put(eventScanner.nextInt(), SuddenEvent.valueOf(eventScanner.next().toUpperCase()));
            }
            catch(FileNotFoundException e)
            {
                System.err.println("[錯誤]在"+filePath+"下找不到檔案，忽略檔案");
            }
            catch(IllegalArgumentException | NullPointerException e)
            {
                System.err.println("[錯誤]"+filePath+"的檔案格式錯誤，忽略檔案");
            }
        }
        catch(URISyntaxException e)
        {
            e.printStackTrace();
        }
    }
    public boolean hasEvent(int turn)
    {
        return events.containsKey(turn);
    }
    public void dealWithEvent(int turn, ArrayList<Mage> players)
    {
        if(!hasEvent(turn))
            return;
        System.out.println("發生"+events.get(turn)+"事件！");
        switch(events.get(turn))
        {
            case HIPPOGRIF:
                System.out.println("從鷹馬馬背上摔下來，雙方體力減少15！");
                for(Mage mage:players)
                {
                    mage.staminaChange(-15);
                }
                break;
            case BERTIEBEANS:
                System.out.println("吃到鼻涕口味的柏蒂全口味豆，雙方玩家智力減少5！");
                for(Mage mage:players)
                {
                    mage.intelligenceChange(-5);
                }
                break;
            case FEAST:
                System.out.println("開學宴會，雙方體力增加30！");
                for(Mage mage:players)
                {
                    mage.staminaChange(30);
                }
                break;
            case DEMENTOR:
                System.out.println("催狂魔來襲，需要護法的協助！");
                for(Mage mage:players)
                {
                    mage.patronusDefense();
                    if(!mage.isInStatus(SpecialEffect.PATRONUS))
                    {
                        System.out.println("["+mage.getName()+"]"+"沒有護法在旁，直接被吸取靈魂而亡！");
                        mage.instantDeath();
                    }
                    else
                        System.out.println("["+mage.getName()+"]"+"藉助護法，成功抵抗來自催狂魔的攻擊！");
                }
                break;
            default:
                break;
        }
    }
}
