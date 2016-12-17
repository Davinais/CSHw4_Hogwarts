public enum Hogwarts
{
    GRIFFENDOR(150, 12, 30), HUFFLEPUFF(120, 12, 30), RAVENCLAW(100, 15, 30), SLYTHERIN(100, 10, 40);

    private int maxStamina, intelligence, magicalAffinityPercent;
    private Hogwarts(int maxStamina, int intelligence, int magicalAffinityPercent) {
        this.maxStamina = maxStamina;
        this.intelligence = intelligence;
        this.magicalAffinityPercent = magicalAffinityPercent;
    }
    public int getMaxStamina()
    {
        return maxStamina;
    }
    public int getInt()
    {
        return intelligence;
    }
    public int percentOfMA()
    {
        return magicalAffinityPercent;
    }
}
