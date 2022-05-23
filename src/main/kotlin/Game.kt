import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class Game(private val player: Player) {

    companion object {

        private val random = Random(1)

        private val enemies = listOf(
            SimpleEnemy("enemy1"),
            SimpleEnemy("enemy2"),
            SimpleEnemy("enemy3"))

        private fun score(expected: Int, min: Int, max: Int) =
            if (expected in min..max) min.toDouble() / max else 0.0

        private fun round(
            unit: CombatUnit,
            otherUnit: CombatUnit,
            status: UnitStatus,
            otherStatus: UnitStatus
        ): Pair<UnitStatus, UnitStatus> {
            val expected = random.nextInt(0, 4)
            val (left, right) = unit.attack(status, otherStatus)
            val (otherLeft, otherRight) = otherUnit.attack(status, otherStatus)
            val score = score(expected, left, right)
            val otherScore = score(expected, otherLeft, otherRight)

            return if (score > otherScore)
                status to otherStatus.copy(hp = otherStatus.hp - 1)
            else
                status.copy(hp = status.hp - 1) to otherStatus
        }
    }

    private var playerStatus: UnitStatus

    init {
//        Hero choice affects this parameters in status
        playerStatus = UnitStatus(name = "Player", hp = 3)
    }

    class SimpleEnemy(val name: String) : CombatUnit {
        override fun attack(my: UnitStatus, other: UnitStatus): Pair<Int, Int> {
            val a = random.nextInt()
            val b = random.nextInt()
            return min(a, b) to max(a, b)
        }
    }

    private fun fight(enemy: CombatUnit, initialEnemyStatus: UnitStatus) {
        var enemyStatus = initialEnemyStatus
        while (true) {
            val (newStatus, newEnemyStatus) = round(player, enemy, playerStatus, enemyStatus)

            if (newStatus.hp < playerStatus.hp) {
                player.event(DamageReceived(playerStatus.hp - newStatus.hp))
            }
            if (newEnemyStatus.hp < enemyStatus.hp) {
                player.event(DamageDealt(enemyStatus.hp - newEnemyStatus.hp))
            }

            playerStatus = newStatus
            enemyStatus = newEnemyStatus

            if (playerStatus.hp == 0) {
                player.event(Death)
                break
            }
            if (enemyStatus.hp == 0) {
                player.event(Victory)
                break
            }
        }
    }

    fun runGame() {
        for (enemy in enemies) {
            fight(enemy, UnitStatus(enemy.name, 1))
        }
    }
}