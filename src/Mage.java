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
    }
    public boolean learnSpell(Spell spell)
    {
        if(!spell.validToLearn(intelligence))
            return false;
        switch(spell.getType())
        {
            case ATTACK:
            {
                if(atkSpells.containsKey(spell))
                    atkSpells.get(spell).addExp(spell.getLearnExp());
                else
                    atkSpells.put(spell, new SpellExp(spell.getInitialExp()));
                break;
            }
            case DEFENSE:
            {
                if(defSpells.containsKey(spell))
                    defSpells.get(spell).addExp(spell.getLearnExp());
                else
                    defSpells.put(spell, new SpellExp(spell.getInitialExp()));
                break;
            }
            case MOVE:
            {
                if(movSpells.containsKey(spell))
                    movSpells.get(spell).addExp(spell.getLearnExp());
                else
                    movSpells.put(spell, new SpellExp(spell.getInitialExp()));
                break;
            }
            default:
                break;
        }
        intelligence += spell.getLearnInt();
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
    public void beCasted(int damage, SpecialEffect effect)
    {
        beDamaged = damage;
        autoCastDefense();
        stamina -= (beDamaged - reduceDamage);
        if(!effect.equals(SpecialEffect.NONE) && !status.containsKey(effect))
            status.put(effect, new StatusTurn(effect.getNeededTurn()));
    }
    public void autoCastDefense()
    {
        if(status.containsKey(SpecialEffect.DISARMED) || status.containsKey(SpecialEffect.PETRIFIED))
            return;
        reduceDamage = 0;
        for(Map.Entry<Spell, SpellExp> spell : defSpells.entrySet())
        {
            int temp = (int)(Math.floor((spell.getKey().getBasicNumber()*0.01)*(1+MA_PERCENT*0.01)*beDamaged));
            if(temp > reduceDamage)
                reduceDamage = temp;
            SpecialEffect spellSE = spell.getKey().getSpecialEffect();
            if(!(spellSE.equals(SpecialEffect.NONE)) && !(status.containsKey(spellSE)))
                status.put(spellSE, new StatusTurn(spellSE.getNeededTurn()));
        }
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
