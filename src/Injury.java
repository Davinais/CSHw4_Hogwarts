import java.util.NoSuchElementException;

public class Injury
{
    private int damage;
    private boolean exist;
    private SpecialEffect effect;
    private static final Injury NO_INJURY = new Injury();

    public Injury(int damage, SpecialEffect effect)
    {
        exist = true;
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
    public int getDamage() throws NoSuchElementException
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
}