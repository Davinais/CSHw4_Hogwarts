public enum Spell
{
    SERPENSORTIA(SpellType.ATTACK, 10, 50, 60, 2, 10, SpecialEffect.NONE), STUPEFY(SpellType.ATTACK, 20, 40, 70, 3, 10, SpecialEffect.NONE),
    SPECTUMSEMPRA(SpellType.ATTACK, 35, 30, 80, 4, 5, SpecialEffect.BLEEDING),
    IMPEDIMENTA(SpellType.DEFENSE, 15, 40, 20, 2, 20, SpecialEffect.NONE), PROTEGO(SpellType.DEFENSE, 25, 30, 30, 3, 8, SpecialEffect.NONE),
    EXPECTO_PATRONUM(SpellType.DEFENSE, 35, 100, 0, 8, 0, SpecialEffect.PATRONUS),
    EXPELLIARMUS(SpellType.MOVE, 10, 50, 0, 2, 10, SpecialEffect.DISARMED), PERTRIFICUS_TOTALUS(SpellType.MOVE, 10, 50, 0, 2, 10, SpecialEffect.PETRIFIED);

    private final int requiredInt, initialExp, basicNumber, learnInt, learnExp;
    private final SpellType type;
    private final SpecialEffect effect;
    private Spell(SpellType type, int requiredInt, int initialExp, int basicNumber, int learnInt, int learnExp, SpecialEffect effect)
    {
        this.type = type;
        this.requiredInt = requiredInt;
        this.initialExp = initialExp;
        this.basicNumber = basicNumber;
        this.learnInt = learnInt;
        this.learnExp = learnExp;
        this.effect = effect;
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
}
