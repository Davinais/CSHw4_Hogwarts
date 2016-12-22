import java.util.NoSuchElementException;

public class Injury
{
    private double damage;
    private boolean exist;
    private SpecialEffect effect;
    private Spell spell;
    private static final Injury NO_INJURY = new Injury();

    public Injury(Spell spell, double damage, SpecialEffect effect)
    {
        exist = true;
        this.spell = spell;
        this.damage = damage;
        this.effect = effect;
    }
    private Injury()
    {
        exist = false;
    }
    public static Injury noInjury()
    {
        return NO_INJURY;
    }
    public boolean isInjuryExisted()
    {
        return exist;
    }
    public double getDamage() throws NoSuchElementException
    {
        if(!exist)
            throw new NoSuchElementException();
        return damage;
    }
    public SpecialEffect getEffect() throws NoSuchElementException
    {
        if(!exist)
            throw new NoSuchElementException();
        return effect;
    }
    public Spell getSpell()
    {
        if(!exist)
            throw new NoSuchElementException();
        return spell;
    }
}