// package Data

public class Parser {
    public static NobelPrizeAward parseNobelWinner(String[] data) {
        int year = Integer.parseInt(data[0]);
        String category = data[1];
        int winnerCount = Integer.parseInt(data[data.length-1]);

        String[] winners = new String[winnerCount];
        for (int winnerCt = 0; winnerCt < winnerCount; winnerCt++) {
            winners[winnerCt] = data[2 + winnerCt];
        }

        return new NobelPrizeAward(year, category, winners, winnerCount);
    }
}
