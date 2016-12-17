import java.util.HashMap;

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
public class Mage
{
    private HashMap<Spell, SpellExp> atkSpell = new HashMap<>();
    private HashMap<Spell, SpellExp> defSpell = new HashMap<>();
    private HashMap<Spell, SpellExp> movSpell = new HashMap<>();
    private final int STAMINA_MAX, MA_PERCENT;
    private int intelligence, stamina;
    public Mage(Hogwarts college)
    {
        STAMINA_MAX = college.getMaxStamina();
        MA_PERCENT = college.percentOfMA();
        intelligence = college.getInt();
        stamina = STAMINA_MAX;
    }
    public boolean learnSpell(Spell spell)
    {
        if(!spell.validToLearn(intelligence))
            return false;
        switch(spell.getType())
        {
            case ATTACK:
            {
                if(atkSpell.containsKey(spell))
                    atkSpell.get(spell).addExp(spell.getLearnExp());
                else
                    atkSpell.put(spell, new SpellExp(spell.getInitialExp()));
                break;
            }
            case DEFENSE:
            {
                if(defSpell.containsKey(spell))
                    defSpell.get(spell).addExp(spell.getLearnExp());
                else
                    defSpell.put(spell, new SpellExp(spell.getInitialExp()));
                break;
            }
            case MOVE:
            {
                if(movSpell.containsKey(spell))
                    movSpell.get(spell).addExp(spell.getLearnExp());
                else
                    movSpell.put(spell, new SpellExp(spell.getInitialExp()));
                break;
            }
            default:
                break;
        }
        intelligence += spell.getLearnInt();
        return true;
    }
}
