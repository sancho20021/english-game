import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class Game(private val player: Player) {

    companion object {

        private val random = Random(1)

        private val enemies = listOf(
            SimpleEnemy(
                name = "Snake",
                hp = 1,
                from = 0,
                to = 4,
                image = SNAKE,
            ),
            SimpleEnemy(
                name = "Vampire",
                hp = 1,
                from = 0,
                to = 8,
                image = VAMPIRE,
            ),
            Boss(
                name = "Dungeon Master",
                hp = 2,
                from = 0,
                to = 8,
                image = BILLY,
                items = listOf(LatexGlove, Whip)
            ),
            SimpleEnemy(
                name = "Minotaur",
                hp = 2,
                from = 0,
                to = 5,
                image = MINOTAUR
            ),
        )


        private fun score(expected: Int, min: Int, max: Int) =
            if (expected in min..max) min.toDouble() / max else 0.0

        private fun round(
            unit: CombatUnit,
            otherUnit: CombatUnit,
            status: UnitStatus,
            otherStatus: UnitStatus,
            from: Int,
            to: Int,
        ): Pair<UnitStatus, UnitStatus> {
            val expected = random.nextInt(from, to)
            val (left, right) = unit.attack(status, otherStatus, from, to)
            val (otherLeft, otherRight) = otherUnit.attack(status, otherStatus, from, to)
            val score = score(expected, left, right)
            val otherScore = score(expected, otherLeft, otherRight)

            return if (score > otherScore)
                status to otherStatus.copy(hp = otherStatus.hp - status.attack)
            else
                status.copy(hp = status.hp - otherStatus.attack) to otherStatus
        }
    }

    private var playerStatus: UnitStatus

    init {
        playerStatus =
            UnitStatus(
                name = "Player",
                hp = 3,
                attack = 1,
                image = MR_INCREDIBLE,
            )
    }

    open class SimpleEnemy(
        val name: String,
        val hp: Int,
        val from: Int,
        val to: Int,
        val image: String
    ) : CombatUnit {
        override fun attack(
            my: UnitStatus,
            other: UnitStatus,
            from: Int,
            to: Int,
        ): Pair<Int, Int> {
            val a = random.nextInt(from, to)
            val b = random.nextInt(from, to)
            return min(a, b) to max(a, b)
        }
    }

    class Boss(
        name: String,
        hp: Int,
        from: Int,
        to: Int,
        image: String,
        val items: List<Item>,
    ) : SimpleEnemy(name, hp, from, to, image)

    private fun fight(enemy: SimpleEnemy, initialEnemyStatus: UnitStatus): Event {
        var enemyStatus = initialEnemyStatus
        while (true) {
            val (newStatus, newEnemyStatus) =
                round(
                    player,
                    enemy,
                    playerStatus,
                    enemyStatus,
                    enemy.from,
                    enemy.to,
                )

            if (newStatus.hp < playerStatus.hp) {
                player.event(DamageReceived(playerStatus.hp - newStatus.hp))
            }
            if (newEnemyStatus.hp < enemyStatus.hp) {
                player.event(DamageDealt(enemyStatus.hp - newEnemyStatus.hp))
            }

            playerStatus = newStatus
            enemyStatus = newEnemyStatus

            if (playerStatus.hp <= 0) {
                player.event(Death)
                return Death
            }
            if (enemyStatus.hp <= 0) {
                player.event(Victory)
                return Victory
            }
        }
    }

    fun runGame() {
        for (enemy in enemies) {
            val hpBeforeFight = playerStatus.hp
            when (fight(enemy, UnitStatus(enemy.name, enemy.hp, attack = 1, enemy.image))) {
                Death -> break
                else -> {
                    if (enemy is Boss) {
                        player.event(GetBonus(when (player.selectItem(enemy.items)) {
                            LatexGlove -> {
                                playerStatus = playerStatus.copy(hp = hpBeforeFight + 1)
                                HealthBonus
                            }
                            Whip -> {
                                playerStatus = playerStatus.copy(
                                    hp = hpBeforeFight,
                                    attack = playerStatus.attack + 1
                                )
                                AttackBonus
                            }
                        }))
                    } else {
                        playerStatus = playerStatus.copy(hp = hpBeforeFight)
                    }
                }
            }
        }
    }
}