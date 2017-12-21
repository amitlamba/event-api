package com.und.eventapi.repository

import com.und.eventapi.model.Event
import com.und.eventapi.model.Identity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update

class EventRepositoryUpdateImpl : EventRepositoryUpdate {

    @Autowired
    lateinit var mongoTemplate: MongoTemplate

    override fun findEventsMatchingIdentity(identity: Identity): List<Event> {
        //FIXME query is empty
        val events = mongoTemplate.find(Query(), Event::class.java)
        return events
    }

    override fun updateEventsWithIdentityMatching(events: List<Event>, identity: Identity) {
        val query = Query().addCriteria(Criteria
                .where("identity.deviceId").`is`(identity.deviceId)
                .and("identity.sessionId").`is`(identity.sessionId)
                .and("identity.userId").exists(false)
        )
        val events2 = mongoTemplate.find(query, Event::class.java)
        var update = Update.update("identity.userId", identity.userId)
        mongoTemplate.updateMulti(query,update,Event::class.java)


        val query2 = Query().addCriteria(Criteria
                .where("identity.deviceId").`is`(identity.deviceId)
                .and("identity.sessionId").exists(false)
                .and("identity.userId").exists(false)
        )
        val events3 = mongoTemplate.find(query, Event::class.java)
        var update2 = Update.update("identity.sessionId", identity.userId)
        update2.set("identity.userId", identity.userId)
        mongoTemplate.updateMulti(query2,update2,Event::class.java)

/*        val eventsMap = events
                .filter { event -> event.identity.deviceId == identity.deviceId && event.identity.userId == null }
                .forEach { event ->
                    event.identity.userId = identity.userId
                    event.identity.sessionId = identity.sessionId
                    mongoTemplate.save(event)
                }*/


        //update.set()
        //mongoTemplate.save(events)
    }
}