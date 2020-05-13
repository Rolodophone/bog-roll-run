package net.rolodophone.covidsim

class People(window: GameWindow) {
    private val people = listOf(
        Person(window, Person.Colour.PINK     , 4f  , 81 , 115, 97 , 116),
        Person(window, Person.Colour.GREEN    , 3f  , 87 , 110, 91 , 112),
        Person(window, Person.Colour.CYAN     , 1f  , 97 , 119, 98 , 123),
        Person(window, Person.Colour.DARK_BLUE, 5f  , 98 , 121, 92 , 124),
        Person(window, Person.Colour.CYAN     , 7f  , 107, 147, 118, 124),
        Person(window, Person.Colour.GREEN    , 2f  , 119, 112, 106, 105),
        Person(window, Person.Colour.PINK     , .5f , 100, 88 , 102, 85),
        Person(window, Person.Colour.CYAN     , 5f  , 106, 80 , 120, 66),
        Person(window, Person.Colour.PURPLE   , 2f  , 107, 52 , 118, 51),
        Person(window, Person.Colour.CYAN     , 10f , 108, 46 , 120, 66),
        Person(window, Person.Colour.RED      , 4f  , 125, 30 , 105, 26),
        Person(window, Person.Colour.RED      , 3f  , 119, 37 , 78 , 33),
        Person(window, Person.Colour.CYAN     , 0f  , 89 , 45 , 0  , 0),
        Person(window, Person.Colour.CYAN     , 10f , 65 , 44 , 68 , 33),
        Person(window, Person.Colour.PINK     , 6f  , 32 , 46 , 44 , 33),
        Person(window, Person.Colour.GREEN    , 11f , 7  , 32 , 31 , 32),
        Person(window, Person.Colour.CYAN     , 0f  , 11 , 50 , 0  , 0),
        Person(window, Person.Colour.PURPLE   , 14f , 34 , 17 , 45 , 4),
        Person(window, Person.Colour.GREEN    , 1f  , 48 , 6  , 49 , 12),
        Person(window, Person.Colour.RED      , .3f , 52 , 6  , 53 , 6),
        Person(window, Person.Colour.GREEN    , 3f  , 55 , 8  , 71 , 7),
        Person(window, Person.Colour.PURPLE   , 7f  , 72 , 7  , 72 , 28),
        Person(window, Person.Colour.DARK_BLUE, 2.5f, 72 , 13 , 71 , 28),
        Person(window, Person.Colour.CYAN     , 4f  , 68 , 15 , 62 , 16),
        Person(window, Person.Colour.GREEN    , 5f  , 64 , 16 , 57 , 16),
        Person(window, Person.Colour.PINK     , 1f  , 59 , 16 , 56 , 15),
        Person(window, Person.Colour.DARK_BLUE, 1f  , 57 , 19 , 53 , 20),
        Person(window, Person.Colour.CYAN     , 6f  , 60 , 23 , 53 , 24),
        Person(window, Person.Colour.PURPLE   , 2f  , 54 , 23 , 59 , 24),
        Person(window, Person.Colour.GREEN    , 0f  , 58 , 23 , 0  , 0),
        Person(window, Person.Colour.DARK_BLUE, 4f  , 57 , 27 , 62 , 28),
        Person(window, Person.Colour.PINK     , .3f , 54 , 28 , 53 , 27),
        Person(window, Person.Colour.PURPLE   , 0f  , 52 , 15 , 51 , 15),
        Person(window, Person.Colour.CYAN     , 0f  , 53 , 15 , 52 , 15),
        Person(window, Person.Colour.RED      , 0f  , 52 , 17 , 51 , 17),
        Person(window, Person.Colour.RED      , 0f  , 53 , 17 , 51 , 17),
        Person(window, Person.Colour.PURPLE   , 0f  , 52 , 19 , 51 , 19),
        Person(window, Person.Colour.CYAN     , 0f  , 52 , 21 , 51 , 21),
        Person(window, Person.Colour.DARK_BLUE, 0f  , 53 , 21 , 52 , 21),
        Person(window, Person.Colour.PINK     , 5f  , 49 , 14 , 50 , 29),
        Person(window, Person.Colour.RED      , 0f  , 48 , 17 , 48 , 18),
        Person(window, Person.Colour.PURPLE   , 0f  , 48 , 22 , 48 , 23)
    )


    fun update() {
        for (person in people) person.update()
    }


    fun draw() {
        for (person in people) person.draw()
    }
}