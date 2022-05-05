class UserPlayer(override var hp: Int) : Player {
    override fun makePrediction(): Pair<Int, Int> {
        println("Make your prediction (type two numbers separated with space)")
        val (a, b) = readLine()!!.split("""\s+""".toRegex()).map(String::toInt)
        return a to b
    }

    fun gameOver(totalWins: Int) {
        println("Game over!!! You loose. You won $totalWins fights")
    }

    fun win() {
        println("You defeated the enemy!!!")
    }

    fun celebrate() {
        println("Well done! You are the Dungeon master!!!")
    }
}