import java.util.HashMap;
import java.util.Map;

class SpellExp
{
    private int exp;
    public SpellExp(int exp)
    {
        this.exp = exp;
    }
    public void addExp(int expToBeAdded)
    {
        exp += expToBeAdded;
        if(exp > 100)
            exp = 100;
    }
    public int getValue()
    {
        return exp;
    }
}
class StatusTurn
{
    private int turn;
    public StatusTurn(int turn)
    {
        this.turn = turn;
    }
    public boolean turnDecrement()
    {
        return (0 < --turn);
    }
    public int getTurn()
    {
        return turn;
    }
}
public class Mage
{
    private final HashMap<Spell, SpellExp> atkSpells = new HashMap<>();
    private final HashMap<Spell, SpellExp> defSpells = new HashMap<>();
    private final HashMap<Spell, SpellExp> movSpells = new HashMap<>();
    private final HashMap<SpecialEffect, StatusTurn> status = new HashMap<>();
    private final String NAME;
    private final int STAMINA_MAX, MA_PERCENT;
    private Spell learnTemp;
    private int intelligence, stamina, potionAmount;
    private int beDamaged, reduceDamage;
    public Mage(String name, Hogwarts college)
    {
        NAME = name;
        STAMINA_MAX = college.getMaxStamina();
        MA_PERCENT = college.percentOfMA();
        intelligence = college.getInt();
        stamina = STAMINA_MAX;
        potionAmount = 5;
        learnTemp = null;
    }
    public Injury attack(Spell spell)
    {
        Injury injury;
        int staminaCost = 10;
        switch(spell.getType())
        {
            case ATTACK:
            {
                int damage = (int)Math.floor(spell.getBasicNumber()*(atkSpells.get(spell).getValue()*0.01)*(1+MA_PERCENT*0.01));
                injury = new Injury(damage, spell.getSpecialEffect());
                break;
            }
            case MOVE:
            {
                injury = new Injury(0, spell.getSpecialEffect());
                staminaCost += spell.getExtraCost();
                break;
            }
            case DEFENSE:
            default:
            {
                injury = Injury.noInjury();
                break;
            }
        }
        stamina -= staminaCost;
        return injury;
    }
    public boolean learnSpell(Spell spell)
    {
        learnTemp = null;
        int staminaCost = 10;
        if(!spell.validToLearn(intelligence))
        {
            System.out.println("["+NAME+"]沒有滿足精進這個技能的前置條件呢，換個行動吧");
            return false;
        }
        switch(spell.getType())
        {
            case ATTACK:
            {
                if(atkSpells.containsKey(spell))
                {
                    if(atkSpells.get(spell).getValue() >= 100)
                    {
                        System.out.println("["+NAME+"]完全熟練的技能，沒有再度學習的必要了呢，換個行動吧");
                        return false;
                    }
                    atkSpells.get(spell).addExp(spell.getLearnExp());
                }
                else
                    learnTemp = spell;
                break;
            }
            case DEFENSE:
            {
                if(defSpells.containsKey(spell))
                {
                    if(defSpells.get(spell).getValue() >= 100)
                    {
                        System.out.println("["+NAME+"]完全熟練的技能，沒有再度學習的必要了呢，換個行動吧");
                        return false;
                    }
                    defSpells.get(spell).addExp(spell.getLearnExp());
                }
                else
                    learnTemp = spell;
                break;
            }
            case MOVE:
            {
                if(movSpells.containsKey(spell))
                {
                    if(movSpells.get(spell).getValue() >= 100)
                    {
                        System.out.println("["+NAME+"]完全熟練的技能，沒有再度學習的必要了呢，換個行動吧");
                        return false;
                    }
                    movSpells.get(spell).addExp(spell.getLearnExp());
                }
                else
                    learnTemp = spell;
                break;
            }
            default:
                break;
        }
        intelligence += spell.getLearnInt();
        stamina -= staminaCost;
        System.out.println("["+NAME+"]成功精進了 " + spell + " 的使用技巧！");
        return true;
    }
    public boolean usePotion()
    {
        if(potionAmount <= 0)
            return false;
        potionAmount--;
        stamina += 50;
        return true;
    }
    public void beCasted(Injury injury)
    {
        beDamaged = injury.getDamage();
        autoCastDefense();
        stamina -= (beDamaged - reduceDamage);
        if(!injury.getEffect().equals(SpecialEffect.NONE) && !status.containsKey(injury.getEffect()))
            status.put(injury.getEffect(), new StatusTurn(injury.getEffect().getNeededTurn()));
    }
    public void autoCastDefense()
    {
        if(status.containsKey(SpecialEffect.DISARMED) || status.containsKey(SpecialEffect.PETRIFIED))
            return;
        reduceDamage = 0;
        for(Map.Entry<Spell, SpellExp> spell : defSpells.entrySet())
        {
            int temp = (int)(Math.floor((spell.getKey().getBasicNumber()*0.01)*(1+spell.getValue().getValue()*0.01)*beDamaged));
            if(temp > reduceDamage)
                reduceDamage = temp;
            SpecialEffect spellSE = spell.getKey().getSpecialEffect();
            if(!(spellSE.equals(SpecialEffect.NONE)) && !(status.containsKey(spellSE)))
                status.put(spellSE, new StatusTurn(spellSE.getNeededTurn()));
        }
    }
    public boolean turnEnd(Injury injury)
    {
        if(injury.isInjuryExisted())
            beCasted(injury);
        for(Map.Entry<SpecialEffect, StatusTurn> stat : status.entrySet())
        {
            if(stat.getValue().getTurn() <= 0)
                status.remove(stat.getKey());
        }
        System.out.println(this);
        for(Map.Entry<SpecialEffect, StatusTurn> stat : status.entrySet())
            stat.getValue().turnDecrement();
        if(learnTemp != null)
        {
            switch(learnTemp.getType())
            {
                case ATTACK:
                    atkSpells.put(learnTemp, new SpellExp(learnTemp.getInitialExp()));
                    break;
                case DEFENSE:
                    defSpells.put(learnTemp, new SpellExp(learnTemp.getInitialExp()));
                    break;
                case MOVE:
                    movSpells.put(learnTemp, new SpellExp(learnTemp.getInitialExp()));
                    break;
                default:
                    break;
            }
        }
        if(stamina > STAMINA_MAX)
            stamina = STAMINA_MAX;
        if(stamina <= 0)
            return true;
        else
            return false;
    }
    @Override
    public String toString()
    {
        StringBuilder statusString = new StringBuilder();
        statusString.append("[").append(NAME).append("]").append(System.lineSeparator());
        statusString.append("   體力：").append(stamina).append("/").append(STAMINA_MAX).append(System.lineSeparator());
        statusString.append("   智力：").append(intelligence).append(System.lineSeparator());
        statusString.append("   狀態：");
        if(status.isEmpty())
            statusString.append(SpecialEffect.NONE);
        else
        {
            for(Map.Entry<SpecialEffect, StatusTurn> stat : status.entrySet())
            {
                statusString.append(stat.getKey()).append("[").append(stat.getValue().getTurn()).append("回合] ");
            }
        }
        return statusString.toString();
    }
    public boolean isInStatus(SpecialEffect effect)
    {
        return status.containsKey(effect);
    }
    public boolean isTired()
    {
        return (stamina <= 10);
    }
    public String getName()
    {
        return NAME;
    }
    public int getStamina()
    {
        return stamina;
    }
    public int getPotionAmount()
    {
        return potionAmount;
    }
}
