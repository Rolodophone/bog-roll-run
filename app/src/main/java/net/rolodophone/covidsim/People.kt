package net.rolodophone.covidsim

class People(window: GameWindow) {
    private val people = listOf(
        Person(window, Person.Colour.PINK, 4f, 81, 115, 97, 116)//,
//        Person(window, Person.Colour.    , f, , , , ),
//        Person(window, Person.Colour.    , f, , , , ),
//        Person(window, Person.Colour.    , f, , , , ),
//        Person(window, Person.Colour.    , f, , , , ),
//        Person(window, Person.Colour.    , f, , , , ),
//        Person(window, Person.Colour.    , f, , , , ),
//        Person(window, Person.Colour.    , f, , , , ),
//        Person(window, Person.Colour.    , f, , , , ),
//        Person(window, Person.Colour.    , f, , , , ),
//        Person(window, Person.Colour.    , f, , , , ),
//        Person(window, Person.Colour.    , f, , , , ),
//        Person(window, Person.Colour.    , f, , , , ),
//        Person(window, Person.Colour.    , f, , , , ),
//        Person(window, Person.Colour.    , f, , , , ),
//        Person(window, Person.Colour.    , f, , , , ),
//        Person(window, Person.Colour.    , f, , , , ),
//        Person(window, Person.Colour.    , f, , , , ),
//        Person(window, Person.Colour.    , f, , , , ),
//        Person(window, Person.Colour.    , f, , , , ),
//        Person(window, Person.Colour.    , f, , , , ),
//        Person(window, Person.Colour.    , f, , , , ),
//        Person(window, Person.Colour.    , f, , , , ),
//        Person(window, Person.Colour.    , f, , , , ),
//        Person(window, Person.Colour.    , f, , , , ),
    )


    fun update() {
        for (person in people) person.update()
    }


    fun draw() {
        for (person in people) person.draw()
    }
}