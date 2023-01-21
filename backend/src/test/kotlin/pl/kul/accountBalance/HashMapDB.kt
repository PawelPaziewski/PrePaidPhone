package pl.kul.accountBalance

import java.util.*

internal class HashMapDB : AccountBalanceRepository {

    private val db = HashMap<String, AccountBalance>()

    override fun <S : AccountBalance> save(entity: S): S {
        db[entity.phoneNumber] = entity
        return entity
    }

    override fun <S : AccountBalance> saveAll(entities: MutableIterable<S>): MutableIterable<S> {
        entities.forEach { save(it) }
        return entities
    }

    override fun findById(id: String) = Optional.ofNullable(db[id])

    override fun existsById(id: String) = db.containsKey(id)

    override fun findAll() = db.values

    override fun findAllById(ids: MutableIterable<String>) = db.filterKeys { ids.contains(it) }.values.toMutableList()

    override fun count() = db.size.toLong()

    override fun deleteById(id: String) {
        db.remove(id)
    }

    override fun delete(entity: AccountBalance) {
        db.remove(entity.phoneNumber, entity)
    }

    override fun deleteAllById(ids: MutableIterable<String>) {
        ids.forEach {
            db.remove(it)
        }
    }

    override fun deleteAll(entities: MutableIterable<AccountBalance>) {
        entities.forEach {
            db.remove(it.phoneNumber, it)
        }
    }

    override fun deleteAll() {
        db.clear()
    }
}