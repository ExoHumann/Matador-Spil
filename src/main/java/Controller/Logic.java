package Controller;

import Model.Dice;
import Model.FieldList;
import Model.PlayerList;
import Model.Playerlist.*;

public class Logic {

    boolean landedOnChance;
    boolean drawAnother;
    public int prePos;
    public int pos;

    private BuyingController buyingController;

    public Logic() {
        this.buyingController = new BuyingController();
    }

    /**
     * Updates the roll and position of the dice
     * Moves the player outside of the GUI.
     * Manages the balance of the player
     * @param pl Playlist used to access players account and position
     * @param fl FieldList used to access the price of the fields
     * @param moveAmount Dice used to update the roll and move the player
     * @param playerTurn Which player is taking a turn
     */
    public void movePlayer(PlayerList pl, FieldList fl, int moveAmount, int playerTurn) {
        Player player = pl.getPlayerList(playerTurn);
        Account account = pl.getAccount(playerTurn);

        prePos = player.getCurrentPosition();
        player.move(moveAmount, fl);
        pos = player.getCurrentPosition();

        if (prePos > pos){
            account.deposit(2);
        }

        switch (pos) {
            case 0: case 6: case 12:
                break;

            case 3: case 9: case 15: case 21:
                landedOnChance = true;
                break;

            case 18:
                player.setInJail();
                player.setCurrentPosition(6);
                break;

            default:
                buyingController.buyField(pl,fl,playerTurn);
        }
        System.out.printf(Game.translation.getLandedString(),
                player.getName(), pos, fl.getField(pos).getTitle(), fl.getField(pos).getPrice());
    }

    public int moveAmount(int moveToPos, FieldList fl){
        return (fl.getSize() + moveToPos - pos-1)%fl.getSize()+1;
    }

        /**
         * Describes the win condition of the game if a player has 3000 balance
         * @param pl player list used to access accounts and check all counts
         * @return Returns if the condition is met or no
         */
    public boolean winCondition(PlayerList pl) {
        boolean winCondition = false;
        for (int i = 0; i <pl.getAccounts().length ; i++) {
            if (pl.getAccount(i).getBalance() <= 0) winCondition = true;
        }
        return winCondition;
    }

    public void findWinner(PlayerList pl, int playerTurn){
        System.out.println(pl.getPlayerList(playerTurn).getName() + Game.translation.getWonString() + pl.getAccount(playerTurn).getBalance());
    }


    public void diceInfo(PlayerList pl, Dice dice, int PNum) {
        System.out.printf(Game.translation.getDiceInfo() +"\n",pl.getPlayerList(PNum).getName(),dice.getDie1(),dice.getDie2(),dice.getSum(),pl.getPlayerList(PNum).getCurrentPosition(),pl.getAccount(PNum).getBalance());
    }

    public void displayTakingTurn(PlayerList pl, int PNum){
        System.out.printf(Game.translation.getTakingTurnString()+ "\n",pl.getPlayerList(PNum).getName(), "Player num: " + PNum);
    }

    public void displayTurn(PlayerList pl, int PNum){
        System.out.printf(Game.translation.getDisplayTurnString()+ "\n", pl.getPlayerList(PNum).getName(),pl.getPlayerList(PNum).getTurn());
    }

    public int getPrePos() { return prePos; }
    public int getPos() { return pos; }
    public void setPos(int pos) { this.pos = pos; }
}








