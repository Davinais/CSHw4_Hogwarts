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
    private double reduceDamage;
    private Injury lastinjured;
    public Mage(String name, Hogwarts college)
    {
        NAME = name;
        STAMINA_MAX = college.getMaxStamina();
        MA_PERCENT = college.percentOfMA();
        intelligence = college.getInt();
        stamina = STAMINA_MAX;
        potionAmount = 5;
        learnTemp = null;
        lastinjured = null;
    }
    public Injury attack(Spell spell)
    {
        Injury injury;
        int staminaCost = 10;
        switch(spell.getType())
        {
            case ATTACK:
            {
                double damage = Math.floor(spell.getBasicNumber()*(atkSpells.get(spell).getValue()*0.01)*(1+MA_PERCENT*0.01));
                injury = new Injury(spell, damage, spell.getSpecialEffect());
                break;
            }
            case MOVE:
            {
                injury = new Injury(spell, 0, spell.getSpecialEffect());
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
    public boolean learnSpellCheck(Spell spell)
    {
        learnTemp = null;
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
                }
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
                }
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
                }
                learnTemp = spell;
                break;
            }
            default:
                return false;
        }
        return true;
    }
    private void learnSpell(Spell spell)
    {
        int staminaCost = 10;
        switch(spell.getType())
        {
            case ATTACK:
            {
                if(atkSpells.containsKey(spell))
                    atkSpells.get(spell).addExp(spell.getLearnExp());
                else
                    atkSpells.put(learnTemp, new SpellExp(learnTemp.getInitialExp()));
                break;
            }
            case DEFENSE:
            {
                if(defSpells.containsKey(spell))
                    defSpells.get(spell).addExp(spell.getLearnExp());
                else
                    defSpells.put(learnTemp, new SpellExp(learnTemp.getInitialExp()));
                break;
            }
            case MOVE:
            {
                if(movSpells.containsKey(spell))
                    movSpells.get(spell).addExp(spell.getLearnExp());
                else
                    movSpells.put(learnTemp, new SpellExp(learnTemp.getInitialExp()));
                break;
            }
            default:
                break;
        }
        intelligence += spell.getLearnInt();
        stamina -= staminaCost;
        System.out.println("["+NAME+"]成功精進了 " + spell + " 的使用技巧！");
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
        lastinjured = injury;
        autoCastDefense();
        stamina -= (int)(Math.ceil(injury.getDamage() - reduceDamage));
        System.out.println("["+NAME+"]"+"遭受到了來自敵方的"+injury.getSpell()+"攻擊！");
    }
    private void addNextTurnStatus()
    {
        if(lastinjured == null)
            return;
        if(!lastinjured.getEffect().equals(SpecialEffect.NONE) && !status.containsKey(lastinjured.getEffect()))
            status.put(lastinjured.getEffect(), new StatusTurn(lastinjured.getEffect().getNeededTurn()));
    }
    public void autoCastDefense()
    {
        if(status.containsKey(SpecialEffect.DISARMED) || status.containsKey(SpecialEffect.PETRIFIED))
            return;
        reduceDamage = 0;
        for(Map.Entry<Spell, SpellExp> spell : defSpells.entrySet())
        {
            double temp = (spell.getKey().getBasicNumber()*0.01)*(1+spell.getValue().getValue()*0.01)*lastinjured.getDamage();
            if(temp > reduceDamage)
                reduceDamage = temp;
        }
        patronusDefense();
    }
    public void patronusDefense()
    {
        if(status.containsKey(SpecialEffect.DISARMED) || status.containsKey(SpecialEffect.PETRIFIED))
            return;
        if(defSpells.containsKey(Spell.EXPECTO_PATRONUM) && !status.containsKey(Spell.EXPECTO_PATRONUM.getSpecialEffect()))
            status.put(Spell.EXPECTO_PATRONUM.getSpecialEffect(), new StatusTurn(Spell.EXPECTO_PATRONUM.getSpecialEffect().getNeededTurn()));
    }
    public void instantDeath()
    {
        stamina = 0;
    }
    public void staminaChange(int change)
    {
        stamina += change;
    }
    public void intelligenceChange(int change)
    {
        intelligence -= change;
    }
    private void staminaCorrect()
    {
        if(stamina > STAMINA_MAX)
            stamina = STAMINA_MAX;
        else if(stamina < 0)
            stamina = 0;
    }
    public boolean turnEnd(Injury injury)
    {
        if(injury.isInjuryExisted())
            beCasted(injury);
        if(learnTemp != null)
        {
            learnSpell(learnTemp);
            learnTemp = null;
        }
        staminaCorrect();
        System.out.println(this);
        for(Map.Entry<SpecialEffect, StatusTurn> stat : status.entrySet())
        {
            if(stat.getValue().turnDecrement())
                status.remove(stat.getKey());
        }
        addNextTurnStatus();
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
        statusString.append("   魔藥數量：").append(potionAmount).append(System.lineSeparator());
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
        statusString.append(System.lineSeparator());
        statusString.append("   咒語：");
        if(atkSpells.isEmpty() && defSpells.isEmpty() && movSpells.isEmpty())
            statusString.append("無");
        else
        {
            for(Map.Entry<Spell, SpellExp> atkSpell : atkSpells.entrySet())
                statusString.append(atkSpell.getKey()).append("[").append(atkSpell.getValue().getValue()).append("%] ");
            for(Map.Entry<Spell, SpellExp> defSpell : defSpells.entrySet())
                statusString.append(defSpell.getKey()).append("[").append(defSpell.getValue().getValue()).append("%] ");
            for(Map.Entry<Spell, SpellExp> movSpell : movSpells.entrySet())
                statusString.append(movSpell.getKey()).append("[").append(movSpell.getValue().getValue()).append("%] ");
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
