public enum Spell
{
    SERPENSORTIA(SpellType.ATTACK, 10, 50, 60, 2, 10, SpecialEffect.NONE, 0), STUPEFY(SpellType.ATTACK, 20, 40, 70, 3, 10, SpecialEffect.NONE, 0),
    SPECTUMSEMPRA(SpellType.ATTACK, 35, 30, 80, 4, 5, SpecialEffect.BLEEDING, 0),
    IMPEDIMENTA(SpellType.DEFENSE, 15, 40, 20, 2, 20, SpecialEffect.NONE, 0), PROTEGO(SpellType.DEFENSE, 25, 30, 30, 3, 8, SpecialEffect.NONE, 0),
    EXPECTO_PATRONUM(SpellType.DEFENSE, 35, 100, 0, 8, 0, SpecialEffect.PATRONUS, 0),
    EXPELLIARMUS(SpellType.MOVE, 10, 100, 0, 2, 10, SpecialEffect.DISARMED, 10), PERTRIFICUS_TOTALUS(SpellType.MOVE, 10, 100, 0, 2, 10, SpecialEffect.PETRIFIED, 20);

    private final int requiredInt, initialExp, basicNumber, learnInt, learnExp, extraCost;
    private final SpellType type;
    private final SpecialEffect effect;
    private Spell(SpellType type, int requiredInt, int initialExp, int basicNumber, int learnInt, int learnExp, SpecialEffect effect, int extraCost)
    {
        this.type = type;
        this.requiredInt = requiredInt;
        this.initialExp = initialExp;
        this.basicNumber = basicNumber;
        this.learnInt = learnInt;
        this.learnExp = learnExp;
        this.effect = effect;
        this.extraCost = extraCost;
    }
    public boolean validToLearn(int mageInt)
    {
        return (mageInt >= requiredInt);
    }
    public SpellType getType()
    {
        return type;
    }
    public int getInitialExp()
    {
        return initialExp;
    }
    public int getBasicNumber()
    {
        return basicNumber;
    }
    public int getLearnInt()
    {
        return learnInt;
    }
    public int getLearnExp()
    {
        return learnExp;
    }
    public SpecialEffect getSpecialEffect()
    {
        return effect;
    }
    public int getExtraCost()
    {
        return extraCost;
    }
    @Override
    public String toString()
    {
        return name().charAt(0)+name().substring(1).toLowerCase().replace("_", " ");
    }
}
