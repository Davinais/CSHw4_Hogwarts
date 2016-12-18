public enum SpecialEffect
{
    NONE("無", 0), BLEEDING("流血", 2), PATRONUS("護法現身", 1), DISARMED("手無寸鐵", 1) ,PETRIFIED("石化", 1);

    private String name;
    private int turn;
    private SpecialEffect(String name, int turn)
    {
        this.name = name;
        this.turn = turn;
    }
    public int getNeededTurn()
    {
        return turn;
    }
    @Override
    public String toString()
    {
        return name;
    }
}
