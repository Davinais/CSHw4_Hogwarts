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
                for(Mage mage:players)
                {
                    mage.staminaChange(-15);
                }
                break;
            case BERTIEBEANS:
                for(Mage mage:players)
                {
                    mage.intelligenceChange(-5);
                }
                break;
            case FEAST:
                for(Mage mage:players)
                {
                    mage.staminaChange(30);
                }
                break;
            case DEMENTOR:
                for(Mage mage:players)
                {
                    if(!mage.isInStatus(SpecialEffect.PATRONUS))
                        mage.instantDeath();
                }
                break;
            default:
                break;
        }
    }
}
