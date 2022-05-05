import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class Game {
    companion object {
        val random = Random(1)
        val enemies = listOf(SimpleEnemy(1), SimpleEnemy(2), SimpleEnemy(3))
    }

    class SimpleEnemy(override var hp: Int) : Player {
        override fun makePrediction(): Pair<Int, Int> {
            val a = random.nextInt()
            val b = random.nextInt()
            return min(a, b) to max(a, b)
        }
    }

    fun fight(player1: Player, player2: Player): Player {
        val expected = random.nextInt(0, Int.MAX_VALUE)
        while (min(player1.hp, player2.hp) > 0) {
            val (pred1Min, pred1Max) = player1.makePrediction()
            val (pred2Min, pred2Max) = player2.makePrediction()
            val score1 = score(expected, pred1Min, pred1Max)
            val score2 = score(expected, pred2Min, pred2Max)
            if (score1 > score2) {
                player2.hp -= 1
            } else {
                player1.hp -= 1
            }
        }
        return if (player1.hp > 0) player1 else player2
    }

    fun score(expected: Int, min: Int, max: Int) = if (expected in min..max) min.toDouble() / max else 0.0

    fun runGame() {
        val user = UserPlayer(3)
        for ((i, enemy) in enemies.withIndex()) {
            val winner = fight(enemy, user)
            if (winner == user) {
                user.win()
            } else {
                user.gameOver(i)
                return
            }
        }
        user.celebrate()
    }
}