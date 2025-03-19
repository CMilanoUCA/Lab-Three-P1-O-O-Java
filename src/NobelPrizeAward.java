public record NobelPrizeAward(int year, String category,
                              String[] winners, int numWinners) {

    public Object[] toObjectArray() {
        Object[] objects = new Object[4];
        objects[0] = year;
        objects[1] = category;
        objects[2] = winners;
        objects[3] = numWinners;
        return objects;
    }
}