public enum SpecialEffect
{
    NONE(0), BLEEDING(2), PATRONUS(1), DISARMED(1) ,PETRIFIED(1);

    private int turn;
    private SpecialEffect(int turn)
    {
        this.turn = turn;
    }
    public int getNeededTurn()
    {
        return turn;
    }
}
